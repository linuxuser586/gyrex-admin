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
package org.eclipse.gyrex.admin.internal.configuration.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import org.eclipse.gyrex.admin.configuration.wizard.ConfigurationWizardStep;
import org.eclipse.gyrex.admin.configuration.wizard.IConfigurationWizardService;
import org.eclipse.gyrex.admin.internal.RunConfigWizardConfigConstraint;
import org.eclipse.gyrex.configuration.internal.impl.PlatformStatusRefreshJob;

/**
 * Internal implementation of {@link IConfigurationWizardService}.
 */
public class ConfigurationWizardServiceImpl implements IConfigurationWizardService {

	private Object registryHelper;
	private final Map<String, ConfigurationWizardStep> registeredSteps = new HashMap<String, ConfigurationWizardStep>(4);
	private final List<String> stepOrder = new ArrayList<String>(4);
	private final Lock stepRegistrationLock = new ReentrantLock();

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.admin.configuration.wizard.IConfigurationWizardService#addStep(java.lang.String, org.eclipse.gyrex.admin.configuration.wizard.ConfigurationWizardStep)
	 */
	@Override
	public void addStep(final ConfigurationWizardStep step) {
		if (null == step) {
			throw new IllegalArgumentException("step must not be null");
		}
		stepRegistrationLock.lock();
		try {
			if (stepOrder.contains(step.getId())) {
				return;
			}
			stepOrder.add(step.getId());
			registeredSteps.put(step.getId(), step);
		} finally {
			stepRegistrationLock.unlock();
		}

		// update platform status because we may now need to execute the wizard
		try {
			if (!RunConfigWizardConfigConstraint.shouldBringUpSetupWizard()) {
				PlatformStatusRefreshJob.scheduleRefreshIfPermitted();
			}
		} catch (final IllegalStateException e) {
			// ignore
		}
	}

	public void clear() {
		stepRegistrationLock.lock();
		try {
			stepOrder.clear();
			registeredSteps.clear();
		} finally {
			stepRegistrationLock.unlock();
		}
	}

	public String[] getStepIds() {
		stepRegistrationLock.lock();
		try {
			return stepOrder.toArray(new String[stepOrder.size()]);
		} finally {
			stepRegistrationLock.unlock();
		}
	}

	public ConfigurationWizardStep[] getSteps() {
		stepRegistrationLock.lock();
		try {
			final String[] stepIds = stepOrder.toArray(new String[stepOrder.size()]);
			final List<ConfigurationWizardStep> steps = new ArrayList<ConfigurationWizardStep>(stepIds.length);
			for (final String id : stepIds) {
				final ConfigurationWizardStep step = registeredSteps.get(id);
				if (null != step) {
					steps.add(step);
				}
			}
			return steps.toArray(new ConfigurationWizardStep[steps.size()]);
		} finally {
			stepRegistrationLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.admin.configuration.wizard.IConfigurationWizardService#removeStep(java.lang.String)
	 */
	@Override
	public void removeStep(final String id) {
		if (null == id) {
			throw new IllegalArgumentException("step id must not be null");
		}
		stepRegistrationLock.lock();
		try {
			if (!stepOrder.contains(id)) {
				return;
			}
			stepOrder.remove(id);
			registeredSteps.remove(id);
		} finally {
			stepRegistrationLock.unlock();
		}

		// update platform status because we may now not need to execute the wizard
		try {
			if (RunConfigWizardConfigConstraint.shouldBringUpSetupWizard()) {
				PlatformStatusRefreshJob.scheduleRefreshIfPermitted();
			}
		} catch (final IllegalStateException e) {
			// ignore
		}
	}

	public void setRegistryHelper(final Object registryHelper) {
		if ((this.registryHelper != null) && (this.registryHelper != registryHelper)) {
			((ConfigurationWizardServiceRegistryHelper) this.registryHelper).stop();
		}
		this.registryHelper = registryHelper;
	}

}
