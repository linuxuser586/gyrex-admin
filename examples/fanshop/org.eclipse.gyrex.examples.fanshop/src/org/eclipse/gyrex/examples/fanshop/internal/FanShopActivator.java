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
package org.eclipse.gyrex.examples.fanshop.internal;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gyrex.common.runtime.BaseBundleActivator;
import org.eclipse.gyrex.configuration.PlatformConfiguration;
import org.eclipse.gyrex.examples.fanshop.internal.app.FanShopApplication;
import org.eclipse.gyrex.examples.fanshop.internal.app.FanShopApplicationProvider;
import org.eclipse.gyrex.examples.fanshop.service.IFanShopService;
import org.eclipse.gyrex.http.application.provider.ApplicationProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.util.tracker.ServiceTracker;

public class FanShopActivator extends BaseBundleActivator {

	public static final String PLUGIN_ID = "org.eclipse.gyrex.examples.fanshop";

	private static final AtomicReference<FanShopActivator> instance = new AtomicReference<FanShopActivator>();

	public static FanShopActivator getInstance() {
		final FanShopActivator fanShopActivator = instance.get();
		if (null == fanShopActivator) {
			throw new IllegalStateException("inactive");
		}
		return fanShopActivator;
	}

	private final ConcurrentMap<FanShopApplication, ServiceRegistration> fanshopServiceRegistrations = new ConcurrentHashMap<FanShopApplication, ServiceRegistration>();

	private volatile FanShopRTSetup fanShopRTSetup;

	private ServiceTracker bundleTracker;

	/**
	 * Creates a new instance.
	 */
	public FanShopActivator() {
		super(PLUGIN_ID);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.common.runtime.BaseBundleActivator#doStart(org.osgi.framework.BundleContext)
	 */
	@Override
	protected void doStart(final BundleContext context) throws Exception {
		instance.set(this);

		// register fan shop provider
		getServiceHelper().registerService(ApplicationProvider.class.getName(), new FanShopApplicationProvider(), "Gyrex.net", "Application provider for the extensible Fan Shop application.", null, null);

		// create default environment in dev mode
		if (PlatformConfiguration.isOperatingInDevelopmentMode()) {
			new Job("Initializing Fan Shop") {

				@Override
				protected IStatus run(final IProgressMonitor monitor) {
					try {
						fanShopRTSetup = new FanShopRTSetup(FanShopActivator.getInstance().getBundle().getBundleContext());
						fanShopRTSetup.runtimeSetup();
					} catch (final IllegalStateException e) {
						// already shutdown
						return Status.CANCEL_STATUS;
					} catch (final Exception e) {
						// TODO consider logging this
						System.err.println("Error during fan shop initialization!");
						e.printStackTrace();
						return Status.CANCEL_STATUS;
					}
					return Status.OK_STATUS;
				}

			}.schedule();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.common.runtime.BaseBundleActivator#doStop(org.osgi.framework.BundleContext)
	 */
	@Override
	protected void doStop(final BundleContext context) throws Exception {
		instance.set(null);
		fanShopRTSetup.close();
	}

	public Bundle getBundle(final String symbolicName) {
		final PackageAdmin packageAdmin = getBundleAdmin();
		if (packageAdmin == null) {
			return null;
		}
		final Bundle[] bundles = packageAdmin.getBundles(symbolicName, null);
		if (bundles == null) {
			return null;
		}
		// return the first bundle that is not installed or uninstalled
		for (int i = 0; i < bundles.length; i++) {
			if ((bundles[i].getState() & (Bundle.INSTALLED | Bundle.UNINSTALLED)) == 0) {
				return bundles[i];
			}
		}
		return null;
	}

	private PackageAdmin getBundleAdmin() {
		if (bundleTracker == null) {
			final Bundle bundle = getBundle();
			if (bundle == null) {
				return null;
			}
			bundleTracker = new ServiceTracker(bundle.getBundleContext(), PackageAdmin.class.getName(), null);
			bundleTracker.open();
		}
		return (PackageAdmin) bundleTracker.getService();
	}

	public void startFanShopService(final FanShopApplication application) {
		if (fanshopServiceRegistrations.containsKey(application)) {
			return;
		}
		synchronized (fanshopServiceRegistrations) {
			if (fanshopServiceRegistrations.containsKey(application)) {
				return;
			}
			final ServiceRegistration serviceRegistration = getServiceHelper().registerService(IFanShopService.class.getName(), application, "Gyrex.net", "Fan Shop Service for extending Fan Shop applications", null, null);
			fanshopServiceRegistrations.put(application, serviceRegistration);
		}
	}

	public void stopFanShopService(final FanShopApplication application) {
		final ServiceRegistration serviceRegistration = fanshopServiceRegistrations.remove(application);
		if (null != serviceRegistration) {
			serviceRegistration.unregister();
		}
	}
}
