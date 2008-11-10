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
package org.eclipse.cloudfree.admin.internal.configuration.wizard;


import org.eclipse.cloudfree.admin.configuration.wizard.ConfigurationWizardStep;
import org.eclipse.cloudfree.admin.internal.AdminActivator;
import org.eclipse.cloudfree.common.logging.LogAudience;
import org.eclipse.cloudfree.common.logging.LogImportance;
import org.eclipse.cloudfree.common.logging.LogSource;
import org.eclipse.cloudfree.toolkit.runtime.commands.CommandExecutionEvent;
import org.eclipse.cloudfree.toolkit.wizard.WizardContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.osgi.util.NLS;

/**
 * 
 */
public class ConfigurationWizardServiceRegistryHelper implements IRegistryChangeListener {

	private static class LazyActivatingStep extends ConfigurationWizardStep {

		private final IConfigurationElement stepElement;
		private ConfigurationWizardStep step;

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
		 * @see org.eclipse.cloudfree.admin.configuration.wizard.ConfigurationWizardStep#createPages(org.eclipse.cloudfree.toolkit.wizard.WizardContainer)
		 */
		@Override
		public void createPages(final WizardContainer wizard) {
			if (null == step) {
				try {
					step = (ConfigurationWizardStep) stepElement.createExecutableExtension(ATTRIBUTE_CLASS);
				} catch (final CoreException e) {
					AdminActivator.getInstance().getLog().log(NLS.bind("Error while instatiating Admin Configuration Wizard step {0} registered by {1}.", stepElement.getAttribute(ATTRIBUTE_CLASS), stepElement.getContributor().getName()), e, (Object) null, LogImportance.ERROR, LogAudience.DEVELOPER, LogSource.PLATFORM);
					return;
				}
			}
			step.createPages(wizard);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.cloudfree.admin.configuration.wizard.ConfigurationWizardStep#wizardFinished(org.eclipse.cloudfree.toolkit.runtime.commands.CommandExecutionEvent)
		 */
		@Override
		public boolean wizardFinished(final CommandExecutionEvent finishEvent) {
			if (null == step) {
				return false;
			}
			return step.wizardFinished(finishEvent);
		}
	}

	private static final String ATTRIBUTE_ID = "id";
	private static final String ATTRIBUTE_CLASS = "class";
	private static final String ELEMENT_STEP = "step";
	private static final String POINT_WIZARD = "wizard";

	private static final String NAMESPACE_CONFIGURATION = "org.eclipse.cloudfree.admin.configuration";

	private static final IExtension[] EMPTY_EXTENSION_ARRAY = new IExtension[0];

	private final IExtensionRegistry registry;
	private final ConfigurationWizardServiceImpl service;

	/**
	 * Creates a new instance.
	 * 
	 * @param configurationWizardServiceImpl
	 * @param service
	 */
	public ConfigurationWizardServiceRegistryHelper(final ConfigurationWizardServiceImpl service, final Object registryObject) {
		this.service = service;
		registry = (IExtensionRegistry) registryObject;
		initializeSteps();
		registry.addRegistryChangeListener(this);
	}

	/**
	 * Returns a list of extension plugged into the extension point
	 * 
	 * @return a list of extensions
	 */
	private IExtension[] getConfigWizardExtensions() {
		IExtension[] extensions = EMPTY_EXTENSION_ARRAY;
		final IExtensionPoint point = registry.getExtensionPoint(NAMESPACE_CONFIGURATION, POINT_WIZARD);
		if (point != null) {
			extensions = point.getExtensions();
		}

		if (extensions.length == 0) {
			// TODO should probably log debug message
		}

		return extensions;
	}

	/**
	 * Initializes steps which are plugged into the extension point.
	 */
	private void initializeSteps() {
		final IExtension[] extensions = getConfigWizardExtensions();
		for (int i = 0; i < extensions.length; i++) {
			final IConfigurationElement[] elements = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				if (ELEMENT_STEP.equalsIgnoreCase(elements[j].getName())) {
					stepAdded(elements[j]);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IRegistryChangeListener#registryChanged(org.eclipse.core.runtime.IRegistryChangeEvent)
	 */
	public void registryChanged(final IRegistryChangeEvent event) {
		final IExtensionDelta[] deltas = event.getExtensionDeltas(NAMESPACE_CONFIGURATION, POINT_WIZARD);

		if (deltas.length == 0) {
			return;
		}

		// dynamically adjust the registered steps
		for (int i = 0; i < deltas.length; i++) {
			final IConfigurationElement[] elements = deltas[i].getExtension().getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				switch (deltas[i].getKind()) {
					case IExtensionDelta.ADDED:
						if (ELEMENT_STEP.equalsIgnoreCase(elements[j].getName())) {
							stepAdded(elements[j]);
						}
						break;
					case IExtensionDelta.REMOVED:
						final String stepId = elements[j].getAttribute(ATTRIBUTE_ID);
						if (stepId != null) {
							stepRemoved(stepId);
						}
						break;
				}
			}
		}
	}

	/**
	 * Removes all steps which are plugged into the extension point.
	 */
	private void removeRegisteredSteps() {
		final IExtension[] extensions = getConfigWizardExtensions();
		for (int i = 0; i < extensions.length; i++) {
			final IConfigurationElement[] elements = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				final String stepId = elements[j].getAttribute(ATTRIBUTE_ID);
				if (null != stepId) {
					stepRemoved(stepId);
				}
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
		registry.removeRegistryChangeListener(this);
		removeRegisteredSteps();
	}

}
