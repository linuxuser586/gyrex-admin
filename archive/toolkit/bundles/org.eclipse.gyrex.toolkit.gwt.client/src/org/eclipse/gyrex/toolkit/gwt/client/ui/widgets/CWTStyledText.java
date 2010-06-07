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
package org.eclipse.gyrex.toolkit.gwt.client.ui.widgets;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.resources.SImageResource;
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

/**
 * Composite for <code>org.eclipse.gyrex.toolkit.widgets.StyledText</code>.
 */
public class CWTStyledText extends CWTWidget {

	class StyledText extends Composite {

		private final class CommandExecutor implements ClickListener {
			/** toolkit */
			private final CWTToolkit toolkit;
			private final String commandId;

			private CommandExecutor(final String commandId, final CWTToolkit toolkit) {
				this.commandId = commandId;
				this.toolkit = toolkit;
			}

			public void onClick(final Widget sender) {
				toolkit.getWidgetFactory().executeCommand(commandId, getWidgetId(), null, null);
			}
		}

		private static final String INTERNAL_LINK_PREFIX = "gyrex:///";

		private final Map<String, SImageSegment> imageSegments = new HashMap<String, SImageSegment>(2);

		private final Map<String, SImageResource> imageResources = new HashMap<String, SImageResource>(2);

		private final Map<String, String> textSegments = new HashMap<String, String>(2);
		private final Map<String, STextHyperlinkSegment> textHyperlinkSegments = new HashMap<String, STextHyperlinkSegment>(2);
		private final Map<String, SImageHyperlinkSegment> imageHyperlinkSegments = new HashMap<String, SImageHyperlinkSegment>(2);
		private final Map<String, SAggregateHyperlinkSegment> aggregateHyperlinkSegments = new HashMap<String, SAggregateHyperlinkSegment>(2);

