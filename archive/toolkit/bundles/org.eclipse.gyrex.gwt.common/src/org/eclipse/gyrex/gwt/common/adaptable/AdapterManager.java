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
package org.eclipse.cloudfree.gwt.common.adaptable;

import java.util.HashMap;
import java.util.Map;

/**
 * The adapter manager manages adapter contributions to adaptable objects.
 * <p>
 * Although the concept is similar to what is in Eclipse Equinox it's different
 * because of limitations in GWT. For example, it's not possible currently to
 * get the type hierarchy in GWT. Thus, adapters are strictly registered to only
 * a particular type and adaptable objects may need to perform multiple lookups
 * to find an adapter.
 * </p>
 * <p>
 * This class is not intended to be extended or instantiated by clients. The
 * static method {@link #getAdapterManager()} must be used to obtain an
 * instance.
 * </p>
 */
public final class AdapterManager {

	/** shared default instance */
	private static AdapterManager adapterManager;

	/**
	 * Returns the adapter manager instance.
	 * 
	 * @return the adapter manager instance
	 */
	public static AdapterManager getAdapterManager() {
		if (null == adapterManager) {
			adapterManager = new AdapterManager();
		}
		return adapterManager;
	}

	/** the map with registered adapters */
	private Map<String, Object> registeredAdaptersMap;

	/**
	 * Hidden constructor.
	 */
	private AdapterManager() {
		// empty
	}

	/**
	 * Returns an adapter.
	 * 
	 * @param adapterType
	 *            the adapter type
	 * @param adaptableTypeHierarchy
	 *            the hierarchy of the adaptable type
	 * @return the adapter (maybe <code>null</code> if none is registered)
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAdapter(final Class<T> adapterType, final Class[] adaptableTypeHierarchy) {
		if (null == registeredAdaptersMap) {
			return null;
		}

		for (int i = 0; i < adaptableTypeHierarchy.length; i++) {
			final Class adaptableType = adaptableTypeHierarchy[i];
			final String key = getMappingKey(adaptableType, adapterType);
			final Object adapter = registeredAdaptersMap.get(key);
			if (null != adapter) {
				// see http://code.google.com/p/google-web-toolkit/issues/detail?id=2710
				return (T) adapter;
			}
		}
		return null;
	}

	private String getMappingKey(final Class adaptableType, final Class adapterType) {
		return adapterType.toString() + ":" + adaptableType.toString();
	}

	/**
	 * Registers and adapter.
	 * 
	 * @param adaptableType
	 *            the {@link IsAdaptable adaptable} type
	 * @param adapterType
	 *            the type of the adapter
	 * @param adapterInstance
	 *            the adapter instance
	 */
	public void registerAdapter(final Class adaptableType, final Class adapterType, final Object adapterInstance) {
		if (null == registeredAdaptersMap) {
			registeredAdaptersMap = new HashMap<String, Object>();
		}

		// the mapping is simple
		final String key = getMappingKey(adaptableType, adapterType);

		// last one wins
		registeredAdaptersMap.put(key, adapterInstance);
	}
}
