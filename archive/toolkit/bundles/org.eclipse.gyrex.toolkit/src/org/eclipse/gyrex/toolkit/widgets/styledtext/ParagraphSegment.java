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
 * A segment.
 * <p>
 * Segments are member of {@link Paragraph paragraphs} and have a tooltip
 * attached.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class ParagraphSegment {

	private final String tooltipText;
	private final boolean wordWrapAllowed;

	/**
	 * Creates a new instance.
	 * 
	 * @param wordWrapAllowed
	 */
	ParagraphSegment(final boolean wordWrapAllowed, final String tooltipText) {
		this.wordWrapAllowed = wordWrapAllowed;
		this.tooltipText = tooltipText;
	}

	/**
	 * Returns the tool tip of this segment or <code>null</code> if not defined.
	 * 
	 * @return tooltip or <code>null</code>.
	 */
	public String getTooltipText() {
		return tooltipText;
	}

	/**
	 * Indicates if word wrapping is allowed.
	 * 
	 * @return <code>true</code> if the text can be word wrapped,
	 *         <code>false</code> otherwise
	 */
	public boolean isWordWrapAllowed() {
		return wordWrapAllowed;
	}
}
