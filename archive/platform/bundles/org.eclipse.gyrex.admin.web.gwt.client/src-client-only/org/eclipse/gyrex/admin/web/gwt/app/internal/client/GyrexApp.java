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
package org.eclipse.gyrex.admin.web.gwt.app.internal.client;

import org.eclipse.gyrex.admin.web.gwt.app.internal.client.widgets.ContentTitleProvider;
import org.eclipse.gyrex.admin.web.gwt.app.internal.client.widgets.Error;
import org.eclipse.gyrex.admin.web.gwt.app.internal.services.client.services.IGyrexAppUIServiceAsync;
import org.eclipse.gyrex.admin.web.gwt.app.internal.services.client.services.ServiceRegistry;
import org.eclipse.gyrex.gwt.common.adaptable.AdapterManager;
import org.eclipse.gyrex.toolkit.gwt.client.GetWidgetCallback;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactory;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactoryException;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Base class for backoffice applications.
 */
public abstract class GyrexApp {

	private static boolean isPlatformOperatingInDevelopmentMode = false;

	/**
	 * Returns the isPlatformOperatingInDevelopmentMode.
	 * 
	 * @return the isPlatformOperatingInDevelopmentMode
	 */
	public static boolean isPlatformOperatingInDevelopmentMode() {
		return isPlatformOperatingInDevelopmentMode;
	}

	/** the current widget id */
	private String currentWidgetId;

	/** the widget factory */
	private WidgetFactory widgetFactory;

	/**
	 * Creates a new instance.
	 */
	public GyrexApp() {
		super();
	}

	/**
	 * Called when the application is loaded to create the widget factory.
	 * 
	 * @return the widget factory instance (may not be <code>null</code>)
	 */
	protected abstract WidgetFactory createWidgetFactory();

	/**
	 * Returns the application title.
	 * <p>
	 * Subclasses may overwrite to provide a custom title of their application.
	 * </p>
	 * 
	 * @return the application detail
	 */
	protected String getApplicationTitle() {
		return "Gyrex";
	}

	/**
	 * Initializes the application and creates all content.
	 * <p>
	 * Typically, this is called by {@link EntryPoint#onModuleLoad() the entry
	 * point method}.
	 * </p>
	 */
	protected void initialize() {
		registerAdapters();
		widgetFactory = createWidgetFactory();
		initializeOperationMode();
	}

	private void initializeOperationMode() {
		final IGyrexAppUIServiceAsync configurationService = ServiceRegistry.getConfigurationService();
		if (null == configurationService) {
			return;
		}
		configurationService.isOperatingInDevelopmentMode(new AsyncCallback<Boolean>() {

			public void onFailure(final Throwable caught) {
				isPlatformOperatingInDevelopmentMode = false;
			}

			public void onSuccess(final Boolean result) {
				isPlatformOperatingInDevelopmentMode = Boolean.TRUE.equals(result);
				onOperationModeInitialized();
			}
		});

	}

	/**
	 * May be called by subclasses during initialization to render a status bar
	 * in the header.
	 * 
	 * @param elementId
	 *            the element to attach to (using {@link RootPanel#get(String)})
	 */
	protected void initialzeHeaderStatusBar(final String elementId) {
		final RootPanel headerStatusBar = RootPanel.get(elementId);
		if (null != headerStatusBar) {
			ServiceRegistry.getConfigurationService().getServerString(new AsyncCallback<String>() {
				public void onFailure(final Throwable caught) {
					headerStatusBar.getElement().setInnerText(caught.getMessage());
				}

				public void onSuccess(final String result) {
					if (null != result) {
						headerStatusBar.getElement().setInnerText(getApplicationTitle() + " running on " + result);
					} else {
						headerStatusBar.getElement().setInnerText(getApplicationTitle());
					}
				}
			});
		}
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
				showWidget(composite);
				updateContentTitle(composite);
			}

		});

	}

	/**
	 * Called when the operation mode has been initialized successfully.
	 * <p>
	 * Subclasses can overwrite to perform additional initialization. However,
	 * they are required to call <code>super.onOperationModeInitialized()</code>
	 * to all the base app to configure itself based on the configuration mode.
	 * </p>
	 */
	protected void onOperationModeInitialized() {
		// disable caching in development mode
		if (isPlatformOperatingInDevelopmentMode() && (null != widgetFactory)) {
			widgetFactory.setCacheFlags(WidgetFactory.CACHE_SERIALIZED_WIDGETS | WidgetFactory.CACHE_RENDERED_WIDGETS);
		}
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

	protected abstract void showWidget(final CWTWidget widget);

	/*package*/void updateContentTitle(final CWTWidget widget) {
		final ContentTitleProvider contentTitleProvider = widget.getAdapter(ContentTitleProvider.class);
		final String title = null != contentTitleProvider ? contentTitleProvider.getTitle(widget) : "";
		final String description = null != contentTitleProvider ? contentTitleProvider.getDescription(widget) : "";
		setContentInformation(title, description);
	}

}
