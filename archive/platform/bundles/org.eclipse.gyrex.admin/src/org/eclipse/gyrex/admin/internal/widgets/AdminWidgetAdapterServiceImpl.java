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
package org.eclipse.gyrex.admin.internal.widgets;

import org.eclipse.gyrex.admin.widgets.IAdminWidgetAdapterService;
import org.eclipse.gyrex.admin.widgets.IAdminWidgetService;
import org.eclipse.gyrex.toolkit.runtime.lookup.BaseWidgetAdapterRegistry;

/**
 * The {@link IAdminWidgetService} implementation.
 */
public class AdminWidgetAdapterServiceImpl extends BaseWidgetAdapterRegistry implements IAdminWidgetAdapterService {

	private Object registryHelper;

	/**
	 * Sets the extension registry
	 * 
	 * @param registry
	 */
	public void setRegistry(final Object registry) {
		if (registryHelper != null) {
			((AdminWidgetAdapterServiceRegistryHelper) registryHelper).stop();
		}
		registryHelper = new AdminWidgetAdapterServiceRegistryHelper(this, registry);
	}

	public void shutdown() {
		setRegistry(null);
		clear();
	}

}