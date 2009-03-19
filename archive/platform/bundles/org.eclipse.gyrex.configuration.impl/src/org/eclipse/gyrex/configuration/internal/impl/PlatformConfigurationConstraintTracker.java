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
package org.eclipse.gyrex.configuration.internal.impl;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


import org.eclipse.gyrex.configuration.constraints.PlatformConfigurationConstraint;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * A tracker for {@link PlatformConfigurationConstraint}.
 */
public class PlatformConfigurationConstraintTracker extends ServiceTracker {

	private final Set<PlatformConfigurationConstraint> checks = new CopyOnWriteArraySet<PlatformConfigurationConstraint>();

	/**
	 * Creates a new instance.
	 * 
	 * @param context
	 */
	public PlatformConfigurationConstraintTracker(final BundleContext context) {
		super(context, PlatformConfigurationConstraint.class.getName(), null);
	}

	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public Object addingService(final ServiceReference reference) {
		final PlatformConfigurationConstraint contraint = (PlatformConfigurationConstraint) super.addingService(reference);
		if (null != contraint) {
			// add
			checks.add(contraint);

			// update the status
			ConfigImplActivator.requestPlatformStatusUpdate();
		}
		return contraint;
	}

	public PlatformConfigurationConstraint[] getPlatformChecks() {
		return checks.toArray(new PlatformConfigurationConstraint[0]);
	}

	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void removedService(final ServiceReference reference, final Object service) {
		final PlatformConfigurationConstraint contraint = (PlatformConfigurationConstraint) service;
		if (null != contraint) {
			// remove
			checks.remove(contraint);

			// update the status
			PlatformStatusRefreshJob.scheduleRefreshIfPermitted();
		}

		// unget service
		super.removedService(reference, contraint);
	}
}
