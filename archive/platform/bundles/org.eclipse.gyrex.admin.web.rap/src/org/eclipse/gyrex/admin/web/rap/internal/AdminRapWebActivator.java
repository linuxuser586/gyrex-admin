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
package org.eclipse.gyrex.admin.web.rap.internal;

import java.lang.reflect.Field;

import javax.servlet.ServletException;

import org.eclipse.gyrex.admin.internal.AdminActivator;
import org.eclipse.gyrex.admin.internal.BaseAdminHttpServiceTracker;
import org.eclipse.gyrex.admin.internal.widgets.DynamicAwareWidgetAdapterFactory;
import org.eclipse.gyrex.admin.internal.widgets.DynamicAwareWidgetFactory;
import org.eclipse.gyrex.toolkit.rap.WidgetService;
import org.eclipse.gyrex.toolkit.rap.WidgetServiceAdvisor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.rap.ui.internal.servlet.RequestHandler;
import org.eclipse.rwt.internal.resources.ResourceManager;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

/**
 * The activator class controls the plug-in life cycle
 */
public class AdminRapWebActivator extends AbstractUIPlugin {

	/** RAP2 */
	private static final String RAP2 = "/rap2";

	/** ADMIN_RAP */
	private static final String ADMIN_RAP = "/admin-rap";

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.gyrex.admin.web.rap";

	// The shared instance
	private static AdminRapWebActivator plugin;

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static AdminRapWebActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	private WidgetService widgetService;

	/**
	 * The constructor
	 */
	public AdminRapWebActivator() {
	}

	public WidgetService getWidgetService() {
		if (null == widgetService) {
			throw new IllegalStateException("inactive");
		}
		return widgetService;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		// set the web base
		//AdminActivator.getInstance().addAdminApplicationBase(ADMIN_RAP);

		// initialize the widget service
		widgetService = new WidgetService(new WidgetServiceAdvisor(new DynamicAwareWidgetFactory(), new DynamicAwareWidgetAdapterFactory()));

		// start RAP on the Admin HTTP Service
		final BaseAdminHttpServiceTracker serviceTracker = new BaseAdminHttpServiceTracker(context) {
			/* (non-Javadoc)
			 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
			 */
			@Override
			public Object addingService(final ServiceReference reference) {
				// TODO Auto-generated method stub
				final HttpService httpService = (HttpService) super.addingService(reference);
				if (null == httpService) {
					return null;
				}

				final ResourceManagerFactoryHack resourceManagerFactory = new ResourceManagerFactoryHack(httpService, reference);

				// initialize the RAP request handler
				final RequestHandler requestHandler = new RequestHandler();

				// register the requestHandler
				try {
					httpService.registerServlet(RAP2, requestHandler, null, null);

					// set our hacked resource manager
					try {
						final Field factoryField = ResourceManager.class.getDeclaredField("factory");
						factoryField.setAccessible(true);
						factoryField.set(null, resourceManagerFactory);
					} catch (final Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (final ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final NamespaceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return httpService;
			}

			/* (non-Javadoc)
			 * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
			 */
			@Override
			public void removedService(final ServiceReference reference, final Object service) {
				final HttpService httpService = (HttpService) service;
				if (null != httpService) {
					httpService.unregister(RAP2);
				}
				// untrack
				super.removedService(reference, service);
			}
		};
		serviceTracker.open();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		// free widget service
		widgetService = null;

		// unset the web base
		AdminActivator.getInstance().removeAdminApplicationBase(ADMIN_RAP);

		plugin = null;
		super.stop(context);
	}
}
