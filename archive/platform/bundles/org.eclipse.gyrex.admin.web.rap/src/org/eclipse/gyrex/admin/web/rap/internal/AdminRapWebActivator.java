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
package org.eclipse.cloudfree.admin.web.rap.internal;


import org.eclipse.cloudfree.admin.internal.AdminActivator;
import org.eclipse.cloudfree.admin.internal.widgets.DynamicAwareWidgetAdapterFactory;
import org.eclipse.cloudfree.admin.internal.widgets.DynamicAwareWidgetFactory;
import org.eclipse.cloudfree.toolkit.rap.WidgetService;
import org.eclipse.cloudfree.toolkit.rap.WidgetServiceAdvisor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class AdminRapWebActivator extends AbstractUIPlugin {

	/** ADMIN_RAP */
	private static final String ADMIN_RAP = "/admin-rap";

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.cloudfree.admin.web.rap";

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
		AdminActivator.getInstance().addAdminApplicationBase(ADMIN_RAP);

		// initialize the widget service
		widgetService = new WidgetService(new WidgetServiceAdvisor(new DynamicAwareWidgetFactory(), new DynamicAwareWidgetAdapterFactory()));
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
