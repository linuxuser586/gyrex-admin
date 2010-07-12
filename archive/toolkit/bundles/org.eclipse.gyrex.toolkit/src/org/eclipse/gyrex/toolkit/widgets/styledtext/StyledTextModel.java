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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.eclipse.gyrex.toolkit.Toolkit;
import org.eclipse.gyrex.toolkit.resources.ImageResource;
import org.eclipse.gyrex.toolkit.widgets.styledtext.ObjectSegment.VerticalAlign;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Text model of styled text.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public final class StyledTextModel {

	private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	private List<Paragraph> paragraphs;

	/**
	 * Creates a new instance.
	 */
	public StyledTextModel() {
		reset();
	}

	private Object checkChildren(final Node node) {
		boolean text = false;
		Node imgNode = null;
		//int status = 0;

		final NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			final Node child = children.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				text = true;
			} else if ((child.getNodeType() == Node.ELEMENT_NODE) && child.getNodeName().equalsIgnoreCase("img")) { //$NON-NLS-1$
				imgNode = child;
			}
		}
		if (text && (imgNode == null)) {
			return getNodeText(node);
		} else if (!text && (imgNode != null)) {
			return imgNode;
		} else {
			return null;
		}
	}

	private String getHref(final Node link) {
		return getNodeAttributeValue(link, "href");
	}

	private String getNodeAttributeValue(final Node node, final String name) {
		final NamedNodeMap atts = node.getAttributes();
		if (atts == null) {
			return null;
		}
		final Node attribute = atts.getNamedItem(name);
		if (attribute == null) {
			return null;
		}
		return attribute.getNodeValue();
	}

	private String getNodeText(final Node node) {
		final NodeList children = node.getChildNodes();
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < children.getLength(); i++) {
			final Node child = children.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				final String value = child.getNodeValue();
				if (null != value) {
					buf.append(value);
				}
			}
		}
		return buf.toString().trim();
	}

	public Paragraph[] getParagraphs() {
		if (paragraphs == null) {
			return new Paragraph[0];
		}
		return paragraphs.toArray(new Paragraph[paragraphs.size()]);
	}

	private String getSingleNodeText(final Node node) {
		return node.getNodeValue();
	}

	/**
	 * @param imageNode
	 * @return
	 */
	private String getTooltip(final Node imageNode) {
		return getNodeAttributeValue(imageNode, "alt");
	}

	private VerticalAlign getVerticalAlign(final Node object) {
		String value = getNodeAttributeValue(object, "align");
		if (value == null) {
			return null;
		} else {
			value = value.toLowerCase();
		}

		if (value.equals("top")) {
			return ObjectSegment.VerticalAlign.TOP;
		} else if (value.equals("middle")) {
			return ObjectSegment.VerticalAlign.MIDDLE;
		} else if (value.equals("bottom")) {
			return ObjectSegment.VerticalAlign.BOTTOM;
		}

		return null;
	}

	private boolean getWordWrapAllowed(final Node link) {
		final String value = getNodeAttributeValue(link, "nowrap");
		if ((value != null) && value.equalsIgnoreCase("true")) {
			return false;
		}
		return true;
	}

	/**
	 * Parses the text from an input stream.
	 * 
	 * @param is
	 *            the input stream
	 * @param expandURLs
	 *            if URLs should be expanded
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void parseInputStream(final InputStream is, final boolean expandURLs) {
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setIgnoringComments(true);

		reset();
		try {
			final DocumentBuilder parser = documentBuilderFactory.newDocumentBuilder();
			final InputSource source = new InputSource(is);
			final Document doc = parser.parse(source);
			processDocument(doc, expandURLs);
		} catch (final ParserConfigurationException e) {
			Toolkit.error(Toolkit.ERROR_INVALID_ARGUMENT, e, e.getMessage());
		} catch (final SAXException e) {
			Toolkit.error(Toolkit.ERROR_INVALID_ARGUMENT, e, e.getMessage());
		} catch (final IOException e) {
			Toolkit.error(Toolkit.ERROR_IO, e, e.getMessage());
		}
	}

	/**
	 * Parses regular text.
	 * 
	 * @param regularText
	 *            the regular text
	 * @param expandURLs
	 *            if URLs should be expanded
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void parseRegularText(final String regularText, final boolean expandURLs) {
		reset();

		if (regularText == null) {
			return;
		}

		Paragraph p = new Paragraph();
		paragraphs.add(p);
		int pstart = 0;

		for (int i = 0; i < regularText.length(); i++) {
			final char c = regularText.charAt(i);
			if (p == null) {
				p = new Paragraph();
				paragraphs.add(p);
			}
			if (c == '\n') {
				final String text = regularText.substring(pstart, i);
				pstart = i + 1;
				p.parseRegularText(text, expandURLs, true);
				p = null;
			}
		}
		if (p != null) {
			// no new line
			final String text = regularText.substring(pstart);
			p.parseRegularText(text, expandURLs, true);
		}
	}

	/**
	 * Parses tagged text.
	 * 
	 * @param taggedText
	 *            the tagged text
	 * @param expandURLs
	 *            if URLs should be expanded
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void parseTaggedText(final String taggedText, final boolean expandURLs) {
		if (taggedText == null) {
			reset();
			return;
		}
		try {
			final InputStream stream = new ByteArrayInputStream(taggedText.getBytes("UTF8")); //$NON-NLS-1$
			parseInputStream(stream, expandURLs);
		} catch (final UnsupportedEncodingException e) {
			Toolkit.error(Toolkit.ERROR_UNSUPPORTED_FORMAT, e);
		}
	}

	private void processDocument(final Document doc, final boolean expandURLs) {
		final Node root = doc.getDocumentElement();
		final NodeList children = root.getChildNodes();
		processSubnodes(paragraphs, children, expandURLs);
	}

	private ParagraphSegment processHyperlinkSegment(final Node link) {
		final String href = getHref(link);
		final String tooltip = getTooltip(link);
		final boolean wrapAllowed = getWordWrapAllowed(link);

		final Object status = checkChildren(link);
		if (status instanceof Node) {
			final Node imageNode = (Node) status;
			return processImageHyperlinkSegment(imageNode, href, tooltip, wrapAllowed);
		} else if (status instanceof String) {
			final String text = (String) status;
			return new TextHyperlinkSegment(text, href, tooltip, wrapAllowed);
		} else {
			final AggregateHyperlinkSegment parent = new AggregateHyperlinkSegment(href, false, tooltip);
			final NodeList children = link.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				final Node child = children.item(i);
				final Object childStatus = checkChildren(link);
				if (childStatus instanceof String) {
					final String text = (String) status;
					parent.add(new TextHyperlinkSegment(text, href, tooltip, wrapAllowed));
				} else if (childStatus instanceof Node) {
					parent.add(processImageHyperlinkSegment(child, href, tooltip, wrapAllowed));
				}
			}
			return parent;
		}
	}

	private ImageHyperlinkSegment processImageHyperlinkSegment(final Node imageNode, final String href, final String tooltip, final boolean wrapAllowed) {
		final String imageUrl = getHref(imageNode);
		final String imageTooltip = getTooltip(imageNode);
		return new ImageHyperlinkSegment(href, ImageResource.createFromUrl(imageUrl), getVerticalAlign(imageNode), wrapAllowed, imageTooltip != null ? imageTooltip : tooltip);
	}

	private ImageSegment processImageSegment(final Node imageNode) {
		final String imageUrl = getHref(imageNode);
		final String imageTooltip = getTooltip(imageNode);
		return new ImageSegment(ImageResource.createFromUrl(imageUrl), getVerticalAlign(imageNode), imageTooltip);
	}

	private Paragraph processListItem(final Node listItem, final boolean expandURLs) {
		final NodeList children = listItem.getChildNodes();

		final String style = getNodeAttributeValue(listItem, "style");

		// determine paragraph style
		DecoratedParagraph p = null;
		if (style != null) {
			if (style.equalsIgnoreCase("text")) { //$NON-NLS-1$
				p = new DecoratedParagraph(getNodeAttributeValue(listItem, "value"));
			} else if (style.equalsIgnoreCase("image")) { //$NON-NLS-1$
				p = new DecoratedParagraph(ImageResource.createFromUrl(getNodeAttributeValue(listItem, "value")));
			} else if (style.equalsIgnoreCase("bullet")) { //$NON-NLS-1$
				p = new DecoratedParagraph();
			}
		}

		// use default
		if (p == null) {
			p = new DecoratedParagraph();
		}

		processSegments(p, children, expandURLs);
		return p;
	}

	private Paragraph processParagraph(final Node paragraph, final boolean expandURLs) {
		final NodeList children = paragraph.getChildNodes();
		final Paragraph p = new Paragraph();
		processSegments(p, children, expandURLs);
		return p;
	}

	private void processSegments(final Paragraph p, final NodeList children, final boolean expandURLs) {
		for (int i = 0; i < children.getLength(); i++) {
			final Node child = children.item(i);
			ParagraphSegment segment = null;

			if (child.getNodeType() == Node.TEXT_NODE) {
				final String value = getSingleNodeText(child);
				if (value != null) {
					p.parseRegularText(value, expandURLs, true);
				}
			} else if (child.getNodeType() == Node.ELEMENT_NODE) {
				final String name = child.getNodeName();
				if (name.equalsIgnoreCase("img")) { //$NON-NLS-1$
					segment = processImageSegment(child);
				} else if (name.equalsIgnoreCase("a")) { //$NON-NLS-1$
					segment = processHyperlinkSegment(child);
				} else if (name.equalsIgnoreCase("b")) { //$NON-NLS-1$
					final String text = getNodeText(child);
					p.parseRegularText(text, expandURLs, true);
				} else if (name.equalsIgnoreCase("br")) { //$NON-NLS-1$
					segment = new BreakSegment();
				}
			}
			if (segment != null) {
				p.addSegment(segment);
			}
		}
	}

	private void processSubnodes(final List<Paragraph> paragraphs, final NodeList children, final boolean expandURLs) {
		for (int i = 0; i < children.getLength(); i++) {
			final Node child = children.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				// Make an implicit paragraph
				final String text = getSingleNodeText(child);
				if (text != null) {
					final Paragraph p = new Paragraph();
					p.parseRegularText(text, expandURLs, true);
					paragraphs.add(p);
				}
			} else if (child.getNodeType() == Node.ELEMENT_NODE) {
				final String tag = child.getNodeName().toLowerCase();
				if (tag.equals("p")) { //$NON-NLS-1$
					final Paragraph p = processParagraph(child, expandURLs);
					if (p != null) {
						paragraphs.add(p);
					}
				} else if (tag.equals("li")) { //$NON-NLS-1$
					final Paragraph p = processListItem(child, expandURLs);
					if (p != null) {
						paragraphs.add(p);
					}
				}
			}
		}
	}

	private void reset() {
		if (paragraphs == null) {
			paragraphs = new ArrayList<Paragraph>();
		}
		paragraphs.clear();
	}
}
