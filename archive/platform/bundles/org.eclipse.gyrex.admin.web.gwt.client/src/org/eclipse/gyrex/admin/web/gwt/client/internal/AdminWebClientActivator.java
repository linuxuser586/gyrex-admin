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
package org.eclipse.gyrex.admin.web.gwt.client.internal;

import org.eclipse.gyrex.admin.internal.AdminActivator;
import org.eclipse.gyrex.admin.web.gwt.client.internal.shared.IAdminClientConstants;
import org.eclipse.gyrex.common.logging.LogAudience;
import org.eclipse.gyrex.common.logging.LogImportance;
import org.eclipse.gyrex.common.logging.LogSource;
import org.eclipse.gyrex.common.runtime.BaseBundleActivator;
import org.eclipse.gyrex.gwt.service.GwtService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class AdminWebClientActivator extends BaseBundleActivator implements ServiceTrackerCustomizer {

	/** ADMIN */
	private static final String ADMIN = "/admin/";

	/**
	 * filter string for the admin http service (value
	 * 
	 * <code>(&(objectClass=org.eclipse.gyrex.gwt.service.GwtService)(http.service.other.info=org.eclipse.gyrex.admin.http))</code>
	 * )
	 */
	public static final String FILTER_ADMIN_GWT_SERVICE = "(&(objectClass=" + GwtService.class.getName() + ")(http.service.other.info=" + AdminActivator.TYPE_ADMIN + "))"; // use class references here to trigger lazy activation

	/** PLUGIN_ID */
	private static final String PLUGIN_ID = "org.eclipse.gyrex.admin.web.gwt.client";

	/** the shared instance */
	private static AdminWebClientActivator sharedInstance;

	/**
	 * Returns the sharedInstance.
	 * 
	 * @return the sharedInstance
	 */
	public static AdminWebClientActivator getInstance() {
		if (null == sharedInstance) {
			throw new IllegalStateException("not started");
		}
		return sharedInstance;
	}

	private ServiceTracker gwtServiceTracker;

	/** the default alias */
	static final String ADMIN_ALIAS = "/admin";

	/** the backoffice resource root */
	static final String ADMIN_RESOURCE_ROOT = "/resources";

	/**
	 * Creates a new instance.
	 * 
	 * @param pluginId
	 */
	public AdminWebClientActivator() {
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
				gwtService.registerModule(ADMIN_ALIAS, IAdminClientConstants.MODULE_ID, ADMIN_RESOURCE_ROOT, "AdminClient.html", null);
			} catch (final NamespaceException e) {
				getLog().log("An error occurred while registering the admin client resources.", e, (Object) null, LogImportance.ERROR, LogAudience.DEVELOPER, LogSource.PLATFORM);
			}
		}
		return service;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.common.runtime.BaseBundleActivator#doStart(org.osgi.framework.BundleContext)
	 */
	@Override
	protected void doStart(final BundleContext context) throws Exception {
		sharedInstance = this;

		// open the tracker
		if (gwtServiceTracker == null) {
			gwtServiceTracker = new ServiceTracker(context, context.createFilter(FILTER_ADMIN_GWT_SERVICE), this);
			gwtServiceTracker.open();
		}

		// set the web base
		AdminActivator.getInstance().addAdminApplicationBase(ADMIN);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.common.runtime.BaseBundleActivator#doStop(org.osgi.framework.BundleContext)
	 */
	@Override
	protected void doStop(final BundleContext context) throws Exception {
		// unset the web base
		AdminActivator.getInstance().removeAdminApplicationBase(ADMIN);

		// stop the service tracker
		if (null != gwtServiceTracker) {
			gwtServiceTracker.close();
			gwtServiceTracker = null;
		}

		sharedInstance = null;
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
			gwtService.unregister(ADMIN_ALIAS);
		}

		getBundle().getBundleContext().ungetService(reference);
	}

}
