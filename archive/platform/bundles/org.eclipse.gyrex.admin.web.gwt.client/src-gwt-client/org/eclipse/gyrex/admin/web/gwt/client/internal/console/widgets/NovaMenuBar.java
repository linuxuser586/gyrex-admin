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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 */
public class NovaMenuBar extends Widget {

	private final UListElement ul;

	/**
	 * Creates a new instance.
	 */
	public NovaMenuBar() {
		ul = Document.get().createULElement();
		setElement(ul);
	}

	/**
	 * @param item
	 */
	public void addItem(final NovaMenuItem item) {
		final Element element = item.getElement();
		ul.appendChild(element);
	}

}
