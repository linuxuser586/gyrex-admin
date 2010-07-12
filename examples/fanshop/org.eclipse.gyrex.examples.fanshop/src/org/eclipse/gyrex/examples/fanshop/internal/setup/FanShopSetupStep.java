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
package org.eclipse.gyrex.examples.fanshop.internal.setup;

import org.eclipse.gyrex.admin.configuration.wizard.ConfigurationWizardStep;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.context.preferences.IRuntimeContextPreferences;
import org.eclipse.gyrex.context.preferences.PreferencesUtil;
import org.eclipse.gyrex.examples.fanshop.internal.FanShopActivator;
import org.eclipse.gyrex.examples.fanshop.internal.IFanShopConstants;
import org.eclipse.gyrex.toolkit.Toolkit;
import org.eclipse.gyrex.toolkit.content.BooleanContent;
import org.eclipse.gyrex.toolkit.content.TextContent;
import org.eclipse.gyrex.toolkit.runtime.commands.CommandExecutionEvent;
import org.eclipse.gyrex.toolkit.widgets.Checkbox;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldGroup;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldRules;
import org.eclipse.gyrex.toolkit.widgets.TextInput;
import org.eclipse.gyrex.toolkit.wizard.WizardContainer;
import org.eclipse.gyrex.toolkit.wizard.WizardPage;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.osgi.service.prefs.BackingStoreException;

public class FanShopSetupStep extends ConfigurationWizardStep {

	public FanShopSetupStep() {
		super("org.eclipse.gyrex.examples.fanshop.setup");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.admin.configuration.wizard.ConfigurationWizardStep#createPages(org.eclipse.gyrex.admin.configuration.wizard.WizardContainer)
	 */
	@Override
	public void createPages(final WizardContainer wizard) {
		final WizardPage wizardPage = new WizardPage("fanshop-setup", wizard);
		wizardPage.setLabel("The Eclipse Fan Shop");
		wizardPage.setDescription("Deploy the Eclipse Fan Shop example application.");

		final DialogFieldGroup enableFanShopGroup = new DialogFieldGroup("fanshop-deployment", wizardPage, Toolkit.NONE);
		enableFanShopGroup.setLabel("Deployment");
		enableFanShopGroup.setDescription("Select whether the Fan Shop application should be deployed.");

		final Checkbox deploy = new Checkbox("fanshop-deploy", enableFanShopGroup, Toolkit.NONE);
		deploy.setLabel("Deploy the Eclipse FanShop demo application.");

		final DialogFieldGroup container = new DialogFieldGroup("fanshop-urls", wizardPage, Toolkit.NONE);
		container.setLabel("Configuration");
		container.setDescription("Configure the URL the Fan Shop application should be deployed to. Please make sure the domain name actually resolves to the machine Gyrex is running on.");
		//container.setVisibilityRule(DialogFieldRules.field(deploy).isSet());

		final TextInput urlInput = new TextInput("fanshop-url", container, Toolkit.NONE);
		urlInput.setLabel("URL:");
		urlInput.setDescription("Enter the URL the Fan Shop application should be mounted on (eg. http://localhost/). ");
		urlInput.setEnablementRule(DialogFieldRules.field(deploy).isSet());

		wizardPage.setContinueRule(DialogFieldRules.allFields().areValid());

	}

	@Override
	public IStatus wizardFinished(final CommandExecutionEvent finishEvent, final IProgressMonitor monitor) {
		final BooleanContent deploy = (BooleanContent) finishEvent.getContentSet().getEntry("fanshop-deploy");
		if ((null != deploy) && deploy.getValue()) {
			try {
				// get the context
				final IRuntimeContext eclipseBugSearchContext = FanShopActivator.getInstance().getContextRegistry().getService().get(IFanShopConstants.CONTEXT_PATH);
				final IRuntimeContextPreferences preferences = PreferencesUtil.getPreferences(eclipseBugSearchContext);

				// enable roles
				FanShopDevSetup.enableFanShopRole();

				// set the url
				final TextContent url = (TextContent) finishEvent.getContentSet().getEntry("fanshop-url");
				if (null != url) {
					final String text = url.getText();
					if ((null != text) && (text.trim().length() > 0)) {
						preferences.put(FanShopActivator.PLUGIN_ID, IFanShopConstants.KEY_URL, text, false);
						preferences.flush(FanShopActivator.PLUGIN_ID);
					}
				}
			} catch (final BackingStoreException e) {
				e.printStackTrace();
				return new Status(IStatus.ERROR, FanShopActivator.PLUGIN_ID, "Error while saving preferences. " + e.getMessage(), e);
			}
		}

		return Status.OK_STATUS;
	}

}
