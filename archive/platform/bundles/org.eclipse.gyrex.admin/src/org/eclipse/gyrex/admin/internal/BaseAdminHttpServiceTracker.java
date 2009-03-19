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
package org.eclipse.gyrex.admin.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Base HTTP service tracker for Gyrex Admin HTTP service.
 */
public abstract class BaseAdminHttpServiceTracker extends ServiceTracker {

	/**
	 * filter string for the admin http service (value
	 * <code>(&(objectClass=org.osgi.service.http.HttpService)(other.info=org.eclipse.gyrex.http.admin))</code>)
	 */
	private static final String FILTER_ADMIN_HTTP_SERVICE = "(&(objectClass=" + HttpService.class.getName() + ")(other.info=" + AdminActivator.TYPE_ADMIN + "))";

	private static Filter createFilter(final BundleContext context) {
		try {
			return context.createFilter(FILTER_ADMIN_HTTP_SERVICE);
		} catch (final InvalidSyntaxException e) {
			// this should never happen because we tested the filter
			throw new IllegalStateException("error in implementation: " + e);
		}
	}

	/**
	 * Creates and returns new admin service tracker instance.
	 * 
	 * @param context
	 *            the bundle context (may not be <code>null</code>)
	 */
	public BaseAdminHttpServiceTracker(final BundleContext context) {
		super(context, createFilter(context), null);
	}

}
