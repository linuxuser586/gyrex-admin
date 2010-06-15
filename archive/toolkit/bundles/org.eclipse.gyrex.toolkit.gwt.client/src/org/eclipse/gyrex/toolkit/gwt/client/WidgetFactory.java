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
package org.eclipse.gyrex.toolkit.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gyrex.toolkit.gwt.client.internal.WidgetService;
import org.eclipse.gyrex.toolkit.gwt.client.internal.WidgetServiceAsync;
import org.eclipse.gyrex.toolkit.gwt.client.ui.commands.CommandExecutedEvent;
import org.eclipse.gyrex.toolkit.gwt.client.ui.commands.CommandExecutionCallback;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTToolkit;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedData;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.actions.SAction;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.commands.SCommandExecutionResult;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SContentSet;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;

/**
 * This is the factory for creating GWT widgets exposed by a server.
 * <p>
 * The factory uses an GWT RPC base service and requires a widget service on the
 * server side. A widget service is usually registered by the server side
 * counterpart using a user-defined widget service entry point. The GWT RPC
 * documentation should give more details about entry points and GWT RPC
 * services in general.
 * </p>
 * <p>
 * The widget factory also exposed the base URL for accessing widget resource
 * (such as images). A {@link CWTToolkit toolkit} may use this information to
 * obtain a accessible URL to a resource.
 * </p>
 * <p>
 * The factory maintains an internal cache so that subsequent lookups for the
 * same widget id will not query the server again. However, sometimes this
 * dynamic behavior is desired (eg., in development environments). Thus, the
 * caching can be {@link #setCacheFlags(int) configured}.
 * </p>
 * <p>
 * Note, this is experimental API. It will not be stable until version 1.0 is
 * officially released. Currently this API lacks support for localization and
 * content handling.
 * </p>
 * 
 * @see org.eclipse.gyrex.toolkit.gwt.server.WidgetService
 * @noextend This class is not intended to be subclassed by clients.
 */
public final class WidgetFactory {

	/**
	 * This callback interface is passed to the widget service.
	 */
	private final class WidgetServiceGetWidgetCallback implements AsyncCallback<ISerializedWidget> {
		/** id */
		private final String requestedId;
		/** callback */
		private final GetWidgetCallback callback;

		/**
		 * Creates a new instance.
		 * 
		 * @param requestedId
		 * @param callback
		 */
		private WidgetServiceGetWidgetCallback(final String requestedId, final GetWidgetCallback callback) {
			this.requestedId = requestedId;
			this.callback = callback;
		}

		public void onFailure(final Throwable caught) {
			if (caught instanceof WidgetFactoryException) {
				callback.onFailure((WidgetFactoryException) caught);
			} else {
				callback.onFailure(new WidgetFactoryException(WidgetFactoryException.INTERNAL_ERROR, "an exception occurred: " + caught, caught));
			}
		}

		public void onSuccess(final ISerializedWidget result) {
			if (null == result) {
				callback.onFailure(new WidgetFactoryException(WidgetFactoryException.NO_WIDGET_RETURNED));
				return;
			}

			if (!(result instanceof ISerializedWidget)) {
				callback.onFailure(new WidgetFactoryException(WidgetFactoryException.INTERNAL_ERROR, "invalid result: " + result));
				return;
			}

			// get the actual id from the widget
			final String serializedWidgetId = (result).getId();

			// we cache the response if enabled
			if (isCacheFlagSet(CACHE_SERIALIZED_WIDGETS)) {
				serializedWidgetCache.put(serializedWidgetId, result);
			}

			// create GWT widget
			CWTWidget composite = null;
			try {
				composite = renderWidget(requestedId, result);
			} catch (final Exception e) {
				callback.onFailure(new WidgetFactoryException(WidgetFactoryException.WIDGET_RENDERING_FAILED, e.getMessage(), e));
				return;
			}

			// cache the widget if enabled
			if (isCacheFlagSet(CACHE_RENDERED_WIDGETS)) {
				renderedWidgetCache.put(composite.getWidgetId(), composite);
			}

			// call client API
			if (null != composite) {
				callback.onSuccess(composite);
			} else {
				callback.onFailure(new WidgetFactoryException(WidgetFactoryException.NO_WIDGET_RETURNED));
			}
			return;
		}
	}

	/** cache nothing (value <code>0</code>) */
	public static final int CACHE_NONE = 0;

	/**
	 * cache serialized widgets received from the server (value
	 * <code>1 &lt;&lt; 1</code>)
	 */
	public static final int CACHE_SERIALIZED_WIDGETS = 1 << 1;

	/**
	 * cache rendered widgets created from serialized widgets (value
	 * <code>1 &lt;&lt; 2</code>)
	 */
	public static final int CACHE_RENDERED_WIDGETS = 1 << 2;

	/** the widget service */
	private WidgetServiceAsync internalWidgetService;

