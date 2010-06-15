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
package org.eclipse.gyrex.admin.configuration.wizard;

/**
 * A service to add pages (aka. "steps") to the configuration wizard.
 * <p>
 * This service is made available to clients as an OSGi service. For
 * convenience, there is also the
 * <code>org.eclipse.gyrex.admin.configuration.wizard</code> extension
 * point available to register wizard steps if the
 * <code>org.eclipse.core.runtime.IExtensionRegistry</code> is available.
 * </p>
 * <p>
 * Note, this interface is not intended to be implemented by clients.
 * </p>
 */
public interface IConfigurationWizardService {

	/**
	 * Adds a step to the configuration wizard.
	 * @param step
	 *            the step
	 */
	void addStep(ConfigurationWizardStep step);

	/**
	 * Removes a step from the configuration wizard.
	 * 
	 * @param id
	 *            the step id
	 */
	void removeStep(String id);
}
