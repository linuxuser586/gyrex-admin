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

import org.eclipse.gyrex.boot.internal.app.AppActivator;
import org.eclipse.gyrex.configuration.constraints.PlatformConfigurationConstraint;
import org.eclipse.gyrex.configuration.internal.ConfigurationActivator;
import org.eclipse.gyrex.server.internal.opsmode.OperationMode;
import org.eclipse.gyrex.server.internal.opsmode.OpsMode;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This constraint issues a warning when the platform is in development mode.
 */
public class ConfigurationModeConstraint extends PlatformConfigurationConstraint {

	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationModeConstraint.class);

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
		final OpsMode opsMode = AppActivator.getOpsMode();

		// 1st: check that the configuration mode has been initialized correctly
		if ((opsMode == null) || !opsMode.isSet()) {
			if (ConfigImplDebug.debugMode) {
				LOG.debug("[ConfigurationModeConstraint] not initialized");
			}
			return new Status(IStatus.WARNING, ConfigurationActivator.PLUGIN_ID, "The platform configuration mode has not been initialized. The platform will operate in development mode. Please complete the inital platform configuration and restart the server.");
		}

		// get the configuration mode
		final OperationMode configurationMode = opsMode != null ? opsMode.getMode() : OperationMode.DEVELOPMENT;

		// 2nd: check that the configuration mode never changed
		try {
			final IEclipsePreferences preferences = new InstanceScope().getNode(ConfigImplActivator.PLUGIN_ID);
			final String lastKnownConfigMode = preferences.get(PREF_KEY_LAST_KNOWN_CONFIG_MODE, null);
			if (lastKnownConfigMode == null) {
				// no known value, save the current one
				preferences.put(PREF_KEY_LAST_KNOWN_CONFIG_MODE, configurationMode.name());
			} else if (!lastKnownConfigMode.equalsIgnoreCase(configurationMode.name())) {
				if (ConfigImplDebug.debugMode) {
					LOG.debug("[ConfigurationModeConstraint] configuration has been changed");
				}
				return new Status(IStatus.ERROR, ConfigurationActivator.PLUGIN_ID, "The platform configuration has been changed. Please restart the server.");
			}
		} catch (final Exception e) {
			if (ConfigImplDebug.debugMode) {
				LOG.debug("[ConfigurationModeConstraint] failed to verify");
			}
			return new Status(IStatus.WARNING, ConfigurationActivator.PLUGIN_ID, "Failed to verify the configuration mode. Is the preference service is available?", e);
		}

		// issue a message in development mode
		if (configurationMode == OperationMode.DEVELOPMENT) {
			if (ConfigImplDebug.debugMode) {
				LOG.debug("[ConfigurationModeConstraint] development");
			}
			return new Status(IStatus.INFO, ConfigurationActivator.PLUGIN_ID, "The platform operates in development mode.");
		}

		// we are ok
		return Status.OK_STATUS;
	}

}
