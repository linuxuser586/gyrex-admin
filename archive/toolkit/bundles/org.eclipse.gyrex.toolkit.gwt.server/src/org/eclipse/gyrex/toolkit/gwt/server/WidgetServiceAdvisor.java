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
package org.eclipse.gyrex.toolkit.gwt.server;

import java.security.Principal;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.eclipse.gyrex.toolkit.gwt.client.WidgetClientEnvironment;
import org.eclipse.gyrex.toolkit.runtime.BaseWidgetEnvironment;
import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetAdapterFactory;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory;

import org.apache.commons.lang.LocaleUtils;

/**
 * Public base class for configuring a {@link WidgetService widget service}.
 * <p>
 * An application should declare a subclass of <code>WidgetServiceAdvisor</code>
 * and override methods to configure widget services to suit the needs of the
 * particular application.
 * </p>
 */
public class WidgetServiceAdvisor {

	/** the widget factory */
	private final IWidgetFactory widgetFactory;

	/** the widget adapter factory */
	private final IWidgetAdapterFactory widgetAdapterFactory;

	/**
	 * Creates a new advisor.
	 * 
	 * @param widgetFactory
	 *            the widget factory to use (may not be <code>null</code>; see
	 *            also {@link #getWidgetFactory()})
	 * @param widgetAdapterFactory
	 *            the widget adapter factory to use (can be <code>null</code>
	 *            but is typically required in a sophisticated application; see
	 *            also {@link #getWidgetAdapterFactory()})
	 */
	public WidgetServiceAdvisor(final IWidgetFactory widgetFactory, final IWidgetAdapterFactory widgetAdapterFactory) {
		this.widgetFactory = widgetFactory;
		this.widgetAdapterFactory = widgetAdapterFactory;
	}

	/**
	 * Returns a map of attributes from the specified client environment.
	 * <p>
	 * The default implementation returns an empty map. Subclasses may overwrite
	 * to provide additional attributes to the
	 * {@link #getWidgetEnvironment(WidgetClientEnvironment) widget environment}
	 * .
	 * </p>
	 * 
	 * @param environment
	 *            the environment (maybe <code>null</code>)
	 * @return the attribute map (maynot be <code>null</code>)
	 */
	protected Map<String, Object> getAttributesMap(final WidgetClientEnvironment environment) {
		return Collections.emptyMap();
	}

	/**
	 * Determines the default locale.
	 * <p>
	 * The default implementation will return the {@link ULocale#getDefault()
	 * current default locale}. Subclasses may overwrite to provide a different
	 * default locale.
	 * </p>
	 * 
	 * @return the default locale (may not be <code>null</code>)
	 */
	protected Locale getDefaultLocale() {
		//		final String acceptLanguage = getThreadLocalRequest().getHeader("Accept-Language");
		//		if ((null != acceptLanguage) && (acceptLanguage.length() > 0)) {
		//			return ULocale.acceptLanguage(acceptLanguage, null);
		//		}

		return Locale.getDefault();
	}

	/**
	 * Determines the locale from the specified environment.
	 * <p>
	 * If the environment is <code>null</code> or no locale id is set a
	 * {@link #getDefaultLocale() default locale} will be returned. Subclasses
	 * may overwrite to customize locale detection.
	 * </p>
	 * 
	 * @param environment
	 *            the client environment
	 * @return a locale (may not be <code>null</code>)
	 * @see #getDefaultLocale()
	 */
	protected Locale getLocale(final WidgetClientEnvironment environment) {
		if ((null != environment) && (null != environment.getLocaleId())) {
			return LocaleUtils.toLocale(environment.getLocaleId());
		}

		return getDefaultLocale();
	}

	/**
	 * Determines the user principal from the specified environment.
	 * <p>
	 * The default implementation returns <code>null</code>. Subclasses should
	 * overwrite to return a user principal.
	 * </p>
	 * 
	 * @param environment
	 *            the client environment
	 * @return a user principal of the authenticated users (may be
	 *         <code>null</code>)
	 */
	protected Principal getUserPrincipal(final WidgetClientEnvironment environment) {
		//return getThreadLocalRequest().getUserPrincipal();
		return null;
	}

	/**
	 * Returns the widget adapter factory.
	 * <p>
	 * The default implementation returns the widget adapter factory passed to
	 * the advisor in
	 * {@link #WidgetServiceAdvisor(IWidgetFactory, IWidgetAdapterFactory)}.
	 * </p>
	 * 
	 * @return the widget adapter factory (may be <code>null</code>)
	 */
	public IWidgetAdapterFactory getWidgetAdapterFactory() {
		return widgetAdapterFactory;
	}

	/**
	 * Creates and returns the widget environment from the client environment.
	 * <p>
	 * Called by widget service on every remote service call to create the
	 * widget environment. Subclasses may overwrite to provide a more
	 * sophisticated widget environment. The default implementation calls
	 * {@link #getLocale(WidgetClientEnvironment)},
	 * {@link #getUserPrincipal(WidgetClientEnvironment)} and
	 * {@link #getAttributesMap(WidgetClientEnvironment)} and creates a
	 * {@link BaseWidgetEnvironment}.
	 * </p>
	 * 
	 * @param environment
	 *            the environment (maybe <code>null</code>)
	 * @return a widget environment
	 */
	public IWidgetEnvironment getWidgetEnvironment(final WidgetClientEnvironment environment) {
		final Locale locale = getLocale(environment);
		final Principal userPrincipal = getUserPrincipal(environment);
		final Map<String, Object> attributesMap = getAttributesMap(environment);
		return new BaseWidgetEnvironment(locale, userPrincipal, attributesMap);
	}

	/**
	 * Returns the widget factory.
	 * <p>
	 * The default implementation returns the widget factory passed to the
	 * advisor in
	 * {@link #WidgetServiceAdvisor(IWidgetFactory, IWidgetAdapterFactory)}.
	 * </p>
	 * 
	 * @return the widget factory (may not be <code>null</code>)
	 */
	public IWidgetFactory getWidgetFactory() {
		return widgetFactory;
	}
}
