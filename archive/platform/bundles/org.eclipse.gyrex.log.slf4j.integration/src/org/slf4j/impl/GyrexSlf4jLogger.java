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
package org.slf4j.impl;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.gyrex.log.internal.LogEvent;
import org.eclipse.gyrex.log.internal.LogEventLevel;
import org.eclipse.gyrex.log.internal.LogEventSourceData;
import org.eclipse.gyrex.log.internal.LogSystem;
import org.osgi.framework.Bundle;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

/**
 * The SLF4J logger implementation which delegates to the Gyrex log system.
 */
class GyrexSlf4jLogger implements LocationAwareLogger, Serializable {

	/** the FQCN of the logger class */
	private static final String FQCN = GyrexSlf4jLogger.class.getName();

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private static Set<String> collectTags(final Marker marker, final Set<String> tags) {
		if (null != marker) {
			final String tag = marker.getName();
			if (!tags.contains(tag)) {
				tags.add(tag);
				for (final Iterator stream = marker.iterator(); stream.hasNext();) {
					collectTags((Marker) stream.next(), tags);
				}
			}
		}
		return tags;
	}

	private static StackTraceElement findCallerStackInCurrentThreadStackTrace(final String stopAfterFqcn) {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stackTrace.length; i++) {
			final StackTraceElement stackTraceElement = stackTrace[i];
			final String className = stackTraceElement.getClassName();
			if (className.equals(stopAfterFqcn)) {
				// look ahead
				if ((i + 1 < stackTrace.length) && !stopAfterFqcn.equals(stackTrace[i + 1].getClassName())) {
					return stackTrace[i + 1];
				}
			}
		}

