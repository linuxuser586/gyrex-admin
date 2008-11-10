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
package org.eclipse.cloudfree.toolkit.runtime.lookup;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.cloudfree.toolkit.CWT;

/**
 * A base registry implementation for registering a factory <code>T</code> for
 * widget ids.
 * <p>
 * The base implementation provides an additional feature. I allows subclasses
 * to set a default factory that is consulted whenever no factory was registered
 * for a widget id.
 * </p>
 * <p>
 * This class may be subclassed by clients.
 * </p>
 */
public abstract class BaseRegistry<T> {

	private final Map<String, T> factoryByWidgetId;
	private final Map<T, Set<String>> factoryMappings;
	private final Lock registrationLock = new ReentrantLock();

	/** the fallback factory */
	private T defaultFactory;

	/**
	 * Creates and returns a new base registry instance.
	 * 
	 * @param initialFactoryCapacity
	 *            the initial capacity for the number of factories to hold (must
	 *            be greater than or equal to zero)
	 * @param estimatedWidgetRatio
	 *            a rough estimation of the number of widgets mapped to a (must
	 *            be greater than or equal to zero) factory
	 */
	protected BaseRegistry(final int initialFactoryCapacity, final int estimatedWidgetRatio) {
		if (initialFactoryCapacity < 0) {
			CWT.error(CWT.ERROR_INVALID_ARGUMENT, "initialFactoryCapacity must be greater than or equal to zero");
		}
		if (estimatedWidgetRatio < 0) {
			CWT.error(CWT.ERROR_INVALID_ARGUMENT, "estimatedWidgetRatio must be greater than or equal to zero");
		}

		factoryMappings = new HashMap<T, Set<String>>(initialFactoryCapacity);
		factoryByWidgetId = new HashMap<String, T>(initialFactoryCapacity * estimatedWidgetRatio);
	}

	/**
	 * Adds a factory for the specified widget ids.
	 * 
	 * @param factory
	 *            the factory to add
	 * @param widgetIds
	 *            the widget ids
	 */
	protected void addFactory(final T factory, final String... widgetIds) throws RegistrationException {
		if (null == factory) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "factory");
		}
		if (null == widgetIds) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "widgetIds");
		}

		registrationLock.lock();
		try {
			if (!factoryMappings.containsKey(factory)) {
				factoryMappings.put(factory, new HashSet<String>(widgetIds.length));
			}
			factoryMappings.get(factory).addAll(Arrays.asList(widgetIds));
			for (final String widgetId : widgetIds) {
				factoryByWidgetId.put(widgetId, factory);
			}
		} finally {
			registrationLock.unlock();
		}
	}

	/**
	 * Clears the registry.
	 */
	protected void clear() {
		registrationLock.lock();
		try {
			factoryByWidgetId.clear();
			factoryMappings.clear();
		} finally {
			registrationLock.unlock();
		}
	}

	/**
	 * Returns the default factory.
	 * 
	 * @return the default factory (maybe <code>null</code> if not available)
	 */
	protected T getDefaultFactory() {
		return defaultFactory;
	}

	/**
	 * Looks up a the factory for the specified widget id.
	 * <p>
	 * The registered widget factory is returned if one is found. If non is
	 * registered the {@link #getDefaultFactory() fallback factory} is returned
	 * (which might be <code>null</code>, too).
	 * </p>
	 * 
	 * @param widgetId
	 *            the widget id
	 * @return the widget factory that can be used for creating the widget(maybe
	 *         <code>null</code>)
	 */
	protected T lookupFactory(final String widgetId) {
		final T widgetFactory = factoryByWidgetId.get(widgetId);
		if (null != widgetFactory) {
			return widgetFactory;
		}

		// use fallback factory
		return getDefaultFactory();
	}

	/**
	 * Removes a factory.
	 * 
	 * @param factory
	 *            the factory to remove
	 */
	protected void removeFactory(final T factory) {
		if (null == factory) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "factory");
		}

		if (!factoryByWidgetId.containsValue(factory)) {
			return;
		}

		registrationLock.lock();
		try {
			final Set<String> mappings = factoryMappings.remove(factory);
			if (null == mappings) {
				return;
			}

			for (final String mapping : mappings) {
				factoryByWidgetId.remove(mapping);
			}
		} finally {
			registrationLock.unlock();
		}
	}

	/**
	 * Sets a default factory.
	 * 
	 * @param factory
	 *            the default factory to set
	 */
	protected void setDefaultFactory(final T factory) {
		defaultFactory = factory;
	}

}
