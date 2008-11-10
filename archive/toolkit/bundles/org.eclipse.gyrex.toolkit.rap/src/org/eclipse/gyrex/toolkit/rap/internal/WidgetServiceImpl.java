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
package org.eclipse.cloudfree.toolkit.rap.internal;


import org.eclipse.cloudfree.toolkit.CWT;
import org.eclipse.cloudfree.toolkit.commands.Command;
import org.eclipse.cloudfree.toolkit.rap.IWidgetServiceUICallback;
import org.eclipse.cloudfree.toolkit.rap.WidgetService;
import org.eclipse.cloudfree.toolkit.rap.WidgetServiceAdvisor;
import org.eclipse.cloudfree.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.cloudfree.toolkit.runtime.lookup.IWidgetAdapterFactory;
import org.eclipse.cloudfree.toolkit.runtime.lookup.IWidgetFactory;
import org.eclipse.cloudfree.toolkit.widgets.Widget;

/**
 * This class is the internal {@link WidgetService} implementation.
 * <p>
 * It is considered an implementation detail. Clients may not rely on any public
 * API provided by this class directly or indirectly through framework classes
 * extending this class.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class WidgetServiceImpl {

	/** the advisor */
	private final WidgetServiceAdvisor advisor;

	/**
	 * Creates a new instance.
	 * 
	 * @param advisor
	 *            the advisor
	 */
	public WidgetServiceImpl(final WidgetServiceAdvisor advisor) {
		if (null == advisor) {
			throw new IllegalStateException("advisor must not be null");
		}
		this.advisor = advisor;
	}

	private IWidgetEnvironment createWidgetEnvironment() {
		return getWidgetServiceAdvisor().getWidgetEnvironment();
	}

	private void executeCommand(final Command cancelCommand) {
		if (cancelCommand == null) {
			return;
		}

	}

	protected final Widget getWidget(final String widgetId) {
		// get the factory
		final IWidgetFactory widgetFactory = getWidgetFactory();
		if (null == widgetFactory) {
			CWT.error(CWT.ERROR_WIDGET_NOT_FOUND, "no widget factory available");
		}

		// lookup view
		final Widget widget = lookupWidget(widgetId, widgetFactory);
		if (null == widget) {
			CWT.error(CWT.ERROR_WIDGET_NOT_FOUND, "widget not found: " + widgetId);
		}

		// TODO: cache result

		return widget;
	}

	protected IWidgetAdapterFactory getWidgetAdapterFactory() {
		return getWidgetServiceAdvisor().getWidgetAdapterFactory();
	}

	protected IWidgetFactory getWidgetFactory() {
		final IWidgetFactory widgetFactory = getWidgetServiceAdvisor().getWidgetFactory();
		if (null == widgetFactory) {
			throw new IllegalStateException("invalid advisor: " + getWidgetServiceAdvisor().getClass().getName() + " (did not return a widget factory)");
		}
		return widgetFactory;
	}

	/**
	 * Returns the widget service advisor.
	 * 
	 * @return the widget service advisor
	 */
	protected WidgetServiceAdvisor getWidgetServiceAdvisor() {
		// null check happens in constructor
		//if (null == advisor) {
		//	throw new IllegalStateException("not initialized");
		//}
		return advisor;
	}

	private Widget lookupWidget(final String widgetId, final IWidgetFactory widgetFactory) {
		return widgetFactory.getWidget(widgetId, createWidgetEnvironment());
	}

	protected void openWidget(final String widgetId, final IWidgetServiceUICallback callback) {
		final Widget widget = getWidget(widgetId);
	}
}
