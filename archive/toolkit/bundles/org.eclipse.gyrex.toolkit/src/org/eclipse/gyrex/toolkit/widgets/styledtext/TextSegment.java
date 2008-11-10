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
package org.eclipse.cloudfree.toolkit.widgets.styledtext;

/**
 * Segment of paragraphs containing text.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TextSegment extends ParagraphSegment {

	private final String text;

	TextSegment(final String text) {
		this(text, true);
	}

	TextSegment(final String text, final boolean wrapAllowed) {
		this(text, wrapAllowed, null);
	}

	TextSegment(final String text, final boolean wrapAllowed, final String tooltipText) {
		super(wrapAllowed, tooltipText);
		this.text = text;
	}

	/**
	 * Returns the text.
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

}
