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

import org.eclipse.gyrex.admin.internal.ExtensionPointTracker;
import org.eclipse.gyrex.admin.internal.ExtensionPointTracker.Listener;
import org.eclipse.gyrex.admin.setupwizard.SetupWizardStep;
import org.eclipse.gyrex.toolkit.runtime.commands.CommandExecutionEvent;
import org.eclipse.gyrex.toolkit.wizard.WizardContainer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ConfigurationWizardServiceRegistryHelper implements Listener {

	private static class LazyActivatingStep extends SetupWizardStep {

		private final IConfigurationElement stepElement;
		private SetupWizardStep step;

		/**
		 * Creates a new instance.
		 * 
		 * @param stepId
		 * @param configurationElement
		 */
		public LazyActivatingStep(final String stepId, final IConfigurationElement configurationElement) {
			super(stepId);
			stepElement = configurationElement;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gyrex.admin.configuration.wizard.ConfigurationWizardStep#createPages(org.eclipse.gyrex.toolkit.wizard.WizardContainer)
		 */
		@Override
		public void createPages(final WizardContainer wizard) {
			if (null == step) {
				try {
					step = (SetupWizardStep) stepElement.createExecutableExtension(ATTRIBUTE_CLASS);
				} catch (final CoreException e) {
					// just log, but don't contribute any step
					LOG.warn("Error while instatiating Admin Configuration Wizard step {} registered by {}.", new Object[] { stepElement.getAttribute(ATTRIBUTE_CLASS), stepElement.getContributor().getName(), e });
					return;
				}
			}
			step.createPages(wizard);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gyrex.admin.configuration.wizard.ConfigurationWizardStep#wizardFinished(org.eclipse.gyrex.toolkit.runtime.commands.CommandExecutionEvent)
		 */
		@Override
		public IStatus wizardFinished(final CommandExecutionEvent finishEvent, final IProgressMonitor monitor) {
			if (null == step) {
				return Status.OK_STATUS; // ignore
			}
			return step.wizardFinished(finishEvent, null);
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationWizardServiceRegistryHelper.class);

	private static final String SETUP_WIZARD_EXTENSION_POINT = "org.eclipse.gyrex.admin.setupwizard";
	private static final String ATTRIBUTE_ID = "id";
	private static final String ATTRIBUTE_CLASS = "class";
	private static final String ELEMENT_STEP = "step";

	private final ConfigurationWizardServiceImpl service;
	private final ExtensionPointTracker tracker;

	/**
	 * Creates a new instance.
	 * 
	 * @param configurationWizardServiceImpl
	 * @param service
	 */
	public ConfigurationWizardServiceRegistryHelper(final ConfigurationWizardServiceImpl service, final Object registryObject) {
		this.service = service;
		tracker = new ExtensionPointTracker((IExtensionRegistry) registryObject, SETUP_WIZARD_EXTENSION_POINT, this);
		tracker.open();
	}

	@Override
	public void added(final IExtension extension) {
		final IConfigurationElement[] elements = extension.getConfigurationElements();
		for (int j = 0; j < elements.length; j++) {
			if (ELEMENT_STEP.equalsIgnoreCase(elements[j].getName())) {
				stepAdded(elements[j]);
			}
		}
	}

	@Override
	public void removed(final IExtension extension) {
		final IConfigurationElement[] elements = extension.getConfigurationElements();
		for (int j = 0; j < elements.length; j++) {
			final String stepId = elements[j].getAttribute(ATTRIBUTE_ID);
			if (stepId != null) {
				stepRemoved(stepId);
			}
		}
	}

	/**
	 * A step has been added.
	 * 
	 * @param configurationElement
	 */
	private void stepAdded(final IConfigurationElement configurationElement) {
		final String stepId = configurationElement.getAttribute(ATTRIBUTE_ID);
		if (null == stepId) {
			// TODO: log invalid step
			return;
		}
		service.addStep(new LazyActivatingStep(stepId, configurationElement));
	}

	/**
	 * A step has been removed.
	 * 
	 * @param stepId
	 */
	private void stepRemoved(final String stepId) {
		service.removeStep(stepId);
	}

	void stop() {
		tracker.close();
	}

}
