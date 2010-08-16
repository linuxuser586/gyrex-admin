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

import org.eclipse.gyrex.admin.web.gwt.client.internal.console.resources.AdminStyles;
import org.eclipse.gyrex.admin.web.gwt.client.internal.console.widgets.ContentTitleProvider;
import org.eclipse.gyrex.admin.web.gwt.client.internal.console.widgets.Error;
import org.eclipse.gyrex.admin.web.gwt.client.internal.console.widgets.NovaMenuBar;
import org.eclipse.gyrex.admin.web.gwt.client.internal.console.widgets.NovaMenuItem;
import org.eclipse.gyrex.admin.web.gwt.client.internal.console.widgets.WizardPopupPanel;
import org.eclipse.gyrex.admin.web.gwt.client.internal.shared.IAdminConsoleConstants;
import org.eclipse.gyrex.admin.web.gwt.client.internal.shared.service.AdminConsoleEnvironment;
import org.eclipse.gyrex.admin.web.gwt.client.internal.shared.service.IAdminConsoleService;
import org.eclipse.gyrex.admin.web.gwt.client.internal.shared.service.IAdminConsoleServiceAsync;
import org.eclipse.gyrex.gwt.common.adaptable.AdapterManager;
import org.eclipse.gyrex.toolkit.gwt.client.GetWidgetCallback;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetClientEnvironment;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactory;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactoryException;
import org.eclipse.gyrex.toolkit.gwt.client.ui.history.HistoryStateBuilder;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget;
import org.eclipse.gyrex.toolkit.gwt.client.ui.wizard.CWTWizardContainer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AdminConsole implements EntryPoint {

	/** singleton instance */
	static AdminConsole instance;

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
	private final Label loadingMessage = new Label("Loading...");

	/** the current widget */
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
		final WidgetFactory widgetFactory = new WidgetFactory(IAdminConsoleConstants.ENTRYPOINT_WIDGET_SERVICE, new AdminToolkit());
		widgetFactory.setResourceBaseUrl(IAdminConsoleConstants.WIDGET_RESOURCE_BASE_URL);
		widgetFactory.setEnvironment(new WidgetClientEnvironment());

		// disable caching in development mode
		if (!isPlatformOperatingInDevelopmentMode()) {
			widgetFactory.setCacheFlags(WidgetFactory.CACHE_SERIALIZED_WIDGETS | WidgetFactory.CACHE_RENDERED_WIDGETS);
		}

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
		final RootPanel menuPanel = RootPanel.get("menuPanel");
		if (null != menuPanel) {
			final NovaMenuBar menuBar = new NovaMenuBar();
			menuBar.addItem(new NovaMenuItem("Dashboard", "Open system dashboard.", new Command() {
				public void execute() {
					requestWidget("dashboard", null);
				}
			}));
//			menuBar.addItem(new NovaMenuItem("Control Panel", "Open system control panel.", new Command() {
//				public void execute() {
//					requestWidget("control-panel", null);
//				}
//			}));
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
					// show the associated widget
					final HistoryStateBuilder historyState = widgetFactory.getToolkit().createHistoryStateBuilder().parseString(token);
					requestWidget(historyState.getWidgetId(), historyState.getWidgetState());
				}
			}
		};
		History.addValueChangeHandler(historyListener);

		// inject our styles
		AdminStyles.ADMIN_STYLES.getAdminCss().ensureInjected();

		// hide the loading message
		final RootPanel loadingMessage = RootPanel.get("initialLoading");
		if (null != loadingMessage) {
			loadingMessage.setVisible(false);
		}

		// Show the initial widget
		History.fireCurrentHistoryState();
	}

	public void onModuleLoad() {
		instance = this;

		// initialize content area
		final RootPanel contentPanel = RootPanel.get("contentPanel");
		if (null != contentPanel) {
			contentPanel.add(contentHolder);
			contentHolder.setStyleName("admin-ContentHolderPanel");
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

	protected void requestWidget(final String widgetId, final String widgetState) {
		if (null == widgetId) {
			throw new IllegalArgumentException("null widget id not allowed");
		}

		// just apply new state if widget didn't change
		if ((null != currentWidgetId) && currentWidgetId.equals(widgetId)) {
			((CWTWidget) contentHolder.getWidget()).applyWidgetState(widgetState);
			return;
		}

		contentHolder.setWidget(loadingMessage);

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onFailure(final Throwable reason) {
				Window.alert(reason.getMessage());
			}

			@Override
			public void onSuccess() {
				// load widget from server
				widgetFactory.getWidget(widgetId, new GetWidgetCallback() {

					public void onFailure(final WidgetFactoryException caught) {
						final Error error = new Error(caught, widgetFactory.getToolkit());
						currentWidgetId = null;
						showWidget(error);
					}

					public void onSuccess(final CWTWidget composite) {
						currentWidgetId = composite.getWidgetId();
						if (!currentWidgetId.equals(History.getToken())) {
							History.newItem(currentWidgetId, false);
						}
						composite.applyWidgetState(widgetState);
						showWidget(composite);
					}

				});
			}
		});
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
			if (widget instanceof CWTWizardContainer) {
				final WizardPopupPanel panel = new WizardPopupPanel();
				panel.setWizard((CWTWizardContainer) widget);
				panel.center();
			} else {
				contentHolder.setWidget(widget);
			}
			updateContentTitle(widget);
		}

	}

	private void updateContentTitle(final CWTWidget widget) {
		final ContentTitleProvider contentTitleProvider = widget.getAdapter(ContentTitleProvider.class);
		final String title = null != contentTitleProvider ? contentTitleProvider.getTitle(widget) : "";
		final String description = null != contentTitleProvider ? contentTitleProvider.getDescription(widget) : "";
		setContentInformation(title, description);
	}

}
