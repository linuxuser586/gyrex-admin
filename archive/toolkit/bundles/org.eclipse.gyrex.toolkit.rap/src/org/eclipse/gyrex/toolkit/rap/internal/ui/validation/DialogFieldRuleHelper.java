/*******************************************************************************
 * Copyright (c) 2008 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.rap.internal.ui.validation;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.gyrex.toolkit.Toolkit;
import org.eclipse.gyrex.toolkit.rap.internal.ui.content.IContentAdapter;
import org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTContainer;
import org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTDialogField;
import org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTWidget;
import org.eclipse.gyrex.toolkit.widgets.DialogField;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldRule;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldRule.FieldSelectionType;
import org.eclipse.gyrex.toolkit.widgets.MultiDialogFieldRule;

/**
 * Helper for working with dialog field rules.
 */
public final class DialogFieldRuleHelper {

	/** constant for no dialog fields */
	private static final CWTDialogField[] NO_DIALOG_FIELDS = new CWTDialogField[0];

	/** PARTIAL_VALIDATION_GROUP */
	private static final String PARTIAL_VALIDATION_GROUP = "partial_validation_group:";

	private static boolean evaluateMultiRule(final MultiDialogFieldRule multiDialogFieldRule, final CWTContainer context) {
		final DialogFieldRule[] rules = multiDialogFieldRule.getRules();
		for (int i = 0; i < rules.length; i++) {
			final DialogFieldRule rule = rules[i];
			final boolean ruleResult = evaluateRule(rule, context);
			switch (multiDialogFieldRule.getCondition()) {
				case OR:
					if (ruleResult) {
						return true;
					}
					break;

				case AND:
					if (!ruleResult) {
						return false;
					}
					break;
			}
		}

		switch (multiDialogFieldRule.getCondition()) {
			case OR:
				return false; // none of the rules evaluated to true

			case AND:

				return true; // all rules evaluated to true

			default:
				// ignore invalid condition
				return true;
		}
	}