		public StyledText(final SParagraph[] paragraphs, final CWTToolkit toolkit) {

			// generate HTML
			final StringBuilder html = new StringBuilder();
			boolean listStarted = false;
			for (int i = 0; i < paragraphs.length; i++) {
				final SParagraph paragraph = paragraphs[i];

				// start paragraph
				if (paragraph instanceof SDecoratedParagraph) {
					switch (((SDecoratedParagraph) paragraph).decoration) {
						case IMAGE:
							final String decorationImageId = HTMLPanel.createUniqueId();
							imageResources.put(decorationImageId, ((SDecoratedParagraph) paragraph).decorationImage);
							html.append("<div style=\"float:left;\"><img id=\"").append(decorationImageId).append("\" /></div><p>");
							break;
						case TEXT:
							final String decorationTextId = HTMLPanel.createUniqueId();
							textSegments.put(decorationTextId, ((SDecoratedParagraph) paragraph).decorationText);
							html.append("<div style=\"float:left;\"><p id=\"").append(decorationTextId).append("\"></p></div><p>");
							break;

						case BULLET:
						default:
							if (!listStarted) {
								listStarted = true;
								html.append("<ul>");
							}
							html.append("<li>");
							break;
					}
				} else {
					if (listStarted) {
						listStarted = false;
						html.append("</ul>");
					}
					html.append("<p>");
				}

				// segments
				for (int j = 0; j < paragraph.segments.length; j++) {
					final SParagraphSegment segment = paragraph.segments[j];
					if (segment.getClass().equals(SAggregateHyperlinkSegment.class)) {
						final String id = HTMLPanel.createUniqueId();
						aggregateHyperlinkSegments.put(id, (SAggregateHyperlinkSegment) segment);
						html.append("<a id=\"").append(id).append("\">&nbsp;</a>");
					} else if (segment.getClass().equals(STextHyperlinkSegment.class)) {
						final String id = HTMLPanel.createUniqueId();
						textHyperlinkSegments.put(id, (STextHyperlinkSegment) segment);
						html.append("<a id=\"").append(id).append("\">&nbsp;</a>");
					} else if (segment.getClass().equals(STextSegment.class)) {
						final String id = HTMLPanel.createUniqueId();
						textSegments.put(id, ((STextSegment) segment).text);
						html.append("<span id=\"").append(id).append("\">&nbsp;</span>");
					} else if (segment.getClass().equals(SImageHyperlinkSegment.class)) {
						final String id = HTMLPanel.createUniqueId();
						imageHyperlinkSegments.put(id, (SImageHyperlinkSegment) segment);
						html.append("<a id=\"").append(id).append("\">&nbsp;</a>");
					} else if (segment.getClass().equals(SImageSegment.class)) {
						final String id = HTMLPanel.createUniqueId();
						imageSegments.put(id, (SImageSegment) segment);
						html.append("<img id=\"").append(id).append("\"/>");
					} else if (segment.getClass().equals(SBreakSegment.class)) {
						html.append("<br/>");
					}
				}

				// end paragraph
				if (paragraph instanceof SDecoratedParagraph) {
					switch (((SDecoratedParagraph) paragraph).decoration) {
						case IMAGE:
						case TEXT:
							html.append("</p><div style=\"clear:both;\"></div>");
							break;

						case BULLET:
						default:
							html.append("</li>");
							break;
					}
				} else {
					html.append("</p>");
				}
			}

			// close list
			if (listStarted) {
				listStarted = false;
				html.append("</ul>");
			}

			// create panel
			final HTMLPanel panel = new HTMLPanel(html.toString());

			// populate text
			for (final Iterator<Entry<String, String>> stream = textSegments.entrySet().iterator(); stream.hasNext();) {
				final Entry<String, String> entry = stream.next();
				panel.getElementById(entry.getKey()).setInnerText(entry.getValue());
			}

			// populate images
			for (final Iterator<Entry<String, SImageSegment>> stream = imageSegments.entrySet().iterator(); stream.hasNext();) {
				final Entry<String, SImageSegment> entry = stream.next();
				final Image image = new Image(toolkit.getResourceUrl(entry.getValue().image.reference));
				setTooltip(entry.getValue(), image);
				setVerticalAlign(entry.getValue(), image);
				panel.addAndReplaceElement(image, entry.getKey());
			}

			// populate image resources
			for (final Iterator<Entry<String, SImageResource>> stream = imageResources.entrySet().iterator(); stream.hasNext();) {
				final Entry<String, SImageResource> entry = stream.next();
				final Image image = new Image(toolkit.getResourceUrl(entry.getValue().reference));
				panel.addAndReplaceElement(image, entry.getKey());
			}

			// populate text hyperlinks
			for (final Iterator<Entry<String, STextHyperlinkSegment>> stream = textHyperlinkSegments.entrySet().iterator(); stream.hasNext();) {
				final Entry<String, STextHyperlinkSegment> entry = stream.next();
				populateTextHyperlink(entry.getValue(), entry.getKey(), panel, toolkit);
			}

			// populate image hyperlinks
			for (final Iterator<Entry<String, SImageHyperlinkSegment>> stream = imageHyperlinkSegments.entrySet().iterator(); stream.hasNext();) {
				final Entry<String, SImageHyperlinkSegment> entry = stream.next();
				populateImageHyperlink(entry.getValue(), entry.getKey(), panel, toolkit);
			}

			// populate aggregated hyperlinks
			for (final Iterator<Entry<String, SAggregateHyperlinkSegment>> stream = aggregateHyperlinkSegments.entrySet().iterator(); stream.hasNext();) {
				final Entry<String, SAggregateHyperlinkSegment> entry = stream.next();
				populateAggregateHyperlink(entry.getValue(), entry.getKey(), panel, toolkit);
			}

			initWidget(panel);
			setStyleName("cwt-StyledText");
		}

