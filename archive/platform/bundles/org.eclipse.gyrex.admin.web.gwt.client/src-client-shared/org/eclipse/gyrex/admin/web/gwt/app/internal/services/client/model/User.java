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
package org.eclipse.cloudfree.admin.web.gwt.app.internal.services.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 */
public class User implements IsSerializable {

	private Long id;
	private String fullName;
	private String logonName;
	private String email;

	/**
	 * Returns the value of the email field.
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the value of the fullName field.
	 * 
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Returns the value of the id field.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Returns the value of the logonName field.
	 * 
	 * @return the logonName
	 */
	public String getLogonName() {
		return logonName;
	}

	/**
	 * Sets the value of the email field.
	 * 
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets the value of the fullName field.
	 * 
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Sets the value of the logonName field.
	 * 
	 * @param logonName
	 *            the logonName to set
	 */
	public void setLogonName(String logonName) {
		this.logonName = logonName;
	}
}
