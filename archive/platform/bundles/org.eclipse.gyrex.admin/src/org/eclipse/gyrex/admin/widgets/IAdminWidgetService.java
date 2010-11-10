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
package org.eclipse.gyrex.admin.widgets;

import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetRegistry;

/**
 * The admin widget service is a {@link IWidgetRegistry widget registry} for the
 * Gyrex admin console.
 * <p>
 * The admin widget service is registered as an OSGi services. Clients may
 * obtain a service reference to integrate admin widgets into their own Gyrex
 * Toolkit based UI technology or to simply register/unregister new widgets
 * dynamically.
 * </p>
 * <p>
 * Note, this interface is not intended to be implemented by clients. It's made
 * available to clients as an OSGi service.
 * </p>
 * 
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IAdminWidgetService extends IWidgetRegistry {
	/** OSGi service name */
	String SERVICE_NAME = IAdminWidgetService.class.getName();
}
