/*******************************************************************************
 * Copyright (c) 2010 <enter-company-name-here> and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     <enter-developer-name-here> - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.gwt.client.ui.widgets;

import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SMenu;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SMenuItem;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 */
public class CWTMenu extends CWTContainer {

	private Label breadcrumbs;
	private DeckPanel menuPanel;

	@Override
	protected Panel createPanel(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		return super.createPanel(serializedWidget, toolkit);
	}

	@Override
	protected void populateChildren(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		// skip
		if (serializedWidget instanceof SMenuItem) {
			return;
		}
		super.populateChildren(serializedWidget, toolkit);
	}

	@Override
	protected Widget render(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SMenu sMenu = (SMenu) serializedWidget;

		final VerticalPanel verticalPanel = new VerticalPanel();

		breadcrumbs = new Label();
		verticalPanel.add(breadcrumbs);

//		menuPanel = new DeckPanel();
//		verticalPanel.add(menuPanel);

		if (null != sMenu.widgets) {
			for (final ISerializedWidget child : sMenu.widgets) {
				if (child instanceof SMenu) {
					verticalPanel.add(renderMenu((SMenu) child, toolkit));
				}
			}
		}

		return verticalPanel;
	}

	private Widget renderMenu(final SMenu menu, final CWTToolkit toolkit) {

		final Image image = null != menu.image ? new Image(toolkit.getResourceUrl(menu.image.reference)) : null;

		final HorizontalPanel panel = new HorizontalPanel();
		if (null != image) {
			panel.add(image);
		} else {
			panel.add(new HTML("&nbsp;"));
		}

		final VerticalPanel right = new VerticalPanel();
		right.add(new HTML("<strong>" + menu.label + "</strong>"));

		if (null != menu.featuresItems) {
			for (final SMenuItem item : menu.featuresItems) {
				right.add(new Label(item.label));
			}
		}

		panel.add(right);
		return panel;
	}
}
