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
package org.eclipse.cloudfree.admin.web.gwt.app.internal.services.client.services;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * A service for querying server configuration information.
 */
public interface ICloudFreeAppUIService extends RemoteService {

	/**
	 * Returns a string for identifying the current connected server in the UI.
	 * 
	 * @return a string for identifying the current connected server in the UI
	 */
	String getServerString();

	/**
	 * Convenient method to check whether the platform is operating in
	 * development mode.
	 * 
	 * @return <code>true</code> if the platform is operating in development
	 *         mode, <code>false</code> otherwise
	 */
	boolean isOperatingInDevelopmentMode();

}
