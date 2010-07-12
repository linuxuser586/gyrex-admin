/*******************************************************************************
 * Copyright (c) 2008, 2010 Gunnar Wagenknecht and others.
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

import org.eclipse.gyrex.toolkit.content.NoContent;

/**
 * A dialog field that presents data in a drop down list.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 */
public class DropDown extends StructuredDialogField<NoContent> {

	/** serialVersionUID */
	private static final long serialVersionUID = 701063106767451943L;

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
	public DropDown(final String id, final Container parent, final int style) {
		super(id, parent, style, NoContent.class);
	}
}
