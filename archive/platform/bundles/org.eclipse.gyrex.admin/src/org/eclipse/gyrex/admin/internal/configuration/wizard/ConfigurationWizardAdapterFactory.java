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
package org.eclipse.gyrex.admin.internal.configuration.wizard;

import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.gyrex.toolkit.runtime.commands.ICommandHandler;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetAdapterFactory;

/**
 * The Setup Wizard adapter factory.
 */
public class ConfigurationWizardAdapterFactory implements IWidgetAdapterFactory {

	private final ICommandHandler handler = new ConfigurationWizardCommandHandler();

	@SuppressWarnings("unchecked")
	private <T> T cast(final Object object, final Class<T> type) {
		// we use a unsafe cast for performance reasons
		return (T) object;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetAdapterFactory#getAdapter(java.lang.String, java.lang.Class, org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment)
	 */
	public <T> T getAdapter(final String widgetId, final Class<T> adapterType, final IWidgetEnvironment environment) {
		if (ICommandHandler.class.equals(adapterType)) {
			return cast(handler, adapterType);
		}
		return null;
	}
}
