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
package org.eclipse.gyrex.toolkit.rap.internal.ui.widgets;


import org.eclipse.gyrex.toolkit.widgets.StyledText;
import org.eclipse.gyrex.toolkit.widgets.styledtext.Paragraph;
import org.eclipse.gyrex.toolkit.widgets.styledtext.ParagraphSegment;
import org.eclipse.gyrex.toolkit.widgets.styledtext.TextSegment;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * Renders {@link StyledText}.
 */
public class CWTStyledText extends CWTWidget<StyledText> {

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTWidget#createWidgetControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createWidgetControl(final Composite parent) {

		// TODO: RAP does not support FormText
		final StringBuilder text = new StringBuilder();
		final Paragraph[] paragraphs = getWidget().getText();
		for (final Paragraph paragraph : paragraphs) {
			for (final ParagraphSegment segment : paragraph.getSegments()) {
				if (segment instanceof TextSegment) {
					text.append(((TextSegment) segment).getText());
					text.append("\n");
				}
			}
			text.append("\n");
		}

		final Text textControl = getToolkit().getFormToolkit().createText(parent, text.toString(), SWT.WRAP | SWT.READ_ONLY);
		return textControl;
	}

}
