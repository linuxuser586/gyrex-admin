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

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.gyrex.common.runtime.BaseBundleActivator;
import org.eclipse.gyrex.configuration.constraints.PlatformConfigurationConstraint;
import org.eclipse.gyrex.configuration.internal.holders.PlatformStatusHolder;

import org.osgi.framework.BundleContext;

public class ConfigImplActivator extends BaseBundleActivator {

	/** PLUGIN_ID */
	public static final String PLUGIN_ID = "org.eclipse.gyrex.configuration.impl";

	/** the service vendor */
	private static final String DEFAULT_SERVICE_VENDOR = "Gyrex";

	/** the shared instance */
	private static final AtomicReference<ConfigImplActivator> instance = new AtomicReference<ConfigImplActivator>();

	/**
	 * Returns the instance.
	 * 
	 * @return the instance
	 */
	public static ConfigImplActivator getInstance() {
		final ConfigImplActivator activator = instance.get();
		if (null == activator) {
			throw new IllegalStateException("inactive");
		}
		return activator;
	}

	/**
	 * Starts the initialization of the platform status
	 */
	/*package*/static void requestPlatformStatusUpdate() {
		// special handling when inactive
		final ConfigImplActivator activator = instance.get();
		if (null == activator) {
			PlatformStatusHolder.setCurrentPlatformStatus(null);
			return;
		}

		// refresh
		PlatformStatusRefreshJob.scheduleRefreshIfPermitted();
	}

	/** the configuration status check service instance */
	private final AtomicReference<PlatformConfigurationConstraintTracker> configurationConstraintTracker = new AtomicReference<PlatformConfigurationConstraintTracker>();

	/**
	 * Creates a new instance.
	 */
	public ConfigImplActivator() {
		super(PLUGIN_ID);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.common.runtime.BaseBundleActivator#doStart(org.osgi.framework.BundleContext)
	 */
	@Override
	protected void doStart(final BundleContext context) throws Exception {
		instance.set(this);

		// register constraints
		getServiceHelper().registerService(PlatformConfigurationConstraint.class.getName(), new ConfigurationModeConstraint(), DEFAULT_SERVICE_VENDOR, "Configuration Mode Check", null, null);

		// start constraint tracker
		configurationConstraintTracker.compareAndSet(null, new PlatformConfigurationConstraintTracker(context));
		configurationConstraintTracker.get().open();

		// enable the refresh job
		PlatformStatusRefreshJob.activate();

		// start initial evaluation
		PlatformStatusRefreshJob.scheduleRefreshIfPermitted();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.common.runtime.BaseBundleActivator#doStop(org.osgi.framework.BundleContext)
	 */
	@Override
	protected void doStop(final BundleContext context) throws Exception {
		// diable the refresh job
		PlatformStatusRefreshJob.disable();

		// stop constraint tracker
		final PlatformConfigurationConstraintTracker configurationConstraintTracker = this.configurationConstraintTracker.getAndSet(null);
		configurationConstraintTracker.close();

		// disable activator
		instance.set(null);

		// unset status
		PlatformStatusHolder.setCurrentPlatformStatus(null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.common.runtime.BaseBundleActivator#getDebugOptions()
	 */
	@Override
	protected Class getDebugOptions() {
		return ConfigImplDebug.class;
	}

	/*package*/PlatformConfigurationConstraint[] getPlatformConfigurationConstraint() {
		final PlatformConfigurationConstraintTracker constraintTracker = configurationConstraintTracker.get();
		if (null == constraintTracker) {
			throw new IllegalStateException("active");
		}
		return constraintTracker.getPlatformChecks();
	}

}