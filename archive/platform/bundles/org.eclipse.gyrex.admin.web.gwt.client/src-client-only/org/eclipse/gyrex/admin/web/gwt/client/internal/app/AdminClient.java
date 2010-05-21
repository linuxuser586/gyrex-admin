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
package org.eclipse.gyrex.admin.web.gwt.client.internal.app;

import org.eclipse.gyrex.admin.web.gwt.app.internal.client.GyrexApp;
import org.eclipse.gyrex.admin.web.gwt.client.internal.app.widgets.NovaMenuBar;
import org.eclipse.gyrex.admin.web.gwt.client.internal.app.widgets.NovaMenuItem;
import org.eclipse.gyrex.admin.web.gwt.client.internal.shared.IAdminClientConstants;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactory;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AdminClient extends GyrexApp implements EntryPoint, IAdminClientConstants {

	private final SimplePanel contentHolder = new SimplePanel();
	private Element titleElement;
	private ParagraphElement descriptionElement;

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.admin.web.gwt.app.internal.client.GyrexApp#createWidgetFactory()
	 */
	@Override
	protected WidgetFactory createWidgetFactory() {
		final WidgetFactory widgetFactory = new WidgetFactory(ENTRYPOINT_WIDGET_SERVICE);
		widgetFactory.setResourceBaseUrl(WIDGET_RESOURCE_BASE_URL);
		return widgetFactory;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.admin.web.gwt.app.internal.client.GyrexApp#getApplicationTitle()
	 */
	@Override
	protected String getApplicationTitle() {
		return "Gyrex System Admin";
	}

	public void onModuleLoad() {

		// prepare header
		final String titleId = DOM.createUniqueId();
		final String descriptionId = DOM.createUniqueId();
		final HTMLPanel headerArea = new HTMLPanel("<h2 id=\"" + titleId + "\"></h2><p id=\"" + descriptionId + "\"></p>");
		if (null != headerArea.getElementById(titleId)) {
			titleElement = headerArea.getElementById(titleId);
		}
		if (null != headerArea.getElementById(descriptionId)) {
			descriptionElement = ParagraphElement.as(headerArea.getElementById(descriptionId));
		}

		// initialize header
		final RootPanel headerPanel = RootPanel.get("header_area");
		if (null != headerPanel) {
			headerPanel.add(headerArea);
		}

		// initialize content area
		final RootPanel contentPanel = RootPanel.get("content_container");
		if (null != contentPanel) {
			contentPanel.add(contentHolder);
		}

		// initialize top bar
		initialzeHeaderStatusBar("headerTopArea");

		// initialize menu
		final RootPanel menuPanel = RootPanel.get("menu");
		if (null != menuPanel) {
			final NovaMenuBar menuBar = new NovaMenuBar();
			final Command cmd = new Command() {

				public void execute() {
					// empty

				}
			};
			final NovaMenuItem item = new NovaMenuItem("Menu 1", cmd);
			menuBar.addItem(item);
			menuBar.addItem(new NovaMenuItem("Menu 2", cmd));
			menuBar.addItem(new NovaMenuItem("Menu 3", cmd));
			menuPanel.add(menuBar);
		}

		// initialize application
		initialize();

		// Setup a history listener to reselect the associate menu item
		final HistoryListener historyListener = new HistoryListener() {
			public void onHistoryChanged(String historyToken) {
				if ((null == historyToken) || (historyToken.trim().length() == 0)) {
					// fallback to the first item if the history token is empty
					historyToken = "";
				}

				// Load the associated CWT widget
				requestWidget(historyToken);
			}
		};
		History.addHistoryListener(historyListener);

		// hide the loading message
		final RootPanel loadingMessage = RootPanel.get("initialLoading");
		if (null != loadingMessage) {
			loadingMessage.setVisible(false);
		}

		// Show the initial widget
		final String initToken = History.getToken();
		if (initToken.length() > 0) {
			historyListener.onHistoryChanged(initToken);
		} else {
			// Use the first token available
			requestWidget("");
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.admin.web.gwt.app.internal.client.GyrexApp#setContentInformation(java.lang.String, java.lang.String)
	 */
	@Override
	protected void setContentInformation(final String title, final String description) {
		// super handles window title
		super.setContentInformation(title, description);

		// set title
		if (null != titleElement) {
			titleElement.setInnerText(null != title ? title : "");
		}

		// set description
		if (null != descriptionElement) {
			descriptionElement.setInnerText(null != description ? description : "");
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.admin.web.gwt.app.internal.client.GyrexApp#showWidget(org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget)
	 */
	@Override
	protected void showWidget(final CWTWidget widget) {
		if (null == widget) {
			contentHolder.setWidget(new HTML("&nbsp;"));
		} else {
			contentHolder.setWidget(widget);
		}
	}
}
