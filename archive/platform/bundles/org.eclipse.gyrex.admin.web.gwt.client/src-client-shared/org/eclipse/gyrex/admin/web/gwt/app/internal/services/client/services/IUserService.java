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

import org.eclipse.cloudfree.admin.web.gwt.app.internal.services.client.model.User;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * This is the user service which provides information about the users in the
 * system.
 */
public interface IUserService extends RemoteService {

	/**
	 * Returns the currently logged in user.
	 * 
	 * @return the logged in user
	 */
	public User getCurrentUser(String authToken);
}
