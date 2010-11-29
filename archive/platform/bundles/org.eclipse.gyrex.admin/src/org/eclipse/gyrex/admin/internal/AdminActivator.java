/*******************************************************************************
 * Copyright (c) 2008, 2010 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.admin.internal;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.equinox.http.jetty.JettyConfigurator;
import org.eclipse.equinox.http.jetty.JettyConstants;

import org.eclipse.gyrex.admin.internal.configuration.wizard.ConfigurationWizardAdapterFactory;
import org.eclipse.gyrex.admin.internal.configuration.wizard.ConfigurationWizardFactory;
import org.eclipse.gyrex.admin.internal.configuration.wizard.ConfigurationWizardServiceImpl;
import org.eclipse.gyrex.admin.internal.configuration.wizard.steps.ConfigModeStep;
import org.eclipse.gyrex.admin.internal.configuration.wizard.steps.WebServerStep;
import org.eclipse.gyrex.admin.internal.widgets.AdminWidgetAdapterServiceImpl;
import org.eclipse.gyrex.admin.internal.widgets.AdminWidgetServiceImpl;
import org.eclipse.gyrex.admin.setupwizard.ISetupWizardService;
import org.eclipse.gyrex.admin.widgets.IAdminWidgetAdapterService;
import org.eclipse.gyrex.admin.widgets.IAdminWidgetService;
import org.eclipse.gyrex.common.runtime.BaseBundleActivator;
import org.eclipse.gyrex.configuration.constraints.PlatformConfigurationConstraint;
import org.eclipse.gyrex.configuration.internal.impl.PlatformStatusRefreshJob;
import org.eclipse.gyrex.toolkit.runtime.lookup.RegistrationException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bundle activator for the Admin plug-in
 */
public class AdminActivator extends BaseBundleActivator {

