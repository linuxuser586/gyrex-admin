/**
 * Copyright (c) 2010 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package org.eclipse.gyrex.admin.web.gwt.client.internal.console.widgets;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 */
public class NovaMenuItem extends Widget {

	/**
	 * Creates a new instance.
	 * 
	 * @param cmd
	 * @param text
	 */
	public NovaMenuItem(final String text, final Command cmd) {
		final LIElement li = Document.get().createLIElement();
		final AnchorElement a = Document.get().createAnchorElement();
		li.appendChild(a);
		a.setInnerText(text);
		a.setHref("#" + text);
		setElement(li);
	}
}
