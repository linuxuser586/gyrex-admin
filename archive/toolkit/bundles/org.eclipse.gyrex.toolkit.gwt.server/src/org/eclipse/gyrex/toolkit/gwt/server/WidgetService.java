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
package org.eclipse.cloudfree.toolkit.gwt.server;


import org.eclipse.cloudfree.toolkit.gwt.server.internal.WidgetServiceImpl;
import org.eclipse.cloudfree.toolkit.runtime.lookup.IWidgetAdapterFactory;
import org.eclipse.cloudfree.toolkit.runtime.lookup.IWidgetFactory;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * This GWT remote service is responsible for delivering CloudFree widgets to
 * any GWT client.
 * <p>
 * A {@link IWidgetFactory} is used to lookup widgets be their ids. The
 * {@link IWidgetAdapterFactory} is used to lookup widget adapters.
 * </p>
 * <p>
 * This class is intended to be instantiated and/or extended by clients. Clients
 * may also use the {@link WidgetServiceServlet} in case they want a ready-to-go
 * servlet.
 * </p>
 */
public class WidgetService extends WidgetServiceImpl implements RemoteService {

	/**
	 * Convenience constructor to create a new service instance.
	 * <p>
	 * Calling this constructor is equivalent to calling
	 * <code>new WidgetService(widgetFactory, null)</code>.
	 * </p>
	 * 
	 * @param widgetFactory
	 *            the widget factory (may not be <code>null</code>)
	 * @see #WidgetService(IWidgetFactory, IWidgetAdapterFactory)
	 */
	public WidgetService(final IWidgetFactory widgetFactory) {
		this(widgetFactory, null);
	}

	/**
	 * Convenience constructor to create a new service instance using a default
	 * widget environment.
	 * 
	 * @param widgetFactory
	 *            the widget factory (may not be <code>null</code>)
	 * @param widgetAdapterFactory
	 *            the widget adapter factory ()
	 * @see WidgetServiceAdvisor#WidgetServiceAdvisor(IWidgetFactory,
	 *      IWidgetAdapterFactory)
	 */
	public WidgetService(final IWidgetFactory widgetFactory, final IWidgetAdapterFactory widgetAdapterFactory) {
		this(new WidgetServiceAdvisor(widgetFactory, widgetAdapterFactory));
	}

	/**
	 * Creates a new instance using the specified service advisor.
	 * 
	 * @param widgetServiceAdvisor
	 *            the widget service advisor (may not be <code>null</code>)
	 */
	public WidgetService(final WidgetServiceAdvisor widgetServiceAdvisor) {
		super(widgetServiceAdvisor);
	}

	/**
	 * Returns the widget adapter factory used by this service.
	 * 
	 * @return the widget adapter factory
	 */
	@Override
	public final IWidgetAdapterFactory getWidgetAdapterFactory() {
		return super.getWidgetAdapterFactory();
	}

	/**
	 * Returns the widget factory used by this service.
	 * 
	 * @return the widget factory
	 */
	@Override
	public final IWidgetFactory getWidgetFactory() {
		return super.getWidgetFactory();
	}

	/**
	 * Returns the widget service advisor used by this service.
	 * 
	 * @return the widget service advisor
	 */
	@Override
	public WidgetServiceAdvisor getWidgetServiceAdvisor() {
		return super.getWidgetServiceAdvisor();
	}
}
