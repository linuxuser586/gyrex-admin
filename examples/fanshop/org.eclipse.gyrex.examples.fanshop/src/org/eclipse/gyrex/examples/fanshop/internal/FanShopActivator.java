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

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.gyrex.common.runtime.BaseBundleActivator;
import org.eclipse.gyrex.common.services.IServiceProxy;
import org.eclipse.gyrex.context.registry.IRuntimeContextRegistry;
import org.eclipse.gyrex.examples.fanshop.internal.app.FanShopApplicationProvider;
import org.eclipse.gyrex.http.application.provider.ApplicationProvider;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.packageadmin.PackageAdmin;

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

	private IServiceProxy<IRuntimeContextRegistry> contextRegistry;

	private IServiceProxy<PackageAdmin> packageAdmin;

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

		// track the context registry
		contextRegistry = getServiceHelper().trackService(IRuntimeContextRegistry.class);

		// track package admin
		packageAdmin = getServiceHelper().trackService(PackageAdmin.class);

		// register fan shop provider
		getServiceHelper().registerService(ApplicationProvider.class.getName(), new FanShopApplicationProvider(), "Eclipse Gyrex", "Application provider for the extensible Fan Shop application.", null, null);
	}

	@Override
	protected void doStop(final BundleContext context) throws Exception {
		instance.set(null);
	}

	public Bundle getBundle(final String symbolicName) {
		final PackageAdmin packageAdmin = getPackageAdmin();
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

	public IServiceProxy<IRuntimeContextRegistry> getContextRegistry() {
		final IServiceProxy<IRuntimeContextRegistry> proxy = contextRegistry;
		if (null == proxy) {
			throw createBundleInactiveException();
		}
		return proxy;
	}

	private PackageAdmin getPackageAdmin() {
		final IServiceProxy<PackageAdmin> proxy = packageAdmin;
		if (null == proxy) {
			throw createBundleInactiveException();
		}
		return proxy.getService();
	}
}
