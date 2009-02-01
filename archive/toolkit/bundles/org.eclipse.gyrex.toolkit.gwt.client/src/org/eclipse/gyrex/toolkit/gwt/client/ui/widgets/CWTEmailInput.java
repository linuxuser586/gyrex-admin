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
package org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets;

import org.eclipse.cloudfree.toolkit.gwt.client.ui.internal.validation.DialogFieldValidator;
import org.eclipse.cloudfree.toolkit.gwt.client.ui.internal.validation.ValidationContext;
import org.eclipse.cloudfree.toolkit.gwt.client.ui.internal.validation.ValidationResult;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SEmailInput;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.STextInput;

/**
 * Composite for <code>org.eclipse.cloudfree.toolkit.widgets.EmailInput</code>.
 */
public class CWTEmailInput extends CWTTextInput {
	static final DialogFieldValidator emailInputValidator = new DialogFieldValidator() {

		@Override
		public ValidationResult validate(final CWTDialogField dialogField, final ValidationContext context) {
			final CWTEmailInput textInput = (CWTEmailInput) dialogField;
			final STextInput sTextInput = textInput.getSEmailInput();

			// get text
			final String text = textInput.getTextBoxWidget().getText();

			// required fields must contain some text
			if (sTextInput.required) {
				if (text.length() <= 0) {
					return ValidationResult.ERROR;
				}
			}

			// validate email address
			if ((text.length() > 0) && !text.matches("[\\w\\.\\-]{2,50}[@]{1}[\\w\\.\\-]{2,50}[\\.]{1}[a-zA-Z]{2,5}")) {
				return ValidationResult.ERROR;
			}

			// text fields are valid
			return ValidationResult.OK;
		}
	};

	private SEmailInput getSEmailInput() {
		return (SEmailInput) getSerializedWidget();
	}

}
