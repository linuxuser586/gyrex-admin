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
package org.eclipse.gyrex.configuration;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.gyrex.configuration.internal.ConfigurationActivator;
import org.eclipse.gyrex.configuration.internal.holders.ConfigurationModeHolder;
import org.eclipse.gyrex.configuration.internal.holders.PlatformStatusHolder;
import org.eclipse.gyrex.configuration.service.IConfigurationService;

/**
 * A convenient class to streamline access to the Eclipse Preferences API and to
 * check if the platform including its services are configured properly.
 * <p>
 * This class provides a thin layer on top the Eclipse Preferences API. It's
 * purpose is to simplify working with the preferences and to better integrate
 * with the Gyrex contextual runtime.
 * </p>
 * <p>
 * Although it's still possible to use the Eclipse Preferences API directly,
 * care must be taken when doing it that way. Gyrex and its applications may
 * rely on an implementation specific behavior which is exposed by this class.
 * </p>
 * <p>
 * For convenience reason this class provides static methods. Additionally, the
 * backing implementation is delivered in its own set of OSGi bundles so that
 * updates to the implementation do not cause massive bundle refreshes/restarts
 * across the board. The <code>org.eclipse.gyrex.configuration</code> bundle is
 * assumed to be a <em>very</em> fundamental bundle which others should heavily
 * make use of.
 * </p>
 * 
 * @see IConfigurationService
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public final class PlatformConfiguration implements IConfigurationConstants {

	/**
	 * Returns the platform configuration mode.
	 * <p>
	 * Gyrex uses the concept of configuration modes to behave differently in a
	 * secure production environment and in a relaxed development environment.
	 * </p>
	 * <p>
	 * For example, in a production environment it is not desired to confront
	 * web site visitors with cryptic error messages and exception stack traces
	 * which may even contain security sensitive data. In contrast, at
	 * development time is sometimes much more convenient to output additional
	 * debugging information. Additionally, it's sometimes better to use
	 * different default values in production than for development.
	 * </p>
	 * <p>
	 * Note, although Gyrex is a dynamic platform, the configuration mode is
	 * static information. It is not anticipated that the platform changes its
	 * configuration mode. Once set it should be <strong>assumed for
	 * lifetime</strong>. A new installation has to be made to rebuild a system
	 * using a different configuration mode. Security and a clean environment
	 * are some of the reasons for this strict decision.
	 * </p>
	 * <p>
	 * The default configuration mode is
	 * <code>{@link ConfigurationMode#DEVELOPMENT}</code>. It can be changed by
	 * setting the system property
	 * <code>{@value IConfigurationConstants#PROPERTY_KEY_CONFIGURATION_MODE}</code>
	 * in the <code>config.ini</code> before starting the platform or on the
	 * command line when starting the platform to "<code>production</code>"
	 * (without the quotes).
	 * </p>
	 * 
	 * @return the current platform configuration mode
	 * @see ConfigurationMode
	 */
	public static ConfigurationMode getConfigurationMode() {
		return ConfigurationModeHolder.getConfigurationMode();
	}

	/**
	 * Return the interface into the Gyrex preference mechanisms. The returned
	 * object can be used for such operations as searching for preference in a
	 * convenient way.
	 * <p>
	 * Clients are also able to acquire the {@link IConfigurationService}
	 * service via OSGi mechanisms and use it for preference functions.
	 * </p>
	 * 
	 * @return an object to interface into the Gyrex preference mechanism
	 * @throws IllegalStateException
	 *             if the configuration service is not available
	 * @deprecated please use the context preferences where appropriate
	 */
	@Deprecated
	public static IConfigurationService getConfigurationService() throws IllegalStateException {
		return ConfigurationActivator.getInstance().getConfigurationService();
	}

	/**
	 * Returns the current status of Gyrex.
	 * <p>
	 * The returned status can be used to obtain detailed status information
	 * about the configuration of Gyrex. <code>{@link IStatus#isOK()}</code>
	 * returns <code>true</code> if the platform is configured properly and in a
	 * state ready for executing core operations and core services. The platform
	 * would be useless otherwise.
	 * </p>
	 * <p>
	 * To obtain the configuration status of a particular service use
	 * <code>{@link #getServiceStatus(String)}</code>.
	 * </p>
	 * <p>
	 * Callers must not hold on the returned status object because it only
	 * represents the platform status at the time of calling this method.
	 * However, given the nature of a true reliable platform it can be assumed
	 * that the status does not change very frequently but it can happen.
	 * </p>
	 * 
	 * @return the platform status
	 */
	public static IStatus getPlatformStatus() {
		return PlatformStatusHolder.getPlatformStatus();
	}

	/**
	 * Convenient method to check whether the platform is operating in
	 * {@link ConfigurationMode#DEVELOPMENT relaxed mode}.
	 * 
	 * @return <code>true</code> if <code>{@link #getConfigurationMode()}</code>
	 *         returns <code>{@link ConfigurationMode#DEVELOPMENT}</code>,
	 *         <code>false</code> otherwise
	 */
	public static boolean isOperatingInDevelopmentMode() {
		return ConfigurationMode.DEVELOPMENT == getConfigurationMode();
	}

	/**
	 * Hidden constructor for disallowing instantiation.
	 */
	private PlatformConfiguration() {
		// empty
	}
}
