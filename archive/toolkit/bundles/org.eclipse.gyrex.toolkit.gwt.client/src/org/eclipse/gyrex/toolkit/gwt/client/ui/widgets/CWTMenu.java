/*******************************************************************************
 * Copyright (c) 2010 AGETO Service GmbH and others.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SMenu;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SMenuItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
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

	private static final char PATH_SEPARATOR = ':';

	private HorizontalPanel breadcrumbs;
	private DeckPanel menuPanel;

	private final Map<String, Integer> indexByMenuId = new HashMap<String, Integer>();

	private Label description;

	@Override
	public void applyWidgetState(final String widgetState) {
		// get root
		SMenu menu = (SMenu) getSerializedWidget();

		// get menu to show
		if (null != widgetState) {
			menu = getMenu(menu, widgetState);
		}

		// redirect to root menu if invalid state
		if (null == menu) {
			GWT.log("Invalid menu path: " + widgetState);
			final String historyToken = getToolkit().createHistoryStateBuilder().setWidgetId(getWidgetId()).buildString();
			GWT.log("Will redirect to: #" + historyToken);
			DeferredCommand.addCommand(new Command() {

				@Override
				public void execute() {
					History.newItem(historyToken);
				}
			});
			return;
		}

		if (!indexByMenuId.containsKey(menu.id)) {
			final Widget menuWidget = buildMenu(menu);
			menuPanel.add(menuWidget);
			final int widgetIndex = menuPanel.getWidgetIndex(menuWidget);
			indexByMenuId.put(menu.id, widgetIndex);
		}

		// show widget
		menuPanel.showWidget(indexByMenuId.get(menu.id));

		// update bread crumps
		updateBreadCrumps(menu);

		// show description
		description.setText(menu.description);
	}

	private Widget buildMenu(final SMenu sMenu) {
		final ISerializedWidget[] menuWidgets = sMenu.widgets;
		if (null != menuWidgets) {
			final ArrayList<Widget> children = new ArrayList<Widget>();
			for (final ISerializedWidget child : menuWidgets) {
				if (child instanceof SMenu) {
					children.add(renderMenu((SMenu) child));
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
			return grid;
		}
		// just the menu
		return renderMenu(sMenu);
	}

	@Override
	protected Panel createPanel(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		return super.createPanel(serializedWidget, toolkit);
	}

	private SMenu findMatchingMenu(final SMenu menu, final String id) {
		if (id.equals(menu.id)) {
			return menu;
		}
		if (null != menu.widgets) {
			for (int i = 0; i < menu.widgets.length; i++) {
				if (id.equals(menu.widgets[i].getId())) {
					// assume SMenu
					return (SMenu) menu.widgets[i];
				}
			}
		}
		return null;
	}

	private SMenu getMenu(SMenu menu, String path) {
		// abort on invalid path
		while ((null != menu) && (null != path) && (path.length() > 0)) {
			final int separator = path.indexOf(PATH_SEPARATOR);
			final String id = separator > -1 ? path.substring(0, separator) : path;
			path = (separator > -1) && (path.length() > separator + 1) ? path.substring(separator + 1) : null;
			menu = findMatchingMenu(menu, id);
		}
		return menu;
	}

	/**
	 * Returns the menu path.
	 * 
	 * @param menu
	 * @return
	 */
	private String getPath(final SMenu menu) {
		if (null != menu.parent) {
			final String parentPath = getPath((SMenu) menu.parent);
			return null != parentPath ? parentPath + PATH_SEPARATOR + menu.id : menu.id;
		}
		// the root item has no path
		return null;
	}

	@Override
	protected void populateChildren(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		// empty
	}

	@Override
	protected Widget render(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStylePrimaryName("cwt-Menu");

		breadcrumbs = new HorizontalPanel();
		breadcrumbs.setStylePrimaryName("cwt-Menu-BreadCrumps");
		breadcrumbs.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(breadcrumbs);

		description = new Label();
		description.setStylePrimaryName("cwt-Menu-Description");
		verticalPanel.add(description);

		menuPanel = new DeckPanel();
		menuPanel.setStylePrimaryName("cwt-Menu-MenuPanel");
		verticalPanel.add(menuPanel);

		return verticalPanel;
	}

	private Widget renderMenu(final SMenu menu) {
		final Image image = null != menu.image ? new Image(getToolkit().getResourceUrl(menu.image.reference)) : null;

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

		final String historyToken = getToolkit().createHistoryStateBuilder().setWidgetId(getWidgetId()).setWidgetState(getPath(menu)).buildString();
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
							getToolkit().getActionHandler().handleAction(item.command.actions);
						}
					});
				}
				right.add(anchor);
			}
		}

		panel.add(right);
		return panel;
	}

	private void updateBreadCrumps(final SMenu menu) {
		breadcrumbs.clear();
		SMenu parent = menu;
		while (null != parent) {
			final Label label = new Label(parent.label);
			label.setStylePrimaryName("cwt-Menu-BreadCrump-Label");
			breadcrumbs.insert(label, 0);
			final HTML separator = new HTML("&gt;");
			separator.setStylePrimaryName("cwt-Menu-BreadCrump-Separator");
			breadcrumbs.insert(separator, 0);
			parent = (SMenu) parent.parent;
		}

		final Image image = null != menu.image ? new Image(getToolkit().getResourceUrl(menu.image.reference)) : null;
		if (null != image) {
			image.setStylePrimaryName("cwt-Menu-BreadCrumps-Image");
			breadcrumbs.insert(image, 0);
		} else {
			final HTML placeholder = new HTML("&nbsp;");
			placeholder.setStylePrimaryName("cwt-Menu-BreadCrumps-MissingImage");
			breadcrumbs.insert(placeholder, 0);
		}
	}
}
