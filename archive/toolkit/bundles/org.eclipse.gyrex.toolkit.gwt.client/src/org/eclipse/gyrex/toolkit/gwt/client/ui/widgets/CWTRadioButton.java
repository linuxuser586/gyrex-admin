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

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;

import org.eclipse.gyrex.toolkit.gwt.client.ui.content.IContentAdapter;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.DialogFieldValidator;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.ValidationContext;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.ValidationResult;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SContentEntry;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SSelectionFlagEntry;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SRadioButton;

/**
 * Composite for <code>org.eclipse.gyrex.toolkit.widgets.RadioButton</code>.
 */
public class CWTRadioButton extends CWTDialogField {

	private final class ButtonSelectionTracker implements SourcesChangeEvents, ClickListener {

		private ChangeListenerCollection changeListeners;
		private boolean oldSelected = isSelected();

		public void addChangeListener(final ChangeListener listener) {
			if (changeListeners == null) {
				changeListeners = new ChangeListenerCollection();
			}
			changeListeners.add(listener);
		}

		void buttonClicked() {
			final boolean selected = isSelected();
			if (selected != oldSelected) {
				oldSelected = selected;
				if (changeListeners != null) {
					changeListeners.fireChange(CWTRadioButton.this);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
		 */
		public void onClick(final Widget sender) {
			buttonClicked();
		}

		public void removeChangeListener(final ChangeListener listener) {
			if (changeListeners != null) {
				changeListeners.remove(listener);
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
				radioButton.radioButton.setChecked(radioButtonEntry.selected);
			}
		}

	};

	private RadioButton radioButton;
	private ButtonSelectionTracker selectionTracker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(final Class adapter) {
		if (SourcesChangeEvents.class == adapter) {
			return selectionTracker;
		}
		if (DialogFieldValidator.class == adapter) {
			return dialogFieldValidator;
		}
		if (IContentAdapter.class == adapter) {
			return radioButtonAdapter;
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
			return radioButton.isChecked();
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
		radioButton.addClickListener(selectionTracker);

		return radioButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gyrex.toolkit.gwt.client.internal.ui.widgets.DialogFieldComposite#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		if (null == radioButton) {
			return;
		}

		radioButton.setEnabled(enabled);
	}
}
