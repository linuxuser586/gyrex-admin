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
package org.eclipse.gyrex.admin.web.gwt.app.internal.services.client.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * This is the service registry which provides convenient access to all
 * services.
 */
public class ServiceRegistry {

	/** userService */
	private static IUserServiceAsync userService;

	private static IGyrexAppUIServiceAsync configurationService;

	/**
	 * Returns the configuration service.
	 * 
	 * @return the configuration service
	 */
	public static IGyrexAppUIServiceAsync getConfigurationService() {
		if (null != configurationService) {
			return configurationService;
		}

		configurationService = (IGyrexAppUIServiceAsync) GWT.create(IGyrexAppUIService.class);
		((ServiceDefTarget) configurationService).setServiceEntryPoint(IServiceConstants.ENTRYPOINT_CONFIGURATION_SERVICE);
		return configurationService;

	}

	/**
	 * Returns the user service.
	 * 
	 * @return the user service
	 */
	public static IUserServiceAsync getUserService() {
		if (null != userService) {
			return userService;
		}

		userService = (IUserServiceAsync) GWT.create(IUserService.class);
		((ServiceDefTarget) userService).setServiceEntryPoint(IServiceConstants.ENTRYPOINT_USER_SERVICE);
		return userService;
	}

	/**
	 * Hidden constructor
	 */
	private ServiceRegistry() {
		// no instance necessary
	}
}
