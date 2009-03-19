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

/**
 * The widget environment specifies details about the environment where the
 * widget is used.
 * <p>
 * IMPORTANT: This interface is not intended to be implemented directly. Clients
 * must subclass {@link BaseWidgetEnvironment} instead to protect themselves
 * from API changes.
 * </p>
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface IWidgetEnvironment {

	/**
	 * Returns the value of a named environment attribute or <code>null</code>
	 * of no such attribute exists.
	 * <p>
	 * Environment attributes will be set by the rendering layer. TODO: write
	 * more info about the intention of environment attributes.
	 * </p>
	 * 
	 * @param name
	 *            the attribute name (may not be <code>null</code>)
	 * @return the attribute value or <code>null</code> if not available
	 */
	Object getAttribute(String name);

	/**
	 * Returns the locale of the target environment.
	 * 
	 * @return the locale or <code>null</code> if non was provided by the target
	 *         system
	 */
	ULocale getLocale();

	/**
	 * Returns the {@link Principal principal} of the authenticated user in the
	 * target environment.
	 * 
	 * @return the {@link Principal principal} of the authenticated user or
	 *         <code>null</code> if non was provided by the target system
	 */
	Principal getUserPrincipal();
}
