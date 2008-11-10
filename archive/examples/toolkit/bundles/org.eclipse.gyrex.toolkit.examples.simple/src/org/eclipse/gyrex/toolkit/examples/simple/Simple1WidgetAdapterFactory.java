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
package org.eclipse.cloudfree.toolkit.examples.simple;

import org.eclipse.cloudfree.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.cloudfree.toolkit.runtime.commands.ICommandHandler;
import org.eclipse.cloudfree.toolkit.runtime.lookup.IWidgetAdapterFactory;

/**
 * 
 */
public class Simple1WidgetAdapterFactory implements IWidgetAdapterFactory {

	private ICommandHandler createSimple1CommandHandler() {
		return Simple1CommandHandler.INSTANCE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.runtime.lookup.IWidgetAdapterFactory#getAdapter(java.lang.String, java.lang.Class, org.eclipse.cloudfree.toolkit.runtime.IWidgetEnvironment)
	 */
	public <T> T getAdapter(final String widgetId, final Class<T> adapterType, final IWidgetEnvironment environment) {
		if (adapterType.equals(ICommandHandler.class)) {
			return adapterType.cast(createSimple1CommandHandler());
		}
		return null;
	}

}
