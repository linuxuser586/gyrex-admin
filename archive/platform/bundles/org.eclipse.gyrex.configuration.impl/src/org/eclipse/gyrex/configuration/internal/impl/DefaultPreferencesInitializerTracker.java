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
package org.eclipse.cloudfree.configuration.internal.impl;


import org.eclipse.cloudfree.configuration.preferences.DefaultPreferencesInitializer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * A tracker for {@link DefaultPreferencesInitializer}.
 */
public class DefaultPreferencesInitializerTracker extends ServiceTracker {

	/**
	 * Creates a new instance.
	 * 
	 * @param context
	 */
	public DefaultPreferencesInitializerTracker(final BundleContext context) {
		super(context, DefaultPreferencesInitializer.class.getName(), null);
	}

	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public Object addingService(final ServiceReference reference) {
		final DefaultPreferencesInitializer initializer = (DefaultPreferencesInitializer) super.addingService(reference);
		if (null != initializer) {
			// initialize
			initializer.initializeDefaultPreferences();
		}
		return initializer;
	}

}
