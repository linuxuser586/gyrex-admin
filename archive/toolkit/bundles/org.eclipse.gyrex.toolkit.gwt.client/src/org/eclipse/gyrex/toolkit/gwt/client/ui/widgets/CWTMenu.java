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

import java.util.ArrayList;

import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SMenu;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SMenuItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
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
	public void applyWidgetState(final String widgetState) {
		// TODO read path from widget state
	}

	@Override
	protected Panel createPanel(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		return super.createPanel(serializedWidget, toolkit);
	}

	/**
	 * Returns the menu path.
	 * 
	 * @param menu
	 * @return
	 */
	private String getPath(final SMenu menu) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void populateChildren(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		// empty
	}

	@Override
	protected Widget render(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SMenu sMenu = (SMenu) serializedWidget;

		final VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStylePrimaryName("cwt-Menu");

		breadcrumbs = new Label();
		verticalPanel.add(breadcrumbs);

//		menuPanel = new FlexTable();
//		verticalPanel.add(menuPanel);

		if (null != sMenu.widgets) {
			final ArrayList<Widget> children = new ArrayList<Widget>();
			for (final ISerializedWidget child : sMenu.widgets) {
				if (child instanceof SMenu) {
					children.add(renderMenu((SMenu) child, toolkit));
				}
			}
			final int rows = (children.size() / 2) + (children.size() % 2);
			final Grid grid = new Grid(rows, 2);
			int row = 0;
			int col = 0;
			for (final Widget widget : children) {
				if (col >= 2) {
					row++;
					col = 0;
				}
				grid.setWidget(row, col, widget);
				col++;
			}
			verticalPanel.add(grid);
		}

		return verticalPanel;
	}

	private Widget renderMenu(final SMenu menu, final CWTToolkit toolkit) {

		final Image image = null != menu.image ? new Image(toolkit.getResourceUrl(menu.image.reference)) : null;

		final HorizontalPanel panel = new HorizontalPanel();
		panel.setStylePrimaryName("cwt-Menu-MenuGroup");
		if (null != image) {
			image.setStylePrimaryName("cwt-Menu-MenuGroup-Image");
			panel.add(image);
		} else {
			final HTML placeholder = new HTML("&nbsp;");
			placeholder.setStylePrimaryName("cwt-Menu-MenuGroup-MissingImage");
			panel.add(placeholder);
		}

		final VerticalPanel right = new VerticalPanel();
		right.setStylePrimaryName("cwt-Menu-MenuGroup-ItemsPanel");

		final String historyToken = toolkit.createHistoryStateBuilder().setWidgetId(getWidgetId()).setWidgetState(getPath(menu)).buildString();
		final Hyperlink labelLink = new Hyperlink(menu.label, historyToken);
		labelLink.setStylePrimaryName("cwt-Menu-MenuGroup-Label");
		right.add(labelLink);

		if (null != menu.featuresItems) {
			for (final SMenuItem item : menu.featuresItems) {
				final Anchor anchor = new Anchor(item.label);
				anchor.setStylePrimaryName("cwt-Menu-MenuItem-Label");
				if ((null != item.command) && (null != item.command.actions)) {
					anchor.setHref("#" + item.command.id);
					anchor.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(final ClickEvent event) {
							event.preventDefault();
							toolkit.getActionHandler().handleAction(item.command.actions);
						}
					});
				}
				right.add(anchor);
			}
		}

		panel.add(right);
		return panel;
	}
}