		// unable to find
		return null;
	}

	private static LogEventLevel toLogLevel(final int level) {
		switch (level) {
			case TRACE_INT:
			case DEBUG_INT:
				return LogEventLevel.DEBUG;

			case INFO_INT:
				return LogEventLevel.INFO;

			case WARN_INT:
				return LogEventLevel.INFO;

			case ERROR_INT:
				return LogEventLevel.ERROR;

			default:
				return LogEventLevel.DEBUG;
		}
	}

	/** the name */
	private final String name;

	/**
	 * Creates a new instance.
	 * 
	 * @param name
	 */
	public GyrexSlf4jLogger(final String name) {
		this.name = name;
	}

	private LogEvent createLogEvent(final StackTraceElement caller, final Marker marker, final int level, final Throwable t, final String messagePattern, final Object[] args) {

		// get the attributes
		final Map<String, String> attributes = readAttributesFromMdc();

		// get the tags
		final Set<String> tags = collectTags(marker, new HashSet<String>(3));

		// the source info
		final LogEventSourceData sourceData = new LogEventSourceData(caller.getClassName(), caller.getMethodName(), caller.getFileName(), caller.getLineNumber());

		// get the log type
		final LogEventLevel type = toLogLevel(level);

		// format the message
		final String message = (null != args) && (args.length > 0) ? MessageFormatter.arrayFormat(messagePattern, args) : messagePattern;
		return new LogEvent(type, message, tags, attributes, sourceData, t);
	}

	@Override
	public void debug(final Marker marker, final String msg) {
		doLog(marker, FQCN, DEBUG_INT, null, msg, (Object[]) null);
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg) {
		doLog(marker, FQCN, DEBUG_INT, null, format, arg);
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
		doLog(marker, FQCN, DEBUG_INT, null, format, arg1, arg2);
	}

	@Override
	public void debug(final Marker marker, final String format, final Object[] argArray) {
		doLog(marker, FQCN, DEBUG_INT, null, format, argArray);
	}

	@Override
	public void debug(final Marker marker, final String msg, final Throwable t) {
		doLog(marker, FQCN, DEBUG_INT, t, msg, (Object[]) null);
	}

	@Override
	public void debug(final String msg) {
		doLog(null, FQCN, DEBUG_INT, null, msg, (Object[]) null);
	}

	@Override
	public void debug(final String format, final Object arg) {
		doLog(null, FQCN, DEBUG_INT, null, format, arg);
	}

	@Override
	public void debug(final String format, final Object arg1, final Object arg2) {
		doLog(null, FQCN, DEBUG_INT, null, format, arg1, arg2);
	}

	@Override
	public void debug(final String format, final Object[] argArray) {
		doLog(null, FQCN, DEBUG_INT, null, format, argArray);

	}

	@Override
	public void debug(final String msg, final Throwable t) {
		doLog(null, FQCN, DEBUG_INT, t, msg, (Object[]) null);
	}

	/**
	 * Log method implementation which is called by all the other log methods.
	 * 
	 * @param marker
	 * @param fqcn
	 * @param level
	 * @param t
	 * @param message
	 * @param args
	 */
	protected void doLog(final Marker marker, final String fqcn, final int level, final Throwable t, final String message, final Object... args) {
		// check if log level is enabled
		if (!shouldLog(level, marker, t)) {
			return;
		}

		// locate the caller
		final StackTraceElement caller = findCallerStackInCurrentThreadStackTrace(null != fqcn ? fqcn : FQCN);

		// get the bundle
		final Bundle bundle = BundleFinder.findCallingBundle(null != fqcn ? fqcn : FQCN);

		// sent event to the log system
		LogSystem.log(bundle, name, createLogEvent(caller, marker, level, t, message, args));
	}

	@Override
	public void error(final Marker marker, final String msg) {
		doLog(marker, FQCN, ERROR_INT, null, msg, (Object[]) null);
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg) {
		doLog(marker, FQCN, ERROR_INT, null, format, arg);
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
		doLog(marker, FQCN, ERROR_INT, null, format, arg1, arg2);
	}

	@Override
	public void error(final Marker marker, final String format, final Object[] argArray) {
		doLog(marker, FQCN, ERROR_INT, null, format, argArray);
	}

	@Override
	public void error(final Marker marker, final String msg, final Throwable t) {
		doLog(marker, FQCN, ERROR_INT, t, msg, (Object[]) null);
	}

	@Override
	public void error(final String msg) {
		doLog(null, FQCN, ERROR_INT, null, msg, (Object[]) null);
	}

	@Override
	public void error(final String format, final Object arg) {
		doLog(null, FQCN, ERROR_INT, null, format, arg);
	}

	@Override
	public void error(final String format, final Object arg1, final Object arg2) {
		doLog(null, FQCN, ERROR_INT, null, format, arg1, arg2);
	}

	@Override
	public void error(final String format, final Object[] argArray) {
		doLog(null, FQCN, ERROR_INT, null, format, argArray);

	}

	@Override
	public void error(final String msg, final Throwable t) {
		doLog(null, FQCN, ERROR_INT, t, msg, (Object[]) null);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void info(final Marker marker, final String msg) {
		doLog(marker, FQCN, INFO_INT, null, msg, (Object[]) null);
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg) {
		doLog(marker, FQCN, INFO_INT, null, format, arg);
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
		doLog(marker, FQCN, INFO_INT, null, format, arg1, arg2);
	}

	@Override
	public void info(final Marker marker, final String format, final Object[] argArray) {
		doLog(marker, FQCN, INFO_INT, null, format, argArray);
	}

	@Override
	public void info(final Marker marker, final String msg, final Throwable t) {
		doLog(marker, FQCN, INFO_INT, t, msg, (Object[]) null);
	}

	@Override
	public void info(final String msg) {
		doLog(null, FQCN, INFO_INT, null, msg, (Object[]) null);
	}

	@Override
	public void info(final String format, final Object arg) {
		doLog(null, FQCN, INFO_INT, null, format, arg);
	}

	@Override
	public void info(final String format, final Object arg1, final Object arg2) {
		doLog(null, FQCN, INFO_INT, null, format, arg1, arg2);
	}

	@Override
	public void info(final String format, final Object[] argArray) {
		doLog(null, FQCN, INFO_INT, null, format, argArray);

	}

	@Override
	public void info(final String msg, final Throwable t) {
		doLog(null, FQCN, INFO_INT, t, msg, (Object[]) null);
	}

	@Override
	public boolean isDebugEnabled() {
		return shouldLog(DEBUG_INT, null, null);
	}

	@Override
	public boolean isDebugEnabled(final Marker marker) {
		return shouldLog(DEBUG_INT, marker, null);
	}

	@Override
	public boolean isErrorEnabled() {
		return shouldLog(ERROR_INT, null, null);
	}

	@Override
	public boolean isErrorEnabled(final Marker marker) {
		return shouldLog(ERROR_INT, marker, null);
	}

	@Override
	public boolean isInfoEnabled() {
		return shouldLog(INFO_INT, null, null);
	}

	@Override
	public boolean isInfoEnabled(final Marker marker) {
		return shouldLog(INFO_INT, marker, null);
	}

	@Override
	public boolean isTraceEnabled() {
		return shouldLog(TRACE_INT, null, null);
	}

	@Override
	public boolean isTraceEnabled(final Marker marker) {
		return shouldLog(TRACE_INT, marker, null);
	}

	@Override
	public boolean isWarnEnabled() {
		return shouldLog(WARN_INT, null, null);
	}

	@Override
	public boolean isWarnEnabled(final Marker marker) {
		return shouldLog(WARN_INT, marker, null);
	}

	@Override
	public void log(final Marker marker, final String fqcn, final int level, final String message, final Throwable t) {
		doLog(marker, fqcn, level, t, message, (Object[]) null);
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> readAttributesFromMdc() {
		return MDC.getCopyOfContextMap();
	}

	/**
	 * Obtains ans returns the unique instance through {@link LoggerFactory}.
	 * 
	 * @return the unique logger instance
	 * @throws ObjectStreamException
	 *             in case of de-serialization issues
	 */
	protected Object readResolve() throws ObjectStreamException {
		return LoggerFactory.getLogger(getName());
	}

	/**
	 * Indicates if the specified arguments should trigger logging of a log
	 * message.
	 * <p>
	 * Note, this method returns <code>true</code> if *any* of the specified
	 * parameters triggers logging.
	 * </p>
	 * 
	 * @param level
	 *            any log level mentioned in {@link LocationAwareLogger}
	 * @param marker
	 *            any marker (maybe <code>null</code>)
	 * @param t
	 *            any throwable (maybe <code>null</code>)
	 * @return <code>true</code> if the log system should proceed logging the
	 *         message
	 */
	protected boolean shouldLog(final int level, final Marker marker, final Throwable t) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void trace(final Marker marker, final String msg) {
		doLog(marker, FQCN, TRACE_INT, null, msg, (Object[]) null);
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg) {
		doLog(marker, FQCN, TRACE_INT, null, format, arg);
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
		doLog(marker, FQCN, TRACE_INT, null, format, arg1, arg2);
	}

	@Override
	public void trace(final Marker marker, final String format, final Object[] argArray) {
		doLog(marker, FQCN, TRACE_INT, null, format, argArray);
	}

	@Override
	public void trace(final Marker marker, final String msg, final Throwable t) {
		doLog(marker, FQCN, TRACE_INT, t, msg, (Object[]) null);
	}

	@Override
	public void trace(final String msg) {
		doLog(null, FQCN, TRACE_INT, null, msg, (Object[]) null);
	}

	@Override
	public void trace(final String format, final Object arg) {
		doLog(null, FQCN, TRACE_INT, null, format, arg);
	}

	@Override
	public void trace(final String format, final Object arg1, final Object arg2) {
		doLog(null, FQCN, TRACE_INT, null, format, arg1, arg2);
	}

	@Override
	public void trace(final String format, final Object[] argArray) {
		doLog(null, FQCN, TRACE_INT, null, format, argArray);

	}

	@Override
	public void trace(final String msg, final Throwable t) {
		doLog(null, FQCN, TRACE_INT, t, msg, (Object[]) null);
	}

	@Override
	public void warn(final Marker marker, final String msg) {
		doLog(marker, FQCN, WARN_INT, null, msg, (Object[]) null);
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg) {
		doLog(marker, FQCN, WARN_INT, null, format, arg);
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
		doLog(marker, FQCN, WARN_INT, null, format, arg1, arg2);
	}

	@Override
	public void warn(final Marker marker, final String format, final Object[] argArray) {
		doLog(marker, FQCN, WARN_INT, null, format, argArray);
	}

	@Override
	public void warn(final Marker marker, final String msg, final Throwable t) {
		doLog(marker, FQCN, WARN_INT, t, msg, (Object[]) null);
	}

	@Override
	public void warn(final String msg) {
		doLog(null, FQCN, WARN_INT, null, msg, (Object[]) null);
	}

	@Override
	public void warn(final String format, final Object arg) {
		doLog(null, FQCN, WARN_INT, null, format, arg);
	}

	@Override
	public void warn(final String format, final Object arg1, final Object arg2) {
		doLog(null, FQCN, WARN_INT, null, format, arg1, arg2);
	}

	@Override
	public void warn(final String format, final Object[] argArray) {
		doLog(null, FQCN, WARN_INT, null, format, argArray);

	}

	@Override
	public void warn(final String msg, final Throwable t) {
		doLog(null, FQCN, WARN_INT, t, msg, (Object[]) null);
	}
}
