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
package org.eclipse.gyrex.configuration.internal;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.gyrex.common.runtime.BaseBundleActivator;
import org.eclipse.gyrex.common.services.IServiceProxy;
import org.eclipse.gyrex.configuration.service.IConfigurationService;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.preferences.IPreferencesService;

import org.osgi.framework.BundleContext;

/**
 * Activates the <code>org.eclipse.gyrex.configuration</code> plug-in.
 * <p>
 * This plug-in is split into an implementation part and an API part. The API
 * part registers an extension with the extension registry. It is therefore
 * bound to the "singleton" limitation of the extension registry.
 * </p>
 * <p>
 * Additionally, this plug-in initializes the configuration mode, which is
 * static within the JVM session and should not be changed for an instance once
 * it has been configured.
 * </p>
 */
public class ConfigurationActivator extends BaseBundleActivator {

	private static final String DEVELOPMENT = "development";
	private static final String PRODUCTION = "production";

	/** PLUGIN_ID */
	public static final String PLUGIN_ID = "org.eclipse.gyrex.configuration";

	/** the shared instance */
	private static final AtomicReference<ConfigurationActivator> sharedInstance = new AtomicReference<ConfigurationActivator>();

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 * @throws IllegalStateException
	 *             is the bundle has not been started yet.
	 */
	public static ConfigurationActivator getInstance() throws IllegalStateException {
		final ConfigurationActivator activator = sharedInstance.get();
		if (null == activator) {
			throw new IllegalStateException("The configuration bundle has not been started yet.");
		}
		return activator;
	}

	private final AtomicReference<IServiceProxy<IPreferencesService>> preferencesServiceRef = new AtomicReference<IServiceProxy<IPreferencesService>>();
	private final AtomicReference<IServiceProxy<IConfigurationService>> configurationServiceRef = new AtomicReference<IServiceProxy<IConfigurationService>>();

	/**
	 * Creates a new instance.
	 */
	public ConfigurationActivator() {
		super(PLUGIN_ID);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gyrex.common.runtime.BaseBundleActivator#doStart(org.osgi.framework.BundleContext)
	 */
	@Override
	protected void doStart(final BundleContext context) throws Exception {
		sharedInstance.set(this);

		// track the preferences service
		preferencesServiceRef.set(getServiceHelper().trackService(IPreferencesService.class));

		// track configuration service
		configurationServiceRef.set(getServiceHelper().trackService(IConfigurationService.class));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gyrex.common.runtime.BaseBundleActivator#doStop(org.osgi.framework.BundleContext)
	 */
	@Override
	protected void doStop(final BundleContext context) throws Exception {
		sharedInstance.set(null);
		preferencesServiceRef.set(null);
		configurationServiceRef.set(null);
	}

	public IConfigurationService getConfigurationService() {
		final IServiceProxy<IConfigurationService> configurationService = configurationServiceRef.get();
		if (null == configurationService) {
			throw createBundleInactiveException();
		}
		return configurationService.getService();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.common.runtime.BaseBundleActivator#getDebugOptions()
	 */
	@Override
	protected Class getDebugOptions() {
		return ConfigDebug.class;
	}

	/**
	 * Returns the instance state file.
	 * 
	 * @return the instance state file (maybe <code>null</code>)
	 */
	private File getInstanceStateFile() {
		// we use "platform:/meta" URL to resolve the instance location ("workspace/.metadata/bundle")
		URL instanceStateUrl;
		try {
			instanceStateUrl = new URL("platform:/meta/".concat(PLUGIN_ID).concat("/configurationMode.state"));
		} catch (final MalformedURLException e1) {
			// should not happen because the URL is hard coded during development
			return null;
		}

		// resolve url
		try {
			instanceStateUrl = FileLocator.toFileURL(instanceStateUrl);
			// check if resolve was ok
			if (!instanceStateUrl.getProtocol().equals("file")) {
				return null;
			}
		} catch (final IOException e1) {
			// TODO consider logging this
			return null;
		}
		return new File(instanceStateUrl.getFile());
	}

	/**
	 * Returns the Eclipse preferences service.
	 * 
	 * @return the Eclipse preferences service
	 * @throws IllegalStateException
	 *             the the preferences service is not available or the plug-in
	 *             has not been started yet
	 */
	public IPreferencesService getPreferencesService() throws IllegalStateException {
		// get the preferences service
		final IServiceProxy<IPreferencesService> service = preferencesServiceRef.get();
		if (null == service) {
			throw new IllegalStateException("The Eclipse preferences service is not available.");
		}

		// return the service instance
		return service.getService();
	}
}
