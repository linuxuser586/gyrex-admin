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
package org.eclipse.gyrex.toolkit.gwt.examples;


import org.eclipse.gyrex.gwt.service.GwtService;
import org.eclipse.gyrex.toolkit.gwt.examples.client.Simple1Constants;
import org.eclipse.gyrex.toolkit.gwt.examples.simple1.Simple1WidgetService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	static class GwtServiceTracker extends ServiceTracker {

		/** NET_Gyrex_TOOLKIT_GWT_EXAMPLES_SIMPLE1 */
		private static final String MODULE_ID_SIMPLE1 = "org.eclipse.gyrex.toolkit.gwt.examples.Simple1";
		private static final String ALIAS_SIMPLE1_MODULE = "/gyrex/gwt/examples/simple1";
		private static final String ALIAS_SIMPLE1_REMOTE_SERVICE_WS = Simple1Constants.EP_SIMPLE1_WIDGETSERVICE;

		/**
		 * Creates a new instance.
		 * 
		 * @param context
		 * @param filter
		 * @param customizer
		 */
		public GwtServiceTracker(final BundleContext context) {
			super(context, GwtService.class.getName(), null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
		 */
		@Override
		public Object addingService(final ServiceReference reference) {
			// calls context.getService
			final GwtService gwtService = (GwtService) super.addingService(reference);
			if (null == gwtService) {
				return null;
			}

			try {
				gwtService.registerModule(ALIAS_SIMPLE1_MODULE, MODULE_ID_SIMPLE1, "/generated/org.eclipse.gyrex.toolkit.gwt.examples.Simple1", "Simple1.html", null);
				System.out.println("Registered " + ALIAS_SIMPLE1_MODULE);

				gwtService.registerRemoteService(ALIAS_SIMPLE1_REMOTE_SERVICE_WS, MODULE_ID_SIMPLE1, new Simple1WidgetService(), null, null);
				System.out.println("Registered " + ALIAS_SIMPLE1_REMOTE_SERVICE_WS);

			} catch (final Exception e) {
				System.err.println("Error while registering GWT resources: " + e.getMessage());
				e.printStackTrace(System.err);
			}

			return gwtService;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference,
		 *      java.lang.Object)
		 */
		@Override
		public void removedService(final ServiceReference reference, final Object service) {
			final GwtService gwtService = (GwtService) service;

			if (null != gwtService) {
				// unregister resources
				gwtService.unregister(ALIAS_SIMPLE1_MODULE);
				gwtService.unregister(ALIAS_SIMPLE1_REMOTE_SERVICE_WS);
			}

			// calls context.ungetService
			super.removedService(reference, service);
		}

	}

	private GwtServiceTracker httpServiceTracker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		httpServiceTracker = new GwtServiceTracker(context);
		httpServiceTracker.open();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(final BundleContext context) throws Exception {
		httpServiceTracker.close();
		httpServiceTracker = null;
	}

}
