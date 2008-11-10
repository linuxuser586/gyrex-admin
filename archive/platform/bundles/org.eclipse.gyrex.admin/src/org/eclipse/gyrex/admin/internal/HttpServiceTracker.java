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
package org.eclipse.cloudfree.admin.internal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.eclipse.cloudfree.common.logging.LogAudience;
import org.eclipse.cloudfree.common.logging.LogImportance;
import org.eclipse.cloudfree.common.logging.LogSource;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

/**
 * A server tracker that registers the root servlet with every tracked HTTP
 * service.
 */
/*package*/class HttpServiceTracker extends BaseAdminHttpServiceTracker {

	private final static class AdminRootServlet extends HttpServlet {
		/** serialVersionUID */
		private static final long serialVersionUID = -7985169474502980696L;

		@Override
		protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
			// check if the 'homepage' is requested
			final String pathInfo = req.getPathInfo();
			if ((null != pathInfo) && !pathInfo.equals("/")) {
				// another page is requested, fail with not found
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Page Not Found");
				return;
			}

			// get the admin application base
			final String adminBase = AdminActivator.getInstance().getAdminApplicationBase();
			if (null == adminBase) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Admin application not available");
				return;
			}

			// avoid endless loops
			if ((adminBase.trim().length() == 0) || adminBase.equals("/")) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid Admin application configured");
				return;
			}

			// redirect
			resp.sendRedirect(adminBase);
		}
	}

	/** ROOT_ALIAS */
	private static final String ROOT_ALIAS = "/";

	/** alias for the error resources */
	private static final String ALIAS_ERROR_RESOURCES = "/error_resources";

	/**
	 * Creates a new instance.
	 * 
	 * @param context
	 */
	public HttpServiceTracker(final BundleContext context) {
		super(context);
	}

	/**
	 * Registers a {@link WidgetServiceServlet} with the {@link HttpService}.
	 * 
	 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public Object addingService(final ServiceReference reference) {
		final HttpService httpService = (HttpService) super.addingService(reference); // calls context.getService(reference);
		if (null == httpService) {
			return null;
		}

		// create the root servlet to redirect to the admin interface
		final AdminRootServlet rootServlet = new AdminRootServlet();

		// register the root servlet
		try {
			httpService.registerServlet(ROOT_ALIAS, rootServlet, null, null);
		} catch (final Exception e) {
			AdminActivator.getInstance().getLog().log("An error occurred while registering the root servlet.", e, (Object) null, LogImportance.CRITICAL, LogAudience.DEVELOPER, LogAudience.ADMIN, LogSource.PLATFORM);
		}
		return httpService;
	}

	/**
	 * Unregisters the registered {@link WidgetServiceServlet}.
	 * 
	 * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference,
	 *      java.lang.Object)
	 */
	@Override
	public void removedService(final ServiceReference reference, final Object service) {
		final HttpService httpService = (HttpService) service;
		httpService.unregister(ROOT_ALIAS);
		httpService.unregister(ALIAS_ERROR_RESOURCES);

		super.removedService(reference, service); // calls context.ungetService(reference);
	}
}
