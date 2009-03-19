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
package org.eclipse.gyrex.examples.fanshop.internal;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.gyrex.configuration.PlatformConfiguration;
import org.eclipse.gyrex.configuration.preferences.PlatformScope;
import org.eclipse.gyrex.configuration.service.IConfigurationService;
import org.eclipse.gyrex.examples.fanshop.internal.app.FanShopApplicationProvider;
import org.eclipse.gyrex.examples.fanshop.internal.setup.FanShopDevSetup;
import org.eclipse.gyrex.http.application.manager.ApplicationRegistrationException;
import org.eclipse.gyrex.http.application.manager.IApplicationManager;
import org.eclipse.gyrex.http.application.manager.MountConflictException;
import org.eclipse.gyrex.http.internal.apps.dummy.RootContext;
import org.eclipse.gyrex.persistence.solr.internal.SolrActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.util.tracker.ServiceTracker;
import org.xml.sax.SAXException;

public class FanShopRTSetup {

	/** HTTP_FANS_ECLIPSE_ORG */

	private static final String REPOSITORY_ID = "fanshop.listings";

	private final BundleContext context;

	private ServiceTracker appManagerTracker;

	public FanShopRTSetup(final BundleContext context) {
		this.context = context;
	}

	public void close() {
		appManagerTracker.close();
	}

	private void configureRepositories() throws BackingStoreException {
		final IConfigurationService configurationService = PlatformConfiguration.getConfigurationService();
		configurationService.putString("org.eclipse.gyrex.persistence", "repositories//application/x-cf-listings-solr", REPOSITORY_ID, null, false);
		configurationService.putString("org.eclipse.gyrex.persistence", "repositories/" + REPOSITORY_ID + "//type", "org.eclipse.gyrex.persistence.solr.embedded", null, false);
		new PlatformScope().getNode("org.eclipse.gyrex.persistence").flush();

		configurationService.putString("org.eclipse.gyrex.cds.service.solr", "facets/style", "Style,field,style_n", null, false);
		configurationService.putString("org.eclipse.gyrex.cds.service.solr", "facets/color", "Color,field,color_n", null, false);
		configurationService.putString("org.eclipse.gyrex.cds.service.solr", "facets/source", "Merchant,field,source_n", null, false);
		configurationService.putString("org.eclipse.gyrex.cds.service.solr", "facets/size", "Size,field,size_n", null, false);
		configurationService.putString("org.eclipse.gyrex.cds.service.solr", "facets/category", "Category,field,category", null, false);
		configurationService.putString("org.eclipse.gyrex.cds.service.solr", "facets/thickness", "Thickness,field,thickness", null, false);
		configurationService.putString("org.eclipse.gyrex.cds.service.solr", "facets/fit", "Fit,field,fit", null, false);
		configurationService.putString("org.eclipse.gyrex.cds.service.solr", "facets/tags", "Tags,field,tags", null, false);
		configurationService.putString("org.eclipse.gyrex.cds.service.solr", "facets/paper", "Paper,field,paper", null, false);
		configurationService.putString("org.eclipse.gyrex.cds.service.solr", "facets/finish", "Finish,field,finish", null, false);
		configurationService.putString("org.eclipse.gyrex.cds.service.solr", "facets/price", "Price,queries,price:[* TO 10]=$10 and below;price:[10 TO 25]=$10 to $25;price:[25 TO 40]=$25 to $40;price:[40 TO *]=$40 and above", null, false);
		new PlatformScope().getNode("org.eclipse.gyrex.cds.service.solr").flush();
	}

	private void importSampleData() {
		// let's populate some default data in dev mode
		new FanShopDataImport(new RootContext()).schedule();
	}

	private void initializeSolrCore() throws URISyntaxException, IOException, SolrServerException, ParserConfigurationException, SAXException {
		// copy config and schema
		// the configuration template
		final File configTemplate = new File(FileLocator.toFileURL(FanShopActivator.getInstance().getBundle().getEntry("conf-solr")).getFile());

		// create Solr instance directory
		final File solrBase = SolrActivator.getInstance().getEmbeddedSolrBase();
		if (null == solrBase) {
			throw new IllegalStateException("no Solr base directory");
		}
		final File instanceDir = new File(solrBase, REPOSITORY_ID);
		if (!instanceDir.isDirectory()) {
			// initialize dir
			instanceDir.mkdirs();
		}

		// publish configuration
		FileUtils.copyDirectory(configTemplate, instanceDir);

		// create core or reload core
		final CoreContainer coreContainer = SolrActivator.getInstance().getEmbeddedCoreContainer();
		if (null == coreContainer) {
			throw new IllegalStateException("no coreContainer");
		}
		final SolrCore core = coreContainer.getCore(REPOSITORY_ID);
		try {
			if (null == core) {
				final SolrCore adminCore = coreContainer.getAdminCore();
				try {
					final EmbeddedSolrServer adminServer = new EmbeddedSolrServer(coreContainer, adminCore.getName());
					CoreAdminRequest.createCore(REPOSITORY_ID, REPOSITORY_ID, adminServer);
				} finally {
					adminCore.close();
				}
			} else {
				coreContainer.reload(REPOSITORY_ID);
			}
		} finally {
			if (null != core) {
				core.close();
			}
		}
	}

	private void registerFanShopApplication() {
		// register the application
		appManagerTracker = new ServiceTracker(context, IApplicationManager.class.getName(), null) {
			/* (non-Javadoc)
			 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
			 */
			@Override
			public Object addingService(final ServiceReference reference) {
				final IApplicationManager applicationManager = (IApplicationManager) super.addingService(reference);
				if (null != applicationManager) {
					try {
						applicationManager.register("fanshop", FanShopApplicationProvider.ID, new RootContext(), null);
					} catch (final ApplicationRegistrationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
						final String url = PlatformConfiguration.getConfigurationService().getString(FanShopDevSetup.PLUGIN_ID_FANSHOP, FanShopDevSetup.URL, FanShopDevSetup.DEFAULT_URL, null);
						applicationManager.mount(url, "fanshop");
					} catch (final MountConflictException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return applicationManager;
			}

			/* (non-Javadoc)
			 * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
			 */
			@Override
			public void removedService(final ServiceReference reference, final Object service) {
				final IApplicationManager applicationManager = (IApplicationManager) service;
				if (null != applicationManager) {
					final String url = PlatformConfiguration.getConfigurationService().getString(FanShopDevSetup.PLUGIN_ID_FANSHOP, FanShopDevSetup.URL, FanShopDevSetup.DEFAULT_URL, null);
					try {
						applicationManager.unmount(url);
					} catch (final IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					applicationManager.unregister("fanshop");
				}
				// unget
				super.removedService(reference, service);
			}
		};
		appManagerTracker.open();
	}

	public void runtimeSetup() throws Exception {
		initializeSolrCore();
		configureRepositories();
		importSampleData();
		registerFanShopApplication();
	}

}
