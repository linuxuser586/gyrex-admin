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
package org.eclipse.gyrex.toolkit.gwt.server.internal.serialization;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gyrex.toolkit.CWT;
import org.eclipse.gyrex.toolkit.commands.Command;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedLayoutHint;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.commands.SCommand;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.resources.SImageResource;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogFieldRule;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SMultiDialogFieldRule;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SWidget;
import org.eclipse.gyrex.toolkit.layout.LayoutHint;
import org.eclipse.gyrex.toolkit.resources.ImageResource;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.widgets.DialogField;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldRule;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldRule.FieldSelectionType;
import org.eclipse.gyrex.toolkit.widgets.MultiDialogFieldRule;
import org.eclipse.gyrex.toolkit.widgets.MultiDialogFieldRule.Condition;
import org.eclipse.gyrex.toolkit.widgets.Widget;

/**
 * Abstract base class for widget serializers.
 */
public abstract class WidgetSerializer {

	/**
	 * Fills the serialized widget base attributes.
	 * 
	 * @param widget
	 *            the widget to read the attributes from
	 * @param parent
	 *            the serialized parent
	 * @param sWidget
	 *            the SWidget to write the attributes to
	 * @return the passed in serialized widget for convenience
	 */
	protected ISerializedWidget populateAttributes(final Widget widget, final ISerializedWidget serializedWidget, final SContainer parent) {
		final SWidget sWidget = (SWidget) serializedWidget;
		sWidget.id = widget.getId();
		sWidget.style = widget.getStyle();
		sWidget.toolTipText = widget.getToolTipText();
		sWidget.parent = parent;
		sWidget.layoutHints = serializeLayoutHints(widget.getLayoutHints());
		sWidget.visibilityRule = serializeDialogFieldRule(widget.getVisibilityRule(), widget);
		return sWidget;
	}

	/**
	 * Serializes the specified widget.
	 * 
	 * @param widget
	 *            the widget to serialize
	 * @param parent
	 *            the parent widget (maybe <code>null</code> if no parent
	 *            provided)
	 * @return the serialized widget
	 */
	public abstract ISerializedWidget serialize(Widget widget, SContainer parent);

	/**
	 * Serialized the specified {@link Command}.
	 * 
	 * @param command
	 *            the command to serialize (maybe <code>null</code>)
	 * @param owner
	 *            the command owner for determining the default scope (may not
	 *            be <code>null</code> if a rule is specified)
	 * @return the serialized command (maybe <code>null</code> if the input was
	 *         <code>null</code>)
	 */
	protected SCommand serializeCommand(final Command command, final Widget owner) {
		if (null == command) {
			return null;
		}

		final SCommand sCommand = new SCommand();
		sCommand.id = command.getId();
		sCommand.contentSubmitRule = serializeDialogFieldRule(command.getContentSubmitRule(), owner);
		return sCommand;
	}

