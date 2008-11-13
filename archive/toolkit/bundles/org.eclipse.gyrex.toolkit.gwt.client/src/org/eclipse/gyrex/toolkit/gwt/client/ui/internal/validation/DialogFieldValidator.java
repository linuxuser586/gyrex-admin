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
package org.eclipse.cloudfree.toolkit.gwt.client.ui.internal.validation;

import org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets.CWTDialogField;

public abstract class DialogFieldValidator {

	/**
	 * Determines if the dialog field is valid
	 * 
	 * @param dialogField
	 * @return
	 */
	public abstract ValidationResult validate(CWTDialogField dialogField, ValidationContext context);
}
