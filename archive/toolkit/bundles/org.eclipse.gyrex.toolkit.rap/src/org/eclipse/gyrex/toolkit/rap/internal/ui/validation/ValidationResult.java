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

public class ValidationResult {

	public static final int RESULT_OK = 0;
	public static final int RESULT_WARNING = 1;
	public static final int RESULT_ERROR = 2;
	public static final int RESULT_ERROR_BUT_CONTINUE = 3;

	public static final ValidationResult OK = new ValidationResult(RESULT_OK);
	public static final ValidationResult WARNING = new ValidationResult(RESULT_WARNING);
	public static final ValidationResult ERROR = new ValidationResult(RESULT_ERROR);

	final int result;
	final String group;

	public ValidationResult(final int result) {
		this(result, null);
	}

	public ValidationResult(final int result, final String group) {
		this.result = result;
		this.group = group;
	}
}
