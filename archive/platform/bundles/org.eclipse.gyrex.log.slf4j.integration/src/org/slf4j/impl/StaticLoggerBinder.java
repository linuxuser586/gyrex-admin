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

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * Hook into the SLF4J API to bind our {@link ILoggerFactory}.
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {

	/**
	 * Called by the SLF4J API to verify the API version we support.
	 * <p>
	 * Note, this must be modified when we upgrade to a newer SLF4J API version.
	 * It is intentionally not marked as <code>final</code> because of
	 * requirements by SLF4J API.
	 * </p>
	 */
	public static String REQUESTED_API_VERSION = "1.5.6";

	/** the singleton instance */
	private static StaticLoggerBinder instance;

	private static synchronized void createInstance() {
		if (null != instance) {
			return;
		}
		instance = new StaticLoggerBinder();
	}

	/**
	 * Called by the SLF4J API to get access to a singleton instance of this
	 * <code>StaticLoggerBinder</code>.
	 * 
	 * @return the singleton instance
	 */
	public static StaticLoggerBinder getSingleton() {
		if (null == instance) {
			createInstance();
		}
		return instance;
	}

	private final GyrexSlf4jLoggerFactory loggerFactory;

	/**
	 * Hidden constructor
	 */
	private StaticLoggerBinder() {
		loggerFactory = new GyrexSlf4jLoggerFactory();
	}

	@Override
	public ILoggerFactory getLoggerFactory() {
		return loggerFactory;
	}

	@Override
	public String getLoggerFactoryClassStr() {
		return GyrexSlf4jLoggerFactory.class.getName();
	}

}
