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
import org.eclipse.cloudfree.configuration.PlatformConfiguration;
import org.eclipse.cloudfree.configuration.preferences.PlatformScope;
import org.eclipse.cloudfree.toolkit.CWT;
import org.eclipse.cloudfree.toolkit.content.NumberContent;
import org.eclipse.cloudfree.toolkit.runtime.commands.CommandExecutionEvent;
import org.eclipse.cloudfree.toolkit.widgets.DialogFieldGroup;
import org.eclipse.cloudfree.toolkit.widgets.DialogFieldRules;
import org.eclipse.cloudfree.toolkit.widgets.NumberInput;
import org.eclipse.cloudfree.toolkit.widgets.NumberType;
import org.eclipse.cloudfree.toolkit.widgets.StyledText;
import org.eclipse.cloudfree.toolkit.wizard.WizardContainer;
import org.eclipse.cloudfree.toolkit.wizard.WizardPage;
import org.osgi.service.prefs.BackingStoreException;

public class WebServerStep extends ConfigurationWizardStep {

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 */
	public WebServerStep() {
		super("webserver");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.admin.configuration.wizard.ConfigurationWizardStep#createPages(org.eclipse.cloudfree.toolkit.wizard.WizardContainer)
	 */
	@Override
	public void createPages(final WizardContainer wizard) {
		final WizardPage webServerPage = new WizardPage("webserver", wizard);
		webServerPage.setTitle("HTTP Server");
		webServerPage.setDescription("Configure the Jetty HTTP server.");

		final StyledText text = new StyledText("webserver-intro", webServerPage, CWT.NONE);
		text.setText("The CloudFree Platform uses an embedded Jetty HTTP server. You may configure the server here.", false, false);

		final DialogFieldGroup container = new DialogFieldGroup("webserver-intro", webServerPage, CWT.NONE);
		container.setTitle("HTTP Server Port");
		container.setDescription("Change the port the server should listen on. Default is port 80.");

		final NumberInput port = new NumberInput("webserver-port", container, CWT.NONE);
		port.setLabel("Port:");
		port.setToolTipText("Change the port the server should listen on. Default is port 80.");
		port.setType(NumberType.INTEGER);
		port.setUpperLimit(new Integer(65535), false);
		port.setLowerLimit(new Integer(0), false);

		webServerPage.setContinueRule(DialogFieldRules.allFields().areValid());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.admin.configuration.wizard.ConfigurationWizardStep#wizardFinished(org.eclipse.cloudfree.toolkit.runtime.commands.CommandExecutionEvent)
	 */
	@Override
	public boolean wizardFinished(final CommandExecutionEvent finishEvent) {
		try {
			// check if production mode is selected
			final NumberContent content = (NumberContent) finishEvent.getContentSet().getEntry("webserver-port");
			if ((null != content) && (null != content.getNumber())) {
				PlatformConfiguration.getConfigurationService().putInt("org.eclipse.cloudfree.http", "port", content.getNumber().intValue(), null, false);
				new PlatformScope().getNode("org.eclipse.cloudfree.http").flush();
			}
		} catch (final IllegalStateException e) {
			// inactive
			return false;
		} catch (final BackingStoreException e) {
			// could not save preferences
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
