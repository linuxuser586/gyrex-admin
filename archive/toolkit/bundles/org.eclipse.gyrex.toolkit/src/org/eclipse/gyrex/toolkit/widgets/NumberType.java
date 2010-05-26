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
package org.eclipse.gyrex.toolkit.widgets;


/**
 * The number type used to describe the input type.
 */
public enum NumberType {
	/** number type for integer */
	INTEGER,

	/** number type for decimal values */
	DECIMAL,

	/** number type for currency values */
	CURRENCY,

	/** number type for percentage values ranging from 0% till 100% */
	PERCENTAGE;
}