	/** cache with serialized widgets */
	final Map<String, ISerializedWidget> serializedWidgetCache = new HashMap<String, ISerializedWidget>();

	/** cache with serialized widgets */
	final Map<String, CWTWidget> renderedWidgetCache = new HashMap<String, CWTWidget>();

	/** the entry point of the widget service */
	private final String widgetServiceEntryPoint;

	/** the base url to access resources */
	private String resourceBaseUrl;

	/** the toolkit used for rendering */
	private final CWTToolkit toolkit;

	/** the environment */
	private WidgetClientEnvironment environment;

	/** the cache flags */
	private int cacheFlags;

	/**
	 * Creates a new widget factory using a default {@link CWTToolkit} instance.
	 * 
	 * @see #WidgetFactory(String, CWTToolkit)
	 */
	public WidgetFactory(final String widgetServiceEntryPoint) {
		this(widgetServiceEntryPoint, new CWTToolkit());
	}

	/**
	 * Creates a new widget factory.
	 * 
	 * @param widgetServiceEntryPoint
	 *            the entry point to the widget service (i.e., the relative or
	 *            absolute URI to the widget service servlet) (may not be
	 *            <code>null</code>)
	 * @param toolkit
	 *            the underlying toolkit for rendering CWT widgets (may not be
	 *            <code>null</code>)
	 */
	public WidgetFactory(final String widgetServiceEntryPoint, final CWTToolkit toolkit) {
		if (null == widgetServiceEntryPoint) {
			CWTToolkit.error(CWTToolkit.ERROR_NULL_ARGUMENT, "widgetServiceEntryPoint");
		}
		if (null == toolkit) {
			CWTToolkit.error(CWTToolkit.ERROR_NULL_ARGUMENT, "toolkit");
		}

		this.widgetServiceEntryPoint = widgetServiceEntryPoint;
		this.toolkit = toolkit;
		this.toolkit.setWidgetFactory(this);

		// default: cache serialized and rendered widgets
		setCacheFlags(CACHE_SERIALIZED_WIDGETS | CACHE_RENDERED_WIDGETS);

		// default: module base url
		setResourceBaseUrl(GWT.getModuleBaseURL());
	}

	/**
	 * Returns a new widget service async instance.
	 * 
	 * @return a widget service async instance
	 */
	private WidgetServiceAsync createWidgetServiceAsync(final String entryPoint) {
		final WidgetServiceAsync instance = (WidgetServiceAsync) GWT.create(WidgetService.class);
		final ServiceDefTarget target = (ServiceDefTarget) instance;
		target.setServiceEntryPoint(entryPoint);
		return instance;
	}

	/**
	 * Executes a command on the server.
	 * 
	 * @param commandId
	 * @param widgetId
	 * @param contentSet
	 * @param executeCommandCallback
	 */
	public void executeCommand(final String commandId, final String widgetId, final ISerializedData contentSet, final CommandExecutionCallback executeCommandCallback) {
		getInternalWidgetService().executeCommand(commandId, widgetId, (SContentSet) contentSet, getEnvironment(), new AsyncCallback<SCommandExecutionResult>() {

			public void onFailure(final Throwable caught) {
				if (caught instanceof WidgetFactoryException) {
					executeCommandCallback.onFailure((WidgetFactoryException) caught);
				} else {
					executeCommandCallback.onFailure(new WidgetFactoryException(WidgetFactoryException.INTERNAL_ERROR, "an exception occurred: " + caught, caught));
				}
			}

			public void onSuccess(final SCommandExecutionResult result) {
				// we expect a result
				if (null == result) {
					executeCommandCallback.onFailure(new WidgetFactoryException(WidgetFactoryException.INTERNAL_ERROR, "missing result from server "));
					return;
				}

				// verify it's the command we triggered
				if (!commandId.equals(result.id)) {
					executeCommandCallback.onFailure(new WidgetFactoryException(WidgetFactoryException.INTERNAL_ERROR, "received wrong result from server"));
					return;
				}

				// verify there is a status
				if (null == result.status) {
					executeCommandCallback.onFailure(new WidgetFactoryException(WidgetFactoryException.INTERNAL_ERROR, "missing status in server response"));
					return;
				}

				// construct event object
				final CommandExecutedEvent event = new CommandExecutedEvent(commandId, result.status);

				// handle status
				executeCommandCallback.onExecuted(event);

				// process actions
				if (event.isContinueEventProcessing()) {
					if ((result != null) && (result.actions != null)) {
						final SAction[] actions = result.actions;
						for (final SAction action : actions) {
							getToolkit().getActionHandler().handleAction(action);
						}
					}
				}
			}
		});

	}

	/**
	 * Returns the environment.
	 * 
	 * @return the environment (maybe <code>null</code> if non was set)
	 */
	public WidgetClientEnvironment getEnvironment() {
		return environment;
	}

