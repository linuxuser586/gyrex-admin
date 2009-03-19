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
package org.eclipse.gyrex.examples.fanshop.internal.setup;

import org.eclipse.gyrex.configuration.PlatformConfiguration;
import org.eclipse.gyrex.configuration.preferences.PlatformScope;
import org.eclipse.gyrex.configuration.service.IConfigurationService;
import org.osgi.service.prefs.BackingStoreException;

public class FanShopDevSetup {

	public static final String DEFAULT_URL = "http://fans.eclipse.org/";
	public static final String PLUGIN_ID_FANSHOP = "org.eclipse.gyrex.examples.fanshop";
	public static final String URL = "url";

	public static void enableFanShopRole() throws BackingStoreException {
		//"org.eclipse.gyrex.examples.fanshop"
		String roles = PlatformConfiguration.getConfigurationService().getString("org.eclipse.gyrex.boot", "rolesToStart", null, null);
		if ((null == roles) || (roles.length() == 0)) {
			roles = PLUGIN_ID_FANSHOP;
		} else {
			roles += ",org.eclipse.gyrex.examples.fanshop";
		}
		PlatformConfiguration.getConfigurationService().putString("org.eclipse.gyrex.boot", "rolesToStart", roles, null, false);
		new PlatformScope().getNode("org.eclipse.gyrex.boot").flush();
	}

	public static void setFanShopUrl(final String url) throws BackingStoreException {
		final IConfigurationService configurationService = PlatformConfiguration.getConfigurationService();
		configurationService.putString(PLUGIN_ID_FANSHOP, URL, url, null, false);
		new PlatformScope().getNode(PLUGIN_ID_FANSHOP).flush();
	}

	private FanShopDevSetup() {
	}

}
