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

/**
 * A widget adapter registry extends a
 * {@link IWidgetAdapterFactory widget adapter factory} with the ability to
 * dynamically register and un-register widget adapters.
 * <p>
 * Note, although this interface is allowed to be implemented by clients
 * adopters really should extend one of the available base implementations
 * instead.
 * </p>
 */
public interface IWidgetAdapterRegistry extends IWidgetAdapterFactory {

	/**
	 * Registers a widget adapter factory for the specified widget ids.
	 * <p>
	 * The widget adapter factory will be called whenever one of the widgets is
	 * requested.
	 * </p>
	 * <p>
	 * If the factory was already registered the list of widget ids will be
	 * updated using the specified list by replacing it. The behavior is
	 * undetermined if one or more widget ids are already registered with a
	 * different factory. Implementors may throw an exception in this case or
	 * may handle this gracefully.
	 * </p>
	 * 
	 * @param factory
	 *            the factory to register
	 * @param widgetIds
	 *            the widget ids to register the factory with
	 * @throws RegistrationException
	 *             when the registration failed
	 */
	void registerFactory(IWidgetAdapterFactory factory, String... widgetIds) throws RegistrationException;

	/**
	 * Unregisters a widget adapter factory.
	 * <p>
	 * Does nothing if the factory was not registered with this registry.
	 * </p>
	 * 
	 * @param factory
	 *            the factory to unregister.
	 */
	void unregisterFactory(IWidgetAdapterFactory factory);
}
