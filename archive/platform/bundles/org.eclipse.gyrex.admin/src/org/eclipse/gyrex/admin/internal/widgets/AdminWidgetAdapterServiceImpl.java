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

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.runtime.lookup.BaseRegistry#clear()
	 */
	@Override
	public void clear() {
		super.clear();
	}
}
