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


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gyrex.configuration.IConfigurationConstants;
import org.eclipse.gyrex.configuration.internal.ConfigurationActivator;

public class PlatformStatusHolder {

	/** the initial not initialized status */
	public static final Status NOT_INITIALIZED = new Status(IStatus.ERROR, ConfigurationActivator.PLUGIN_ID, IConfigurationConstants.ERROR_NOT_INITIALIZED, "not initialized", null);

	/** the current platform status */
	private static final AtomicReference<IStatus> currentPlatformStatus = new AtomicReference<IStatus>();

	/**
	 * Returns the platform status.
	 * 
	 * @return the platform status, or {@link #NOT_INITIALIZED}
	 */
	public static IStatus getPlatformStatus() {
		final IStatus status = currentPlatformStatus.get();
		if (null != status) {
			return status;
		}

		return NOT_INITIALIZED;
	}

	/**
	 * Returns the status of the current platform.
	 * 
	 * @return the platform status
	 */
	public static void setCurrentPlatformStatus(final IStatus status) {
		currentPlatformStatus.set(status);
	}
}
