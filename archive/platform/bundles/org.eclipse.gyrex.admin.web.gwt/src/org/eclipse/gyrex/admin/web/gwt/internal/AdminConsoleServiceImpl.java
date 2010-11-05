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
package org.eclipse.gyrex.admin.web.gwt.internal;

import org.eclipse.gyrex.admin.web.gwt.client.internal.shared.service.AdminConsoleEnvironment;
import org.eclipse.gyrex.admin.web.gwt.client.internal.shared.service.IAdminConsoleService;
import org.eclipse.gyrex.server.Platform;

public class AdminConsoleServiceImpl implements IAdminConsoleService {

	@Override
	public AdminConsoleEnvironment loadEnvironment() {
		final AdminConsoleEnvironment environment = new AdminConsoleEnvironment();
		environment.devMode = Platform.inDevelopmentMode();
		environment.debugMode = Platform.inDebugMode();
		environment.defaultWidget = "dashboard";
		environment.topMenu = new String[][] {//@formatter:off
			{"Dashboard", "Open the system dashboard.", "dashboard"},
			{"Control Panel", "Configure the system using the control panel.", "control-panel"},
		}; //@formatter:on
		return environment;
	}
}
