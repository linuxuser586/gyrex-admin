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

import java.io.PrintStream;

import org.eclipse.equinox.log.ExtendedLogEntry;
import org.eclipse.equinox.log.SynchronousLogListener;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogService;

/**
 *
 */
public class ConsoleLogger implements SynchronousLogListener {

	public static String getLabel(final LogEntry entry) {
		final LogEvent logEvent = getLogEvent(entry);
		if (null != logEvent) {
			return getLabel(logEvent);
		}
		return entry.getMessage();
	}

	public static String getLabel(final LogEvent logEvent) {
		final LogEventSourceData sourceData = logEvent.getSourceData();
		return String.format("[%S] [%s] %s", logEvent.getLevel(), null != sourceData ? sourceData.getSimpleClassName() : null, logEvent.getMessage());
	}

	public static LogEvent getLogEvent(final LogEntry entry) {
		if (entry instanceof ExtendedLogEntry) {
			final Object context = ((ExtendedLogEntry) entry).getContext();
			if (context instanceof LogEvent) {
				return (LogEvent) context;
			}
		}
		return null;
	}

	static PrintStream getPrintStream(final LogEntry entry) {
		switch (entry.getLevel()) {
			case LogService.LOG_ERROR:
				return System.err;
		}
		return System.out;
	}

	static PrintStream getPrintStream(final LogEvent event) {
		switch (event.getLevel()) {
			case ERROR:
				return System.err;
		}
		return System.out;
	}

	static void print(final LogEntry entry) {
		final PrintStream printStream = getPrintStream(entry);
		printStream.println(ConsoleLogger.getLabel(entry));
		final Throwable exception = entry.getException();
		if (null != exception) {
			exception.printStackTrace(printStream);
		}
	}

	static void print(final LogEvent event) {
		final PrintStream printStream = getPrintStream(event);
		printStream.println(ConsoleLogger.getLabel(event));
		final Throwable exception = event.getException();
		if (null != exception) {
			exception.printStackTrace(printStream);
		}
	}

	@Override
	public void logged(final LogEntry entry) {
		//		final String symbolicName = entry.getBundle().getSymbolicName();
		//		// get the trace
		//		DebugTrace trace;
		//		try {
		//			final FrameworkDebugOptions frameworkDebugOptions = FrameworkDebugOptions.getDefault();
		//			final Field field = frameworkDebugOptions.getClass().getDeclaredField("debugTraceCache");
		//			if (!field.isAccessible()) {
		//				field.setAccessible(true);
		//			}
		//			trace = (DebugTrace) ((Map) field.get(frameworkDebugOptions)).get(symbolicName);
		//		} catch (final Exception e) {
		//			e.printStackTrace();
		//			trace = null;
		//		}
		//
		//		if (null != trace) {
		//			trace.trace(null, entry.getMessage());
		//		}
		print(entry);
	}

}
