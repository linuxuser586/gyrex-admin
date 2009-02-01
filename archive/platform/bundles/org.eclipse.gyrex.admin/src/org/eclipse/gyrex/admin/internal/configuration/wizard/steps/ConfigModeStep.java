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
package org.eclipse.cloudfree.admin.internal.configuration.wizard.steps;


import org.eclipse.cloudfree.admin.configuration.wizard.ConfigurationWizardStep;
import org.eclipse.cloudfree.configuration.ConfigurationMode;
import org.eclipse.cloudfree.configuration.internal.ConfigurationActivator;
import org.eclipse.cloudfree.toolkit.CWT;
import org.eclipse.cloudfree.toolkit.content.BooleanContent;
import org.eclipse.cloudfree.toolkit.runtime.commands.CommandExecutionEvent;
import org.eclipse.cloudfree.toolkit.widgets.DialogFieldGroup;
import org.eclipse.cloudfree.toolkit.widgets.DialogFieldRules;
import org.eclipse.cloudfree.toolkit.widgets.RadioButton;
import org.eclipse.cloudfree.toolkit.wizard.WizardContainer;
import org.eclipse.cloudfree.toolkit.wizard.WizardPage;

public class ConfigModeStep extends ConfigurationWizardStep {

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 */
	public ConfigModeStep() {
		super("config-mode");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.admin.configuration.wizard.ConfigurationWizardStep#createPages(org.eclipse.cloudfree.toolkit.wizard.WizardContainer)
	 */
	@Override
	public void createPages(final WizardContainer wizard) {
		final WizardPage configModePage = new WizardPage("config-mode", wizard);
		configModePage.setTitle("Configuration Mode");
		configModePage.setDescription("Select the platform configuration mode.");

		final DialogFieldGroup configModeFieldGroup = new DialogFieldGroup("configurationMode", configModePage, CWT.NONE);
		configModeFieldGroup.setTitle("Configuration Mode");
		configModeFieldGroup
				.setDescription("The platform supports operating in different configuration modes. This allows to apply different default configurations depending on the environment where the platform is operating. For example, in a development environment the platform should show more details about the occurred errors whereas in production customer friendly error messages are preferred as well as more strict security settings.");

		final RadioButton prodModeButton = new RadioButton("modeProduction", configModeFieldGroup, CWT.REQUIRED);
		prodModeButton.setLabel("Production");
		prodModeButton.setToolTipText("Select to enable production mode.");
		prodModeButton.setDescription("Select this option if the system is operating in a production like environment. It enables typical production settings like strict security, error logging with alert messaging, customer client friendly error pages and specific cluster layouts.");
		prodModeButton.setEnablementRule(DialogFieldRules.never());

		final RadioButton devModeButton = new RadioButton("modeDevelopment", configModeFieldGroup, CWT.REQUIRED);
		devModeButton.setLabel("Development");
		devModeButton.setToolTipText("Select to enable development mode.");
		devModeButton.setDescription("Select this option if the system is operating in a development like environment (eg., on your local machine). It enables relaxed security, debug logging and verbose error pages with details like stack traces and comes with additional default settings to reduce system complexity at development time.");

		configModePage.setContinueRule(DialogFieldRules.allFields().areValid());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.admin.configuration.wizard.ConfigurationWizardStep#wizardFinished(org.eclipse.cloudfree.toolkit.runtime.commands.CommandExecutionEvent)
	 */
	@Override
	public boolean wizardFinished(final CommandExecutionEvent finishEvent) {
		try {
			// check if production mode is selected
			BooleanContent content = (BooleanContent) finishEvent.getContentSet().getEntry("modeProduction");
			if ((null != content) && content.getValue()) {
				ConfigurationActivator.getInstance().persistConfigurationMode(ConfigurationMode.PRODUCTION);
			}

			// check if development mode is selected
			content = (BooleanContent) finishEvent.getContentSet().getEntry("modeDevelopment");
			if ((null != content) && content.getValue()) {
				ConfigurationActivator.getInstance().persistConfigurationMode(ConfigurationMode.DEVELOPMENT);
			}
		} catch (final IllegalStateException e) {
			// inactive
			return false;
		}

		return true;
	}

}
