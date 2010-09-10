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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 *
 */
public class NovaMenuItem extends SimplePanel {

	private final Anchor anchor;

	/**
	 * Creates a new instance.
	 * 
	 * @param cmd
	 * @param text
	 */
	public NovaMenuItem(final String text, final String tooltip, final Command cmd) {
		super(Document.get().createLIElement());

		anchor = new Anchor();
		anchor.setText(text);
		anchor.setTitle(tooltip);
		anchor.setHref("#" + text);
		anchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				event.preventDefault();
				DeferredCommand.addCommand(cmd);
			}
		});

		anchor.getParent();

		setWidget(anchor);
	}
}
