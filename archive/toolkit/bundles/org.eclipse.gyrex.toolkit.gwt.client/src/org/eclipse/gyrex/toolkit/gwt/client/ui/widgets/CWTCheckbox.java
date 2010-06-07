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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;

import org.eclipse.gyrex.toolkit.gwt.client.ui.content.IContentAdapter;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.DialogFieldValidator;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.ValidationContext;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.ValidationResult;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SContentEntry;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SSelectionFlagEntry;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SCheckbox;

/**
 * Composite for <code>org.eclipse.gyrex.toolkit.widgets.RadioButton</code>.
 */
public class CWTCheckbox extends CWTDialogField {

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
					changeListeners.fireChange(CWTCheckbox.this);
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
				checkbox.checkBox.setChecked(radioButtonEntry.selected);
			}
		}

	};

	private CheckBox checkBox;
	private ButtonSelectionTracker selectionTracker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(final Class<T> adapter) {
		if (SourcesChangeEvents.class == adapter) {
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
			return checkBox.isChecked();
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
		checkBox.addClickListener(selectionTracker);

		return checkBox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gyrex.toolkit.gwt.client.internal.ui.widgets.DialogFieldComposite#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		if (null == checkBox) {
			return;
		}

		checkBox.setEnabled(enabled);
	}
}
