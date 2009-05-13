package org.eclipse.gyrex.log.internal;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.gyrex.common.runtime.BaseBundleActivator;
import org.eclipse.gyrex.common.services.IServiceProxy;
import org.osgi.framework.BundleContext;

public class LogActivator extends BaseBundleActivator {

	/** SYMBOLIC_NAME */
	public static final String SYMBOLIC_NAME = "org.eclipse.gyrex.log";

	private static final AtomicReference<LogActivator> instanceRef = new AtomicReference<LogActivator>();

	public static LogActivator getInstance() {
		final LogActivator logActivator = instanceRef.get();
		if (null == logActivator) {
			throw new IllegalStateException("The log system is inactive.");
		}
		return logActivator;
	}

	private final AtomicReference<IServiceProxy<ExtendedLogService>> extendedLogServiceRef = new AtomicReference<IServiceProxy<ExtendedLogService>>();

	/**
	 * Creates a new instance.
	 */
	public LogActivator() {
		super(SYMBOLIC_NAME);
	}

	@Override
	protected void doStart(final BundleContext context) throws Exception {
		instanceRef.set(this);
		extendedLogServiceRef.set(getServiceHelper().trackService(ExtendedLogService.class));
	}

	@Override
	protected void doStop(final BundleContext context) throws Exception {
		instanceRef.set(null);
		extendedLogServiceRef.set(null);
	}

	public ExtendedLogService getLogService() {
		final IServiceProxy<ExtendedLogService> serviceProxy = extendedLogServiceRef.get();
		if (null != serviceProxy) {
			return serviceProxy.getService();
		}
		throw new IllegalStateException("The log system is inactive.");
	}
}
