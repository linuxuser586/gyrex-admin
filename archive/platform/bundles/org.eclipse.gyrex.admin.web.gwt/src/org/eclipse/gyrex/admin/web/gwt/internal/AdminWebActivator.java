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
package org.eclipse.gyrex.admin.web.gwt.internal;

import java.security.Principal;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.gyrex.admin.internal.widgets.DynamicAwareWidgetAdapterFactory;
import org.eclipse.gyrex.admin.internal.widgets.DynamicAwareWidgetFactory;
import org.eclipse.gyrex.admin.web.gwt.client.internal.AdminWebClientActivator;
import org.eclipse.gyrex.admin.web.gwt.client.internal.shared.IAdminConsoleConstants;
import org.eclipse.gyrex.common.logging.LogAudience;
import org.eclipse.gyrex.common.logging.LogImportance;
import org.eclipse.gyrex.common.logging.LogSource;
import org.eclipse.gyrex.common.runtime.BaseBundleActivator;
import org.eclipse.gyrex.gwt.service.GwtRequestResponseListener;
import org.eclipse.gyrex.gwt.service.GwtService;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetClientEnvironment;
import org.eclipse.gyrex.toolkit.gwt.server.WidgetResourceServlet;
import org.eclipse.gyrex.toolkit.gwt.server.WidgetService;
import org.eclipse.gyrex.toolkit.gwt.server.WidgetServiceAdvisor;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * The activator class controls the plug-in life cycle
 */
public class AdminWebActivator extends BaseBundleActivator implements ServiceTrackerCustomizer {

	class AdminWidgetServiceAdvisor extends WidgetServiceAdvisor {
		public AdminWidgetServiceAdvisor() {
			super(new DynamicAwareWidgetFactory(), new DynamicAwareWidgetAdapterFactory());
		}

		@Override
		protected Locale getDefaultLocale() {
			return getThreadLocalRequest().getLocale();
		}

		private HttpServletRequest getThreadLocalRequest() {
			return requestResponseListener.getThreadLocalRequest();
		};

		/* (non-Javadoc)
		 * @see org.eclipse.gyrex.toolkit.gwt.server.WidgetServiceAdvisor#getUserPrincipal(org.eclipse.gyrex.toolkit.gwt.client.WidgetClientEnvironment)
		 */
		@Override
		protected Principal getUserPrincipal(final WidgetClientEnvironment environment) {
			return getThreadLocalRequest().getUserPrincipal();
		}
	}

	/** plug-in ID */
	private static final String PLUGIN_ID = "org.eclipse.gyrex.admin.web.gwt";

	/** the default alias */
	static final String ADMIN_WIDGET_SERVICE_ALIAS = IAdminConsoleConstants.ENTRYPOINT_WIDGET_SERVICE;
	static final String ADMIN_WIDGET_RESOURCES_BASE_URL = IAdminConsoleConstants.WIDGET_RESOURCE_BASE_URL;
	static final String ADMIN_CONSOLE_SERVICE_ALIAS = IAdminConsoleConstants.ENTRYPOINT_CONSOLE_SERVICE;

	private ServiceTracker gwtServiceTracker;
	private WidgetService widgetService;

	final GwtRequestResponseListener requestResponseListener = new GwtRequestResponseListener();

	/**
	 * The constructor
	 */
	public AdminWebActivator() {
		super(PLUGIN_ID);
	}

	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#addingService(org.osgi.framework.ServiceReference)
	 */
	public Object addingService(final ServiceReference reference) {
		final Object service = getBundle().getBundleContext().getService(reference);
		if (service instanceof GwtService) {
			final GwtService gwtService = (GwtService) service;
			try {
				gwtService.registerRemoteService(ADMIN_WIDGET_SERVICE_ALIAS, IAdminConsoleConstants.MODULE_ID, getWidgetService(), requestResponseListener, null);
				gwtService.registerServlet(ADMIN_WIDGET_RESOURCES_BASE_URL, IAdminConsoleConstants.MODULE_ID, new WidgetResourceServlet(), null, null);
				gwtService.registerRemoteService(ADMIN_CONSOLE_SERVICE_ALIAS, IAdminConsoleConstants.MODULE_ID, new AdminConsoleServiceImpl(), requestResponseListener, null);
			} catch (final Exception e) {
				getLog().log("An error occurred while registering the admin widget service.", e, (Object) null, LogImportance.ERROR, LogAudience.DEVELOPER, LogSource.PLATFORM);
			}
		}
		return service;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.common.runtime.BaseBundleActivator#doStart(org.osgi.framework.BundleContext)
	 */
	@Override
	protected void doStart(final BundleContext context) throws Exception {
		// open the tracker
		if (gwtServiceTracker == null) {
			final String filterAdminGwtService = AdminWebClientActivator.FILTER_ADMIN_GWT_SERVICE; // use reference to AdminWebClientActivator here to trigger lazy bundle activation
			gwtServiceTracker = new ServiceTracker(context, context.createFilter(filterAdminGwtService), this);
			gwtServiceTracker.open();
		}
	}

	@Override
	protected void doStop(final BundleContext context) throws Exception {
		// stop the service tracker
		if (null != gwtServiceTracker) {
			gwtServiceTracker.close();
			gwtServiceTracker = null;
		}

		// free widget servlet service
		if (null != widgetService) {
			widgetService = null;
		}
	}

	private synchronized WidgetService getWidgetService() {
		if (null != widgetService) {
			return widgetService;
		}

		// initialize widget service servlet
		return widgetService = new WidgetService(new AdminWidgetServiceAdvisor());
	}

	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#modifiedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	public void modifiedService(final ServiceReference reference, final Object service) {
		// empty
	}

	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	public void removedService(final ServiceReference reference, final Object service) {
		if (service instanceof GwtService) {
			final GwtService gwtService = (GwtService) service;
			try {
				gwtService.unregister(ADMIN_WIDGET_RESOURCES_BASE_URL);
			} catch (final IllegalArgumentException e) {
				// ignore
			}
			try {
				gwtService.unregister(ADMIN_WIDGET_SERVICE_ALIAS);
			} catch (final IllegalArgumentException e) {
				// ignore
			}
			try {
				gwtService.unregister(ADMIN_CONSOLE_SERVICE_ALIAS);
			} catch (final IllegalArgumentException e) {
				// ignore
			}
		}

		getBundle().getBundleContext().ungetService(reference);
	}

}
