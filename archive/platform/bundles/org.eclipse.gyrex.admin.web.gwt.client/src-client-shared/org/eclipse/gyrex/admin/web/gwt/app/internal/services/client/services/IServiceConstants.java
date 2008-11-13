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

/**
 * Interface with shared service constants
 */
public interface IServiceConstants {

	/** the common entry point prefix */
	String COMMON_ENTRYPOINT_PREFIX = "/org.eclipse.cloudfree.admin.web.gwt.app.internal.services.client.services";

	/** the configuration service entry point */
	String ENTRYPOINT_CONFIGURATION_SERVICE = COMMON_ENTRYPOINT_PREFIX + "/configuration";

	/** the user service entry point */
	String ENTRYPOINT_USER_SERVICE = COMMON_ENTRYPOINT_PREFIX + "/user";
}
