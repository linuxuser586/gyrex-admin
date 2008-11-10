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
package org.eclipse.cloudfree.configuration.preferences;


import org.eclipse.cloudfree.configuration.ConfigurationMode;
import org.eclipse.cloudfree.configuration.internal.ConfigurationActivator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

/**
 * Object representing the platform scope in the Eclipse preferences hierarchy.
 * Can be used as a context for searching for preference values (in the
 * IPreferencesService APIs) or for determining the correct preference node to
 * set values in the store.
 * <p>
 * Platform preferences are stored on a per cluster basis depending on the
 * configuration mode. In {@link ConfigurationMode#PRODUCTION production mode}
 * the preferences are shared across a CloudFree platform cluster. In
 * {@link ConfigurationMode.DEVELOPMENT development mode} the preferences are
 * actually backed by {@link InstanceScope instance preferences} . No location
 * is provided for platform preferences.
 * </p>
 * <p>
 * The path for preferences defined in the platform scope hierarchy is as
 * follows: <code>/platform/&lt;qualifier&gt;</code>
 * </p>
 * <p>
 * This class is not intended to be subclassed. This class may be instantiated.
 * </p>
 */
public final class PlatformScope implements IScopeContext {

	/**
	 * String constant (value of <code>"platform"</code>) used for the scope
	 * name for the platform preference scope.
	 */
	public static final String NAME = "platform"; //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof IScopeContext)) {
			return false;
		}
		final IScopeContext other = (IScopeContext) obj;
		if (!getName().equals(other.getName())) {
			return false;
		}
		final IPath location = getLocation();
		return location == null ? other.getLocation() == null : location.equals(other.getLocation());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.IScopeContext#getLocation()
	 */
	@Override
	public IPath getLocation() {
		return null; // no location
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.IScopeContext#getName()
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/*
	 * Default path hierarchy for nodes is /<scope>/<qualifier>.
	 * 
	 * @see org.eclipse.core.runtime.preferences.IScopeContext#getNode(java.lang.String)
	 */
	public IEclipsePreferences getNode(final String qualifier) {
		if (qualifier == null) {
			throw new IllegalArgumentException("qualifier must not be null");
		}
		return (IEclipsePreferences) ConfigurationActivator.getInstance().getPreferencesService().getRootNode().node(getName()).node(qualifier);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

}
