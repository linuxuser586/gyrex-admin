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
package org.eclipse.cloudfree.toolkit.gwt.examples.client;

import org.eclipse.cloudfree.toolkit.gwt.client.GetWidgetCallback;
import org.eclipse.cloudfree.toolkit.gwt.client.WidgetFactory;
import org.eclipse.cloudfree.toolkit.gwt.client.WidgetFactoryException;
import org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets.CWTWidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Simple1 implements EntryPoint {

	static class Preformatted extends Widget {
		public Preformatted(final String text) {
			setElement(DOM.createElement("pre"));
			setStyleName("cwt-Error-DetailsText");
			getElement().setInnerText(text);
		}
	}

	static Widget createErrorDetails(final WidgetFactoryException error) {
		final FlowPanel flowPanel = new FlowPanel();
		flowPanel.add(new HTML("&nbsp;"));
		flowPanel.add(new Label(error.getMessage()));
		final String detail = error.getDetail();
		if ((null != detail) && (detail.length() > 0)) {
			flowPanel.add(new HTML("&nbsp;"));
			flowPanel.add(new Preformatted(detail));
		}
		return flowPanel;
	}

	private WidgetFactory widgetFactory;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// init the widget factory with the URL of the widget RPC service running on the server
		widgetFactory = new WidgetFactory(Simple1Constants.EP_SIMPLE1_WIDGETSERVICE);

		// retrieve the hello world widget
		widgetFactory.getWidget("helloworld", new GetWidgetCallback() {

			public void onFailure(final WidgetFactoryException caught) {
				RootPanel.get("slot1").add(createErrorDetails(caught));
			}

			public void onSuccess(final CWTWidget widget) {
				// add to root panel
				RootPanel.get("slot1").add(widget);

				// populate with initial value found in the query string
				final String email = Window.Location.getParameter("email");
				if (null != email) {

					// TODO: implement data population
					//widgetFactory.populateWidget(widget, email);
				}
			}

		});
	}
}
