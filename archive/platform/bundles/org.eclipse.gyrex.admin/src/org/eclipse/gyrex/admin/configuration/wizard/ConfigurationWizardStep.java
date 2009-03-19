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
package org.eclipse.gyrex.admin.configuration.wizard;

import org.eclipse.gyrex.toolkit.runtime.commands.CommandExecutionEvent;
import org.eclipse.gyrex.toolkit.wizard.WizardContainer;

/**
 * A setup wizard step which will be integrated into the setup wizard.
 * <p>
 * Clients which wish to proved a setup wizard step need to subclass this class.
 * </p>
 */
public abstract class ConfigurationWizardStep {

	/** the step id */
	private final String id;

	/**
	 * Protected constructor to create a new instance using the specified id.
	 * 
	 * @param id
	 */
	protected ConfigurationWizardStep(final String id) {
		if (null == id) {
			throw new IllegalArgumentException("step id must not be null");
		}
		this.id = id;
	}

	/**
	 * Creates the wizard pages for this step.
	 * <p>
	 * The step is allowed to add dependencies to other pages in the wizard by
	 * referring to dialog fields in other pages when creating dialog field
	 * rules. However, care must be taken on the ordering because it's awkward
	 * to depend on pages which have not reached the user yet. Usually it's safe
	 * to depend on the pages which have already been defined for the wizard.
	 * </p>
	 * 
	 * @param wizard
	 */
	public abstract void createPages(final WizardContainer wizard);

	/**
	 * Returns the step id.
	 * 
	 * @return the step id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * Calles when the wizard has been finished.
	 * <p>
	 * Implementors need to overwrite this method and perform the desired
	 * configuration based on the content submitted in the finish event. Note,
	 * implementors must prepare for the dynamic effect in case the step was
	 * registered *after* the wizard has already created its pages.
	 * </p>
	 * 
	 * @param finishEvent
	 *            the event triggered as a result of pressing the wizard finish
	 *            button
	 * @return <code>true</code> if the configuration was processed successfully
	 *         and the wizard can finish, <code>false</code> otherwise
	 */
	// TODO: add IProgressMonitor support
	// TODO: changed to IStatus based return value
	public abstract boolean wizardFinished(final CommandExecutionEvent finishEvent);
}
