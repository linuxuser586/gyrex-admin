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
package org.eclipse.gyrex.admin.widgets;

import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetAdapterRegistry;

/**
 * The admin widget adapter service is a
 * {@link IWidgetAdapterRegistry widget adapter registry} for the admin
 * application.
 * <p>
 * The admin widget adapter service is registered as an OSGi services. Clients
 * may obtain a service reference to hook the admin application into their
 * preferred UI technology.
 * </p>
 * <p>
 * Note, this interface is not intended to be implemented by clients. It's made
 * available to clients as an OSGi service.
 * </p>
 */
public interface IAdminWidgetAdapterService extends IWidgetAdapterRegistry {
}
