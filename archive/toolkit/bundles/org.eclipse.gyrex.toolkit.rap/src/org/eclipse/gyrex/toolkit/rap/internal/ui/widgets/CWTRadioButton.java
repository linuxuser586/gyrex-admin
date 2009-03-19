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
package org.eclipse.gyrex.toolkit.rap.internal.ui.widgets;


import org.eclipse.gyrex.toolkit.CWT;
import org.eclipse.gyrex.toolkit.content.BooleanContent;
import org.eclipse.gyrex.toolkit.content.ContentObject;
import org.eclipse.gyrex.toolkit.rap.internal.ui.content.IContentAdapter;
import org.eclipse.gyrex.toolkit.rap.internal.ui.validation.DialogFieldValidator;
import org.eclipse.gyrex.toolkit.rap.internal.ui.validation.ValidationContext;
import org.eclipse.gyrex.toolkit.rap.internal.ui.validation.ValidationResult;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.widgets.RadioButton;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Renders a {@link RadioButton}.
 */
public class CWTRadioButton extends CWTDialogField<RadioButton> {

	private static final DialogFieldValidator DIALOG_FIELD_VALIDATOR = new DialogFieldValidator() {

		@Override
		public ValidationResult validate(final CWTDialogField dialogField, final ValidationContext context) {
			final CWTRadioButton radioButton = (CWTRadioButton) dialogField;
			if (radioButton.getWidget().isRequired()) {
				// check if the group is already ok
				final String group = radioButton.getGroup();
				if (context.getBoolean(group, false)) {
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

	private static final IContentAdapter CONTENT_ADAPTER = new IContentAdapter() {

		@Override
		public ContentObject getContent(final CWTWidget widget) {
			final CWTRadioButton radioButton = (CWTRadioButton) widget;
			return new BooleanContent(radioButton.isSelected());
		}

		public boolean hasContent(final CWTWidget widget) {
			// we always have content
			return true;
		}

		@Override
		public void setContent(final CWTWidget widget, final ContentObject content) {
			final CWTRadioButton radioButton = (CWTRadioButton) widget;
			final BooleanContent radioButtonEntry = (BooleanContent) content;
			final Button button = radioButton.getRadioButtonControl();
			if ((null != button) && !button.isDisposed()) {
				button.setSelection(radioButtonEntry.getValue());
			}
		}

	};

	private Button radioButtonControl;

	private Label emptyLabelControl;

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTDialogField#createDescriptionControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createDescriptionControl(final Composite parent) {
		// no description
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTDialogField#createLabelControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createLabelControl(final Composite parent) {
		// no label
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTDialogField#createWidgetControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createWidgetControl(final Composite parent) {
		// empty label
		emptyLabelControl = getToolkit().getFormToolkit().createLabel(parent, EMTPY_STRING);

		// no label , no description, just button
		radioButtonControl = getToolkit().getFormToolkit().createButton(parent, getLabelText(), SWT.RADIO);
		setObservable(SWTObservables.observeSelection(radioButtonControl));
		setContentAdapter(CONTENT_ADAPTER);
		setDialogFieldValidator(DIALOG_FIELD_VALIDATOR);

		return radioButtonControl;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTDialogField#fillIntoGrid(int)
	 */
	@Override
	protected void fillIntoGrid(final int columns) {
		if (columns < 2) {
			CWT.error(CWT.ERROR_INVALID_ARGUMENT, "at least 2 columns needed");
		}

		GridDataFactory.generate(emptyLabelControl, 1, 1);
		GridDataFactory.generate(getRadioButtonControl(), columns - 1, 1);
	}

	/**
	 * Returns the group id of the radio button.
	 * 
	 * @return the group id
	 */
	public String getGroup() {
		return getGroup(getWidget());
	}

	private String getGroup(final RadioButton radioButton) {
		final Container parent = radioButton.getParent();
		return null == parent ? radioButton.getId() : parent.getId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTDialogField#getNumberOfColumns()
	 */
	@Override
	protected int getNumberOfColumns() {
		return 2;
	}

	/**
	 * Returns the radio button control.
	 * 
	 * @return the radio button control (maybe <code>null</code> if not created
	 *         yet)
	 */
	protected Button getRadioButtonControl() {
		return radioButtonControl;
	}

	/**
	 * Indicates if the radio button is selected.
	 * 
	 * @return <code>true</code> if the radio button is selected,
	 *         <code>false</code> otherwise
	 */
	public boolean isSelected() {
		final Button button = getRadioButtonControl();
		if (null != button) {
			return button.getSelection();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gyrex.toolkit.gwt.client.internal.ui.widgets.DialogFieldComposite#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		final Button button = getRadioButtonControl();
		if ((null == button) && !button.isDisposed()) {
			button.setEnabled(enabled);
		}
	}
}
