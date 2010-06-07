/*******************************************************************************
 * Copyright (c) 2008, 2009 Gunnar Wagenknecht and others.
 * All rights reserved.
 *  
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.gyrex.toolkit.gwt.client.ui.content.IContentAdapter;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTContainer;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTDialogField;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTToolkit;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogFieldRule;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SMultiDialogFieldRule;

/**
 * Helper for working with dialog field rules.
 */
public final class DialogFieldRuleHelper {

	private static interface IVisitor {
		void visit(CWTDialogField dialogField);
	}

	/** constant for no dialog fields */
	private static final CWTDialogField[] NO_DIALOG_FIELDS = new CWTDialogField[0];

	/** PARTIAL_VALIDATION_GROUP */
	private static final String PARTIAL_VALIDATION_GROUP = "partial_validation_group:";

	private static boolean evaluateMultiRule(final SMultiDialogFieldRule multiDialogFieldRule, final CWTContainer context) {
		final SDialogFieldRule[] rules = multiDialogFieldRule.rules;
		for (int i = 0; i < rules.length; i++) {
			final SDialogFieldRule rule = rules[i];
			final boolean ruleResult = evaluateRule(rule, context);
			switch (multiDialogFieldRule.condition) {
				case SMultiDialogFieldRule.C_OR:
					if (ruleResult) {
						return true;
					}
					break;

				case SMultiDialogFieldRule.C_AND:
					if (!ruleResult) {
						return false;
					}
					break;
			}
		}

		switch (multiDialogFieldRule.condition) {
			case SMultiDialogFieldRule.C_OR:
				return false; // none of the rules evaluated to true

			case SMultiDialogFieldRule.C_AND:
				// all rules evaluated to true
				return true;
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
	public static boolean evaluateRule(final SDialogFieldRule dialogFieldRule, final CWTContainer context) {
		if (null == dialogFieldRule) {
			CWTToolkit.error(CWTToolkit.ERROR_NULL_ARGUMENT, "dialogFieldRule");
		}
		if (null == context) {
			CWTToolkit.error(CWTToolkit.ERROR_NULL_ARGUMENT, "context");
		}

		if (dialogFieldRule instanceof SMultiDialogFieldRule) {
			return evaluateMultiRule((SMultiDialogFieldRule) dialogFieldRule, context);
		} else {
			return evaluateSingleRule(dialogFieldRule, context);
		}
	}

	private static boolean evaluateSingleRule(final SDialogFieldRule dialogFieldRule, final CWTContainer context) {
		// special case: NEVER condition
		if (dialogFieldRule.fieldCondition == SDialogFieldRule.FC_NEVER) {
			return false;
		}

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
			switch (dialogFieldRule.fieldCondition) {

				case SDialogFieldRule.FC_IS_VALID:
					final DialogFieldValidator validator = dialogField.getAdapter(DialogFieldValidator.class);
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
								willFail[0] = !validationContext.getBoolean(PARTIAL_VALIDATION_GROUP + result.group);
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

				case SDialogFieldRule.FC_IS_SET:
					final IContentAdapter contentAdapter = dialogField.getAdapter(IContentAdapter.class);
					if (null != contentAdapter) {
						if (!contentAdapter.isSet(dialogField)) {
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
	public static CWTDialogField[] findAffectedDialogFields(final SDialogFieldRule dialogFieldRule, final CWTContainer context) {
		if (null == dialogFieldRule) {
			CWTToolkit.error(CWTToolkit.ERROR_NULL_ARGUMENT, "dialogFieldRule");
		}
		if (null == context) {
			CWTToolkit.error(CWTToolkit.ERROR_NULL_ARGUMENT, "context");
		}

		// test if this is a NO_FIELDS rule
		if (dialogFieldRule.fieldSelectionType == SDialogFieldRule.FST_NO_FIELDS) {
			return NO_DIALOG_FIELDS;
		}

		// test if rule has widget ids specified directly
		final Set widgetIds = getSpecificWidgetIds(dialogFieldRule);

		// use fields directly specified with rule
		if (null != widgetIds) {
			// performance: return empty if specified field ids is not null but zero length
			if (widgetIds.isEmpty()) {
				return NO_DIALOG_FIELDS;
			}

			// lookup the fields starting at the ROOT context
			// this makes it more convenient when creating the rule
			final Set<CWTDialogField> allDialogFields = new HashSet<CWTDialogField>();
			findDialogFieldsAndGroupsInContext(getRootContainer(context), allDialogFields, widgetIds);
			return allDialogFields.toArray(new CWTDialogField[allDialogFields.size()]);
		} else {
			// lookup widgets using scope
			final CWTContainer scope = findScope(context, dialogFieldRule);

			// required fields only
			boolean requiredFieldsOnly = false;
			if (null == widgetIds) {
				requiredFieldsOnly = dialogFieldRule.fieldSelectionType == SDialogFieldRule.FST_REQUIRED_FIELDS;
			}

			// get all fields
			final Set<CWTDialogField> allDialogFields = new HashSet<CWTDialogField>();
			findDialogFieldsAndGroupsInScope(scope, allDialogFields, requiredFieldsOnly);
			return allDialogFields.toArray(new CWTDialogField[allDialogFields.size()]);
		}
	}

	/**
	 * Finds all dialog fields using a specified set of ids in a given context.
	 * 
	 * @param context
	 *            the context to search
	 * @param allDialogFields
	 *            the set to store all found fields
	 * @param widgetIds
	 *            a list of widget ids to search for (maybe <code>null</code> to
	 *            search for all widgets)
	 */
	private static void findDialogFieldsAndGroupsInContext(final CWTContainer context, final Set<CWTDialogField> allDialogFields, final Set widgetIds) {
		findDialogFieldsRecursively(context, new IVisitor() {
			public void visit(final CWTDialogField dialogField) {
				// widget id must match
				if (widgetIds.contains(dialogField.getWidgetId())) {
					allDialogFields.add(dialogField);
				}
			}
		});
	}

	/**
	 * Finds dialog fields in the specified container recursively.
	 * 
	 * @param scope
	 *            the scope to search recursively
	 * @param allDialogFields
	 *            the set to store all found fields
	 * @param requiredFieldsOnly
	 *            <code>true</code> if only required fields should be found or
	 *            <code>false</code> all fields should be found (only used if
	 *            <code>widgetIds</code> is <code>null</code>)
	 */
	private static void findDialogFieldsAndGroupsInScope(final CWTContainer scope, final Set<CWTDialogField> allDialogFields, final boolean requiredFieldsOnly) {
		findDialogFieldsRecursively(scope, new IVisitor() {
			public void visit(final CWTDialogField dialogField) {
				// check if required fields only
				if (requiredFieldsOnly) {
					// must be a required field
					if (dialogField.isRequired()) {
						allDialogFields.add(dialogField);
					}
				} else {
					// good to add
					allDialogFields.add(dialogField);
				}
			}
		});
	}

	/**
	 * Finds all dialog fields in the specified container recursively and calls
	 * the visitor.
	 * 
	 * @param container
	 *            the container
	 * @param visitor
	 *            the visitor
	 */
	private static void findDialogFieldsRecursively(final CWTContainer container, final IVisitor visitor) {
		if ((null == container) || (null == visitor)) {
			return;
		}
		for (final Iterator stream = container.childrenIterator(); stream.hasNext();) {
			final CWTWidget widget = (CWTWidget) stream.next();
			if (widget instanceof CWTDialogField) {
				final CWTDialogField dialogField = (CWTDialogField) widget;
				visitor.visit(dialogField);
			} else if (widget instanceof CWTContainer) {
				findDialogFieldsRecursively((CWTContainer) widget, visitor);
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
	private static CWTContainer findScope(final CWTContainer context, final SDialogFieldRule dialogFieldRule) {
		CWTContainer scope = context;
		if (null != dialogFieldRule.scopeContainerId) {
			// search context parents for scope
			while ((null != scope) && !dialogFieldRule.scopeContainerId.equals(scope.getWidgetId())) {
				scope = context.getParentContainer();
			}
			// TODO: support search in context children for scope?
			if (null == scope) {
				CWTToolkit.error(CWTToolkit.ERROR_INVALID_ARGUMENT, "context '" + context.getWidgetId() + "' not member of scope '" + dialogFieldRule + "'");
			}
		}
		return scope;
	}

	/**
	 * Returns the root container
	 * 
	 * @param container
	 *            the container
	 * @return the root container, i.e. the one without a parent
	 */
	private static CWTContainer getRootContainer(final CWTContainer container) {
		CWTContainer parent = container;
		while (null != parent.getParentContainer()) {
			parent = parent.getParentContainer();
		}
		return parent;
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
	private static Set getSpecificWidgetIds(final SDialogFieldRule dialogFieldRule) {
		Set<String> widgetIds = null;
		final String[] selectedWidgetIds = dialogFieldRule.selectedFieldIds;
		if (null != selectedWidgetIds) {
			widgetIds = new HashSet<String>(selectedWidgetIds.length);
			for (int i = 0; i < selectedWidgetIds.length; i++) {
				final String widgetId = selectedWidgetIds[i];
				if (null != widgetId) {
					widgetIds.add(widgetId);
				}
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
