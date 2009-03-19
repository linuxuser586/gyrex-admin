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
package org.eclipse.gyrex.configuration.internal.holders;

import java.util.concurrent.atomic.AtomicReference;


import org.eclipse.gyrex.common.debug.BundleDebug;
import org.eclipse.gyrex.configuration.ConfigurationMode;
import org.eclipse.gyrex.configuration.internal.ConfigDebug;

public class ConfigurationModeHolder {

	/**
	 * the configuration mode
	 */
	private static final AtomicReference<ConfigurationMode> configurationMode = new AtomicReference<ConfigurationMode>();

	/**
	 * Returns the configuration mode.
	 * 
	 * @return the configuration mode (defaults to
	 *         {@link ConfigurationMode#DEVELOPMENT})
	 */
	public static ConfigurationMode getConfigurationMode() {
		final ConfigurationMode mode = configurationMode.get();
		if (null != mode) {
			return mode;
		}
		return ConfigurationMode.DEVELOPMENT;
	}

	/**
	 * Indicates if the configuration mode is initialized.
	 * 
	 * @return <code>true</code> if the configuration mode is initialized,
	 *         <code>false</code> otherwise
	 */
	public static boolean isConfigurationModeInitialized() {
		return null != configurationMode.get();
	}

	/**
	 * Sets the configuration mode only if it hasnt been set before.
	 * 
	 * @param mode
	 *            the mode to set
	 */
	public static void setConfigurationMode(final ConfigurationMode mode) {
		if (null == mode) {
			throw new IllegalArgumentException("mode must not be null");
		}
		// only set when never set before
		if (configurationMode.compareAndSet(null, mode)) {
			if (ConfigDebug.debugMode) {
				BundleDebug.debug("initialized configuration mode: " + mode);
			}
		}
	}

}
