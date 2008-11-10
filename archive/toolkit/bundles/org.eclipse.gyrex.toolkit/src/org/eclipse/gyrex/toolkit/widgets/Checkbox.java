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
package org.eclipse.cloudfree.toolkit.widgets;

import org.eclipse.cloudfree.toolkit.content.SelectionFlagContent;

/**
 * A dialog field with a check-box to represent a boolean state (on/off, yes/no,
 * etc.).
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 */
public class Checkbox extends DialogField<SelectionFlagContent> {
	/** serialVersionUID */
	private static final long serialVersionUID = -5770686514166691314L;

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 *            the widget id
	 * @param parent
	 *            the widget parent
	 * @param style
	 *            the widget style
	 */
	public Checkbox(final String id, final Container parent, final int style) {
		super(id, parent, style, SelectionFlagContent.class);
	}
}
