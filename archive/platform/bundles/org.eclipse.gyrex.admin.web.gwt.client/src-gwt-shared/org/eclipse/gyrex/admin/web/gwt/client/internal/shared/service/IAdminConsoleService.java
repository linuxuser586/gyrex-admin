/**
 * Copyright (c) 2010 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package org.eclipse.gyrex.admin.web.gwt.client.internal.shared.service;

import com.google.gwt.rpc.client.RpcService;

/**
 * IAdminConsoleService.
 */
public interface IAdminConsoleService extends RpcService {

	AdminConsoleEnvironment loadEnvironment();
}
