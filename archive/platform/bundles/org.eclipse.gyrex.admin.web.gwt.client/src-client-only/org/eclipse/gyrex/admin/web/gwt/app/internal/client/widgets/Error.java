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
package org.eclipse.gyrex.admin.web.gwt.app.internal.client.widgets;


import org.eclipse.gyrex.admin.web.gwt.app.internal.client.GyrexApp;
import org.eclipse.gyrex.admin.web.gwt.app.internal.client.widgets.SError;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactoryException;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTToolkit;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * A composite to render an error for a {@link WidgetFactoryException}.
 */
public class Error extends CWTWidget {

	static class DetailsText extends Widget {
		public DetailsText(final String text) {
			setElement(DOM.createElement("pre"));
			setStyleName("cwt-Error-DetailsText");
			getElement().setInnerText(text);
		}
	}

	private static void createDetails(final WidgetFactoryException error, final FlowPanel flowPanel) {
		flowPanel.add(new HTML("&nbsp;"));
		flowPanel.add(new Label(error.getMessage()));
		final String detail = error.getDetail();
		if ((null != detail) && (detail.length() > 0)) {
			flowPanel.add(new HTML("&nbsp;"));
			flowPanel.add(new DetailsText(detail));
		}
	}

	private final WidgetFactoryException error;

	/**
	 * Creates a new instance.
	 * 
	 * @param widgetId
	 * @param widget
	 */
	public Error(final WidgetFactoryException error, final CWTToolkit toolkit) {
		this.error = error;
		init(new SError(), toolkit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget#render(org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget,
	 *      org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTToolkit)
	 */
	protected Widget render(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final FlowPanel flowPanel = new FlowPanel();
		switch (error.getErrorCode()) {
			case WidgetFactoryException.WIDGET_NOT_FOUND:
				flowPanel.add(new HTML("<h1>Not Found!</h1>"));
				flowPanel.add(new Label("The requested page could not be found on the server."));
				break;

			default:
				if (GyrexApp.isPlatformOperatingInDevelopmentMode()) {
					flowPanel.add(new HTML("<h1>Upps..</h1>"));
					flowPanel.add(new Label("An error occurred while processing your request."));
					createDetails(error, flowPanel);
				} else {
					flowPanel.add(new HTML("<h1>We Are Sorry!</h1>"));
					flowPanel.add(new Label("An error occurred while processing your request. Please try again in a few seconds. If the error persists please contact our help desk. We are sorry for any inconvenience this may cause."));
				}
				break;
		}
		return flowPanel;
	}
}
