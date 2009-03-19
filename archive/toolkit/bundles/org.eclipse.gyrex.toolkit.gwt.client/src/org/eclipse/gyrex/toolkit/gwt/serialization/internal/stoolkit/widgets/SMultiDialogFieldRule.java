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
package org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets;

/**
 * Serializable MultiDialogFieldRule.
 */
public class SMultiDialogFieldRule extends SDialogFieldRule {

	public static final int C_OR = 0;
	public static final int C_AND = 1;

	public int condition;
	public SDialogFieldRule[] rules;

}
