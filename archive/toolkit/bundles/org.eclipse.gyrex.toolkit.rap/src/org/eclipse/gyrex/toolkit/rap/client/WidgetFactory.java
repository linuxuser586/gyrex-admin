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
package org.eclipse.cloudfree.toolkit.rap.client;

import java.util.HashMap;
import java.util.Map;


import org.eclipse.cloudfree.toolkit.CWT;
import org.eclipse.cloudfree.toolkit.CWTException;
import org.eclipse.cloudfree.toolkit.commands.Command;
import org.eclipse.cloudfree.toolkit.content.ContentSet;
import org.eclipse.cloudfree.toolkit.rap.WidgetService;
import org.eclipse.cloudfree.toolkit.rap.internal.ui.widgets.CWTToolkit;
import org.eclipse.cloudfree.toolkit.rap.internal.ui.widgets.CWTWidget;
import org.eclipse.cloudfree.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.cloudfree.toolkit.runtime.commands.CommandExecutionResult;
import org.eclipse.cloudfree.toolkit.widgets.Widget;

/**
 * This is the factory for creating SWT/JFace/Forms UI widgets exposed by a
 * service.
 * <p>
 * The factory uses a widget service to lookup CWT widgets, execute widgets
 * commands, etc.
 * </p>
 * <p>
 * The factory maintains an internal cache so that subsequent lookups for the
 * same widget id will not query the service again. However, sometimes this
 * dynamic behavior is desired (eg., in development environments). Thus, the
 * caching can be {@link #setCacheFlags(int) configured}.
 * </p>
 * <p>
 * Note, this is experimental API. It will not be stable until version 1.0 is
 * officially released. Currently this API lacks support for localization and
 * content handling.
 * </p>
 * 
 * @see WidgetService
 * @noextend This class is not intended to be subclassed by clients.
 */
public final class WidgetFactory {

	/** cache nothing (value <code>0</code>) */
	public static final int CACHE_NONE = 0;

	/**
	 * cache widgets received from the service (value <code>1 &lt;&lt; 1</code>)
	 */
	public static final int CACHE_WIDGETS = 1 << 1;

	/** the widget service */
	private final WidgetService widgetService;

	/** cache with widgets */
	final Map<String, Widget> widgetCache = new HashMap<String, Widget>();

	/** the toolkit used for rendering */
	private final CWTToolkit toolkit;

	/** the environment */
	private IWidgetEnvironment environment;

	/** the cache flags */
	private int cacheFlags;

	/**
	 * Creates a new widget factory.
	 * 
	 * @param widgetService
	 *            the widget service (may not be <code>null</code>)
	 * @param toolkit
	 *            the underlying toolkit for rendering CWT widgets (may not be
	 *            <code>null</code>)
	 */
	public WidgetFactory(final WidgetService widgetService, final CWTToolkit toolkit) {
		if (null == widgetService) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "widgetService");
		}
		if (null == toolkit) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "toolkit");
		}

		this.widgetService = widgetService;
		this.toolkit = toolkit;
		this.toolkit.setWidgetFactory(this);

		// default: cache widgets
		setCacheFlags(CACHE_WIDGETS);
	}

	/**
	 * Executes a command on the server.
	 * 
	 * @param command
	 * @param source
	 * @param contentSet
	 */
	public CommandExecutionResult executeCommand(final Command command, final Widget source, final ContentSet contentSet) {
		return getWidgetService().executeCommand(command, source, contentSet, getEnvironment());
	}

	/**
	 * Returns the environment used by this factory.
	 * 
	 * @return the environment (maybe <code>null</code> if non was set)
	 */
	public IWidgetEnvironment getEnvironment() {
		return environment;
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
	 * Creates and returns the widget with the specified id.
	 * 
	 * @param widgetId
	 *            the widget id
	 * @return the widget (maybe <code>null</code> if not found
	 * @throws CWTException
	 *             if an error occurred during widget initialization
	 */
	public CWTWidget getWidget(final String widgetId) throws CWTException {
		// try raw cache if enabled
		if (isCacheFlagSet(CACHE_WIDGETS) && widgetCache.containsKey(widgetId)) {
			final Widget widget = widgetCache.get(widgetId);
			if (null != widget) {
				final CWTWidget<Widget> renderedWidget = renderWidget(widgetId, widget);
				if (null != renderedWidget) {
					return renderedWidget;
				}
			}
			return null; // not found
		}

		// get widget from service
		final Widget widget = getWidgetService().getWidget(widgetId, getEnvironment());
		if (null != widget) {
			if (isCacheFlagSet(CACHE_WIDGETS)) {
				widgetCache.put(widgetId, widget);
			}
			final CWTWidget<Widget> renderedWidget = renderWidget(widgetId, widget);
			if (null != renderedWidget) {
				return renderedWidget;
			}
		}

		return null; // not found
	}

	/**
	 * Returns the widget service.
	 * 
	 * @return the widget service
	 */
	public WidgetService getWidgetService() {
		return widgetService;
	}

	/**
	 * Indicates if a specific cache flag is set.
	 * 
	 * @param flag
	 *            the fleg to test
	 * @return <code>true</code> if the flag is set, <code>false</code>
	 *         otherwise
	 * @see #CACHE_NONE
	 * @see #CACHE_WIDGETS
	 * @see #CACHE_RENDERED_WIDGETS
	 */
	public boolean isCacheFlagSet(final int flag) {
		return (cacheFlags & flag) != 0;
	}

	/**
	 * Renders the specified container into a {@link CWTWidget}.
	 * 
	 * @param widget
	 * @return the composite
	 */
	private <T extends Widget> CWTWidget<T> renderWidget(final String requestedId, final T widget) {
		final CWTWidget<T> createWidget = getToolkit().createWidget(widget);
		if (null != createWidget) {
			return createWidget;
		}

		return getToolkit().createDefaultWidget(widget);
	}

	/**
	 * Resets the caches
	 */
	private void resetCaches() {
		widgetCache.clear();
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
	 * @see #CACHE_WIDGETS
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
	public void setEnvironment(final IWidgetEnvironment environment) {
		this.environment = environment;
	}
}