	private final static class RestartPlatformConstraint extends PlatformConfigurationConstraint {
		@Override
		public IStatus evaluateConfiguration(final IProgressMonitor progressMonitor) {
			if (AdminActivator.getInstance().mustRestartPlatform) {
				return new Status(IStatus.ERROR, AdminActivator.SYMBOLIC_NAME, "In order to complete the initial configuration the platform must be restarted.");
			}
			return Status.OK_STATUS;
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(AdminActivator.class);

	/** the plug-in id */
	public static final String SYMBOLIC_NAME = "org.eclipse.gyrex.admin";

	/** the default port for the admin server */
	public static final int DEFAULT_ADMIN_PORT = 3110;

	/** server type for the admin server */
	public static final String TYPE_ADMIN = SYMBOLIC_NAME + ".http";

	/** the service vendor */
	private static final String DEFAULT_SERVICE_VENDOR = "Eclipse Gyrex";

	/** the service description */
	private static final String DEFAULT_SERVICE_DESCRIPTION_WIDGET_SERVICE = "Gyrex Admin Widget Service";

	/** the service description */
	private static final String DEFAULT_SERVICE_DESCRIPTION_WIDGET_ADAPTER_SERVICE = "Gyrex Admin Widget Adapter Service";

	/** the service description */
	private static final String DEFAULT_SERVICE_DESCRIPTION_CONFIG_WIZARD_SERVICE = "Gyrex Admin Configuration Wizard Service";

	/** the shared instance */
	private static AdminActivator sharedInstance;

	/**
	 * Returns the shared instance.
	 * 
	 * @return the instance
	 */
	public static AdminActivator getInstance() {
		final AdminActivator instance = sharedInstance;
		if (null == instance) {
			throw new IllegalStateException("inactive");
		}
		return instance;
	}

	/** the admin widget service */
	private AdminWidgetServiceImpl adminWidgetService;

	/** the service registration of the admin widget service */
	private ServiceRegistration adminWidgetServiceRegistration;

	/** the admin widget adapter service */
	private AdminWidgetAdapterServiceImpl adminWidgetAdapterService;

	/** the service registration of the admin widget adapter service */
	private ServiceRegistration adminWidgetAdapterServiceRegistration;

	/** tracker for the extension registry */
	private ServiceTracker<Object, Object> registryServiceTracker;

	/** the configuration wizard service */
	private ConfigurationWizardServiceImpl setupWizardService;

	/** the service registration of the configuration wizard service */
	private ServiceRegistration configurationWizardServiceRegistration;

	private final Set<String> adminApplicationBases = new CopyOnWriteArraySet<String>();
	boolean mustRestartPlatform;

	private HttpServiceTracker httpServiceTracker;

	/**
	 * Creates a new instance.
	 */
	public AdminActivator() {
		super(SYMBOLIC_NAME);
	}

	/**
	 * Sets the adminApplicationBase.
	 * 
	 * @param adminApplicationBase
	 *            the adminApplicationBase to set
	 */
	public void addAdminApplicationBase(final String adminApplicationBase) {
		adminApplicationBases.add(adminApplicationBase);
	}

	private void closeRegistryServiceTracker() {
		if (null != registryServiceTracker) {
			registryServiceTracker.close();
			registryServiceTracker = null;
		}
	}

	private Dictionary createAdminSettings() {
		final Dictionary<String, Object> settings = new Hashtable<String, Object>(4);
		settings.put(JettyConstants.OTHER_INFO, TYPE_ADMIN);
		settings.put(JettyConstants.HTTP_ENABLED, Boolean.TRUE);
		settings.put(JettyConstants.HTTP_PORT, new Integer(DEFAULT_ADMIN_PORT));
		return settings;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gyrex.common.runtime.BaseBundleActivator#doStart(org.osgi.framework.BundleContext)
	 */
	@Override
	protected void doStart(final BundleContext context) throws Exception {
		sharedInstance = this;
		startAdminWidgetService(context);
		startAdminWidgetAdapterService(context);

		startConfigurationWizardService(context);

		getServiceHelper().registerService(PlatformConfigurationConstraint.class.getName(), new RunConfigWizardConfigConstraint(), DEFAULT_SERVICE_VENDOR, "Run Config Wizard Check", null, null);
		getServiceHelper().registerService(PlatformConfigurationConstraint.class.getName(), new RestartPlatformConstraint(), DEFAULT_SERVICE_VENDOR, "Restart Platform Check", null, null);

		registerDefaultWidgetAdapters();

		openRegistryServiceTracker(context);

		// start the admin server
		JettyConfigurator.startServer("admin", createAdminSettings());

		// open the tracker
		if (null == httpServiceTracker) {
			httpServiceTracker = new HttpServiceTracker(context);
			httpServiceTracker.open();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gyrex.common.runtime.BaseBundleActivator#doStop(org.osgi.framework.BundleContext)
	 */
	@Override
	protected void doStop(final BundleContext context) throws Exception {
		closeRegistryServiceTracker();

		stopConfigurationWizardService(context);

		stopAdminWidgetAdapterService(context);
		stopAdminWidgetService(context);

		// stop the service tracker
		if (null != httpServiceTracker) {
			httpServiceTracker.close();
			httpServiceTracker = null;
		}

		// stop Jetty
		JettyConfigurator.stopServer("admin");

		sharedInstance = null;
	}

	/**
	 * Returns the admin application base
	 * 
	 * @return the admin application base
	 */
	public String getAdminApplicationBase() {
		try {
			return adminApplicationBases.iterator().next();
		} catch (final NoSuchElementException e) {
			return null;
		}
	}

	/**
	 * Returns the adminWidgetAdapterService.
	 * 
	 * @return the adminWidgetAdapterService
	 */
	public IAdminWidgetAdapterService getAdminWidgetAdapterService() {
		return adminWidgetAdapterService;
	}

	/**
	 * Returns the adminWidgetService.
	 * 
	 * @return the adminWidgetService
	 */
	public IAdminWidgetService getAdminWidgetService() {
		return adminWidgetService;
	}

	/**
	 * Returns the configurationWizardService.
	 * 
	 * @return the configurationWizardService
	 */
	public ConfigurationWizardServiceImpl getConfigurationWizardService() {
		return setupWizardService;
	}

	private void openRegistryServiceTracker(final BundleContext context) {
		registryServiceTracker = new ServiceTracker<Object, Object>(context, "org.eclipse.core.runtime.IExtensionRegistry", null) {

			@Override
			public Object addingService(final ServiceReference<Object> reference) {
				final Object service = super.addingService(reference);
				if (service != null) {
					try {
						setupWizardService.setRegistry(service);
						adminWidgetService.setRegistry(service);
						adminWidgetAdapterService.setRegistry(service);
					} catch (final Exception e) {
						LOG.error("Error while initializing extension registry helpers.", e);
					} catch (final LinkageError error) {
						// something is invalid regarding class visibility, ignore for now
						return null;
					}
				}
				return service;
			}

			@Override
			public void removedService(final ServiceReference<Object> reference, final Object service) {
				setupWizardService.setRegistry(null);

				// unget service
				super.removedService(reference, service);
			}
		};
		registryServiceTracker.open();
	}

	private void registerDefaultWidgetAdapters() throws RegistrationException {
		// TODO: allow inheritance for "restart-button"
		adminWidgetAdapterService.registerFactory(new ConfigurationWizardAdapterFactory(), ConfigurationWizardFactory.ID_CONFIGURATION_WIZARD, ConfigurationWizardFactory.ID_CONFIGURATION_WIZARD_FINISHED, "restart-button");
	}

	/**
	 * Sets the adminApplicationBase.
	 * 
	 * @param adminApplicationBase
	 *            the adminApplicationBase to set
	 */
	public void removeAdminApplicationBase(final String adminApplicationBase) {
		adminApplicationBases.remove(adminApplicationBase);
	}

	public void setShouldRestartServer(final boolean restart) {
		mustRestartPlatform = restart;
		PlatformStatusRefreshJob.scheduleRefreshIfPermitted();
	}

	public boolean shouldRestartServer() {
		return mustRestartPlatform;
	}

	private synchronized void startAdminWidgetAdapterService(final BundleContext context) {
		if (null != adminWidgetAdapterServiceRegistration) {
			return;
		}

		// create the service instance
		if (null == adminWidgetAdapterService) {
			adminWidgetAdapterService = new AdminWidgetAdapterServiceImpl();
		}

		// prepare service properties
		final Dictionary<String, Object> serviceProperties = new Hashtable<String, Object>(2);
		serviceProperties.put(Constants.SERVICE_VENDOR, DEFAULT_SERVICE_VENDOR);
		serviceProperties.put(Constants.SERVICE_DESCRIPTION, DEFAULT_SERVICE_DESCRIPTION_WIDGET_ADAPTER_SERVICE);

		// register the service
		adminWidgetAdapterServiceRegistration = context.registerService(IAdminWidgetAdapterService.class.getName(), adminWidgetAdapterService, serviceProperties);
	}

	private synchronized void startAdminWidgetService(final BundleContext context) {
		if (null != adminWidgetServiceRegistration) {
			return;
		}

		// create the service instance
		if (null == adminWidgetService) {
			adminWidgetService = new AdminWidgetServiceImpl();
		}

		// prepare service properties
		final Dictionary<String, Object> serviceProperties = new Hashtable<String, Object>(2);
		serviceProperties.put(Constants.SERVICE_VENDOR, DEFAULT_SERVICE_VENDOR);
		serviceProperties.put(Constants.SERVICE_DESCRIPTION, DEFAULT_SERVICE_DESCRIPTION_WIDGET_SERVICE);

		// register the service
		adminWidgetServiceRegistration = context.registerService(IAdminWidgetService.class.getName(), adminWidgetService, serviceProperties);
	}

	private synchronized void startConfigurationWizardService(final BundleContext context) {
		if (null != configurationWizardServiceRegistration) {
			return;
		}

		// create the service instance
		if (null == setupWizardService) {
			setupWizardService = new ConfigurationWizardServiceImpl();
		}

		// prepare service properties
		final Dictionary<String, Object> serviceProperties = new Hashtable<String, Object>(2);
		serviceProperties.put(Constants.SERVICE_VENDOR, DEFAULT_SERVICE_VENDOR);
		serviceProperties.put(Constants.SERVICE_DESCRIPTION, DEFAULT_SERVICE_DESCRIPTION_CONFIG_WIZARD_SERVICE);

		// register the service
		configurationWizardServiceRegistration = context.registerService(ISetupWizardService.class.getName(), setupWizardService, serviceProperties);

		// add our registration
		setupWizardService.addStep(new ConfigModeStep());

		// TODO: move into separate bundle shipped with Jetty only
		setupWizardService.addStep(new WebServerStep());
	}

	private synchronized void stopAdminWidgetAdapterService(final BundleContext context) {
		if (null != adminWidgetAdapterServiceRegistration) {
			adminWidgetAdapterServiceRegistration.unregister();
			adminWidgetAdapterServiceRegistration = null;
		}
		if (null != adminWidgetAdapterService) {
			adminWidgetAdapterService.shutdown();
			adminWidgetAdapterService = null;
		}
	}

	private synchronized void stopAdminWidgetService(final BundleContext context) {
		if (null != adminWidgetServiceRegistration) {
			adminWidgetServiceRegistration.unregister();
			adminWidgetServiceRegistration = null;
		}
		if (null != adminWidgetService) {
			adminWidgetService.shutdown();
			adminWidgetService = null;
		}
	}

	private synchronized void stopConfigurationWizardService(final BundleContext context) {

		if (null != configurationWizardServiceRegistration) {
			configurationWizardServiceRegistration.unregister();
			configurationWizardServiceRegistration = null;
		}
		if (null != setupWizardService) {
			setupWizardService.shutdown();
			setupWizardService = null;
		}
	}
}
