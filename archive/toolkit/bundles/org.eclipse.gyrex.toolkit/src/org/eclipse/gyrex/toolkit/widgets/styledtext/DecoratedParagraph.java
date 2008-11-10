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
 * A decorated paragraph (<code>&lt;li&gt;</code>) element.
 * <p>
 * The decorated paragraph displays a {@link #getDecoration() decoration}
 * followed by the {@link #getSegments() segments}.
 * </p>
 */
public class DecoratedParagraph extends Paragraph {

	/**
	 * The available decorations.
	 */
	public static enum Decoration {
		BULLET, TEXT, IMAGE
	}

	/** serialVersionUID */
	private static final long serialVersionUID = 458497427446235226L;

	private final Decoration decoration;
	private final ImageResource decorationImage;

	private final String decorationText;

	DecoratedParagraph() {
		this(Decoration.BULLET, null, null);
	}

	private DecoratedParagraph(final Decoration decoration, final ImageResource decorationImage, final String decorationText) {
		this.decoration = decoration;
		this.decorationImage = decorationImage;
		this.decorationText = decorationText;
	}

	DecoratedParagraph(final ImageResource decorationImage) {
		this(Decoration.IMAGE, decorationImage, null);
	}

	DecoratedParagraph(final String decorationText) {
		this(Decoration.TEXT, null, decorationText);
	}

	/**
	 * Returns the decoration used by this paragraph.
	 * 
	 * @return the decoration
	 */
	public Decoration getDecoration() {
		return decoration;
	}

	/**
	 * Returns the decoration image if the {@link #getDecoration() decoration}
	 * is {@link Decoration#IMAGE}.
	 * 
	 * @return the decoration image, or <code>null</code> if not set or the
	 *         decoration is not is {@link Decoration#IMAGE}
	 */
	public ImageResource getDecorationImage() {
		return decorationImage;
	}

	/**
	 * Returns the decoration text if the {@link #getDecoration() decoration} is
	 * {@link Decoration#TEXT}.
	 * 
	 * @return the decoration text, or <code>null</code> if not set or the
	 *         decoration is not is {@link Decoration#TEXT}
	 */
	public String getDecorationText() {
		return decorationText;
	}
}
