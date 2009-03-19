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
package org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.widgets;


import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.resources.SImageResource;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText.SAggregateHyperlinkSegment;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText.SBreakSegment;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText.SDecoratedParagraph;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText.SHyperlinkSegment;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText.SImageHyperlinkSegment;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText.SImageSegment;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText.SObjectSegment;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText.SParagraph;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText.SParagraphSegment;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText.STextHyperlinkSegment;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText.STextSegment;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText.SObjectSegment.VerticalAlign;
import org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.ToolkitSerialization;
import org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.WidgetSerializer;
import org.eclipse.gyrex.toolkit.resources.ImageResource;
import org.eclipse.gyrex.toolkit.widgets.StyledText;
import org.eclipse.gyrex.toolkit.widgets.Widget;
import org.eclipse.gyrex.toolkit.widgets.styledtext.AggregateHyperlinkSegment;
import org.eclipse.gyrex.toolkit.widgets.styledtext.BreakSegment;
import org.eclipse.gyrex.toolkit.widgets.styledtext.DecoratedParagraph;
import org.eclipse.gyrex.toolkit.widgets.styledtext.IHyperlinkSegment;
import org.eclipse.gyrex.toolkit.widgets.styledtext.ImageHyperlinkSegment;
import org.eclipse.gyrex.toolkit.widgets.styledtext.ImageSegment;
import org.eclipse.gyrex.toolkit.widgets.styledtext.ObjectSegment;
import org.eclipse.gyrex.toolkit.widgets.styledtext.Paragraph;
import org.eclipse.gyrex.toolkit.widgets.styledtext.ParagraphSegment;
import org.eclipse.gyrex.toolkit.widgets.styledtext.TextHyperlinkSegment;
import org.eclipse.gyrex.toolkit.widgets.styledtext.TextSegment;

/**
 * {@link StyledText} serializer.
 */
public class StyledTextSerializer extends WidgetSerializer {

	private SParagraph serialize(final Paragraph paragraph) {
		SParagraph sParagraph;
		if (DecoratedParagraph.class.equals(paragraph.getClass())) {
			sParagraph = new SDecoratedParagraph();
			switch (((DecoratedParagraph) paragraph).getDecoration()) {
				case TEXT:
					((SDecoratedParagraph) sParagraph).decoration = SDecoratedParagraph.SDecorcation.TEXT;
					((SDecoratedParagraph) sParagraph).decorationText = ((DecoratedParagraph) paragraph).getDecorationText();
					break;
				case IMAGE:
					((SDecoratedParagraph) sParagraph).decoration = SDecoratedParagraph.SDecorcation.IMAGE;
					((SDecoratedParagraph) sParagraph).decorationImage = serializeImageResource(((DecoratedParagraph) paragraph).getDecorationImage());
					break;
				case BULLET:
				default:
					((SDecoratedParagraph) sParagraph).decoration = SDecoratedParagraph.SDecorcation.BULLET;
					break;
			}
		} else {
			sParagraph = new SParagraph();
		}
		final ParagraphSegment[] segments = paragraph.getSegments();
		sParagraph.segments = new SParagraphSegment[segments.length];
		for (int i = 0; i < segments.length; i++) {
			sParagraph.segments[i] = serialize(segments[i]);
		}
		return sParagraph;
	}

	private SParagraphSegment serialize(final ParagraphSegment paragraphSegment) {
		if (BreakSegment.class.equals(paragraphSegment.getClass())) {
			return new SBreakSegment();
		} else if (BreakSegment.class.equals(paragraphSegment.getClass())) {
			return new SBreakSegment();
		} else if (TextSegment.class.equals(paragraphSegment.getClass())) {
			return serializeTextSegment((TextSegment) paragraphSegment, new STextSegment());
		} else if (TextHyperlinkSegment.class.equals(paragraphSegment.getClass())) {
			return serializeTextHyperlinkSegment((TextHyperlinkSegment) paragraphSegment, new STextHyperlinkSegment());
		} else if (ImageSegment.class.equals(paragraphSegment.getClass())) {
			return serializeImageSegment((ImageSegment) paragraphSegment, new SImageSegment());
		} else if (ImageHyperlinkSegment.class.equals(paragraphSegment.getClass())) {
			return serializeImageHyperlinkSegment((ImageHyperlinkSegment) paragraphSegment, new SImageHyperlinkSegment());
		} else if (AggregateHyperlinkSegment.class.equals(paragraphSegment.getClass())) {
			return serializeAggregateHyperlinkSegment((AggregateHyperlinkSegment) paragraphSegment, new SAggregateHyperlinkSegment());
		}
		throw new IllegalStateException("unable to serialzes: " + paragraphSegment.getClass().getName());
	}

