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

import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.gyrex.admin.configuration.wizard.ConfigurationWizardStep;
import org.eclipse.gyrex.admin.internal.AdminActivator;
import org.eclipse.gyrex.configuration.PlatformConfiguration;
import org.eclipse.gyrex.configuration.internal.holders.ConfigurationModeHolder;
import org.eclipse.gyrex.configuration.internal.impl.PlatformStatusRefreshJob;
import org.eclipse.gyrex.toolkit.CWT;
import org.eclipse.gyrex.toolkit.commands.Command;
import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory;
import org.eclipse.gyrex.toolkit.widgets.Button;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldRules;
import org.eclipse.gyrex.toolkit.widgets.StyledText;
import org.eclipse.gyrex.toolkit.widgets.Widget;
import org.eclipse.gyrex.toolkit.wizard.WizardContainer;
import org.eclipse.gyrex.toolkit.wizard.WizardPage;

/**
 * The Setup Wizard factory.
 */
public class ConfigurationWizardFactory implements IWidgetFactory {

	/** setup wizard "restart" command */
	public static final String CMD_RESTART = "restart";

	/** setup wizard "finish" command */
	public static final String CMD_FINISH = "finish";

	/** setup wizard widget id */
	public static final String ID_CONFIGURATION_WIZARD = "config-wizard";

	/** setup wizard finished widget */
	public static final String ID_CONFIGURATION_WIZARD_FINISHED = "config-wizard-finished";

	public static final String[] ALL_IDS = new String[] { ID_CONFIGURATION_WIZARD_FINISHED, ID_CONFIGURATION_WIZARD };

	private static void addStatus(final StringBuilder text, final IStatus status) {
		if (status.isMultiStatus()) {
			for (final IStatus childStatus : status.getChildren()) {
				addStatus(text, childStatus);
			}
			return;
		}

		if (status.isOK()) {
			return;
		}

		text.append("<li><img href=\"");
		switch (status.getSeverity()) {
			case IStatus.CANCEL:
			case IStatus.ERROR:
				text.append(AdminActivator.getInstance().getBundle().getEntry("/images/error.gif").toString());
				break;
			case IStatus.WARNING:
				text.append(AdminActivator.getInstance().getBundle().getEntry("/images/warning.gif").toString());
				break;
			case IStatus.INFO:
				text.append(AdminActivator.getInstance().getBundle().getEntry("/images/information.gif").toString());
				break;

			default:
				break;
		}
		text.append("\"/> ").append(status.getMessage()).append("<br/>");
		text.append("(").append(status.getPlugin()).append(", code ").append(status.getCode()).append(")");
		text.append("</li>");
	}

	public static void createPlatformStatusInfo(final Container parent) {
		final Container statusContainer = new Container("status", parent, CWT.NONE);
		statusContainer.setTitle("System Status");
		statusContainer.setDescription("The following system status reports are available.");

		// refresh the status and wait
		PlatformStatusRefreshJob.scheduleRefreshIfPermitted();
		try {
			PlatformStatusRefreshJob.waitForScheduledRefresh(4, TimeUnit.SECONDS);
		} catch (final InterruptedException e) {
			// reset interrupted state
			Thread.currentThread().interrupt();
		}

		final StringBuilder statusText = new StringBuilder();
		statusText.append("<text>");
		addStatus(statusText, PlatformConfiguration.getPlatformStatus());
		statusText.append("</text>");

		final StyledText status = new StyledText("text", statusContainer, CWT.NONE);
		status.setText(statusText.toString(), true, true);
	}

	private void createAdditionalPages(final WizardContainer wizard) {
		final ConfigurationWizardStep[] steps = AdminActivator.getInstance().getConfigurationWizardService().getSteps();
		for (final ConfigurationWizardStep step : steps) {
			step.createPages(wizard);
		}
	}

	private WizardPage createIntroPage(final WizardContainer wizard) {
		final WizardPage introPage = new WizardPage("overview", wizard);
		introPage.setTitle("Overview");
		introPage.setDescription("Welcome to the Platform Configuration wizard.");

		final StyledText introText = new StyledText("overview-text", introPage, CWT.NONE);
		introText.setText("This wizard will walk you through the initial configuration of Gyrex. It is shown automatically when the platform is started for the first time and whenever a new central platform component has been installed which requires initial configuration.", false, false);

		return introPage;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory#getWidget(java.lang.String, org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetEnvironment)
	 */
	public Widget getWidget(final String id, final IWidgetEnvironment environment) {
		if (ID_CONFIGURATION_WIZARD_FINISHED.equals(id)) {
			return setupWizardFinished(id, environment);
		}
		return setupWizard(id, environment);
	}

	private Widget setupWizard(final String id, final IWidgetEnvironment environment) {
		final WizardContainer wizard = new WizardContainer(ID_CONFIGURATION_WIZARD, CWT.NONE);
		wizard.setTitle("Setup Wizard");
		wizard.setDescription("The Setup Wizard helps with the initial configuration of a system.");

		createIntroPage(wizard);
		createAdditionalPages(wizard);

		wizard.setFinishCommand(new Command(CMD_FINISH, DialogFieldRules.allFields().submit()));

		return wizard;
	}

	private Widget setupWizardFinished(final String id, final IWidgetEnvironment environment) {
		final Container container = new Container(ID_CONFIGURATION_WIZARD_FINISHED, CWT.NONE);
		container.setTitle("Setup Wizard Finished");
		container.setDescription("The Setup Wizard finished configuring the system.");

		createPlatformStatusInfo(container);

		// add a restart button
		if (!ConfigurationModeHolder.isConfigurationModeInitialized() || AdminActivator.getInstance().shouldRestartServer()) {
			final Button button = new Button("restart-button", container, CWT.NONE);
			button.setLabel("Restart");
			button.setDescription("Press the button to restart the platform.");
			button.setCommand(new Command(CMD_RESTART));
		}

		return container;
	}
}
