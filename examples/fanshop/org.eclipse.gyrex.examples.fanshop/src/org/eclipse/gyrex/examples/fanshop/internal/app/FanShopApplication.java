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
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;

import org.eclipse.gyrex.configuration.PlatformConfiguration;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.examples.fanshop.internal.FanShopActivator;
import org.eclipse.gyrex.http.application.Application;
import org.eclipse.gyrex.http.application.context.IResourceProvider;
import org.eclipse.gyrex.persistence.solr.internal.SolrActivator;

import org.eclipse.core.runtime.CoreException;

import org.osgi.framework.Bundle;

/**
 * A fan shop application instance.
 */
public class FanShopApplication extends Application {

	private final class BundleResourceProvider implements IResourceProvider {
		private final Bundle bundle;

		public BundleResourceProvider(final Bundle bundle) {
			if (null == bundle) {
				throw new IllegalArgumentException("bundle must not be null");
			}
			this.bundle = bundle;
		}

		@Override
		public URL getResource(final String path) throws MalformedURLException {
			return bundle.getEntry(path);
		}

		@Override
		public Set getResourcePaths(final String path) {
			final Enumeration entryPaths = bundle.getEntryPaths(path);
			if (entryPaths == null) {
				return null;
			}

			final Set<String> result = new HashSet<String>();
			while (entryPaths.hasMoreElements()) {
				result.add((String) entryPaths.nextElement());
			}
			return result;
		}
	}

	FanShopApplication(final String id, final IRuntimeContext context) {
		super(id, context);
	}

	@Override
	protected void doInit() throws CoreException {
		try {
			// register the  listing servlet
			getApplicationServiceSupport().registerServlet("/", new ListingServlet(getContext()), null);
			getApplicationServiceSupport().registerServlet("/json", new JsonListingServlet(getContext()), null);

			// let's expose the Solr admin interface in dev mode
			if (PlatformConfiguration.isOperatingInDevelopmentMode()) {
				//getApplicationServiceSupport().registerServlet("/solr/admin/*.jsp", new SolrAdminJspServlet("/solr", SolrActivator.getInstance().getEmbeddedCoreContainer(), IFanShopConstants.REPOSITORY_ID), null);
				getApplicationServiceSupport().registerResources("/solr", "web", new BundleResourceProvider(FanShopActivator.getInstance().getBundle("org.apache.solr.servlet")));

				// let's expose the Solr request handler
				getApplicationServiceSupport().registerServlet("/solr/select", new SolrServlet("/solr", SolrActivator.getInstance().getEmbeddedCoreContainer(), "fanshop.listings"), null);

			}
		} catch (final ServletException e) {
			throw new CoreException(FanShopActivator.getInstance().getStatusUtil().createError(0, e.getMessage(), e));
		}

	}
}
