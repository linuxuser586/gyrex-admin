/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Gunnar Wagenknecht - fork for Gyrex Admin UI 
 *******************************************************************************/
package org.eclipse.gyrex.admin.ui.internal.widgets;

import org.eclipse.core.runtime.IStatus;

/**
 * Used in selection dialogs to validate selections
 * 
 * @since 1.1
 */
public interface ISelectionStatusValidator {

	/**
	 * Validates an array of elements and returns the resulting status.
	 * 
	 * @param selection
	 *            The elements to validate
	 * @return The resulting status
	 */
	IStatus validate(Object[] selection);

}