		private void populateAggregateHyperlink(final SAggregateHyperlinkSegment aggregateHyperlinkSegment, final String id, final HTMLPanel panel, final CWTToolkit toolkit) {
			if (aggregateHyperlinkSegment.href.toLowerCase().startsWith(INTERNAL_LINK_PREFIX)) {
				// internal hyperlink
				String commandId = aggregateHyperlinkSegment.href.substring(INTERNAL_LINK_PREFIX.length());
				final int commandIdEnd = commandId.indexOf('/');
				if (commandIdEnd != -1) {
					commandId = commandId.substring(0, commandIdEnd);
				}
				final Hyperlink hyperlink = new Hyperlink();
				final Element linkElement = hyperlink.getElement();
				setTooltip(aggregateHyperlinkSegment, hyperlink);
				hyperlink.addClickListener(new CommandExecutor(commandId, toolkit));
				panel.addAndReplaceElement(hyperlink, id);

				for (int i = 0; i < aggregateHyperlinkSegment.segments.length; i++) {
					final SHyperlinkSegment hyperlinkSegment = aggregateHyperlinkSegment.segments[i];
					if (hyperlinkSegment instanceof STextHyperlinkSegment) {
						final STextHyperlinkSegment textHyperlinkSegment = (STextHyperlinkSegment) hyperlinkSegment;
						final SpanElement spanElement = SpanElement.as(DOM.createSpan());
						linkElement.appendChild(spanElement);
						spanElement.setInnerText(textHyperlinkSegment.text);
					} else if (hyperlinkSegment instanceof SImageHyperlinkSegment) {
						final SImageHyperlinkSegment imageHyperlinkSegment = (SImageHyperlinkSegment) hyperlinkSegment;
						final ImageElement imageElement = ImageElement.as(DOM.createImg());
						linkElement.appendChild(imageElement);
						imageElement.setSrc(toolkit.getResourceUrl(imageHyperlinkSegment.image.reference));
						if (null != imageHyperlinkSegment.tooltip) {
							imageElement.setAlt(imageHyperlinkSegment.tooltip);
							imageElement.setTitle(imageHyperlinkSegment.tooltip);
						} else if (null != aggregateHyperlinkSegment.tooltip) {
							imageElement.setAlt(aggregateHyperlinkSegment.tooltip);
							imageElement.setTitle(aggregateHyperlinkSegment.tooltip);
						}
					}
				}
			} else {
				// external hyperlink
				final AnchorElement anchorElement = AnchorElement.as(panel.getElementById(id));
				anchorElement.setHref(aggregateHyperlinkSegment.href);
				anchorElement.setTarget("_blank");
				if (null != aggregateHyperlinkSegment.tooltip) {
					anchorElement.setTitle(aggregateHyperlinkSegment.tooltip);
				}
				for (int i = 0; i < aggregateHyperlinkSegment.segments.length; i++) {
					final SHyperlinkSegment hyperlinkSegment = aggregateHyperlinkSegment.segments[i];
					if (hyperlinkSegment instanceof STextHyperlinkSegment) {
						final STextHyperlinkSegment textHyperlinkSegment = (STextHyperlinkSegment) hyperlinkSegment;
						final SpanElement spanElement = SpanElement.as(DOM.createSpan());
						anchorElement.appendChild(spanElement);
						spanElement.setInnerText(textHyperlinkSegment.text);
					} else if (hyperlinkSegment instanceof SImageHyperlinkSegment) {
						final SImageHyperlinkSegment imageHyperlinkSegment = (SImageHyperlinkSegment) hyperlinkSegment;
						final ImageElement imageElement = ImageElement.as(DOM.createImg());
						anchorElement.appendChild(imageElement);
						imageElement.setSrc(toolkit.getResourceUrl(imageHyperlinkSegment.image.reference));
						if (null != imageHyperlinkSegment.tooltip) {
							imageElement.setAlt(imageHyperlinkSegment.tooltip);
							imageElement.setTitle(imageHyperlinkSegment.tooltip);
						} else if (null != aggregateHyperlinkSegment.tooltip) {
							imageElement.setAlt(aggregateHyperlinkSegment.tooltip);
							imageElement.setTitle(aggregateHyperlinkSegment.tooltip);
						}
					}
				}
			}
		}

