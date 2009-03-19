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
package org.eclipse.gyrex.toolkit.rap.internal.ui.validation;

import org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTDialogField;

public abstract class DialogFieldValidator {

	/**
	 * Determines if the dialog field is valid
	 * 
	 * @param dialogField
	 *            the dialog field
	 * @param context
	 *            the validation context
	 * @return
	 */
	public abstract ValidationResult validate(CWTDialogField dialogField, ValidationContext context);
}
