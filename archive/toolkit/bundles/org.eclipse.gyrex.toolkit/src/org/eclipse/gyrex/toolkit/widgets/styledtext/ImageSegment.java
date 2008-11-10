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

import org.eclipse.cloudfree.toolkit.resources.ImageResource;

/**
 * Image (<code>&lt;img&gt;</code>) element.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ImageSegment extends ObjectSegment {

	private final ImageResource imageResource;

	/**
	 * Creates a new instance.
	 * 
	 * @param imageResource
	 * @param verticalAlign
	 * @param wordWrapAllowed
	 * @param tooltipText
	 */
	ImageSegment(final ImageResource imageResource, final VerticalAlign verticalAlign, final boolean wordWrapAllowed, final String tooltipText) {
		super(verticalAlign, wordWrapAllowed, tooltipText);
		this.imageResource = imageResource;
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param imageResource
	 * @param verticalAlign
	 * @param tooltipText
	 */
	ImageSegment(final ImageResource imageResource, final VerticalAlign verticalAlign, final String tooltipText) {
		this(imageResource, verticalAlign, true, tooltipText);
	}

	/**
	 * Returns the image resource.
	 * 
	 * @return the image resource
	 */
	public ImageResource getImageResource() {
		return imageResource;
	}
}
