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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This is the user service which provides information about the users in the
 * system.
 * @generated generated asynchronous callback interface to be used on the client side
 */

public interface IUserServiceAsync {

	/**
	 * Returns the currently logged in user.
	 * 
	 * @gwt.callbackReturn the logged in user
	 * @param  callback the callback that will be called to receive the return value (see <code>@gwt.callbackReturn</code> tag)
	 * @generated generated method with asynchronous callback parameter to be used on the client side
	 */
	void getCurrentUser(String authToken, AsyncCallback<User> callback);

}
