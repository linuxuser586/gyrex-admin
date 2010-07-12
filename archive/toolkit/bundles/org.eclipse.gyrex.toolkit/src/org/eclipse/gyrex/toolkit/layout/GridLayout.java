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
 * Aligns children in a grid.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 */
public class GridLayout extends Layout {

	/**
	 * numberOfColumns specifies the number of cell columns in the layout.
	 * <p>
	 * The default value is <code>1</code>.
	 * </p>
	 */
	public int numberOfColumns = 1;

	/**
	 * makeColumnsEqualWidth specifies whether all columns in the layout will be
	 * forced to have the same width.
	 * <p>
	 * The default value is <code>false</code>.
	 * </p>
	 */
	public boolean makeColumnsEqualWidth = false;

	/**
	 * Creates and returns new grid layout using the specified number of
	 * columns.
	 * 
	 * @param numberOfColumns
	 *            the number of columns
	 * @param makeColumnsEqualWidth
	 *            to set whether all columns should have the same width
	 */
	public GridLayout(final int numberOfColumns, final boolean makeColumnsEqualWidth) {
		this.numberOfColumns = numberOfColumns;
		this.makeColumnsEqualWidth = makeColumnsEqualWidth;
	}
}
