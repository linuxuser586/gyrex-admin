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
package org.eclipse.gyrex.toolkit.gwt.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The <code>IWidgetEnvironment</code> equivalent that will be passed to the
 * server with every request.
 * <p>
 * The client environment specifies details about application client. This
 * allows to embed concepts like localization and authentication into the UI
 * toolkit and to automatically make the server aware of it.
 * </p>
 */
public class WidgetClientEnvironment implements IsSerializable {

	/** locale id */
	private String localeId;

	/** authentication token */
	private String authenticationToken;

	/**
	 * Returns the authentication token.
	 * 
	 * @return the authentication token
	 * @see #setAuthenticationToken(String)
	 */
	public String getAuthenticationToken() {
		return authenticationToken;
	}

	/**
	 * Returns the locale id.
	 * 
	 * @return the locale id
	 * @see #setLocaleId(String)
	 */
	public String getLocaleId() {
		return localeId;
	}

	/**
	 * Sets the authentication token.
	 * <p>
	 * The authentication token will be used by widget service on the server to
	 * authenticate the request. The token is entirely application specific as
	 * is the authentication process. It can by anything. The widget service is
	 * responsible for the authentication process. Thus, it needs to understand
	 * the token. By default no authentication will be done.
	 * </p>
	 * 
	 * @param authenticationToken
	 *            the authentication token to set
	 */
	public void setAuthenticationToken(final String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	/**
	 * Sets the locale id.
	 * <p>
	 * The locale id will be passed to the For details about the locale id
	 * please refer to <code>java.util.Locale</code>.
	 * </p>
	 * 
	 * @param localeId
	 *            the locale id to set
	 */
	public void setLocaleId(final String localeId) {
		this.localeId = localeId;
	}
}
