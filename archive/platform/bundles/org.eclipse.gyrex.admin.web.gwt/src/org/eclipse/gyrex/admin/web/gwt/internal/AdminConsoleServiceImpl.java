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
import org.eclipse.gyrex.configuration.PlatformConfiguration;

public class AdminConsoleServiceImpl implements IAdminConsoleService {

	@Override
	public AdminConsoleEnvironment loadEnvironment() {
		final AdminConsoleEnvironment environment = new AdminConsoleEnvironment();
		environment.devMode = PlatformConfiguration.isOperatingInDevelopmentMode();
		return environment;
	}
}
