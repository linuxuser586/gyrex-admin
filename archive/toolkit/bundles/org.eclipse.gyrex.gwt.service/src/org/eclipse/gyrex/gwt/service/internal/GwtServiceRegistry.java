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

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import org.eclipse.cloudfree.gwt.service.GwtService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;

public class GwtServiceRegistry implements ServiceFactory {

	private final BundleContext context;
	private List<GwtServiceImpl> services;
	private final HttpService httpService;
	private ServiceRegistration serviceRegistration;
	private final ServiceReference reference;

	private final ConcurrentMap<String, GwtHttpContext> contextByModuleId = new ConcurrentHashMap<String, GwtHttpContext>();

	/**
	 * Creates a new instance.
	 * 
	 * @param context
	 * @param service
	 * @param reference
	 */
	GwtServiceRegistry(final BundleContext context, final HttpService service, final ServiceReference reference) {
		this.context = context;
		httpService = service;
		this.reference = reference;
	}

	/**
	 * Closes the registry.
	 * <p>
	 * This will stop all created services and unregister the service factory.
	 * </p>
	 */
	synchronized void close() {
		// unregister service factory
		if (null != serviceRegistration) {
			serviceRegistration.unregister();
			serviceRegistration = null;
		}

		// stop created services
		if (null != services) {
			for (final GwtServiceImpl service : services) {
				service.stop();
			}
			services = null;
		}
	}

	GwtHttpContext getModuleContext(final String moduleId) {
		return contextByModuleId.get(moduleId);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.ServiceFactory#getService(org.osgi.framework.Bundle, org.osgi.framework.ServiceRegistration)
	 */
	@Override
	public synchronized Object getService(final Bundle bundle, final ServiceRegistration registration) {
		final GwtServiceImpl service = new GwtServiceImpl(this, bundle, httpService);
		if (services == null) {
			services = new ArrayList<GwtServiceImpl>();
		}
		services.add(service);
		return service;
	}

	/**
	 * Opens the registry.
	 * <p>
	 * This will register a service factory.
	 * </p>
	 */
	synchronized void open() {
		if (null != serviceRegistration) {
			throw new IllegalStateException("already open");
		}

		// properties
		final Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(Constants.SERVICE_PID, "org.eclipse.cloudfree.gwt.service." + reference.getProperty(Constants.SERVICE_ID));
		properties.put(Constants.SERVICE_VENDOR, "CloudFree.net");
		properties.put(Constants.SERVICE_DESCRIPTION, "CloudFree GWT Service for registering GWT modules and remote services");
		final String[] httpServicePropertyKeys = reference.getPropertyKeys();
		for (final String key : httpServicePropertyKeys) {
			properties.put(GwtService.HTTP_SERVICE_PROPERTY_PREFIX.concat(key), reference.getProperty(key));
		}

		// register service factory
		serviceRegistration = context.registerService(GwtService.class.getName(), this, properties);
	}

	public void setModuleContext(final String moduleId, final GwtHttpContext gwtContext) {
		contextByModuleId.put(moduleId, gwtContext);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.ServiceFactory#ungetService(org.osgi.framework.Bundle, org.osgi.framework.ServiceRegistration, java.lang.Object)
	 */
	@Override
	public synchronized void ungetService(final Bundle bundle, final ServiceRegistration registration, final Object service) {
		final GwtServiceImpl gwtService = (GwtServiceImpl) service;
		// remove (note, can be null if already stopped globally)
		if (null != services) {
			services.remove(gwtService);
		}

		// stop service
		gwtService.stop();
	}
}
