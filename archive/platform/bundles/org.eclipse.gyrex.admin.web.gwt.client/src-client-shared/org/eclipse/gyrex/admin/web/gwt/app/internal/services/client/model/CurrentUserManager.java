/*******************************************************************************
 * Copyright (c) 2008, 2009 Gunnar Wagenknecht and others.
 * All rights reserved.
 *  
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.admin.web.gwt.app.internal.services.client.model;

import org.eclipse.gyrex.admin.web.gwt.app.internal.services.client.services.ServiceRegistry;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Manages the currently logged in {@link User}.
 */
public class CurrentUserManager {

	/**
	 * Interface for receiving the user info.
	 */
	public static interface IUserCallback {
		/**
		 * Called if an error occurred while receiving the user info.
		 * 
		 * @param throwable
		 *            the error
		 */
		void onError(Throwable throwable);

		/**
		 * Called with the user info.
		 * 
		 * @param user
		 *            the user info
		 */
		void onUser(User user);
	}

	/** the singleton instance */
	private static final CurrentUserManager instance = new CurrentUserManager();

	/**
	 * Returns the current user manager instance.
	 * 
	 * @return the current user manager instance
	 */
	public static CurrentUserManager getManager() {
		return instance;
	}

	/** the current user */
	private User currentUser;

	/**
	 * Hidden constructor.
	 */
	private CurrentUserManager() {
		// empty
	}

	/**
	 * Retrieves the current user info.
	 * <p>
	 * The info is retrieved from the server and cached locally during the
	 * session.
	 * </p>
	 * 
	 * @param callback
	 *            the {@link IUserCallback} that will receive the user info
	 */
	public void getCurrentUser(final IUserCallback callback) {
		if (null != currentUser) {
			callback.onUser(currentUser);
			return;
		}

		final String authToken = Cookies.getCookie("authToken");
		if (authToken == null) {
			callback.onUser(null);
			return;
		}

		ServiceRegistry.getUserService().getCurrentUser(authToken, new AsyncCallback<User>() {

			public void onFailure(final Throwable caught) {
				currentUser = null;
				callback.onError(caught);
			}

			public void onSuccess(final User result) {
				currentUser = result;
				callback.onUser(currentUser);
			}
		});
	}

}
