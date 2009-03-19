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
package org.eclipse.gyrex.toolkit.widgets.styledtext;

/**
 * A text hyperlink.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TextHyperlinkSegment extends TextSegment implements IHyperlinkSegment {
	private final String href;

	TextHyperlinkSegment(final String href) {
		this(href, href, null, false);
	}

	TextHyperlinkSegment(final String text, final String href) {
		this(text, href, null, false);
	}

	TextHyperlinkSegment(final String text, final String href, final String tooltipText) {
		this(text, href, tooltipText, false);
	}

	TextHyperlinkSegment(final String text, final String href, final String tooltipText, final boolean wordWrapAllowed) {
		super(text, wordWrapAllowed, tooltipText);
		this.href = href;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.widgets.styledtext.IHyperlinkSegment#getHref()
	 */
	@Override
	public String getHref() {
		return href;
	}

}