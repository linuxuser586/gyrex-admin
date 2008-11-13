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
package org.eclipse.cloudfree.admin.web.gwt.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;


import org.eclipse.cloudfree.admin.web.gwt.app.internal.services.client.services.ICloudFreeAppUIService;
import org.eclipse.cloudfree.configuration.PlatformConfiguration;

import com.google.gwt.user.client.rpc.RemoteService;

public class ConfigurationServiceImpl implements ICloudFreeAppUIService, RemoteService {

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.admin.web.gwt.app.internal.services.client.services.ICloudFreeAppUIService#getServerString()
	 */
	@Override
	public String getServerString() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException e) {
			return e.getMessage();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.admin.web.gwt.app.internal.services.client.services.ICloudFreeAppUIService#isOperatingInDevelopmentMode()
	 */
	@Override
	public boolean isOperatingInDevelopmentMode() {
		return PlatformConfiguration.isOperatingInDevelopmentMode();
	}

}
