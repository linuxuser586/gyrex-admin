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
package org.eclipse.cloudfree.gwt.service.internal;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletException;


import org.eclipse.cloudfree.gwt.service.GwtRequestResponseListener;
import org.eclipse.cloudfree.gwt.service.GwtService;
import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.google.gwt.user.client.rpc.RemoteService;

public class GwtServiceImpl implements GwtService {

	private final GwtServiceRegistry serviceRegistry;
	private final Bundle bundle;
	private final HttpService httpService;
	private final Set<String> aliases = new HashSet<String>(2);
	private boolean stopped;

	/**
	 * Creates a new instance.
	 * 
	 * @param serviceRegistry
	 * @param bundle
	 * @param httpService
	 */
	GwtServiceImpl(final GwtServiceRegistry serviceRegistry, final Bundle bundle, final HttpService httpService) {
		this.serviceRegistry = serviceRegistry;
		this.bundle = bundle;
		this.httpService = httpService;
	}

	/**
	 * 
	 */
	private void checkStopped() {
		if (stopped) {
			throw new IllegalStateException("service stopped");
		}
	}

	/**
	 * Returns the bundle.
	 * 
	 * @return the bundle
	 */
	public Bundle getBundle() {
		return bundle;
	}

	GwtHttpContext getModuleContext(final String moduleId) {
		return serviceRegistry.getModuleContext(moduleId);
	}

	@Override
	public synchronized void registerModule(final String alias, final String moduleId, final String baseName, final String defaultName, final HttpContext context) throws NamespaceException {
		checkStopped();
		// check required arguments
		if (alias == null) {
			throw new IllegalArgumentException("alias must not be null");
		}
		if (moduleId == null) {
			throw new IllegalArgumentException("moduleId must not be null");
		}
		if (baseName == null) {
			throw new IllegalArgumentException("baseName must not be null");
		}

		// create the context
		final GwtHttpContext gwtContext = new GwtHttpContext(this, moduleId, context, baseName, defaultName);

		// register resource
		httpService.registerResources(alias, baseName, gwtContext);

		// remember context
		serviceRegistry.setModuleContext(moduleId, gwtContext);
	}

	@Override
	public synchronized void registerRemoteService(final String alias, final String moduleId, final RemoteService service, final GwtRequestResponseListener requestResponseListener, final HttpContext context) throws ServletException, NamespaceException {
		checkStopped();

		// check required arguments
		if (alias == null) {
			throw new IllegalArgumentException("alias must not be null");
		}
		if (moduleId == null) {
			throw new IllegalArgumentException("moduleId must not be null");
		}
		if (service == null) {
			throw new IllegalArgumentException("service must not be null");
		}

		// create the context
		final GwtHttpContext gwtContext = new GwtHttpContext(this, moduleId, context);

		// create the servlet
		final OSGiRemoteServiceServlet servlet = new OSGiRemoteServiceServlet(this, moduleId, service, requestResponseListener);

		// register servlet
		httpService.registerServlet(alias, servlet, null, gwtContext);

		// remember registered aliases
		aliases.add(alias);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.gwt.service.GwtService#registerServlet(java.lang.String, java.lang.String, javax.servlet.Servlet, java.util.Dictionary, org.osgi.service.http.HttpContext)
	 */
	@Override
	public void registerServlet(final String alias, final String moduleId, final Servlet servlet, final Dictionary initparams, final HttpContext context) throws ServletException, NamespaceException {
		checkStopped();

		// check required arguments
		if (alias == null) {
			throw new IllegalArgumentException("alias must not be null");
		}
		if (moduleId == null) {
			throw new IllegalArgumentException("moduleId must not be null");
		}
		if (servlet == null) {
			throw new IllegalArgumentException("servlet must not be null");
		}

		// create the context
		final GwtHttpContext gwtContext = new GwtHttpContext(this, moduleId, context);

		// register servlet
		httpService.registerServlet(alias, servlet, initparams, gwtContext);

		// remember registered aliases
		aliases.add(alias);
	}

	/**
	 * Stops the service
	 */
	synchronized void stop() {
		for (final String alias : aliases) {
			unregister(alias);
		}
		stopped = true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.gwt.service.GwtService#unregister(java.lang.String)
	 */
	@Override
	public synchronized void unregister(final String alias) {
		checkStopped();
		try {
			httpService.unregister(alias);
		} finally {
			aliases.remove(alias);
		}
	}

}