	@Override
	public ISerializedWidget serialize(final Widget widget, final SContainer parent) {
		final StyledText text = (StyledText) widget;
		final SStyledText sText = new SStyledText();
		final Paragraph[] paragraphs = text.getText();
		final SParagraph[] sParagraphs = new SParagraph[paragraphs.length];
		for (int i = 0; i < sParagraphs.length; i++) {
			sParagraphs[i] = serialize(paragraphs[i]);
		}
		sText.paragraphs = sParagraphs;
		return populateAttributes(text, sText, parent);
	}

	private SParagraphSegment serializeAggregateHyperlinkSegment(final AggregateHyperlinkSegment aggregateHyperlinkSegment, final SAggregateHyperlinkSegment sAggregateHyperlinkSegment) {
		final IHyperlinkSegment[] segments = aggregateHyperlinkSegment.getSegments();
		sAggregateHyperlinkSegment.segments = new SHyperlinkSegment[segments.length];
		for (int i = 0; i < segments.length; i++) {
			final IHyperlinkSegment hyperlinkSegment = segments[i];
			if (TextHyperlinkSegment.class.equals(hyperlinkSegment.getClass())) {
				sAggregateHyperlinkSegment.segments[i] = (SHyperlinkSegment) serializeTextHyperlinkSegment((TextHyperlinkSegment) hyperlinkSegment, new STextHyperlinkSegment());
			} else if (ImageHyperlinkSegment.class.equals(hyperlinkSegment.getClass())) {
				sAggregateHyperlinkSegment.segments[i] = (SHyperlinkSegment) serializeImageHyperlinkSegment((ImageHyperlinkSegment) hyperlinkSegment, new SImageHyperlinkSegment());
			}

		}
		return serializeParagraphSegment(aggregateHyperlinkSegment, sAggregateHyperlinkSegment);
	}

	private SParagraphSegment serializeImageHyperlinkSegment(final ImageHyperlinkSegment imageHyperlinkSegment, final SImageHyperlinkSegment sImageHyperlinkSegment) {
		sImageHyperlinkSegment.href = imageHyperlinkSegment.getHref();
		return serializeImageSegment(imageHyperlinkSegment, sImageHyperlinkSegment);
	}

	private SImageResource serializeImageResource(final ImageResource imageResource) {
		if (null == imageResource) {
			return null;
		}
		return (SImageResource) ToolkitSerialization.serializeResource(imageResource);
	}

	private SParagraphSegment serializeImageSegment(final ImageSegment imageSegment, final SImageSegment sImageSegment) {
		sImageSegment.image = serializeImageResource(imageSegment.getImageResource());
		return serializeObjectSegment(imageSegment, sImageSegment);
	}

	private SParagraphSegment serializeObjectSegment(final ObjectSegment objectSegment, final SObjectSegment sObjectSegment) {
		if (null != objectSegment.getVerticalAlign()) {
			switch (objectSegment.getVerticalAlign()) {
				case TOP:
					sObjectSegment.verticalAlign = VerticalAlign.TOP;
					break;

				case MIDDLE:
					sObjectSegment.verticalAlign = VerticalAlign.MIDDLE;
					break;

				case BOTTOM:
					sObjectSegment.verticalAlign = VerticalAlign.BOTTOM;
					break;

				default:
					// nothing
					break;
			}
		}
		return serializeParagraphSegment(objectSegment, sObjectSegment);
	}

	private SParagraphSegment serializeParagraphSegment(final ParagraphSegment paragraphSegment, final SParagraphSegment sParagraphSegment) {
		sParagraphSegment.tooltip = paragraphSegment.getTooltipText();
		sParagraphSegment.wordwrap = paragraphSegment.isWordWrapAllowed();
		return sParagraphSegment;
	}

	private SParagraphSegment serializeTextHyperlinkSegment(final TextHyperlinkSegment textHyperlinkSegment, final STextHyperlinkSegment sTextHyperlinkSegment) {
		sTextHyperlinkSegment.href = textHyperlinkSegment.getHref();
		return serializeTextSegment(textHyperlinkSegment, sTextHyperlinkSegment);
	}

	private SParagraphSegment serializeTextSegment(final TextSegment textSegment, final STextSegment sTextSegment) {
		sTextSegment.text = textSegment.getText();
		return serializeParagraphSegment(textSegment, sTextSegment);
	}
}
