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
package org.eclipse.cloudfree.configuration;

/**
 * Interface with shared configuration constants.
 */
public interface IConfigurationConstants {

	/**
	 * general error code <code>100</code> indicating 'not initialized'
	 */
	int ERROR_NOT_INITIALIZED = 100;

	/**
	 * property key '<code>org.eclipse.cloudfree.configuration.mode</code>' for
	 * <code>config.ini</code>
	 */
	String PROPERTY_KEY_CONFIGURATION_MODE = "org.eclipse.cloudfree.configuration.mode";

}
