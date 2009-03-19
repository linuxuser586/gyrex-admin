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
package org.eclipse.gyrex.admin.web.rap.internal;


import org.eclipse.gyrex.toolkit.rap.client.WidgetFactory;
import org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTToolkit;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.SessionSingletonBase;
import org.eclipse.rwt.service.SessionStoreEvent;
import org.eclipse.rwt.service.SessionStoreListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Provides access to a shared {@link WidgetFactory}.
 * <p>
 * In RAP we maintain a {@link WidgetFactory} per session. This we we can
 * benefit from some caching in the {@link WidgetFactory}.
 * </p>
 */
public class WidgetFactoryProvider extends SessionSingletonBase {

	private static WidgetFactoryProvider getInstance() {
		return (WidgetFactoryProvider) getInstance(WidgetFactoryProvider.class);
	}

	public static WidgetFactory getWidgetFactory() {
		final WidgetFactory widgetFactory = (WidgetFactory) RWT.getSessionStore().getAttribute(WidgetFactory.class.getName());
		if (null == widgetFactory) {
			return getInstance().createWidgetFactory();
		}
		return widgetFactory;
	}

	private synchronized WidgetFactory createWidgetFactory() {
		WidgetFactory widgetFactory = (WidgetFactory) RWT.getSessionStore().getAttribute(WidgetFactory.class.getName());
		if (null != widgetFactory) {
			return widgetFactory;
		}

		final Display display = PlatformUI.getWorkbench().getDisplay();
		final FormToolkit formToolkit = new FormToolkit(display);
		RWT.getSessionStore().addSessionStoreListener(new SessionStoreListener() {

			@Override
			public void beforeDestroy(final SessionStoreEvent event) {
				formToolkit.dispose();
			}
		});
		widgetFactory = new WidgetFactory(AdminRapWebActivator.getDefault().getWidgetService(), new CWTToolkit(formToolkit));
		RWT.getSessionStore().setAttribute(WidgetFactory.class.getName(), widgetFactory);
		return widgetFactory;
	}
}
