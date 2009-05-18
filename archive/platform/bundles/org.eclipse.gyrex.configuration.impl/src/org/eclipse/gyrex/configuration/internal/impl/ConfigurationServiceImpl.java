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
package org.eclipse.gyrex.configuration.internal.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.gyrex.configuration.service.IConfigurationService;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Internal implementation of {@link IConfigurationService}.
 */
public class ConfigurationServiceImpl implements IConfigurationService {

	private static final String DEFAULT = "default";
	private static final String PLATFORM = "platform";
	private static final String UTF_8 = "UTF-8";

	private String get(final String key, final String defaultValue, final Preferences[] nodes) {
		return PreferencesUtil.getPreferenceService().get(PreferencesUtil.decodePath(key)[1], defaultValue, nodes);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#getBoolean(java.lang.String, java.lang.String, boolean, org.eclipse.gyrex.context.IRuntimeContext)
	 */
	@Override
	public boolean getBoolean(final String qualifier, final String key, final boolean defaultValue, final IRuntimeContext context) {
		final String result = get(key, null, getNodes(qualifier, key, context));
		return result == null ? defaultValue : Boolean.valueOf(result).booleanValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#getByteArray(java.lang.String, java.lang.String, byte[], org.eclipse.gyrex.context.IRuntimeContext)
	 */
	@Override
	public byte[] getByteArray(final String qualifier, final String key, final byte[] defaultValue, final IRuntimeContext context) {
		final String result = get(key, null, getNodes(qualifier, key, context));
		try {
			return result == null ? defaultValue : Base64.decodeBase64(result.getBytes(UTF_8));
		} catch (final UnsupportedEncodingException e) {
			throw new IllegalStateException("Gyrex requires a platform which supports UTF-8.", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#getDouble(java.lang.String, java.lang.String, double, org.eclipse.gyrex.context.IRuntimeContext)
	 */
	@Override
	public double getDouble(final String qualifier, final String key, final double defaultValue, final IRuntimeContext context) {
		final String value = get(key, null, getNodes(qualifier, key, context));
		return NumberUtils.toDouble(value, defaultValue);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#getFloat(java.lang.String, java.lang.String, float, org.eclipse.gyrex.context.IRuntimeContext)
	 */
	@Override
	public float getFloat(final String qualifier, final String key, final float defaultValue, final IRuntimeContext context) {
		final String value = get(key, null, getNodes(qualifier, key, context));
		return NumberUtils.toFloat(value, defaultValue);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#getInt(java.lang.String, java.lang.String, int, org.eclipse.gyrex.context.IRuntimeContext)
	 */
	@Override
	public int getInt(final String qualifier, final String key, final int defaultValue, final IRuntimeContext context) {
		final String value = get(key, null, getNodes(qualifier, key, context));
		return NumberUtils.toInt(value, defaultValue);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#getLong(java.lang.String, java.lang.String, long, org.eclipse.gyrex.context.IRuntimeContext)
	 */
	@Override
	public long getLong(final String qualifier, final String key, final long defaultValue, final IRuntimeContext context) {
		final String value = get(key, null, getNodes(qualifier, key, context));
		return NumberUtils.toLong(value, defaultValue);
	}

	private Preferences getNode(final String qualifier, final String key, final IRuntimeContext context) {
		final IEclipsePreferences rootNode = PreferencesUtil.getRootNode();
		final String childPath = PreferencesUtil.makeRelative(PreferencesUtil.decodePath(key)[0]);
		Preferences node = rootNode.node(PLATFORM).node(qualifier);
		if (null != context) {
			final IPath contextPath = context.getContextPath();
			if (!contextPath.isEmpty() && !contextPath.isRoot()) {
				node = node.node(contextPath.makeRelative().toString());
			}
		}
		if (childPath.length() > 0) {
			node = node.node(childPath);
		}
		return node;
	}

	private Preferences[] getNodes(final String qualifier, final String key, final IRuntimeContext context) {
		final String[] order = new String[] { PLATFORM, DEFAULT };
		final IEclipsePreferences rootNode = PreferencesUtil.getRootNode();
		final String childPath = PreferencesUtil.makeRelative(PreferencesUtil.decodePath(key)[0]);
		final List<Preferences> result = new ArrayList<Preferences>();
		for (int i = 0; i < order.length; i++) {
			final String scopeString = order[i];
			final Preferences node = rootNode.node(scopeString).node(qualifier);
			if (null != context) {
				for (IPath contextPath = context.getContextPath(); !contextPath.isEmpty() && !contextPath.isRoot(); contextPath = contextPath.removeLastSegments(1)) {
					final String path = childPath.length() > 0 ? contextPath.append(childPath).makeRelative().toString() : contextPath.makeRelative().toString();
					try {
						if (node.nodeExists(path)) {
							result.add(node.node(path));
						}
					} catch (final BackingStoreException e) {
						// node has been removed
					}
				}
			}
			// append always the root preference node
			final String root = childPath.length() > 0 ? childPath : "";
			try {
				if (node.nodeExists(root)) {
					result.add(node.node(root));
				}
			} catch (final BackingStoreException e) {
				// node has been removed;
			}
		}
		return result.toArray(new Preferences[result.size()]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#getString(java.lang.String, java.lang.String, java.lang.String, org.eclipse.gyrex.context.IRuntimeContext)
	 */
	@Override
	public String getString(final String qualifier, final String key, final String defaultValue, final IRuntimeContext context) {
		return get(key, defaultValue, getNodes(qualifier, key, context));
	}

	private void put(final String qualifier, final String key, final String value, final IRuntimeContext context, final boolean encrypt) {
		final Preferences node = getNode(qualifier, key, context);
		node.put(PreferencesUtil.decodePath(key)[1], value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#putBoolean(java.lang.String, java.lang.String, boolean, org.eclipse.gyrex.context.IRuntimeContext, boolean)
	 */
	@Override
	public void putBoolean(final String qualifier, final String key, final boolean value, final IRuntimeContext context, final boolean encrypt) {
		put(qualifier, key, Boolean.toString(value), context, encrypt);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#putByteArray(java.lang.String, java.lang.String, byte[], org.eclipse.gyrex.context.IRuntimeContext, boolean)
	 */
	@Override
	public void putByteArray(final String qualifier, final String key, final byte[] value, final IRuntimeContext context, final boolean encrypt) {
		if (null == value) {
			remove(qualifier, key, context);
		} else {
			try {
				put(qualifier, key, new String(Base64.encodeBase64(value), UTF_8), context, encrypt);
			} catch (final UnsupportedEncodingException e) {
				throw new IllegalStateException("Gyrex requires a platform which supports UTF-8.", e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#putDouble(java.lang.String, java.lang.String, double, org.eclipse.gyrex.context.IRuntimeContext, boolean)
	 */
	@Override
	public void putDouble(final String qualifier, final String key, final double value, final IRuntimeContext context, final boolean encrypt) {
		put(qualifier, key, Double.toString(value), context, encrypt);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#putFloat(java.lang.String, java.lang.String, float, org.eclipse.gyrex.context.IRuntimeContext, boolean)
	 */
	@Override
	public void putFloat(final String qualifier, final String key, final float value, final IRuntimeContext context, final boolean encrypt) {
		put(qualifier, key, Float.toString(value), context, encrypt);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#putInt(java.lang.String, java.lang.String, int, org.eclipse.gyrex.context.IRuntimeContext, boolean)
	 */
	@Override
	public void putInt(final String qualifier, final String key, final int value, final IRuntimeContext context, final boolean encrypt) {
		put(qualifier, key, Integer.toString(value), context, encrypt);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#putLong(java.lang.String, java.lang.String, long, org.eclipse.gyrex.context.IRuntimeContext, boolean)
	 */
	@Override
	public void putLong(final String qualifier, final String key, final long value, final IRuntimeContext context, final boolean encrypt) {
		put(qualifier, key, Long.toString(value), context, encrypt);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#putString(java.lang.String, java.lang.String, java.lang.String, org.eclipse.gyrex.context.IRuntimeContext, boolean)
	 */
	@Override
	public void putString(final String qualifier, final String key, final String value, final IRuntimeContext context, final boolean encrypt) {
		if (null == value) {
			remove(qualifier, key, context);
		} else {
			put(qualifier, key, value, context, encrypt);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.configuration.service.IConfigurationService#remove(java.lang.String, java.lang.String, org.eclipse.gyrex.context.IRuntimeContext)
	 */
	@Override
	public void remove(final String qualifier, final String key, final IRuntimeContext context) {
		getNode(qualifier, key, context).remove(PreferencesUtil.decodePath(key)[1]);
	}

}
