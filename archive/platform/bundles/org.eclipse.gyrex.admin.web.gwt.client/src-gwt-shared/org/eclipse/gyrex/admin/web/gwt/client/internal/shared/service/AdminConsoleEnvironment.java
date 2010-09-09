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

import java.io.Serializable;

/**
 * The environment for the admin console
 */
public class AdminConsoleEnvironment implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** is the server operating in development mode? */
	public boolean devMode;

	/** the default widget */
	public String defaultWidget = "dashboard";

	/**
	 * top menu entries (array of String[3] array, eg.
	 * <code>{{"Label", "Tooltip", "widgetId"},..}</code>)
	 */
	public String[][] topMenu;

}
