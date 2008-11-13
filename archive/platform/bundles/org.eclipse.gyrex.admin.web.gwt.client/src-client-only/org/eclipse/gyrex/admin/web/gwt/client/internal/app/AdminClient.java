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
package org.eclipse.cloudfree.admin.web.gwt.client.internal.app;


import org.eclipse.cloudfree.admin.web.gwt.app.internal.client.CloudFreeApp;
import org.eclipse.cloudfree.admin.web.gwt.client.internal.shared.IAdminClientConstants;
import org.eclipse.cloudfree.toolkit.gwt.client.WidgetFactory;
import org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets.CWTWidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AdminClient extends CloudFreeApp implements EntryPoint, IAdminClientConstants {

	private final SimplePanel contentPanel = new SimplePanel();
	private Element titleElement;
	private ParagraphElement descriptionElement;

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.admin.web.gwt.app.internal.client.CloudFreeApp#createWidgetFactory()
	 */
	@Override
	protected WidgetFactory createWidgetFactory() {
		final WidgetFactory widgetFactory = new WidgetFactory(ENTRYPOINT_WIDGET_SERVICE);
		widgetFactory.setResourceBaseUrl(WIDGET_RESOURCE_BASE_URL);
		return widgetFactory;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.admin.web.gwt.app.internal.client.CloudFreeApp#getApplicationTitle()
	 */
	@Override
	protected String getApplicationTitle() {
		return "CloudFree Platform System Admin";
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
		RootPanel.get("headerArea").add(headerArea);

		// initialize content area
		RootPanel.get("contentArea").add(contentPanel);

		// initialize top bar
		initialzeHeaderStatusBar("headerTopArea");

		// initialize application
		initialize();

		// hide the loading message
		final RootPanel loadingMessage = RootPanel.get("initialLoading");
		if (null != loadingMessage) {
			loadingMessage.setVisible(false);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.admin.web.gwt.app.internal.client.CloudFreeApp#setContentInformation(java.lang.String, java.lang.String)
	 */
	@Override
	protected void setContentInformation(final String title, final String description) {
		// super handles window title
		super.setContentInformation(title, description);

		if (null != title) {
			if (null != titleElement) {
				titleElement.setInnerText(title);
			}
		} else {
			if (null != titleElement) {
				titleElement.setInnerText("");
			}
		}

		// set description
		if (null != descriptionElement) {
			if (null != description) {
				descriptionElement.setInnerText(description);
			} else {
				descriptionElement.setInnerText("");
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.admin.web.gwt.app.internal.client.CloudFreeApp#showWidget(org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets.CWTWidget)
	 */
	@Override
	protected void showWidget(final CWTWidget widget) {
		if (null == widget) {
			contentPanel.setWidget(new HTML("&nbsp;"));
		} else {
			contentPanel.setWidget(widget);
		}
	}
}
