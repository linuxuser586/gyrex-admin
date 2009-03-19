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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.gyrex.toolkit.rap.client.WidgetFactory;
import org.eclipse.gyrex.toolkit.rap.client.editor.WidgetFormEditor;

/**
 * Create widget editors for Gyrex admin
 */
public class GyrexAdminEditorFactory implements IExecutableExtensionFactory {

	public static final String EDITOR_ID = AdminRapWebActivator.PLUGIN_ID.concat(".widgetEditor");

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IExecutableExtensionFactory#create()
	 */
	@Override
	public Object create() throws CoreException {
		final WidgetFactory factory = WidgetFactoryProvider.getWidgetFactory();
		return new WidgetFormEditor(factory);
	}

}
