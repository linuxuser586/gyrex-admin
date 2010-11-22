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


import org.eclipse.gyrex.admin.internal.AdminActivator;
import org.eclipse.gyrex.admin.widgets.IAdminWidgetService;
import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory;
import org.eclipse.gyrex.toolkit.widgets.Widget;

/**
 * {@link IWidgetFactory} which is able to handle the inactive case.
 */
public class DynamicAwareWidgetFactory implements IWidgetFactory {
	public Widget getWidget(final String id, final IWidgetEnvironment environment) {
		try {
			final IAdminWidgetService adminWidgetService = AdminActivator.getInstance().getAdminWidgetService();
			if (null == adminWidgetService) {
				return null;
			}
			return adminWidgetService.getWidget(id, environment);
		} catch (final IllegalStateException e) {
			// inactive
			return null;
		}
	}
}