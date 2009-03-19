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
package org.eclipse.gyrex.examples.fanshop.internal.app;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

import javax.servlet.ServletException;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.gyrex.common.context.IContext;
import org.eclipse.gyrex.configuration.PlatformConfiguration;
import org.eclipse.gyrex.examples.fanshop.internal.FanShopActivator;
import org.eclipse.gyrex.examples.fanshop.service.IFanShopService;
import org.eclipse.gyrex.http.application.Application;
import org.eclipse.gyrex.http.application.servicesupport.IResourceProvider;
import org.eclipse.gyrex.persistence.solr.internal.SolrActivator;

/**
 * A fan shop application instance.
 */
public class FanShopApplication extends Application implements IFanShopService {

	FanShopApplication(final String id, final IContext context) {
		super(id, context);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.http.application.Application#doDestroy()
	 */
	@Override
	protected void doDestroy() {
		FanShopActivator.getInstance().stopFanShopService(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.http.application.Application#doInit()
	 */
	@Override
	protected void doInit() throws CoreException {
		FanShopActivator.getInstance().startFanShopService(this);

		try {
			// register the  listing servlet
			getApplicationServiceSupport().registerServlet("/", new ListingServlet(getContext()), null);
			getApplicationServiceSupport().registerServlet("/json", new JsonListingServlet(getContext()), null);

			// let's expose the Solr admin interface in dev mode
			if (PlatformConfiguration.isOperatingInDevelopmentMode()) {
				getApplicationServiceSupport().registerServlet("/solr/admin/*.jsp", new SolrAdminJspServlet("/solr", SolrActivator.getInstance().getEmbeddedCoreContainer(), "fanshop.listings"), null);
				getApplicationServiceSupport().registerResources("/solr", "web", new IResourceProvider() {

					@Override
					public String getMimeType(final String path) {
						// let the container handle mime types
						return null;
					}

					@Override
					public URL getResource(final String path) throws MalformedURLException {
						return FanShopActivator.getInstance().getBundle("org.apache.solr.servlet").getEntry(path);
					}

					@Override
					public Set getResourcePaths(final String path) {
						return Collections.emptySet();
					}
				});

				// let's expose the Solr request handler
				getApplicationServiceSupport().registerServlet("/solr/select", new SolrServlet("/solr", SolrActivator.getInstance().getEmbeddedCoreContainer(), "fanshop.listings"), null);

			}
		} catch (final ServletException e) {
			throw new CoreException(FanShopActivator.getInstance().getStatusUtil().createError(0, e.getMessage(), e));
		}

	}
}
