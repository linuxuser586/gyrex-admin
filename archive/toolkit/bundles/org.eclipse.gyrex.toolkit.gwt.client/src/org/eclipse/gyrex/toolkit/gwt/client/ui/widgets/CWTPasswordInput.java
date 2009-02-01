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

import com.google.gwt.user.client.ui.PasswordTextBox;

/**
 * Composite for
 * <code>org.eclipse.cloudfree.toolkit.widgets.PasswordInput</code>.
 */
public class CWTPasswordInput extends CWTTextInput {

	static class PasswordInputPanel extends TextInputPanel {

		public PasswordInputPanel() {
			super(new PasswordTextBox(), "passwordInput");
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets.CWTTextInput#createTextInput()
	 */
	@Override
	protected TextInputPanel createTextInput() {
		final TextInputPanel textInputPanel = new PasswordInputPanel();
		textInputPanel.setStyleName("cwt-PasswordInput");
		return textInputPanel;
	}
}
