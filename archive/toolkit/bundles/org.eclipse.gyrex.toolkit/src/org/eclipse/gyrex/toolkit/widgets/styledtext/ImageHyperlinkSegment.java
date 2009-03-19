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

import org.eclipse.gyrex.toolkit.resources.ImageResource;

/**
 * An image hyperlink.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ImageHyperlinkSegment extends ImageSegment implements IHyperlinkSegment {
	private final String href;

	/**
	 * Creates a new instance.
	 * 
	 * @param href
	 * @param imageResource
	 * @param verticalAlign
	 * @param wordWrapAllowed
	 * @param tooltipText
	 */
	ImageHyperlinkSegment(final String href, final ImageResource imageResource, final VerticalAlign verticalAlign, final boolean wordWrapAllowed, final String tooltipText) {
		super(imageResource, verticalAlign, wordWrapAllowed, tooltipText);
		this.href = href;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.widgets.styledtext.IHyperlinkSegment#getHref()
	 */
	@Override
	public String getHref() {
		return href;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.widgets.styledtext.IHyperlinkSegment#getText()
	 */
	@Override
	public String getText() {
		return null;
	}
}
