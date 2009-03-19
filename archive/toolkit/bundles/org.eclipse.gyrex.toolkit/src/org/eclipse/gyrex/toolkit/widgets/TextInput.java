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

import org.eclipse.gyrex.toolkit.content.TextContent;

/**
 * A dialog field with a single-line text input field and optionally an action
 * button.
 * <p>
 * If the actionId is not null a button will be created next to the text input
 * field and execute that action when it is clicked.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 */
public class TextInput extends DialogField<TextContent> {

	/** serialVersionUID */
	private static final long serialVersionUID = -7749117712214141283L;
	private int maxLength;

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
	public TextInput(final String id, final Container parent, final int style) {
		super(id, parent, style, TextContent.class);
	}

	/**
	 * Returns the maximum number of allowed characters.
	 * <p>
	 * A value of zero or less will be interpreted as unlimited length.
	 * </p>
	 * 
	 * @return the maximum number of allowed characters.
	 */
	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * Sets the maximum number of allowed characters.
	 * 
	 * @param maxLength
	 *            the maxLength to set
	 */
	public void setMaxLength(final int maxLength) {
		this.maxLength = maxLength;
	}

}
