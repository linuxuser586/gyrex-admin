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
package org.eclipse.gyrex.toolkit.widgets;

import java.io.InputStream;

import org.eclipse.gyrex.toolkit.commands.Command;
import org.eclipse.gyrex.toolkit.widgets.styledtext.Paragraph;
import org.eclipse.gyrex.toolkit.widgets.styledtext.StyledTextModel;

/**
 * A styled read-only text widget that allows to apply different styles to its
 * content.
 * <p>
 * Text can be rendered as-is or by parsing the formatting XML tags. If XML
 * formatting is used the text must be well-formed XML and the XML root element
 * <code>text</code> is required to be used. The following tags can be children
 * of the <code>text</code> element and are supported by each rendering
 * implementation:
 * </p>
 * <ul>
 * <li><b>p</b> - for defining paragraphs.</li>
 * <li><b>li</b> - for defining list items. The following attributes are
 * allowed:
 * <ul>
 * <li><b>style</b> - could be 'bullet' (default), 'text' and 'image'</li>
 * <li><b>value</b> - not used for 'bullet'. For text, it is the value of the
 * text that is rendered as a bullet. For image, it is the href of the image to
 * be rendered as a bullet.</li>
 * </ul>
 * </li>
 * </ul>
 * <p>
 * Text in paragraphs and list items will be wrapped according to the width of
 * the rendered control. The following tags can appear as children of either
 * <b>p</b> or <b>li</b> elements:
 * <ul>
 * <li><b>img</b> - to render an image. Element accepts attribute 'href' that is
 * a URL to an <code>ImageResource</code>. Vertical position of image relative
 * to surrounding text is optionally controlled by the attribute <b>align</b>
 * that can have values <b>top</b>, <b>middle</b> and <b>bottom</b>. A tooltip
 * can be specified using the 'alt' attribute.</li>
 * <li><b>a</b> - to render a hyperlink. Element accepts attribute 'href' which
 * specifies either an external or an internal hyperlink. Internal hyperlinks
 * are in the form 'command:/<command_id>' and trigger a {@link Command command}
 * . External hyperlinks will be opened at runtime directly. The element also
 * accepts 'nowrap' attribute (default is <code>false</code>). When set to
 * 'true', the hyperlink will not be wrapped. Hyperlinks automatically created
 * when 'http://' is encountered in text are not wrapped.</li>
 * <li><b>b</b> - the enclosed text will use bold font.</li>
 * <li><b>br</b> - forced line break (no attributes).</li>
 * </ul>
 * <p>
 * None of the elements can nest. For example, you cannot have <b>b</b> inside a
 * <b>a</b>. This was done to keep everything simple and transparent. An
 * exception to this rule has been added to support nesting images and text
 * inside the hyperlink tag (<b>a</b>). Image enclosed in the hyperlink tag acts
 * as a hyperlink and can be clicked on. When both text and image is enclosed,
 * rendering will affect both as a single hyperlink.
 * </p>
 * <p>
 * Care should be taken when using this widget. Styled text is not an HTML
 * browser and should not be treated as such. If you need complex formatting
 * capabilities, create your own custom widget. If all you need is to wrap text,
 * use {@link #setText(String, boolean, boolean)} with parsing tags turned off (
 * <code>false</code>).
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class StyledText extends Widget {

	/** serialVersionUID */
	private static final long serialVersionUID = 3657776235311775455L;

	/** the model */
	private final StyledTextModel model;

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 *            the widget id
	 * @param parent
	 *            the widget parent
	 * @param style
	 *            the widget style
	 */
	public StyledText(final String id, final Container parent, final int style) {
		super(id, parent, style);
		model = new StyledTextModel();
	}

	/**
	 * Returns the text model parsed from the text.
	 * 
	 * @return the text model
	 */
	public Paragraph[] getText() {
		return model.getParagraphs();
	}

	/**
	 * Sets the contents of the stream. Optionally, URLs in untagged text can be
	 * converted into hyperlinks. The caller is responsible for closing the
	 * stream.
	 * 
	 * @param is
	 *            stream to render
	 * @param expandURLs
	 *            if <samp>true </samp>, URLs found in untagged text will be
	 *            converted into hyperlinks.
	 */
	public void setContents(final InputStream is, final boolean expandURLs) {
		model.parseInputStream(is, expandURLs);
	}

	/**
	 * Sets the provided text.
	 * <p>
	 * Text can be rendered as-is, or by parsing the formatting tags.
	 * Optionally, sections of text starting with 'http://' will be converted to
	 * hyperlinks.
	 * </p>
	 * 
	 * @param text
	 *            the text to render
	 * @param parseTags
	 *            if <samp>true </samp>, formatting tags will be parsed.
	 *            Otherwise, text will be rendered as-is.
	 * @param expandURLs
	 *            if <samp>true </samp>, URLs found in the untagged text will be
	 *            converted into hyperlinks. Sets the text.
	 */
	public void setText(final String text, final boolean parseTags, final boolean expandURLs) {
		if (parseTags) {
			model.parseTaggedText(text, expandURLs);
		} else {
			model.parseRegularText(text, expandURLs);
		}
	}
}