	private WidgetServiceAsync getInternalWidgetService() {
		if (null == internalWidgetService) {
			internalWidgetService = createWidgetServiceAsync(widgetServiceEntryPoint);
		}
		return internalWidgetService;
	}

	/**
	 * Returns the base URL for accessing resources.
	 * 
	 * @return the resource base URL (if non-empty, the base URL is guaranteed
	 *         to end with a slash)
	 * @see #setResourceBaseUrl(String)
	 */
	public String getResourceBaseUrl() {
		return resourceBaseUrl;
	}

	/**
	 * Returns the toolkit used by this factory.
	 * 
	 * @return the toolkit
	 */
	public CWTToolkit getToolkit() {
		return toolkit;
	}

	/**
	 * Creates the widget with the specified id and submits the result to the
	 * specified receiver.
	 * 
	 * @param widgetId
	 * @param callback
	 */
	public void getWidget(final String widgetId, final GetWidgetCallback callback) {
		// try rendered cache if enabled
		if (isCacheFlagSet(CACHE_RENDERED_WIDGETS) && renderedWidgetCache.containsKey(widgetId)) {
			final CWTWidget composite = renderedWidgetCache.get(widgetId);
			if (null != composite) {
				callback.onSuccess(composite);
			} else {
				callback.onFailure(new WidgetFactoryException(WidgetFactoryException.NO_WIDGET_RETURNED));
			}
			return;
		}

		// try serialized cache if enabled
		if (isCacheFlagSet(CACHE_SERIALIZED_WIDGETS) && serializedWidgetCache.containsKey(widgetId)) {
			final SContainer container = (SContainer) serializedWidgetCache.get(widgetId);
			if (null != container) {
				final CWTWidget composite = renderWidget(widgetId, container);
				if (null != composite) {
					callback.onSuccess(composite);
				} else {
					callback.onFailure(new WidgetFactoryException(WidgetFactoryException.NO_WIDGET_RETURNED));
				}
				return;
			}
		}

		// get widget from server
		getInternalWidgetService().getWidget(widgetId, getEnvironment(), new WidgetServiceGetWidgetCallback(widgetId, callback));
	}

	/**
	 * Indicates if a specific cache flag is set.
	 * 
	 * @param flag
	 *            the fleg to test
	 * @return <code>true</code> if the flag is set, <code>false</code>
	 *         otherwise
	 * @see #CACHE_NONE
	 * @see #CACHE_SERIALIZED_WIDGETS
	 * @see #CACHE_RENDERED_WIDGETS
	 */
	public boolean isCacheFlagSet(final int flag) {
		return (cacheFlags & flag) != 0;
	}

	/**
	 * Renders the specified container into a GWT {@link Composite}.
	 * 
	 * @param widget
	 * @return the composite
	 */
	private CWTWidget renderWidget(final String requestedId, final ISerializedWidget widget) {
		final CWTWidget composite = getToolkit().createWidget(widget);
		if (null != composite) {
			return composite;
		}

		return getToolkit().createDefaultWidget(widget);
	}

	/**
	 * Resets the caches
	 */
	private void resetCaches() {
		renderedWidgetCache.clear();
		serializedWidgetCache.clear();
	}

	/**
	 * Sets the cache flags to use.
	 * <p>
	 * Note, calling this method will also flush the current cache content.
	 * </p>
	 * 
	 * @param cacheFlags
	 *            the cache flags to set
	 * @see #isCacheFlagSet(int)
	 * @see #CACHE_NONE
	 * @see #CACHE_SERIALIZED_WIDGETS
	 * @see #CACHE_RENDERED_WIDGETS
	 */
	public void setCacheFlags(final int cacheFlags) {
		this.cacheFlags = cacheFlags;
		resetCaches();
	}

	/**
	 * Sets the environment to use.
	 * 
	 * @param environment
	 *            the environment to set (maybe <code>null</code> to unset)
	 */
	public void setEnvironment(final WidgetClientEnvironment environment) {
		this.environment = environment;
	}

	/**
	 * Sets the base URL for accessing resources.
	 * <p>
	 * Note, using <code>null</code> here will reset the URL to the
	 * {@link GWT#getModuleBaseURL() GWT module base URL}.
	 * </p>
	 * 
	 * @param resourceBaseUrl
	 *            the base URL to set
	 */
	public void setResourceBaseUrl(final String resourceBaseUrl) {
		if (resourceBaseUrl == null) {
			// default to GWT module base URL
			this.resourceBaseUrl = GWT.getModuleBaseURL();
		} else {
			// make sure the URL ends with a slash
			if (!resourceBaseUrl.endsWith("/")) {
				this.resourceBaseUrl = resourceBaseUrl.concat("/");
			} else {
				this.resourceBaseUrl = resourceBaseUrl;
			}
		}
	}
}
