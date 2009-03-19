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
import org.eclipse.gyrex.toolkit.content.ContentObject;
import org.eclipse.gyrex.toolkit.content.TextContent;
import org.eclipse.gyrex.toolkit.rap.internal.ui.content.IContentAdapter;
import org.eclipse.gyrex.toolkit.rap.internal.ui.validation.DialogFieldValidator;
import org.eclipse.gyrex.toolkit.rap.internal.ui.validation.ValidationContext;
import org.eclipse.gyrex.toolkit.rap.internal.ui.validation.ValidationResult;
import org.eclipse.gyrex.toolkit.widgets.TextInput;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * Renders {@link TextInput}.
 */
public class CWTTextInput extends CWTDialogField<TextInput> {

	private static final DialogFieldValidator TEXT_INPUT_VALIDATOR = new DialogFieldValidator() {

		@Override
		public ValidationResult validate(final CWTDialogField dialogField, final ValidationContext context) {
			final CWTTextInput textInput = (CWTTextInput) dialogField;
			// required fields must contain some text
			if (textInput.getWidget().isRequired()) {
				if (textInput.getText().length() <= 0) {
					return ValidationResult.ERROR;
				}
			}

			// text fields are valid
			return ValidationResult.OK;
		}
	};

	private static final IContentAdapter CONTENT_ADAPTER = new IContentAdapter() {

		/* (non-Javadoc)
		 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.content.IContentAdapter#getContent(org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTWidget)
		 */
		@Override
		public ContentObject getContent(final CWTWidget widget) {
			final CWTTextInput textInput = (CWTTextInput) widget;
			return new TextContent(textInput.getText());
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.content.IContentAdapter#hasContent(org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTWidget)
		 */
		@Override
		public boolean hasContent(final CWTWidget widget) {
			final CWTTextInput textInput = (CWTTextInput) widget;
			return (textInput.getText().length() > 0);
		}

		@Override
		public void setContent(final CWTWidget widget, final ContentObject content) {
			final CWTTextInput textInput = (CWTTextInput) widget;
			final TextContent textEntry = (TextContent) content;
			textInput.setText(textEntry.getText());
		}

	};

	private Text textControl;

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTDialogField#createWidgetControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createWidgetControl(final Composite parent) {
		// create label
		createLabelControl(parent);

		// create text
		textControl = getToolkit().getFormToolkit().createText(parent, EMTPY_STRING, getWidget().isReadOnly() ? SWT.READ_ONLY : SWT.NONE);
		if (getWidget().getMaxLength() > 0) {
			textControl.setTextLimit(getNumberOfColumns());
		}
		if (null != getWidget().getToolTipText()) {
			textControl.setToolTipText(getWidget().getToolTipText());
		}
		setObservable(SWTObservables.observeText(textControl, SWT.FocusOut));
		setContentAdapter(CONTENT_ADAPTER);
		setDialogFieldValidator(TEXT_INPUT_VALIDATOR);

		// create description
		createDescriptionControl(parent);

		return textControl;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTDialogField#fillIntoGrid(int)
	 */
	@Override
	protected void fillIntoGrid(final int columns) {
		if (columns < getNumberOfColumns()) {
			CWT.error(CWT.ERROR_INVALID_ARGUMENT, "unsifficient number of columns");
		}

		if (null == getDescriptionControl()) {
			GridDataFactory.generate(getLabelControl(), 1, 1);
			GridDataFactory.generate(getTextControl(), columns - 1, 1);
		} else {
			GridDataFactory.generate(getLabelControl(), 1, 2);
			GridDataFactory.generate(getTextControl(), columns - 1, 1);
			GridDataFactory.generate(getDescriptionControl(), columns - 1, 1);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTDialogField#getNumberOfColumns()
	 */
	@Override
	protected int getNumberOfColumns() {
		return null != getDescriptionControl() ? 3 : 2;
	}

	/**
	 * Returns the current text value.
	 * 
	 * @return the current text value
	 */
	protected String getText() {
		final Text text = getTextControl();
		if ((null != text) && !text.isDisposed()) {
			return text.getText();
		}
		return EMTPY_STRING;
	}

	/**
	 * Returns the text control.
	 * 
	 * @return the text control
	 */
	protected Text getTextControl() {
		return textControl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gyrex.toolkit.gwt.client.internal.ui.widgets.DialogFieldComposite#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		final Text textControl = getTextControl();
		if ((null != textControl) && !textControl.isDisposed()) {
			textControl.setEnabled(enabled);
		}
		super.setEnabled(enabled);
	}

	/**
	 * Set the text value.
	 * 
	 * @param text
	 *            the text to set
	 */
	protected void setText(final String text) {
		final Text textControl = getTextControl();
		if ((null != textControl) && !textControl.isDisposed()) {
			textControl.setText(null != text ? text : EMTPY_STRING);
		}
	}
}