	/**
	 * Serialized the specified {@link DialogFieldRule}.
	 * 
	 * @param dialogFieldRule
	 *            the dialog field rule to serialize (maybe <code>null</code>)
	 * @param owner
	 *            the rule owner for determining the default scope (may not be
	 *            <code>null</code> if a rule is specified)
	 * @return the serialized rule (maybe <code>null</code> if the input was
	 *         <code>null</code>)
	 */
	protected SDialogFieldRule serializeDialogFieldRule(final DialogFieldRule dialogFieldRule, final Widget owner) {
		if (null == dialogFieldRule) {
			return null;
		}

		if (null == owner) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "owner is null");
		}

		if (dialogFieldRule.isMulti()) {
			return serializeMultiDialogFieldRule((MultiDialogFieldRule) dialogFieldRule, owner);
		} else {
			return serializeNonMultiDialogFieldRule(dialogFieldRule, owner);
		}
	}

	/**
	 * Serializes the specified {@link ImageResource}.
	 * 
	 * @param imageResource
	 *            the image resource to serialize
	 * @return the serialized resource (maybe <code>null</code> if the input was
	 *         <code>null</code>)
	 */
	protected SImageResource serializeImageResource(final ImageResource imageResource) {
		if (null == imageResource) {
			return null;
		}
		return (SImageResource) ToolkitSerialization.serializeResource(imageResource);
	}

	private ISerializedLayoutHint[] serializeLayoutHints(final LayoutHint[] layoutHints) {
		if ((null == layoutHints) || (layoutHints.length == 0)) {
			return null;
		}

		if (layoutHints.length == 1) {
			final ISerializedLayoutHint sHint = ToolkitSerialization.serializeLayoutHint(layoutHints[0]);
			if (null == sHint) {
				return null;
			}
			return new ISerializedLayoutHint[] { sHint };
		}

		final List<ISerializedLayoutHint> sHints = new ArrayList<ISerializedLayoutHint>(layoutHints.length);
		for (final LayoutHint element : layoutHints) {
			final ISerializedLayoutHint sHint = ToolkitSerialization.serializeLayoutHint(element);
			if (null != sHint) {
				sHints.add(sHint);
			}
		}
		if (sHints.isEmpty()) {
			return null;
		}

		return sHints.toArray(new ISerializedLayoutHint[sHints.size()]);
	}

	private SDialogFieldRule serializeMultiDialogFieldRule(final MultiDialogFieldRule multiDialogFieldRule, final Widget owner) {
		final SMultiDialogFieldRule sMultiDialogFieldRule = new SMultiDialogFieldRule();

		// condition
		if (multiDialogFieldRule.getCondition() == Condition.OR) {
			sMultiDialogFieldRule.condition = SMultiDialogFieldRule.C_OR;
		} else if (multiDialogFieldRule.getCondition() == Condition.AND) {
			sMultiDialogFieldRule.condition = SMultiDialogFieldRule.C_AND;
		}

		// rules
		final DialogFieldRule[] rules = multiDialogFieldRule.getRules();
		final List<SDialogFieldRule> serializedRules = new ArrayList<SDialogFieldRule>(rules.length);
		for (final DialogFieldRule subRule : rules) {
			serializedRules.add(serializeDialogFieldRule(subRule, owner));
		}
		sMultiDialogFieldRule.rules = serializedRules.toArray(new SDialogFieldRule[serializedRules.size()]);

		return sMultiDialogFieldRule;
	}

	private SDialogFieldRule serializeNonMultiDialogFieldRule(final DialogFieldRule dialogFieldRule, final Widget owner) {
		final SDialogFieldRule sDialogFieldRule = new SDialogFieldRule();

		// field condition
		switch (dialogFieldRule.getFieldCondition()) {
			case IS_VALID:
				sDialogFieldRule.fieldCondition = SDialogFieldRule.FC_IS_VALID;
				break;

			case IS_SET:
				sDialogFieldRule.fieldCondition = SDialogFieldRule.FC_IS_SET;
				break;

			case SUBMIT:
				sDialogFieldRule.fieldCondition = SDialogFieldRule.FC_SUBMIT;
				break;

			case NEVER:
				sDialogFieldRule.fieldCondition = SDialogFieldRule.FC_NEVER;
				break;

			default:
				// fallback to IS_VALID
				sDialogFieldRule.fieldCondition = SDialogFieldRule.FC_IS_VALID;
				break;
		}

		boolean needsScope = false;
		boolean needsSelectedFields = false;

		// field selection type
		if (dialogFieldRule.getFieldSelectionType() == FieldSelectionType.REQUIRED_FIELDS) {
			sDialogFieldRule.fieldSelectionType = SDialogFieldRule.FST_REQUIRED_FIELDS;
			// need scope
			needsScope = true;
		} else if (dialogFieldRule.getFieldSelectionType() == FieldSelectionType.ALL_FIELDS) {
			sDialogFieldRule.fieldSelectionType = SDialogFieldRule.FST_ALL_FIELDS;
			// need scope
			needsScope = true;
		} else if (dialogFieldRule.getFieldSelectionType() == FieldSelectionType.SPECIFIED_FIELDS) {
			sDialogFieldRule.fieldSelectionType = SDialogFieldRule.FST_SPECIFIED_FIELDS;
			// don't need scope but list of fields
			needsSelectedFields = true;
		} else if (dialogFieldRule.getFieldSelectionType() == FieldSelectionType.NO_FIELDS) {
			sDialogFieldRule.fieldSelectionType = SDialogFieldRule.FST_NO_FIELDS;
			// don't need scope or list of fields
		} else {
			// fallback to required or specified fields
			if (dialogFieldRule.getSelectedFields().length > 1) {
				sDialogFieldRule.fieldSelectionType = SDialogFieldRule.FST_SPECIFIED_FIELDS;
				needsSelectedFields = true;
			} else {
				sDialogFieldRule.fieldSelectionType = SDialogFieldRule.FST_REQUIRED_FIELDS;
				needsScope = true;
			}
		}

		// scope
		if (needsScope) {
			if (null != dialogFieldRule.getScope()) {
				// use specified scope
				sDialogFieldRule.scopeContainerId = dialogFieldRule.getScope().getId();
			} else {
				// use default scope if possible
				if (owner instanceof Container) {
					sDialogFieldRule.scopeContainerId = owner.getId();
				} else {
					CWT.error(CWT.ERROR_NULL_ARGUMENT, MessageFormat.format("invalid dialog field rule (owner {0}); requires a scope if owner is not a container", owner.getId()));
				}
			}
		}

		// selected fields
		if (needsSelectedFields) {
			final DialogField[] selectedFields = dialogFieldRule.getSelectedFields();
			if ((null == selectedFields) || (selectedFields.length == 0)) {
				CWT.error(CWT.ERROR_NULL_ARGUMENT, MessageFormat.format("invalid dialog field rule (owner {0}); requires selected fields", owner.getId()));
			}
			sDialogFieldRule.selectedFieldIds = new String[selectedFields.length];
			for (int i = 0; i < selectedFields.length; i++) {
				final DialogField dialogField = selectedFields[i];
				sDialogFieldRule.selectedFieldIds[i] = dialogField.getId();
			}
		}

		return sDialogFieldRule;
	}

}
