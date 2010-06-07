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
package org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets;


import com.google.gwt.user.client.rpc.IsSerializable;

import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.resources.SImageResource;

/**
 * Serializable StyledText
 */
public class SStyledText extends SWidget {

	public static class SAggregateHyperlinkSegment extends SParagraphSegment {
		public SHyperlinkSegment[] segments;
		public String href;
	}

	public static class SBreakSegment extends SParagraphSegment {
		// empty
	}

	public static class SDecoratedParagraph extends SParagraph {
		public static enum SDecorcation {
			BULLET, TEXT, IMAGE
		}

		public SDecorcation decoration;
		public SImageResource decorationImage;
		public String decorationText;
	}

	public static interface SHyperlinkSegment extends IsSerializable {
		// empty
	}

	public static class SImageHyperlinkSegment extends SImageSegment implements SHyperlinkSegment {
		public String href;
	}

	public static class SImageSegment extends SObjectSegment {
		public SImageResource image;
	}

	public static class SObjectSegment extends SParagraphSegment {
		public static enum VerticalAlign {
			TOP, MIDDLE, BOTTOM
		}

		public VerticalAlign verticalAlign;
	}

	public static class SParagraph implements IsSerializable {
		public SParagraphSegment[] segments;
	}

	public static class SParagraphSegment implements IsSerializable {
		public String tooltip;
		public boolean wordwrap;
	}

	public static class STextHyperlinkSegment extends STextSegment implements SHyperlinkSegment {
		public String href;
	}

	public static class STextSegment extends SParagraphSegment {
		public String text;
	}

	public SParagraph[] paragraphs;

}
