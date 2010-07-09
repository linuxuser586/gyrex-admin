/*******************************************************************************
 * Copyright (c) 2008, 2010 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.admin.web.gwt.client.internal.console;

import org.eclipse.gyrex.admin.web.gwt.client.internal.console.widgets.ContentTitleProvider;
import org.eclipse.gyrex.admin.web.gwt.client.internal.console.widgets.Error;
import org.eclipse.gyrex.admin.web.gwt.client.internal.console.widgets.NovaMenuBar;
import org.eclipse.gyrex.admin.web.gwt.client.internal.console.widgets.NovaMenuItem;
import org.eclipse.gyrex.admin.web.gwt.client.internal.shared.IAdminConsoleConstants;
import org.eclipse.gyrex.admin.web.gwt.client.internal.shared.service.AdminConsoleEnvironment;
import org.eclipse.gyrex.admin.web.gwt.client.internal.shared.service.IAdminConsoleService;
import org.eclipse.gyrex.admin.web.gwt.client.internal.shared.service.IAdminConsoleServiceAsync;
import org.eclipse.gyrex.gwt.common.adaptable.AdapterManager;
import org.eclipse.gyrex.toolkit.gwt.client.GetWidgetCallback;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetClientEnvironment;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactory;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactoryException;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AdminConsole implements EntryPoint {

	/**
	 * Returns the isPlatformOperatingInDevelopmentMode.
	 * 
	 * @return the isPlatformOperatingInDevelopmentMode
	 */
	public static boolean isPlatformOperatingInDevelopmentMode() {
		return (null == environment) || environment.devMode;
	}

	private IAdminConsoleServiceAsync adminConsoleService;

	private final SimplePanel contentHolder = new SimplePanel();

	/** the current widget id */
	private String currentWidgetId;

	/** the widget factory */
	private WidgetFactory widgetFactory;

	private static AdminConsoleEnvironment environment;

	/**
	 * Called when the application is loaded to create the widget factory.
	 * 
	 * @return the widget factory instance (may not be <code>null</code>)
	 */
	protected WidgetFactory createWidgetFactory() {
		final WidgetFactory widgetFactory = new WidgetFactory(IAdminConsoleConstants.ENTRYPOINT_WIDGET_SERVICE);
		widgetFactory.setResourceBaseUrl(IAdminConsoleConstants.WIDGET_RESOURCE_BASE_URL);
		widgetFactory.setEnvironment(new WidgetClientEnvironment());
		return widgetFactory;
	}

	/**
	 * Returns the adminConsoleService.
	 * 
	 * @return the adminConsoleService
	 */
	public IAdminConsoleServiceAsync getAdminConsoleService() {
		if (null == adminConsoleService) {
			adminConsoleService = GWT.create(IAdminConsoleService.class);
			((ServiceDefTarget) adminConsoleService).setServiceEntryPoint(IAdminConsoleConstants.ENTRYPOINT_CONSOLE_SERVICE);
		}
		return adminConsoleService;
	}

	/**
	 * Returns the application title.
	 * <p>
	 * Subclasses may overwrite to provide a custom title of their application.
	 * </p>
	 * 
	 * @return the application detail
	 */
	protected String getApplicationTitle() {
		return "Gyrex Admin Console";
	}

	void hideInitialLoadingMessage() {
		final RootPanel loadingMessage = RootPanel.get("initialLoading");
		if (null != loadingMessage) {
			loadingMessage.setVisible(false);
		}
	}

	private void loadEnvironmentInfoFromServer() {
		getAdminConsoleService().loadEnvironment(new AsyncCallback<AdminConsoleEnvironment>() {
			@Override
			public void onFailure(final Throwable caught) {
				// hide the loading message
				hideInitialLoadingMessage();

				// log error
				GWT.log("Error while initializing environment. " + caught.getMessage(), caught);

				// show error
				Window.alert("Error while initializing environment. " + caught.getMessage());
			}

			public void onSuccess(final AdminConsoleEnvironment result) {
				onEnvironmentInitialized(result);
			};
		});
	}

	private void loadWidget(final String widgetId) {
		// do nothing if widget didn't change
		if ((null != currentWidgetId) && currentWidgetId.equals(widgetId)) {
			return;
		}

		// load widget from server
		widgetFactory.getWidget(widgetId, new GetWidgetCallback() {

			public void onFailure(final WidgetFactoryException caught) {
				final Error error = new Error(caught, widgetFactory.getToolkit());
				currentWidgetId = null;
				showWidget(error);
				updateContentTitle(error);
			}

			public void onSuccess(final CWTWidget composite) {
				currentWidgetId = composite.getWidgetId();
				if (!currentWidgetId.equals(History.getToken())) {
					History.newItem(currentWidgetId, false);
				}
				showWidget(composite);
				updateContentTitle(composite);
			}

		});

	}

	/**
	 * Called when the environment has been initialized successfully.
	 * 
	 * @param environment
	 */
	void onEnvironmentInitialized(final AdminConsoleEnvironment environment) {
		// remember environment
		AdminConsole.environment = environment;

		// register adapters
		registerAdapters();

		// create the widget factory
		widgetFactory = createWidgetFactory();

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

		// setup a history listener
		final ValueChangeHandler<String> historyListener = new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(final ValueChangeEvent<String> event) {
				final String token = event.getValue();

				if ("".equals(token)) {
					// show dashboard
					History.newItem("dashboard");
				} else {
					// show the associated  widget
					requestWidget(token);
				}
			}
		};
		History.addValueChangeHandler(historyListener);

		// hide the loading message
		final RootPanel loadingMessage = RootPanel.get("initialLoading");
		if (null != loadingMessage) {
			loadingMessage.setVisible(false);
		}

		// disable caching in development mode
		if (isPlatformOperatingInDevelopmentMode() && (null != widgetFactory)) {
			widgetFactory.setCacheFlags(WidgetFactory.CACHE_SERIALIZED_WIDGETS | WidgetFactory.CACHE_RENDERED_WIDGETS);
		}

		// Show the initial widget
		History.fireCurrentHistoryState();
	}

	public void onModuleLoad() {
		// initialize content area
		final RootPanel contentPanel = RootPanel.get("content_container");
		if (null != contentPanel) {
			contentPanel.add(contentHolder);
		}

		// load server environment
		loadEnvironmentInfoFromServer();

		// all other stuff should happen when the environment could be loaded
	}

	/**
	 * Called during initialization to register adapters with the
	 * {@link AdapterManager}.
	 * <p>
	 * Subclasses may overwrite to register additional adapters. But they are
	 * required to call <code>super</code>.
	 * </p>
	 */
	protected void registerAdapters() {
		AdapterManager.getAdapterManager().registerAdapter(CWTWidget.class, ContentTitleProvider.class, new ContentTitleProvider());
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.HistoryListener#onHistoryChanged(java.lang.String)
	 */
	protected void requestWidget(final String widgetId) {
		loadWidget(((null != widgetId) && (widgetId.trim().length() > 0)) ? widgetId : "");
	}

	/**
	 * Updates the application with the new content information.
	 * <p>
	 * The default implementation updates the browser window's title. Subclasses
	 * my overwrite and call <code>super</code> where appropriate.
	 * </p>
	 * 
	 * @param title
	 *            the content title
	 * @param description
	 *            the content description
	 */
	protected void setContentInformation(final String title, final String description) {
		if (null != title) {
			Window.setTitle(title + " - " + getApplicationTitle());
		} else {
			Window.setTitle(getApplicationTitle());
		}
	}

	protected void showWidget(final CWTWidget widget) {
		if (null == widget) {
			contentHolder.setWidget(new HTML("&nbsp;"));
			Window.alert("no widget wo show");
		} else {
			contentHolder.setWidget(widget);
		}
	}

	/*package*/void updateContentTitle(final CWTWidget widget) {
		final ContentTitleProvider contentTitleProvider = widget.getAdapter(ContentTitleProvider.class);
		final String title = null != contentTitleProvider ? contentTitleProvider.getTitle(widget) : "";
		final String description = null != contentTitleProvider ? contentTitleProvider.getDescription(widget) : "";
		setContentInformation(title, description);
	}

}
