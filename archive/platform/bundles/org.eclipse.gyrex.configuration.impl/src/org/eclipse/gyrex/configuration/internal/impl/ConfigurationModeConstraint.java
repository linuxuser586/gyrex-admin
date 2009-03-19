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
package org.eclipse.gyrex.configuration.internal.impl;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.gyrex.common.debug.BundleDebug;
import org.eclipse.gyrex.configuration.ConfigurationMode;
import org.eclipse.gyrex.configuration.PlatformConfiguration;
import org.eclipse.gyrex.configuration.constraints.PlatformConfigurationConstraint;
import org.eclipse.gyrex.configuration.internal.ConfigurationActivator;
import org.eclipse.gyrex.configuration.internal.holders.ConfigurationModeHolder;

/**
 * This constraint issues a warning when the platform is in development mode.
 * 
 * @see PlatformConfiguration#getConfigurationMode()
 */
public class ConfigurationModeConstraint extends PlatformConfigurationConstraint {

	/**
	 * preference key holding the last known config mode (value
	 * <code>lastKnownConfigMode</code>)
	 */
	private static final String PREF_KEY_LAST_KNOWN_CONFIG_MODE = "lastKnownConfigMode";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gyrex.configuration.service.ConfigurationConstraint#evaluateConfiguration(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus evaluateConfiguration(final IProgressMonitor progressMonitor) {
		// 1st: check that the configuration mode has been initialized correctly
		if (!ConfigurationModeHolder.isConfigurationModeInitialized()) {
			if (ConfigImplDebug.debugMode) {
				BundleDebug.debug("[ConfigurationModeConstraint] not initialized");
			}
			return new Status(IStatus.WARNING, ConfigurationActivator.PLUGIN_ID, "The platform configuration mode has not been initialized. The platform will operate in development mode. Please complete the inital platform configuration and restart the server.");
		}

		// get the configuration mode
		final ConfigurationMode configurationMode = PlatformConfiguration.getConfigurationMode();

		// 2nd: check that the configuration mode never changed
		try {
			final IEclipsePreferences preferences = new InstanceScope().getNode(ConfigImplActivator.PLUGIN_ID);
			final String lastKnownConfigMode = preferences.get(PREF_KEY_LAST_KNOWN_CONFIG_MODE, null);
			if (lastKnownConfigMode == null) {
				// no known value, save the current one
				preferences.put(PREF_KEY_LAST_KNOWN_CONFIG_MODE, configurationMode.name());
			} else if (!lastKnownConfigMode.equalsIgnoreCase(configurationMode.name())) {
				if (ConfigImplDebug.debugMode) {
					BundleDebug.debug("[ConfigurationModeConstraint] configuration has been changed");
				}
				return new Status(IStatus.ERROR, ConfigurationActivator.PLUGIN_ID, "The platform configuration has been changed. Please restart the server.");
			}
		} catch (final Exception e) {
			if (ConfigImplDebug.debugMode) {
				BundleDebug.debug("[ConfigurationModeConstraint] failed to verify");
			}
			return new Status(IStatus.WARNING, ConfigurationActivator.PLUGIN_ID, "Failed to verify the configuration mode. Is the preference service is available?", e);
		}

		// issue a message in development mode
		if (configurationMode == ConfigurationMode.DEVELOPMENT) {
			if (ConfigImplDebug.debugMode) {
				BundleDebug.debug("[ConfigurationModeConstraint] development");
			}
			return new Status(IStatus.INFO, ConfigurationActivator.PLUGIN_ID, "The platform operates in development mode.");
		}

		// we are ok
		return Status.OK_STATUS;
	}

}
