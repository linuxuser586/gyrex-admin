package org.eclipse.gyrex.admin.cloud.internal;

import org.eclipse.gyrex.common.runtime.BaseBundleActivator;

import org.osgi.framework.BundleContext;

public class CloudAdminActivator extends BaseBundleActivator {

	public static String SYMBOLIC_NAME = "org.eclipse.gyrex.admin.cloud";

	/**
	 * Creates a new instance.
	 */
	public CloudAdminActivator() {
		super(SYMBOLIC_NAME);
	}

	@Override
	protected void doStart(final BundleContext context) throws Exception {
	}

	@Override
	protected void doStop(final BundleContext context) throws Exception {
	}
}
