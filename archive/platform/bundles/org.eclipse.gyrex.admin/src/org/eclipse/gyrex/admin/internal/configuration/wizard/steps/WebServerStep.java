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
package org.eclipse.gyrex.admin.internal.configuration.wizard.steps;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.equinox.http.jetty.JettyConstants;
import org.eclipse.gyrex.admin.configuration.wizard.ConfigurationWizardStep;
import org.eclipse.gyrex.admin.internal.AdminActivator;
import org.eclipse.gyrex.configuration.preferences.PlatformScope;
import org.eclipse.gyrex.toolkit.CWT;
import org.eclipse.gyrex.toolkit.content.NumberContent;
import org.eclipse.gyrex.toolkit.runtime.commands.CommandExecutionEvent;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldGroup;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldRules;
import org.eclipse.gyrex.toolkit.widgets.NumberInput;
import org.eclipse.gyrex.toolkit.widgets.NumberType;
import org.eclipse.gyrex.toolkit.widgets.StyledText;
import org.eclipse.gyrex.toolkit.wizard.WizardContainer;
import org.eclipse.gyrex.toolkit.wizard.WizardPage;

/**
 * TODO should also configure SSL
 */
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
	 * @see org.eclipse.gyrex.admin.configuration.wizard.ConfigurationWizardStep#createPages(org.eclipse.gyrex.toolkit.wizard.WizardContainer)
	 */
	@Override
	public void createPages(final WizardContainer wizard) {
		final WizardPage webServerPage = new WizardPage("webserver", wizard);
		webServerPage.setTitle("HTTP Server");
		webServerPage.setDescription("Configure the Jetty HTTP server.");

		final StyledText text = new StyledText("webserver-intro", webServerPage, CWT.NONE);
		text.setText("Gyrex uses an embedded Jetty HTTP server. You may configure the server here.", false, false);

		final DialogFieldGroup container = new DialogFieldGroup("webserver-intro", webServerPage, CWT.NONE);
		container.setTitle("HTTP Server Port");
		container.setDescription("Change the port the server should listen on. Default is port 80.");

		final NumberInput port = new NumberInput("webserver-port", container, CWT.NONE);
		port.setLabel("Port:");
		port.setToolTipText("Change the port the server should listen on. Default is port 80.");
		port.setType(NumberType.INTEGER);
		port.setUpperLimit(new Integer(65535), false);
		port.setLowerLimit(new Integer(0), false);

		final NumberInput sslPort = new NumberInput("webserver-ssl-port", container, CWT.NONE);
		sslPort.setLabel("SSL Port:");
		sslPort.setToolTipText("Set the port the server should listen for SSL requests. Default is none.");
		sslPort.setType(NumberType.INTEGER);
		sslPort.setUpperLimit(new Integer(65535), false);
		sslPort.setLowerLimit(new Integer(0), false);

		webServerPage.setContinueRule(DialogFieldRules.allFields().areValid());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.admin.configuration.wizard.ConfigurationWizardStep#wizardFinished(org.eclipse.gyrex.toolkit.runtime.commands.CommandExecutionEvent)
	 */
	@Override
	public boolean wizardFinished(final CommandExecutionEvent finishEvent) {
		try {
			final IEclipsePreferences preferences = new PlatformScope().getNode("org.eclipse.gyrex.http.jetty");

			// configure port
			final NumberContent content = (NumberContent) finishEvent.getContentSet().getEntry("webserver-port");
			if ((null != content) && (null != content.getNumber())) {
				preferences.putInt("port", content.getNumber().intValue());
				preferences.flush();
			}

			// configure SSL
			final NumberContent sslPort = (NumberContent) finishEvent.getContentSet().getEntry("webserver-ssl-port");
			if ((null != sslPort) && (null != sslPort.getNumber())) {
				final File keystoreFileSource = new File(FileLocator.toFileURL(AdminActivator.getInstance().getBundle().getEntry("jetty-ssl-keystore")).getFile());
				final URL instanceLocation = AdminActivator.getInstance().getInstanceLocation().getURL();
				if (null == instanceLocation) {
					throw new IllegalStateException("no instance location available");
				}
				final File jettyKeystore = new Path(instanceLocation.getFile()).append("jetty-keystore").toFile();
				if (!jettyKeystore.exists()) {
					FileUtils.copyFile(keystoreFileSource, jettyKeystore);
				}
				preferences.put(JettyConstants.SSL_KEYSTORE, jettyKeystore.getAbsolutePath());
				preferences.put(JettyConstants.SSL_PASSWORD, "cloudfree");
				preferences.put(JettyConstants.SSL_KEYPASSWORD, "cloudfree");
				preferences.putInt(JettyConstants.HTTPS_PORT, sslPort.getNumber().intValue());
				preferences.flush();
			}
		} catch (final Exception e) {
			// could not save preferences
			AdminActivator.getInstance().getLog().log("Error while configuring Jetty. " + e.getMessage(), e);
			return false;
		}

		return true;
	}

}
