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
package org.eclipse.cloudfree.admin.internal.widgets;


import org.eclipse.cloudfree.admin.internal.AdminActivator;
import org.eclipse.cloudfree.admin.widgets.IAdminWidgetAdapterService;
import org.eclipse.cloudfree.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.cloudfree.toolkit.runtime.lookup.IWidgetAdapterFactory;

/**
 * {@link IWidgetAdapterFactory} which is able to handle the inactive case.
 */
public class DynamicAwareWidgetAdapterFactory implements IWidgetAdapterFactory {
	public <T> T getAdapter(final String widgetId, final Class<T> adapterType, final IWidgetEnvironment environment) {
		try {
			final IAdminWidgetAdapterService adminWidgetAdapterService = AdminActivator.getInstance().getAdminWidgetAdapterService();
			if (null == adminWidgetAdapterService) {
				return null;
			}
			return adminWidgetAdapterService.getAdapter(widgetId, adapterType, environment);
		} catch (final IllegalStateException e) {
			// inactive
			return null;
		}
	}
}