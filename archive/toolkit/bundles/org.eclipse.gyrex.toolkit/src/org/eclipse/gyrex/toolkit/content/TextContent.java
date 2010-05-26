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
package org.eclipse.gyrex.toolkit.content;

/**
 * A text content.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 */
public class TextContent extends ContentObject {

	/** serialVersionUID */
	private static final long serialVersionUID = -5945936249354971412L;

	/** text */
	private final String text;

	/**
	 * Creates a new instance.
	 * 
	 * @param text
	 */
	public TextContent(final String text) {
		super();
		this.text = text;
	}

	/**
	 * Returns the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Text[%s]", text);
	}
}
