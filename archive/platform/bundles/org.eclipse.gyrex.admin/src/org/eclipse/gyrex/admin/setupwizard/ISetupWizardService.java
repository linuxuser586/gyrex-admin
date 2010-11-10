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
package org.eclipse.gyrex.admin.setupwizard;

/**
 * A service to add pages (aka. "steps") to the configuration wizard.
 * <p>
 * This service is made available to clients as an OSGi service. For
 * convenience, there is also the
 * <code>org.eclipse.gyrex.admin.setupwizard</code> extension point available to
 * register wizard steps if the Equinox Extension Registry is available.
 * </p>
 * <p>
 * Note, this interface is not intended to be implemented by clients.
 * </p>
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ISetupWizardService {

	/** the OSGi service name */
	String SERVICE_NAME = ISetupWizardService.class.getName();

	/**
	 * Adds a step to the configuration wizard.
	 * 
	 * @param step
	 *            the step
	 */
	void addStep(SetupWizardStep step);

	/**
	 * Removes a step from the configuration wizard.
	 * 
	 * @param id
	 *            the step id
	 */
	void removeStep(String id);
}
