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
package org.eclipse.gyrex.toolkit.widgets;

import org.eclipse.gyrex.toolkit.CWT;
import org.eclipse.gyrex.toolkit.content.StructuredContent;

/**
 * A dialog field that is capable of presenting structured data (eg. a list of
 * elements) in one ore more columns and rows.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 */
public abstract class StructuredDialogField<T extends StructuredContent> extends DialogField<T> {

	/** serialVersionUID */
	private static final long serialVersionUID = 8360413893896191140L;

	static int checkStyle(int style) {
		style = checkBits(style, CWT.SELECT_SINGLE, CWT.SELECT_MULTI, 0, 0, 0, 0);
		style = checkBits(style, CWT.SELECT_ROW, CWT.SELECT_CELL, 0, 0, 0, 0);
		return style;
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 *            the widget id
	 * @param parent
	 *            the widget parent
	 * @param style
	 *            the widget style
	 * @param contentType
	 *            the content type
	 */
	public StructuredDialogField(final String id, final Container parent, final int style, final Class<T> contentType) {
		super(id, parent, checkStyle(style), contentType);
	}
}
