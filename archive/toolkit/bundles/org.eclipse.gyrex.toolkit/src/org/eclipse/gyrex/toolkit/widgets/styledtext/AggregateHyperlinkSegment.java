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

import java.util.ArrayList;
import java.util.List;

/**
 * This segment contains a collection of images and links that all belong to one
 * logical hyperlink.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class AggregateHyperlinkSegment extends ParagraphSegment implements IHyperlinkSegment {

	private final String href;
	private final List<IHyperlinkSegment> segments = new ArrayList<IHyperlinkSegment>(2);

	/**
	 * Creates a new instance.
	 * 
	 * @param href
	 * @param wordWrapAllowed
	 * @param tooltip
	 */
	AggregateHyperlinkSegment(final String href, final boolean wordWrapAllowed, final String tooltip) {
		super(true, tooltip);
		this.href = href;
	}

	void add(final ImageHyperlinkSegment segment) {
		segments.add(segment);
	}

	void add(final TextHyperlinkSegment segment) {
		segments.add(segment);
	}

	public String getHref() {
		return href;
	}

	/**
	 * Returns the segments.
	 * 
	 * @return the segments
	 */
	public IHyperlinkSegment[] getSegments() {
		return segments.toArray(new IHyperlinkSegment[segments.size()]);
	}

	@Override
	public String getText() {
		final StringBuffer buf = new StringBuffer();
		for (int i = 0; i < segments.size(); i++) {
			final IHyperlinkSegment segment = segments.get(i);
			buf.append(segment.getText());
		}
		return buf.toString();
	}

	@Override
	public String getTooltipText() {
		if (segments.size() > 0) {
			return ((ParagraphSegment) segments.get(0)).getTooltipText();
		}
		return super.getTooltipText();
	}

}
