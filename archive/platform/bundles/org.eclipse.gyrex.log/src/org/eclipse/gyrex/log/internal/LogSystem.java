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

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogService;

/**
 * Gyrex log system entrance.
 */
public class LogSystem {

	private static int getLevel(final LogEventLevel level) {
		switch (level) {
			case DEBUG:
				return LogService.LOG_DEBUG;

			case INFO:
				return LogService.LOG_INFO;
			case WARNING:
				return LogService.LOG_WARNING;
			case ERROR:
				return LogService.LOG_ERROR;
			default:
				return LogService.LOG_DEBUG;
		}
	}

	/**
	 * Logs a log event on behalf of a particular bundle.
	 * 
	 * @param bundle
	 *            the bundle
	 * @param logEvent
	 *            the log event
	 */
	public static void log(final Bundle bundle, final String name, final LogEvent logEvent) {
		if ((null == bundle) || (null == logEvent)) {
			return;
		}

		try {
			LogActivator.getInstance().getLogService().getLogger(bundle, name).log(logEvent, getLevel(logEvent.getLevel()), logEvent.getMessage(), logEvent.getException());
		} catch (final IllegalStateException e) {
			// ignore
			// TODO consider logging this ;)
			return;
		}
	}

	/**
	 * Hidden
	 */
	private LogSystem() {
		// empty
	}
}
