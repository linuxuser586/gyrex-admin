/**
 * Copyright (c) 2009 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package org.eclipse.gyrex.log.internal;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.gyrex.log.internal.firephp.FirePHPLogger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 */
public class LogReaderServiceTracker extends ServiceTracker {

	private final AtomicInteger firePhpRefCount = new AtomicInteger();
	private FirePHPLogger firePHPLogger;
	private final ConsoleLogger consoleLogger = new ConsoleLogger();

	/**
	 * Creates a new instance.
	 * 
	 * @param context
	 */
	public LogReaderServiceTracker(final BundleContext context) {
		super(context, ExtendedLogReaderService.class.getName(), null);
	}

	@Override
	public Object addingService(final ServiceReference reference) {
		// get service
		final ExtendedLogReaderService logReaderService = (ExtendedLogReaderService) super.addingService(reference);
		if (null == logReaderService) {
			return logReaderService;
		}

		if (null == firePHPLogger) {
			firePHPLogger = new FirePHPLogger();
		}
		firePhpRefCount.incrementAndGet();
		logReaderService.addLogListener(firePHPLogger);
		logReaderService.addLogListener(consoleLogger);

		return logReaderService;
	}

	@Override
	public void removedService(final ServiceReference reference, final Object service) {
		final ExtendedLogReaderService logReaderService = (ExtendedLogReaderService) service;

		logReaderService.removeLogListener(firePHPLogger);
		if (firePhpRefCount.decrementAndGet() == 0) {
			firePHPLogger = null;
		}

		logReaderService.removeLogListener(consoleLogger);

		// unget service
		super.removedService(reference, service);
	}
}
