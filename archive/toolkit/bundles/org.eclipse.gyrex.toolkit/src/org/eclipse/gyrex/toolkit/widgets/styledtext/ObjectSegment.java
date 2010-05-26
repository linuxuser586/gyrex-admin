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
package org.eclipse.gyrex.toolkit.widgets.styledtext;

/**
 * Base class for elements representing objects.
 */
public abstract class ObjectSegment extends ParagraphSegment {

	/**
	 * Vertical alignment.
	 */
	public static enum VerticalAlign {
		TOP, MIDDLE, BOTTOM
	}

	private final VerticalAlign verticalAlign;

	/**
	 * Creates a new instance.
	 */
	ObjectSegment(final VerticalAlign verticalAlign, final boolean wordWrapAllowed, final String tooltipText) {
		super(wordWrapAllowed, tooltipText);
		this.verticalAlign = verticalAlign;
	}

	/**
	 * Returns the vertical alignment of this object.
	 * 
	 * @return the vertical alignment
	 */
	public VerticalAlign getVerticalAlign() {
		return verticalAlign;
	}
}
