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
package org.eclipse.cloudfree.admin.internal.widgets;


import org.eclipse.cloudfree.admin.internal.AdminActivator;
import org.eclipse.cloudfree.admin.internal.RunConfigWizardConfigConstraint;
import org.eclipse.cloudfree.admin.internal.configuration.wizard.ConfigurationWizardFactory;
import org.eclipse.cloudfree.admin.widgets.IAdminWidgetService;
import org.eclipse.cloudfree.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.cloudfree.toolkit.runtime.lookup.BaseWidgetRegistry;
import org.eclipse.cloudfree.toolkit.runtime.lookup.RegistrationException;
import org.eclipse.cloudfree.toolkit.widgets.Widget;

/**
 * The {@link IAdminWidgetService} implementation.
 */
public class AdminWidgetServiceImpl extends BaseWidgetRegistry implements IAdminWidgetService {

	/** the setup wizard factory */
	private final ConfigurationWizardFactory configurationWizardFactory = new ConfigurationWizardFactory();

	/**
	 * Creates a new instance.
	 */
	public AdminWidgetServiceImpl() {
		super();

		// register our factories
		try {
			registerFactory(configurationWizardFactory, ConfigurationWizardFactory.ALL_IDS);
		} catch (final RegistrationException e) {
			throw new IllegalStateException("implementation error; registration failed: " + e.getMessage());
		}

		setDefaultFactory(new DefaultAdminWidgetFactory());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.runtime.lookup.BaseRegistry#clear()
	 */
	@Override
	public void clear() {
		super.clear();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.runtime.lookup.BaseWidgetRegistry#getWidget(java.lang.String, org.eclipse.cloudfree.toolkit.runtime.IWidgetEnvironment)
	 */
	@Override
	public Widget getWidget(final String id, final IWidgetEnvironment environment) {
		// run setup wizard if required
		if (RunConfigWizardConfigConstraint.shouldBringUpSetupWizard()) {
			return configurationWizardFactory.getWidget(ConfigurationWizardFactory.ID_CONFIGURATION_WIZARD, environment);
		}

		// show reboot notice if necessary
		if (AdminActivator.getInstance().shouldRestartServer()) {
			return configurationWizardFactory.getWidget(ConfigurationWizardFactory.ID_CONFIGURATION_WIZARD_FINISHED, environment);
		}

		// lookup from registry
		return super.getWidget(id, environment);
	}
}
