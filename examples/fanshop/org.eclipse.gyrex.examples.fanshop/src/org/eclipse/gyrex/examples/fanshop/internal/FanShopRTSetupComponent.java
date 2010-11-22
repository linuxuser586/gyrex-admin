/**
 * Copyright (c) 2009 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package org.eclipse.gyrex.examples.fanshop.internal;

import java.io.File;

import org.eclipse.gyrex.cds.facets.IFacet;
import org.eclipse.gyrex.cds.facets.IFacetManager;
import org.eclipse.gyrex.cds.query.FacetSelectionStrategy;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.context.preferences.IRuntimeContextPreferences;
import org.eclipse.gyrex.context.registry.IRuntimeContextRegistry;
import org.eclipse.gyrex.examples.fanshop.internal.app.FanShopApplicationProvider;
import org.eclipse.gyrex.http.application.manager.IApplicationManager;
import org.eclipse.gyrex.model.common.ModelException;
import org.eclipse.gyrex.model.common.ModelUtil;
import org.eclipse.gyrex.persistence.solr.internal.SolrActivator;
import org.eclipse.gyrex.preferences.PlatformScope;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import org.osgi.service.component.ComponentContext;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FanShopRTSetupComponent {

	private static final Logger LOG = LoggerFactory.getLogger(FanShopRTSetupComponent.class);

	private IApplicationManager applicationManager;
	private IRuntimeContextRegistry contextRegistry;
	private String url;
	private IRuntimeContext eclipseFanShopContext;

	protected void activate(final ComponentContext context) {
		LOG.trace("FanShopRTSetupComponent activation triggered");

		contextRegistry = (IRuntimeContextRegistry) context.locateService("IRuntimeContextRegistry");
		applicationManager = (IApplicationManager) context.locateService("IApplicationManager");

		eclipseFanShopContext = contextRegistry.get(IFanShopConstants.CONTEXT_PATH);
		if (null == eclipseFanShopContext) {
			LOG.error("Eclipse bug search context not found!");
			return;
		}

		// lookup the configured URL
		final IRuntimeContextPreferences preferences = eclipseFanShopContext.getPreferences();
		url = preferences.get(FanShopActivator.PLUGIN_ID, IFanShopConstants.KEY_URL, null);
		if (null == url) {
			LOG.debug("No URL configured for the fan shop, not registering the application");
			return;
		}

		// create the Solr index if necessary & initialize it
		try {
			initializeSolrCore();
		} catch (final Exception e) {
			LOG.error("Error while creating the Solr index: " + e.getMessage(), e);
			return;
		}

		// define the repository
		final IEclipsePreferences repositoryStore = new PlatformScope().getNode("org.eclipse.gyrex.persistence");
		repositoryStore.node("repositories/" + IFanShopConstants.REPOSITORY_ID).put("type", "org.eclipse.gyrex.persistence.solr.embedded");
		try {
			repositoryStore.flush();
		} catch (final Exception e) {
			LOG.error("Error while flushing preferences after defining repository: " + e, e);
			return;
		}

		// configure the repository which the application should use
		preferences.put("org.eclipse.gyrex.persistence", "repositories//application/x-cf-listings-solr", IFanShopConstants.REPOSITORY_ID, false);
		try {
			preferences.flush("org.eclipse.gyrex.persistence");
		} catch (final Exception e) {
			LOG.error("Error while flushing preferences after setting repositories: " + e, e);
			return;
		}

		// configure the facets
		try {
			final IFacetManager facetManager = ModelUtil.getManager(IFacetManager.class, eclipseFanShopContext);
			preferences.put("org.eclipse.gyrex.cds.service.solr", "activeFacets", ",,,,,,,,,,", false);
			createFacet(facetManager, "style", "Style", true);
			createFacet(facetManager, "color", "Color", true);
			createFacet(facetManager, "source", "Merchant", true);
			createFacet(facetManager, "size", "Size", true);
			createFacet(facetManager, "category", "Category", true);
			createFacet(facetManager, "thickness", "Thickness", true);
			createFacet(facetManager, "fit", "Fit", true);
			createFacet(facetManager, "tags", "Tags", true);
			createFacet(facetManager, "paper", "Paper", true);
			createFacet(facetManager, "finish", "Finish", true);
			createFacet(facetManager, "price", "Price", false);
		} catch (final Exception e) {
			LOG.error("Error while flushing preferences after configuring the facets: " + e, e);
			return;
		}

		// define & mount the Eclipse FanShop application
		try {
			applicationManager.register(IFanShopConstants.APPLICATION_ID, FanShopApplicationProvider.ID, eclipseFanShopContext, null);
			applicationManager.mount(url, IFanShopConstants.APPLICATION_ID);
		} catch (final Exception e) {
			LOG.error("Error while registering the bugsearch application", e);
			return;
		}

		// let's populate some default data in dev mode
		new FanShopDataImport(eclipseFanShopContext).schedule(1000);
	}

	private void createFacet(final IFacetManager facetManager, final String attributeId, final String name, final boolean enabled) throws ModelException {
		final IFacet facet = facetManager.create(attributeId);
		facet.setName(name);
		facet.setSelectionStrategy(FacetSelectionStrategy.MULTI);
		facet.setEnabled(enabled);
		facetManager.save(facet);
	}

	protected void deactivate(final ComponentContext context) {
		LOG.trace("FanShopRTSetupComponent de-activation triggered");

		// unmount the application
		try {
			if (null != url) {
				applicationManager.unmount(url);
			}
			applicationManager.unregister(IFanShopConstants.APPLICATION_ID);
		} catch (final Exception e) {
			LOG.error("Error while un-registering the bugsearch application", e);
		}

		// release references
		url = null;
		applicationManager = null;
		contextRegistry = null;
	}

	@SuppressWarnings("restriction")
	private void initializeSolrCore() throws Exception {
		// copy config and schema
		// the configuration template
		final File configTemplate = new File(FileLocator.toFileURL(FanShopActivator.getInstance().getBundle().getEntry("conf-solr")).getFile());

		// create Solr instance directory
		final File solrBase = SolrActivator.getInstance().getEmbeddedSolrBase();
		if (null == solrBase) {
			throw new IllegalStateException("no Solr base directory");
		}
		final File instanceDir = new File(solrBase, IFanShopConstants.REPOSITORY_ID);
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
		final SolrCore core = coreContainer.getCore(IFanShopConstants.REPOSITORY_ID);
		try {
			if (null == core) {
				final SolrCore adminCore = coreContainer.getAdminCore();
				try {
					final EmbeddedSolrServer adminServer = new EmbeddedSolrServer(coreContainer, adminCore.getName());
					CoreAdminRequest.createCore(IFanShopConstants.REPOSITORY_ID, IFanShopConstants.REPOSITORY_ID, adminServer);
				} finally {
					adminCore.close();
				}
			} else {
				coreContainer.reload(IFanShopConstants.REPOSITORY_ID);
			}
		} finally {
			if (null != core) {
				core.close();
			}
		}
	}

}