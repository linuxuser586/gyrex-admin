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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Paragraph (<code>&lt;p&gt;</code>) element.
 * <p>
 * The paragraph element consists of {@link #getSegments() segments}.
 * </p>
 */
public class Paragraph implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 6679790318321420629L;

	private static final String HTTPS = "https://"; //$NON-NLS-1$
	private static final String HTTP = "http://"; //$NON-NLS-1$

	private List<ParagraphSegment> segments;

	Paragraph() {
	}

	void addSegment(final ParagraphSegment segment) {
		if (segments == null) {
			segments = new ArrayList<ParagraphSegment>();
		}
		segments.add(segment);
	}

	private int findUrlStart(final String text, final int fromIndex) {
		final int httpIndex = text.indexOf(HTTP, fromIndex);
		final int httpsIndex = text.indexOf(HTTPS, fromIndex);

		if (httpIndex == -1) {
			return httpsIndex;
		} else if (httpsIndex == -1) {
			return httpIndex;
		} else {
			return httpIndex < httpsIndex ? httpIndex : httpsIndex;
		}

	}

	/**
	 * Return the segments of this paragraph.
	 * 
	 * @return the segments
	 */
	public ParagraphSegment[] getSegments() {
		if (segments == null) {
			return new ParagraphSegment[0];
		}
		return segments.toArray(new ParagraphSegment[segments.size()]);
	}

	void parseRegularText(final String text, final boolean expandURLs, final boolean wrapAllowed) {
		if (text.length() == 0) {
			return;
		}
		if (expandURLs) {
			int loc = findUrlStart(text, 0);
			if (loc == -1) {
				addSegment(new TextSegment(text, wrapAllowed));
			} else {
				int textLoc = 0;
				while (loc != -1) {
					addSegment(new TextSegment(text.substring(textLoc, loc), wrapAllowed));
					boolean added = false;
					for (textLoc = loc; textLoc < text.length(); textLoc++) {
						final char c = text.charAt(textLoc);
						if (Character.isSpaceChar(c)) {
							addSegment(new TextHyperlinkSegment(text.substring(loc, textLoc)));
							added = true;
							break;
						}
					}
					if (!added) {
						// there was no space - just end of text
						addSegment(new TextHyperlinkSegment(text.substring(loc)));
						break;
					}
					loc = findUrlStart(text, textLoc);
				}
				if (textLoc < text.length()) {
					addSegment(new TextSegment(text.substring(textLoc), wrapAllowed));
				}
			}
		} else {
			addSegment(new TextSegment(text, wrapAllowed));
		}
	}
}
