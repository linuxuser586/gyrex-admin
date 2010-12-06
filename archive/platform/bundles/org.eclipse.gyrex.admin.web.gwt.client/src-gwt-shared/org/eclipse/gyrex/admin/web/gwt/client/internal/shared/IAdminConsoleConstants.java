/*******************************************************************************
 * Copyright (c) 2008, 2010 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.admin.web.gwt.client.internal.shared;

/**
 * Constances shared by the client GWT module and the server side.
 */
public interface IAdminConsoleConstants {

	/** the module id */
	String MODULE_ID = "org.eclipse.gyrex.admin.web.gwt.client.internal.AdminClient";

	/** the default entry point prefix */
	String DEFAULT_ENTRYPOINT_PREFIX = "/org.eclipse.gyrex.admin.web.gwt.client.internal";

	/** the widget service entry point */
	String ENTRYPOINT_WIDGET_SERVICE = DEFAULT_ENTRYPOINT_PREFIX + "/widget-service";

	/** the widget resources base url */
	String WIDGET_RESOURCE_BASE_URL = DEFAULT_ENTRYPOINT_PREFIX + "/widget-resources";

	/** the console service entry point */
	String ENTRYPOINT_CONSOLE_SERVICE = DEFAULT_ENTRYPOINT_PREFIX + "/console-service";
}