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
package org.eclipse.gyrex.toolkit.runtime.lookup;

import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;

/**
 * The base widget registry implementation.
 * <p>
 * The base implementation provides an additional feature. I allows subclasses
 * to set a fallback factory that is consulted whenever no factory was
 * registered for a widget id.
 * </p>
 * <p>
 * This class is intended to be instantiated or subclassed by clients.
 * </p>
 */
public class BaseWidgetAdapterRegistry extends BaseRegistry<IWidgetAdapterFactory> implements IWidgetAdapterRegistry {

	/**
	 * Creates and returns a new base widget adapter registry instance.
	 */
	public BaseWidgetAdapterRegistry() {
		this(1, 1);
	}

	/**
	 * Creates and returns a new base widget adapter registry instance.
	 * 
	 * @param initialFactoryCapacity
	 *            the initial capacity for the number of factories to hold (must
	 *            be greater than or equal to zero)
	 * @param estimatedWidgetRatio
	 *            a rough estimation of the number of widgets mapped to a (must
	 *            be greater than or equal to zero) factory
	 */
	protected BaseWidgetAdapterRegistry(final int initialFactoryCapacity, final int estimatedWidgetRatio) {
		super(initialFactoryCapacity, estimatedWidgetRatio);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetAdapterFactory#getAdapter(java.lang.String, java.lang.Class, org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment)
	 */
	@Override
	public <T> T getAdapter(final String widgetId, final Class<T> adapterType, final IWidgetEnvironment environment) {
		final IWidgetAdapterFactory factory = lookupFactory(widgetId);
		if (null == factory) {
			return null;
		}

		return factory.getAdapter(widgetId, adapterType, environment);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetAdapterRegistry#registerFactory(org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetAdapterFactory, java.lang.String[])
	 */
	@Override
	public void registerFactory(final IWidgetAdapterFactory factory, final String... widgetIds) throws RegistrationException {
		addFactory(factory, widgetIds);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetAdapterRegistry#unregisterFactory(org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetAdapterFactory)
	 */
	@Override
	public void unregisterFactory(final IWidgetAdapterFactory factory) {
		removeFactory(factory);
	}
}
