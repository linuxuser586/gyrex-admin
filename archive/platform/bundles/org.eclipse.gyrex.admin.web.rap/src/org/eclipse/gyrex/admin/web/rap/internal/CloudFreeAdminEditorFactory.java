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
package org.eclipse.cloudfree.admin.web.rap.internal;


import org.eclipse.cloudfree.toolkit.rap.client.WidgetFactory;
import org.eclipse.cloudfree.toolkit.rap.client.editor.WidgetFormEditor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExecutableExtensionFactory;

/**
 * Create widget editors for CloudFree admin
 */
public class CloudFreeAdminEditorFactory implements IExecutableExtensionFactory {

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
