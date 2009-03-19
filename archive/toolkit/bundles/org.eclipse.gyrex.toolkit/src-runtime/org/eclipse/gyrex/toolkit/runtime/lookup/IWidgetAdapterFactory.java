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
package org.eclipse.gyrex.toolkit.runtime.lookup;

import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;

/**
 * An adapter factory is responsible for creating widget adapters.
 * <p>
 * This interface is intended to be implemented by clients.
 * </p>
 */
public interface IWidgetAdapterFactory {

	/**
	 * Returns an adapter (object) which is an instance of the given class
	 * (adapter type) associated with the specified widget id. Returns
	 * <code>null</code> if no such object can be found.
	 * 
	 * @param widgetId
	 *            the widget id
	 * @param adapterType
	 *            the adapter type
	 * @param environment
	 *            the environment of the widget
	 * @return the widget adapter or <code>null</code> if the factor does not
	 *         have an adapter of the given type for the specified widget id
	 */
	<T> T getAdapter(String widgetId, Class<T> adapterType, IWidgetEnvironment environment);
}
