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
package org.eclipse.gyrex.toolkit.runtime;

import java.security.Principal;
import java.util.Locale;
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
	private final Locale locale;
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
	public BaseWidgetEnvironment(final Locale locale, final Principal userPrincipal, final Map<String, Object> attributesMap) {
		this.locale = locale;
		this.userPrincipal = userPrincipal;
		this.attributesMap = attributesMap;
	}

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

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public Principal getUserPrincipal() {
		return userPrincipal;
	}
}
