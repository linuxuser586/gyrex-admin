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

import org.eclipse.gyrex.preferences.PlatformScope;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import org.osgi.service.prefs.BackingStoreException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FanShopDevSetup {

	private static final Logger LOG = LoggerFactory.getLogger(FanShopDevSetup.class);

	public static final String DEFAULT_URL = "http://fans.eclipse.org/";
	public static final String PLUGIN_ID_FANSHOP = "org.eclipse.gyrex.examples.fanshop";
	public static final String URL = "url";

	public static void enableFanShopRole() throws BackingStoreException {
		// enable required server roles
		//  - org.eclipse.gyrex.boot.role.admin.gwt
		//  - org.eclipse.gyrex.boot.role.http.registry
		//  - org.eclipse.gyrex.boot.role.http.jetty
		//  - org.eclipse.gyrex.examples.fanshop
		try {
			final IEclipsePreferences preferences = new PlatformScope().getNode("org.eclipse.gyrex.boot");
			String roles = preferences.get("rolesToStart", "");
			if (StringUtils.isNotBlank(roles)) {
				roles += ",";
			}
			roles += "org.eclipse.gyrex.boot.role.admin.gwt,org.eclipse.gyrex.boot.role.http.registry,org.eclipse.gyrex.boot.role.http.jetty,org.eclipse.gyrex.examples.fanshop";
			preferences.put("rolesToStart", roles);
			preferences.flush();
		} catch (final BackingStoreException e) {
			LOG.error("Error while activating required server roles. " + e.getMessage(), e);
			throw e;
		}
	}

	private FanShopDevSetup() {
	}

}
