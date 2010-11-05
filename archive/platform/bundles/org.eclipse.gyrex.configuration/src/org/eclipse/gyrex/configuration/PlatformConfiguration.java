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

import org.eclipse.gyrex.configuration.internal.holders.PlatformStatusHolder;
import org.eclipse.gyrex.configuration.service.IConfigurationService;

import org.eclipse.core.runtime.IStatus;

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
	 * Hidden constructor for disallowing instantiation.
	 */
	private PlatformConfiguration() {
		// empty
	}
}
