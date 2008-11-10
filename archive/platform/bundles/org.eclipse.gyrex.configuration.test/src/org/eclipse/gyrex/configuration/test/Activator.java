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
package org.eclipse.cloudfree.configuration.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static final String PLUGIN_ID = "org.eclipse.cloudfree.configuration.test";

	private static Activator sharedInstance;

	/**
	 * Returns the sharedInstance.
	 * 
	 * @return the sharedInstance
	 */
	public static Activator getInstance() {
		if (null == sharedInstance) {
			throw new IllegalStateException("inactive: " + PLUGIN_ID);
		}
		return sharedInstance;
	}

	private BundleContext bundleContext;

	/**
	 * Returns the bundleContext.
	 * 
	 * @return the bundleContext
	 */
	public BundleContext getBundleContext() {
		if (null == bundleContext) {
			throw new IllegalStateException("inactive: " + PLUGIN_ID);
		}
		return bundleContext;
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		sharedInstance = this;
		bundleContext = context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(final BundleContext context) throws Exception {
		sharedInstance = null;
		bundleContext = null;
	}

}
