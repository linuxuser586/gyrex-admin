/*******************************************************************************
 * Copyright (c) 2006, 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package org.eclipse.gyrex.admin.web.rap.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.http.registry.HttpContextExtensionService;
import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.resources.JsConcatenator;
import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.resources.IResourceManager;
import org.eclipse.rwt.resources.IResourceManagerFactory;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

final class ResourceManagerFactoryHack implements IResourceManagerFactory {

	private final class HttpContextWrapper implements HttpContext {
		private final HttpContext context;

		private HttpContextWrapper(final HttpContext context) {
			Assert.isNotNull(context);
			this.context = context;
		}

		public String getMimeType(final String name) {
			return context.getMimeType(name);
		}

		public URL getResource(final String name) {
			URL result = null;
			try {
				// Preliminary fix for bug 268759
				// 268759: ResourceManager handles non-existing resources incorrectly
				final File file = new File(name);
				if (file.exists() && !file.isDirectory()) {
					result = new URL("file", "", name);
				}
			} catch (final MalformedURLException shouldNotHappen) {
				throw new RuntimeException(shouldNotHappen);
			}
			return result;
		}

		public boolean handleSecurity(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
			return true;
		}
	}

	private final class ResourceManagerWrapper implements IResourceManager, Adaptable {
		private final IResourceManager resourceManager;

		private ResourceManagerWrapper(final IResourceManager internal) {
			resourceManager = internal;
			final Adaptable adaptable = (Adaptable) resourceManager;
			final JsConcatenator jsConcatenator = (JsConcatenator) adaptable.getAdapter(JsConcatenator.class);
			jsConcatenator.startJsConcatenation();
		}

		public Object getAdapter(final Class adapter) {
			return ((Adaptable) resourceManager).getAdapter(adapter);
		}

		public String getCharset(final String name) {
			return resourceManager.getCharset(name);
		}

		public ClassLoader getContextLoader() {
			return resourceManager.getContextLoader();
		}

		private HttpContext getHttpContext() {
			final String contextExtension = HttpContextExtensionService.class.getName();
			final BundleContext context = WorkbenchPlugin.getDefault().getBundleContext();
			final ServiceReference ref = context.getServiceReference(contextExtension);
			final HttpContextExtensionService service = (HttpContextExtensionService) context.getService(ref);
			final String id = ID_HTTP_CONTEXT;
			HttpContext result = null;
			if (httpServiceRef != null) {
				result = service.getHttpContext(httpServiceRef, id);
			} else {
				throw new IllegalStateException("HttpService is not available.");
			}
			return result;
		}

		public String getLocation(final String name) {
			return resourceManager.getLocation(name);
		}

		public InputStream getRegisteredContent(final String name) {
			return resourceManager.getRegisteredContent(name);
		}

		public URL getResource(final String name) {
			return resourceManager.getResource(name);
		}

		public InputStream getResourceAsStream(final String name) {
			return resourceManager.getResourceAsStream(name);
		}

		public Enumeration getResources(final String name) throws IOException {
			return resourceManager.getResources(name);
		}

		public boolean isRegistered(final String name) {
			return resourceManager.isRegistered(name);
		}

		public void register(final String name) {
			resourceManager.register(name);
			registerAtHttpService(name);
		}

		public void register(final String name, final InputStream is) {
			resourceManager.register(name, is);
			registerAtHttpService(name);
		}

		public void register(final String name, final InputStream is, final String charset, final RegisterOptions options) {
			resourceManager.register(name, is, charset, options);
			registerAtHttpService(name);
		}

		public void register(final String name, final String charset) {
			resourceManager.register(name, charset);
			registerAtHttpService(name);
		}

		public void register(final String name, final String charset, final RegisterOptions options) {
			resourceManager.register(name, charset, options);
			registerAtHttpService(name);
		}

		private void registerAtHttpService(final String name) {
			final String contextRoot = ContextProvider.getWebAppBase();
			final IPath path = new Path(name).removeLastSegments(1);
			final IPath location = new Path(contextRoot).append(path);
			if (httpService != null) {
				final HttpContext httpContext = getHttpContext();
				final HttpContext wrapper = new HttpContextWrapper(httpContext);
				try {
					httpService.registerResources("/" + path.toString(), location.toString(), wrapper);
				} catch (final NamespaceException ignore) {
					// TODO: [fappel] for the first shot we simply ignore the exception
					//                that's thrown if we register an alias twice. A better
					//                approach could be to take track of the namespaces that
					//                have already been registered
				}
			}
		}

		public void setContextLoader(final ClassLoader classLoader) {
			resourceManager.setContextLoader(classLoader);
		}
	}

	public static final String ID_HTTP_CONTEXT = "org.eclipse.rap.httpcontext";
	final HttpService httpService;
	final ServiceReference httpServiceRef;

	ResourceManagerFactoryHack(final HttpService httpService, final ServiceReference httpServiceRef) {
		this.httpService = httpService;
		this.httpServiceRef = httpServiceRef;
	}

	public IResourceManager create() {
		return new ResourceManagerWrapper(ResourceManagerImpl.getInstance());
	}
}
