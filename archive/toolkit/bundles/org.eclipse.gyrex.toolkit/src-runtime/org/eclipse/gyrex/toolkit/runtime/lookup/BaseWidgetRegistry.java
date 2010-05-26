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
import org.eclipse.gyrex.toolkit.widgets.Widget;

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
public class BaseWidgetRegistry extends BaseRegistry<IWidgetFactory> implements IWidgetRegistry {

	/**
	 * Creates and returns a new base widget registry instance.
	 */
	public BaseWidgetRegistry() {
		this(1, 1);
	}

	/**
	 * Creates and returns a new base widget registry instance.
	 * 
	 * @param initialFactoryCapacity
	 *            the initial capacity for the number of factories to hold (must
	 *            be greater than or equal to zero)
	 * @param estimatedWidgetRatio
	 *            a rough estimation of the number of widgets mapped to a (must
	 *            be greater than or equal to zero) factory
	 */
	protected BaseWidgetRegistry(final int initialFactoryCapacity, final int estimatedWidgetRatio) {
		super(initialFactoryCapacity, estimatedWidgetRatio);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory#getWidget(java.lang.String, org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetEnvironment)
	 */
	public Widget getWidget(final String id, final IWidgetEnvironment environment) {
		final IWidgetFactory factory = lookupFactory(id);
		if (null == factory) {
			return null;
		}

		return factory.getWidget(id, environment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetRegistry#registerFactory(org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory,
	 *      java.lang.String[])
	 */
	public void registerFactory(final IWidgetFactory factory, final String... widgetIds) throws RegistrationException {
		addFactory(factory, widgetIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetRegistry#unregisterFactory(org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory)
	 */
	public void unregisterFactory(final IWidgetFactory factory) {
		removeFactory(factory);
	}

}
