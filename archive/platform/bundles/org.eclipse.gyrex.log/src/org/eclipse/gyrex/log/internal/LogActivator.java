package org.eclipse.gyrex.log.internal;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.equinox.log.ExtendedLogService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class LogActivator implements BundleActivator {

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

	private final AtomicReference<ServiceTracker> extendedLogServiceTrackerRef = new AtomicReference<ServiceTracker>();
	private final AtomicReference<LogReaderServiceTracker> logReaderServiceTrackerRef = new AtomicReference<LogReaderServiceTracker>();

	public ExtendedLogService getLogService() {
		final ServiceTracker extendedLogServiceTracker = extendedLogServiceTrackerRef.get();
		if (null != extendedLogServiceTracker) {
			return (ExtendedLogService) extendedLogServiceTracker.getService();
		}
		throw new IllegalStateException("The log system is inactive.");
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		instanceRef.set(this);

		// track ExtendedLogService
		extendedLogServiceTrackerRef.compareAndSet(null, new ServiceTracker(context, ExtendedLogService.class.getName(), null));
		final ServiceTracker extendedLogServiceTracker = extendedLogServiceTrackerRef.get();
		if (null != extendedLogServiceTracker) {
			extendedLogServiceTracker.open();
		}

		// track ExtendedLogReaderService
		logReaderServiceTrackerRef.compareAndSet(null, new LogReaderServiceTracker(context));
		final LogReaderServiceTracker logReaderServiceTracker = logReaderServiceTrackerRef.get();
		if (null != logReaderServiceTracker) {
			logReaderServiceTracker.open();
		}
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		instanceRef.set(null);

		final ServiceTracker extendedLogServiceTracker = extendedLogServiceTrackerRef.getAndSet(null);
		if (null != extendedLogServiceTracker) {
			extendedLogServiceTracker.close();
		}

		final LogReaderServiceTracker logReaderServiceTracker = logReaderServiceTrackerRef.getAndSet(null);
		if (null != logReaderServiceTracker) {
			logReaderServiceTracker.close();
		}

	}
}
