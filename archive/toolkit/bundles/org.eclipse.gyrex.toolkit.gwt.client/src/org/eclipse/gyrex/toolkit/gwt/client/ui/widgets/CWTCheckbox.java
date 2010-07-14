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
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SCheckbox;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Composite for <code>org.eclipse.gyrex.toolkit.widgets.RadioButton</code>.
 */
public class CWTCheckbox extends CWTDialogField {

	private final class ButtonSelectionTracker implements HasWidgetChangeHandlers, ValueChangeHandler<Boolean> {

		private boolean oldSelected = isSelected();

		@Override
		public HandlerRegistration addWidgetChangeHandler(final WidgetChangeHandler handler) {
			return addHandler(handler, WidgetChangeEvent.TYPE);
		}

		@Override
		public void fireEvent(final GwtEvent<?> event) {
			CWTCheckbox.this.fireEvent(event);
		}

		@Override
		public void onValueChange(final ValueChangeEvent<Boolean> event) {
			final boolean selected = event.getValue().booleanValue();
			if (selected != oldSelected) {
				oldSelected = selected;
				WidgetChangeEvent.fire(this, CWTCheckbox.this);
			}
		}
	};

	private static final DialogFieldValidator dialogFieldValidator = new DialogFieldValidator() {

		@Override
		public ValidationResult validate(final CWTDialogField dialogField, final ValidationContext context) {
			final CWTCheckbox checkbox = (CWTCheckbox) dialogField;

			final SCheckbox sCheckbox = checkbox.getSCheckbox();
			if (sCheckbox.required) {
				// check if the group is already ok
				final String group = checkbox.getGroup();
				if (context.getBoolean(group)) {
					return new ValidationResult(ValidationResult.RESULT_OK, group);
				}

				if (checkbox.isSelected()) {
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

	private static final IContentAdapter checkBoxAdapter = new IContentAdapter() {

		public SContentEntry getContent(final CWTWidget widget) {
			final CWTCheckbox checkbox = (CWTCheckbox) widget;
			final SSelectionFlagEntry radioButtonEntry = new SSelectionFlagEntry();
			radioButtonEntry.selected = checkbox.isSelected();
			return radioButtonEntry;
		}

		public boolean isSet(final CWTWidget widget) {
			final CWTCheckbox checkbox = (CWTCheckbox) widget;
			return checkbox.isSelected();
		}

		public void setContent(final CWTWidget widget, final SContentEntry content) {
			final CWTCheckbox checkbox = (CWTCheckbox) widget;
			final SSelectionFlagEntry radioButtonEntry = (SSelectionFlagEntry) content;
			if (null != checkbox.checkBox) {
				checkbox.checkBox.setValue(radioButtonEntry.selected, true);
			}
		}

	};

	private CheckBox checkBox;
	private ButtonSelectionTracker selectionTracker;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(final Class<T> adapter) {
		if (HasWidgetChangeHandlers.class == adapter) {
			// see http://code.google.com/p/google-web-toolkit/issues/detail?id=2710
			return (T) selectionTracker;
		}
		if (DialogFieldValidator.class == adapter) {
			// see http://code.google.com/p/google-web-toolkit/issues/detail?id=2710
			return (T) dialogFieldValidator;
		}
		if (IContentAdapter.class == adapter) {
			// see http://code.google.com/p/google-web-toolkit/issues/detail?id=2710
			return (T) checkBoxAdapter;
		}

		// call super
		return super.getAdapter(adapter);
	}

	public String getGroup() {
		final String group = getGroup(getSCheckbox());
		if (null != group) {
			return group;
		}

		return getSCheckbox().id;
	}

	private String getGroup(final SCheckbox checkbox) {
		final ISerializedWidget parent = checkbox.getParent();
		return null == parent ? null : parent.getId();
	}

	private SCheckbox getSCheckbox() {
		return (SCheckbox) getSerializedWidget();
	}

	/**
	 * Indicates if the radio button is selected.
	 * 
	 * @return <code>true</code> if the radio button is selected,
	 *         <code>false</code> otherwise
	 */
	public boolean isSelected() {
		if (null != checkBox) {
			return checkBox.getValue().booleanValue();
		}
		return false;
	}

	@Override
	protected Widget renderFieldWidget(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SCheckbox sCheckbox = (SCheckbox) serializedWidget;
		String group = getGroup(sCheckbox);
		if (null == group) {
			group = sCheckbox.id;
		}

		String label = sCheckbox.label;
		if (null == label) {
			label = "";
		}

		checkBox = new CheckBox(label);
		if (null != sCheckbox.toolTipText) {
			checkBox.setTitle(sCheckbox.toolTipText);
		}

		selectionTracker = new ButtonSelectionTracker();
		checkBox.addValueChangeHandler(selectionTracker);

		return checkBox;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (null == checkBox) {
			return;
		}

		checkBox.setEnabled(enabled);
	}
}
