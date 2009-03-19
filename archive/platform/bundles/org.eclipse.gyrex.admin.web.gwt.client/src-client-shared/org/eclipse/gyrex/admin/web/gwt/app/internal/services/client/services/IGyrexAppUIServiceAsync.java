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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A service for querying server configuration information.
 * @generated generated asynchronous callback interface to be used on the client side
 */
public interface IGyrexAppUIServiceAsync {

	/**
	 * Returns a string for identifying the current connected server in the UI.
	 * 
	 * @gwt.callbackReturn a string for identifying the current connected server in the UI
	 * @param  callback the callback that will be called to receive the return value (see <code>@gwt.callbackReturn</code> tag)
	 * @generated generated method with asynchronous callback parameter to be used on the client side
	 */
	void getServerString(AsyncCallback<String> callback);

	/**
	 * Convenient method to check whether the platform is operating in
	 * development mode.
	 * 
	 * @gwt.callbackReturn <code>true</code> if the platform is operating in development
	 *         mode, <code>false</code> otherwise
	 * @param  callback the callback that will be called to receive the return value (see <code>@gwt.callbackReturn</code> tag)
	 * @generated generated method with asynchronous callback parameter to be used on the client side
	 */
	void isOperatingInDevelopmentMode(AsyncCallback<Boolean> callback);

}
