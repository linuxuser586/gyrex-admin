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
package org.eclipse.gyrex.toolkit.runtime;


import com.ibm.icu.util.ULocale;

import java.security.Principal;
import java.util.Map;

import org.eclipse.gyrex.toolkit.CWT;

/**
 * Public base implementation of {@link IWidgetEnvironment}.
 * <p>
 * Clients may subclass to provide are more sophisticated environment.
 * </p>
 */
public class BaseWidgetEnvironment implements IWidgetEnvironment {

	private final Principal userPrincipal;
	private final ULocale locale;
	private final Map<String, Object> attributesMap;

	/**
	 * Creates a new environment using the specified values.
	 * 
	 * @param locale
	 *            the target locale
	 * @param userPrincipal
	 *            the authenticated user principal
	 * @param attributesMap
	 *            the map of named attributes
	 */
	public BaseWidgetEnvironment(final ULocale locale, final Principal userPrincipal, final Map<String, Object> attributesMap) {
		this.locale = locale;
		this.userPrincipal = userPrincipal;
		this.attributesMap = attributesMap;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(final String name) {
		if (null == name) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "attribute name must not be null");
		}

		// don't fail if no map is available
		if (null == attributesMap) {
			return null;
		}

		return attributesMap.get(name);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetEnvironment#getLocale()
	 */
	@Override
	public ULocale getLocale() {
		return locale;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetEnvironment#getAuthenticationToken()
	 */
	@Override
	public Principal getUserPrincipal() {
		return userPrincipal;
	}
}