	/**
	 * Evaluates the rule in the specified context.
	 * 
	 * @param dialogFieldRule
	 *            the dialog field rule
	 * @param context
	 *            the context
	 * @return <code>true</code> if validation was ok, <code>false</code>
	 *         otherwise
	 */
	public static boolean evaluateRule(final DialogFieldRule dialogFieldRule, final CWTContainer context) {
		if (null == dialogFieldRule) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "dialogFieldRule");
		}
		if (null == context) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "context");
		}

		if (dialogFieldRule instanceof MultiDialogFieldRule) {
			return evaluateMultiRule((MultiDialogFieldRule) dialogFieldRule, context);
		} else {
			return evaluateSingleRule(dialogFieldRule, context);
		}
	}

	private static boolean evaluateSingleRule(final DialogFieldRule dialogFieldRule, final CWTContainer context) {
		// find affected dialog fields
		final CWTDialogField[] affectedDialogFields = findAffectedDialogFields(dialogFieldRule, context);

		// performance: evaluate to true if no fields selected
		if (affectedDialogFields.length == 0) {
			return true;
		}

		// TODO: this is buggy and ugly, we need a robust support for partial validation
		final boolean[] willFail = new boolean[1];

		// evaluate for dialog fields
		final ValidationContext validationContext = new ValidationContext();
		for (int i = 0; i < affectedDialogFields.length; i++) {
			final CWTDialogField dialogField = affectedDialogFields[i];
			switch (dialogFieldRule.getFieldCondition()) {

				case IS_VALID:
					final DialogFieldValidator validator = (DialogFieldValidator) dialogField.getAdapter(DialogFieldValidator.class);
					if (null != validator) {
						// validate
						final ValidationResult result = validator.validate(dialogField, validationContext);
						if (null == result) {
							return false; // invalid validator
						}
						switch (result.result) {
							case ValidationResult.RESULT_ERROR:
								return false;

							case ValidationResult.RESULT_ERROR_BUT_CONTINUE:
								// special: the validator request continue of validation but want to fail if no further validator submits a corrected result
								if (null == result.group) {
									return false; // invalid result
								}
								willFail[0] = !validationContext.getBoolean(PARTIAL_VALIDATION_GROUP + result.group, false);
								break;
							case ValidationResult.RESULT_WARNING:
							case ValidationResult.RESULT_OK:
								if (null != result.group) {
									validationContext.set(PARTIAL_VALIDATION_GROUP + result.group, true);
									willFail[0] = false;
								}
								break;
						}
					}
					break;

				case IS_SET:
					final IContentAdapter contentAdapter = (IContentAdapter) dialogField.getAdapter(IContentAdapter.class);
					if (null != contentAdapter) {
						if (!contentAdapter.hasContent(dialogField)) {
							return false; // no content, fail
						}
					}
					break;
				default:
					// ignore invalid/unknown condition
					break;
			}
		}

		// check if we need to fail because of invalid partial validation
		if (willFail[0]) {
			return false;
		}

		// we are valid
		return true;
	}

	/**
	 * Finds all dialog fields affected by the specified rule in the given
	 * context.
	 * 
	 * @param dialogFieldRule
	 *            the dialog field rule
	 * @param context
	 *            the context
	 * @return a list of found dialog fields
	 */
	public static CWTDialogField<?>[] findAffectedDialogFields(final DialogFieldRule dialogFieldRule, final CWTContainer context) {
		if (null == dialogFieldRule) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "dialogFieldRule");
		}
		if (null == context) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "context");
		}

		// find scope
		final CWTContainer scope = findScope(context, dialogFieldRule);

		// limit to specified ids
		final Set widgetIds = getSpecificWidgetIds(dialogFieldRule);

		// performance: return empty if specified field ids is not null but zero length
		if ((null != widgetIds) && widgetIds.isEmpty()) {
			return NO_DIALOG_FIELDS;
		}

		// required fields only
		boolean requiredFieldsOnly = false;
		if (null == widgetIds) {
			requiredFieldsOnly = dialogFieldRule.getFieldSelectionType() == FieldSelectionType.REQUIRED_FIELDS;
		}

		// get all fields
		final Set<CWTDialogField> allDialogFields = new HashSet<CWTDialogField>();
		findDialogFieldsAndGroups(scope, allDialogFields, widgetIds, requiredFieldsOnly);
		return allDialogFields.toArray(new CWTDialogField[allDialogFields.size()]);
	}

	/**
	 * Finds dialog fields in the specified container recursively.
	 * 
	 * @param container
	 *            the container to search recursively
	 * @param allDialogFields
	 *            the set to store all found fields
	 * @param widgetIds
	 *            a list of widget ids to search for (maybe <code>null</code> to
	 *            search for all widgets)
	 * @param requiredFieldsOnly
	 *            <code>true</code> if only required fields should be found or
	 *            <code>false</code> all fields should be found (only used if
	 *            <code>widgetIds</code> is <code>null</code>)
	 */
	private static void findDialogFieldsAndGroups(final CWTContainer<?> container, final Set<CWTDialogField> allDialogFields, final Set widgetIds, final boolean requiredFieldsOnly) {
		if (null == container) {
			return;
		}

		for (final CWTWidget widget : container.getChildren()) {

			if (widget instanceof CWTDialogField) {
				final CWTDialogField dialogField = (CWTDialogField) widget;
				// check if only specific widgets are allowed
				if (null != widgetIds) {
					// widget id must match
					if (widgetIds.contains(widget.getWidgetId())) {
						allDialogFields.add(dialogField);
					}
				} else {
					// check if required fields only
					if (requiredFieldsOnly) {
						// must be a required field
						if (((DialogField) widget.getWidget()).isRequired()) {
							allDialogFields.add(dialogField);
						}
					} else {
						// good to add
						allDialogFields.add(dialogField);
					}
				}
			} else if (widget instanceof CWTContainer) {
				findDialogFieldsAndGroups((CWTContainer) widget, allDialogFields, widgetIds, requiredFieldsOnly);
			}
		}
	}

	/**
	 * Finds the specified scope within a given context.
	 * 
	 * @param context
	 *            the context
	 * @param dialogFieldRule
	 *            the dialog field rule
	 * @return the found scope or the context itself if the passed in scope id
	 *         is <code>null</code>
	 */
	private static CWTContainer findScope(final CWTContainer context, final DialogFieldRule dialogFieldRule) {
		CWTContainer scope = context;
		if (null != dialogFieldRule.getScope()) {
			// search context parents for scope
			while ((null != scope) && !dialogFieldRule.getScope().getId().equals(scope.getWidgetId())) {
				scope = context.getParentContainer();
			}
			// TODO: support search in context children for scope?
			if (null == scope) {
				Toolkit.error(Toolkit.ERROR_INVALID_ARGUMENT, "context '" + context.getWidgetId() + "' not member of scope '" + dialogFieldRule + "'");
			}
		}
		return scope;
	}

	/**
	 * Returns a modifiable set of specific of specific widgets ids if the
	 * dialog field rule defines them. Returns <code>null</code> if the dialog
	 * field rule does not define any specific widgets.
	 * 
	 * @param dialogFieldRule
	 *            the dialog field rule
	 * @return a set of specific widgets ids (maybe <code>null</code>)
	 */
	private static Set getSpecificWidgetIds(final DialogFieldRule dialogFieldRule) {
		if (dialogFieldRule.getFieldSelectionType() != FieldSelectionType.SPECIFIED_FIELDS) {
			return null;
		}
		final DialogField[] selectedWidgets = dialogFieldRule.getSelectedFields();
		final Set<String> widgetIds = new HashSet<String>(selectedWidgets.length);
		for (int i = 0; i < selectedWidgets.length; i++) {
			final String widgetId = selectedWidgets[i].getId();
			if (null != widgetId) {
				widgetIds.add(widgetId);
			}
		}
		return widgetIds;
	}

	/**
	 * Hidden constructor, no need to instantiate.
	 */
	private DialogFieldRuleHelper() {
		// empty
	}
}
