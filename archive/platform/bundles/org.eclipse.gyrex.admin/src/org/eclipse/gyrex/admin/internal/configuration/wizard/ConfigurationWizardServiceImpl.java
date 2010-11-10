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
package org.eclipse.gyrex.admin.internal.configuration.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.gyrex.admin.internal.RunConfigWizardConfigConstraint;
import org.eclipse.gyrex.admin.setupwizard.ISetupWizardService;
import org.eclipse.gyrex.admin.setupwizard.SetupWizardStep;
import org.eclipse.gyrex.configuration.internal.impl.PlatformStatusRefreshJob;

/**
 * Internal implementation of {@link ISetupWizardService}.
 */
public class ConfigurationWizardServiceImpl implements ISetupWizardService {

	private Object registryHelper;
	private final Map<String, SetupWizardStep> registeredSteps = new HashMap<String, SetupWizardStep>(4);
	private final List<String> stepOrder = new ArrayList<String>(4);
	private final Lock stepRegistrationLock = new ReentrantLock();

	@Override
	public void addStep(final SetupWizardStep step) {
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

	private void clear() {
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

	public SetupWizardStep[] getSteps() {
		stepRegistrationLock.lock();
		try {
			final String[] stepIds = stepOrder.toArray(new String[stepOrder.size()]);
			final List<SetupWizardStep> steps = new ArrayList<SetupWizardStep>(stepIds.length);
			for (final String id : stepIds) {
				final SetupWizardStep step = registeredSteps.get(id);
				if (null != step) {
					steps.add(step);
				}
			}
			return steps.toArray(new SetupWizardStep[steps.size()]);
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

	public void setRegistry(final Object registry) {
		if (registryHelper != null) {
			((ConfigurationWizardServiceRegistryHelper) registryHelper).stop();
		}
		registryHelper = new ConfigurationWizardServiceRegistryHelper(this, registry);
	}

	public void shutdown() {
		setRegistry(null);
		clear();
	}

}
