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
package org.eclipse.gyrex.toolkit.layout;

/**
 * Layout hint for the {@link GridLayout}.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 */
public class GridLayoutHint extends LayoutHint {

	/**
	 * {@link #spanColumns} specifies the number of column cells that the widget
	 * will take up. The default value is 1.
	 */
	public int spanColumns = 1;

	/**
	 * {@link #spanRows} specifies the number of row cells that the widget will
	 * take up. The default value is 1.
	 */
	public int spanRows = 1;

	/**
	 * Creates and returns a new grid layout hint.
	 * 
	 * @param spanColumns
	 *            the number of {@link #spanColumns column cells to span}
	 */
	public GridLayoutHint(int spanColumns) {
		this.spanColumns = spanColumns;
	}
}
