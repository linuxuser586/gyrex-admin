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
package org.eclipse.gyrex.toolkit.gwt.client.ui.widgets;

import org.eclipse.gyrex.toolkit.gwt.client.ui.content.IContentAdapter;
import org.eclipse.gyrex.toolkit.gwt.client.ui.events.HasWidgetChangeHandlers;
import org.eclipse.gyrex.toolkit.gwt.client.ui.events.WidgetChangeEvent;
import org.eclipse.gyrex.toolkit.gwt.client.ui.events.WidgetChangeHandler;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.DialogFieldValidator;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.ValidationContext;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.ValidationResult;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SContentEntry;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SSelectionFlagEntry;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SRadioButton;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Composite for <code>org.eclipse.gyrex.toolkit.widgets.RadioButton</code>.
 */
public class CWTRadioButton extends CWTDialogField {

	private final class ButtonSelectionTracker implements HasWidgetChangeHandlers, ValueChangeHandler<Boolean> {

		private boolean oldSelected = isSelected();

		@Override
		public HandlerRegistration addWidgetChangeHandler(final WidgetChangeHandler handler) {
			return addHandler(handler, WidgetChangeEvent.TYPE);
		}

		@Override
		public void fireEvent(final GwtEvent<?> event) {
			CWTRadioButton.this.fireEvent(event);
		}

		@Override
		public void onValueChange(final ValueChangeEvent<Boolean> event) {
			final Boolean selected = event.getValue().booleanValue();
			if (selected != oldSelected) {
				oldSelected = selected;
				WidgetChangeEvent.fire(this, CWTRadioButton.this);
			}
		}
	};

	private static final DialogFieldValidator dialogFieldValidator = new DialogFieldValidator() {

		@Override
		public ValidationResult validate(final CWTDialogField dialogField, final ValidationContext context) {
			final CWTRadioButton radioButton = (CWTRadioButton) dialogField;

			final SRadioButton sRadioButton = radioButton.getSRadioButton();
			if (sRadioButton.required) {
				// check if the group is already ok
				final String group = radioButton.getGroup();
				if (context.getBoolean(group)) {
					return new ValidationResult(ValidationResult.RESULT_OK, group);
				}

				if (radioButton.isSelected()) {
					// store group ok in context
					context.set(group, true);
					return new ValidationResult(ValidationResult.RESULT_OK, group);
				} else {
					return new ValidationResult(ValidationResult.RESULT_ERROR_BUT_CONTINUE, group);
				}
			}

			return ValidationResult.OK;
		}

	};

	private static final IContentAdapter radioButtonAdapter = new IContentAdapter() {

		public SContentEntry getContent(final CWTWidget widget) {
			final CWTRadioButton radioButton = (CWTRadioButton) widget;
			final SSelectionFlagEntry radioButtonEntry = new SSelectionFlagEntry();
			radioButtonEntry.selected = radioButton.isSelected();
			return radioButtonEntry;
		}

		public boolean isSet(final CWTWidget widget) {
			final CWTRadioButton radioButton = (CWTRadioButton) widget;
			return radioButton.isSelected();
		}

		public void setContent(final CWTWidget widget, final SContentEntry content) {
			final CWTRadioButton radioButton = (CWTRadioButton) widget;
			final SSelectionFlagEntry radioButtonEntry = (SSelectionFlagEntry) content;
			if (null != radioButton.radioButton) {
				radioButton.radioButton.setValue(radioButtonEntry.selected, true);
			}
		}

	};

	private RadioButton radioButton;
	private ButtonSelectionTracker selectionTracker;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(final Class<T> adapter) {
		if (HasWidgetChangeHandlers.class == adapter) {
			return (T) selectionTracker;
		}
		if (DialogFieldValidator.class == adapter) {
			return (T) dialogFieldValidator;
		}
		if (IContentAdapter.class == adapter) {
			return (T) radioButtonAdapter;
		}

		// call super
		return super.getAdapter(adapter);
	}

	public String getGroup() {
		final String group = getGroup(getSRadioButton());
		if (null != group) {
			return group;
		}

		return getSRadioButton().id;
	}

	private String getGroup(final SRadioButton radioButton) {
		final ISerializedWidget parent = radioButton.getParent();
		return null == parent ? null : parent.getId();
	}

	private SRadioButton getSRadioButton() {
		return (SRadioButton) getSerializedWidget();
	}

	/**
	 * Indicates if the radio button is selected.
	 * 
	 * @return <code>true</code> if the radio button is selected,
	 *         <code>false</code> otherwise
	 */
	public boolean isSelected() {
		if (null != radioButton) {
			return radioButton.getValue().booleanValue();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.rep.web.gwt.client.internal.ui.widgets.DialogFieldComposite#renderFieldWidget(org.eclipse.rep.web.gwt.client.rwt.ISerializedWidget,
	 *      org.eclipse.rep.web.gwt.client.ui.RenderingToolkit)
	 */
	@Override
	protected Widget renderFieldWidget(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SRadioButton sRadioButton = (SRadioButton) serializedWidget;
		String group = getGroup(sRadioButton);
		if (null == group) {
			group = sRadioButton.id;
		}

		String label = sRadioButton.label;
		if (null == label) {
			label = "";
		}

		radioButton = new RadioButton(group, label);
		if (null != sRadioButton.toolTipText) {
			radioButton.setTitle(sRadioButton.toolTipText);
		}

		selectionTracker = new ButtonSelectionTracker();
		radioButton.addValueChangeHandler(selectionTracker);

		return radioButton;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (null == radioButton) {
			return;
		}

		radioButton.setEnabled(enabled);
	}
}