		private void populateImageHyperlink(final SImageHyperlinkSegment imageHyperlinkSegment, final String id, final HTMLPanel panel, final CWTToolkit toolkit) {
			if (imageHyperlinkSegment.href.toLowerCase().startsWith(INTERNAL_LINK_PREFIX)) {
				// internal hyperlink
				String commandId = imageHyperlinkSegment.href.substring(INTERNAL_LINK_PREFIX.length());
				final int commandIdEnd = commandId.indexOf('/');
				if (commandIdEnd != -1) {
					commandId = commandId.substring(0, commandIdEnd);
				}
				final Image image = new Image(toolkit.getResourceUrl(imageHyperlinkSegment.image.reference));
				setTooltip(imageHyperlinkSegment, image);
				setVerticalAlign(imageHyperlinkSegment, image);
				image.addClickListener(new CommandExecutor(commandId, toolkit));
				panel.addAndReplaceElement(image, id);
			} else {
				// external hyperlink
				final AnchorElement anchorElement = AnchorElement.as(panel.getElementById(id));
				anchorElement.setHref(imageHyperlinkSegment.href);
				anchorElement.setTarget("_blank");
				if (null != imageHyperlinkSegment.tooltip) {
					anchorElement.setTitle(imageHyperlinkSegment.tooltip);
				}
				final ImageElement imageElement = ImageElement.as(DOM.createImg());
				anchorElement.appendChild(imageElement);
				imageElement.setSrc(toolkit.getResourceUrl(imageHyperlinkSegment.image.reference));
				if (null != imageHyperlinkSegment.tooltip) {
					imageElement.setAlt(imageHyperlinkSegment.tooltip);
					imageElement.setTitle(imageHyperlinkSegment.tooltip);
				}
			}
		}

		private void populateTextHyperlink(final STextHyperlinkSegment textHyperlinkSegment, final String id, final HTMLPanel panel, final CWTToolkit toolkit) {
			if (textHyperlinkSegment.href.toLowerCase().startsWith(INTERNAL_LINK_PREFIX)) {
				// internal hyperlink
				String commandId = textHyperlinkSegment.href.substring(INTERNAL_LINK_PREFIX.length());
				final int commandIdEnd = commandId.indexOf('/');
				if (commandIdEnd != -1) {
					commandId = commandId.substring(0, commandIdEnd);
				}
				final Hyperlink hyperlink = new Hyperlink();
				hyperlink.setText(textHyperlinkSegment.text);
				setTooltip(textHyperlinkSegment, hyperlink);
				hyperlink.addClickListener(new CommandExecutor(commandId, toolkit));
				panel.addAndReplaceElement(hyperlink, id);
			} else {
				// external hyperlink
				final AnchorElement anchorElement = AnchorElement.as(panel.getElementById(id));
				anchorElement.setHref(textHyperlinkSegment.href);
				anchorElement.setInnerText(textHyperlinkSegment.text);
				anchorElement.setTarget("_blank");
				if (null != textHyperlinkSegment.tooltip) {
					anchorElement.setTitle(textHyperlinkSegment.tooltip);
				}
			}
		}

		private void setTooltip(final SParagraphSegment paragraphSegment, final Widget widget) {
			if (paragraphSegment.tooltip != null) {
				widget.setTitle(paragraphSegment.tooltip);
			}
		}

		private void setVerticalAlign(final SObjectSegment object, final Widget widget) {
			if (object.verticalAlign == null) {
				return;
			}
			switch (object.verticalAlign) {
				case TOP:
					widget.addStyleName("cwt-valign-TOP");
					break;
				case MIDDLE:
					widget.addStyleName("cwt-valign-MIDDLE");
					break;
				case BOTTOM:
					widget.addStyleName("cwt-valign-BOTTOM");
					break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rep.web.gwt.client.ui.RenderedWidget#render(org.eclipse.rep.web.gwt.client.rwt.ISerializedWidget,
	 *      org.eclipse.rep.web.gwt.client.ui.RenderingToolkit)
	 */
	@Override
	protected Widget render(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SStyledText widget = (SStyledText) serializedWidget;

		return new StyledText(widget.paragraphs, toolkit);
	}

}
