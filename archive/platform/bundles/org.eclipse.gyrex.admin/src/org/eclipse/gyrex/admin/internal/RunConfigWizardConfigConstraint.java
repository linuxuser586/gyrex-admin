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
package org.eclipse.gyrex.admin.internal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.gyrex.configuration.PlatformConfiguration;
import org.eclipse.gyrex.configuration.constraints.PlatformConfigurationConstraint;
import org.eclipse.gyrex.configuration.preferences.PlatformScope;
import org.osgi.service.prefs.BackingStoreException;

/**
 * A configuration constraint that checks if the setup wizard already run.
 */
public final class RunConfigWizardConfigConstraint extends PlatformConfigurationConstraint {

	/** preference key for the run of the setup wizard */
	static final String PREF_KEY_CONFIG_WIZARD_STEPS_OK = "configWizard.steps.ok";

	public static void addStepToExecutedList(final String stepId) {
		final IEclipsePreferences adminPref = new PlatformScope().getNode(AdminActivator.PLUGIN_ID);
		final String stepsExecutedOk = adminPref.get(PREF_KEY_CONFIG_WIZARD_STEPS_OK, null);
		if ((null == stepsExecutedOk) || (stepsExecutedOk.trim().length() == 0)) {
			adminPref.put(PREF_KEY_CONFIG_WIZARD_STEPS_OK, stepId);
		} else {
			adminPref.put(PREF_KEY_CONFIG_WIZARD_STEPS_OK, stepsExecutedOk.concat(",").concat(stepId));
		}
		try {
			adminPref.flush();
		} catch (final BackingStoreException e) {
			// TODO consider logging this but do not fail 
		}
	}

	public static void removeStepFromExecutedList(final String stepId) {
		final IEclipsePreferences adminPref = new PlatformScope().getNode(AdminActivator.PLUGIN_ID);
		String stepsExecutedOk = adminPref.get(PREF_KEY_CONFIG_WIZARD_STEPS_OK, null);
		if ((null == stepsExecutedOk) || (stepsExecutedOk.trim().length() == 0)) {
			return;
		}

		// remove all instances
		final String[] steps = stepsExecutedOk.split(",");
		stepsExecutedOk = "";
		for (final String step : steps) {
			if (!step.equals(stepId)) {
				if (stepsExecutedOk.length() > 0) {
					stepsExecutedOk += ",";
				}
				stepsExecutedOk += step;
			}
		}

		// save
		if (stepsExecutedOk.length() > 0) {
			adminPref.put(PREF_KEY_CONFIG_WIZARD_STEPS_OK, stepsExecutedOk);
		} else {
			adminPref.remove(PREF_KEY_CONFIG_WIZARD_STEPS_OK);
		}

		try {
			adminPref.flush();
		} catch (final BackingStoreException e) {
			// TODO consider logging this but do not fail 
		}
	}

	/**
	 * Indicated if the setup wizard should be shown.
	 * 
	 * @return <code>true</code> if the setup wizard should be shown,
	 *         <code>false</code> otherwise
	 */
	public static boolean shouldBringUpSetupWizard() throws IllegalStateException {
		try {
			final String stepsExecutedOk = PlatformConfiguration.getConfigurationService().getString(AdminActivator.PLUGIN_ID, PREF_KEY_CONFIG_WIZARD_STEPS_OK, null, null);
			if (null == stepsExecutedOk) {
				return true; // nothing executed yet
			}

			final Set<String> executedSteps = new HashSet<String>(Arrays.asList(stepsExecutedOk.split(",")));
			final String[] steps = AdminActivator.getInstance().getConfigurationWizardService().getStepIds();
			for (final String step : steps) {
				if (!executedSteps.contains(step)) {
					return true;
				}
			}

			// all steps executed
			return false;
		} catch (final IllegalStateException e) {
			// TODO consider logging this, something may not be properly started 
			// assume yes to try to bing up the wizard
			return true;
		}
	}

	@Override
	public IStatus evaluateConfiguration(final IProgressMonitor progressMonitor) {
		// don't show a warning if we should shutdown
		if (AdminActivator.getInstance().mustRestartPlatform) {
			return Status.OK_STATUS;
		}

		// now let's check if we need to bring up the wizard
		final boolean shouldBringUpWizard = RunConfigWizardConfigConstraint.shouldBringUpSetupWizard();
		if (shouldBringUpWizard) {
			return new Status(IStatus.INFO, AdminActivator.PLUGIN_ID, "Please run the configuration wizard.");
		}
		return Status.OK_STATUS;
	}

}