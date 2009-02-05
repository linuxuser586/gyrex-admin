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
package org.eclipse.cloudfree.examples.fanshop.internal.setup;

import org.eclipse.cloudfree.admin.configuration.wizard.ConfigurationWizardStep;
import org.eclipse.cloudfree.toolkit.CWT;
import org.eclipse.cloudfree.toolkit.content.BooleanContent;
import org.eclipse.cloudfree.toolkit.content.TextContent;
import org.eclipse.cloudfree.toolkit.runtime.commands.CommandExecutionEvent;
import org.eclipse.cloudfree.toolkit.widgets.Checkbox;
import org.eclipse.cloudfree.toolkit.widgets.DialogFieldGroup;
import org.eclipse.cloudfree.toolkit.widgets.DialogFieldRules;
import org.eclipse.cloudfree.toolkit.widgets.TextInput;
import org.eclipse.cloudfree.toolkit.wizard.WizardContainer;
import org.eclipse.cloudfree.toolkit.wizard.WizardPage;
import org.osgi.service.prefs.BackingStoreException;

public class FanShopSetupStep extends ConfigurationWizardStep {

	public FanShopSetupStep() {
		super("org.eclipse.cloudfree.examples.fanshop.setup");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.admin.configuration.wizard.ConfigurationWizardStep#createPages(org.eclipse.cloudfree.admin.configuration.wizard.WizardContainer)
	 */
	@Override
	public void createPages(final WizardContainer wizard) {
		final WizardPage wizardPage = new WizardPage("fanshop-setup", wizard);
		wizardPage.setTitle("The Eclipse Fan Shop");
		wizardPage.setDescription("Deploy the Eclipse Fan Shop example application.");

		final DialogFieldGroup enableFanShopGroup = new DialogFieldGroup("fanshop-deployment", wizardPage, CWT.NONE);
		enableFanShopGroup.setTitle("Deployment");
		enableFanShopGroup.setDescription("Select whether the Fan Shop application should be deployed.");

		final Checkbox deploy = new Checkbox("fanshop-deploy", enableFanShopGroup, CWT.NONE);
		deploy.setLabel("Deploy the Eclipse FanShop demo application.");

		final DialogFieldGroup container = new DialogFieldGroup("fanshop-urls", wizardPage, CWT.NONE);
		container.setTitle("Configuration");
		container.setDescription("Configure the URL the Fan Shop application should be deployed to. Please make sure the domain name actually resolves to the machine CloudFree is running on.");
		//container.setVisibilityRule(DialogFieldRules.field(deploy).isSet());

		final TextInput urlInput = new TextInput("fanshop-url", container, CWT.NONE);
		urlInput.setLabel("URL:");
		urlInput.setDescription("Enter the URL the Fan Shop application should be mounted on (eg. http://localhost/). ");
		urlInput.setEnablementRule(DialogFieldRules.field(deploy).isSet());

		wizardPage.setContinueRule(DialogFieldRules.allFields().areValid());

	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.admin.configuration.wizard.ConfigurationWizardStep#wizardFinished(org.eclipse.cloudfree.admin.configuration.wizard.CommandExecutionEvent)
	 */
	@Override
	public boolean wizardFinished(final CommandExecutionEvent finishEvent) {
		final BooleanContent deploy = (BooleanContent) finishEvent.getContentSet().getEntry("fanshop-deploy");
		if ((null != deploy) && deploy.getValue()) {
			try {
				FanShopDevSetup.enableFanShopRole();
				final TextContent url = (TextContent) finishEvent.getContentSet().getEntry("fanshop-url");
				if (null != url) {
					final String text = url.getText();
					if ((null != text) && (text.trim().length() > 0)) {
						FanShopDevSetup.setFanShopUrl(text.trim());
					}
				}
			} catch (final BackingStoreException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

}
