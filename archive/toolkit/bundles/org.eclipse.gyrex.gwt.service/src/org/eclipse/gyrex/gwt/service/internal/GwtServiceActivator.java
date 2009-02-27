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

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class GwtServiceActivator implements BundleActivator, ServiceTrackerCustomizer {

	private static GwtServiceActivator instance;

	/**
	 * Returns the current active bundle instance.
	 * 
	 * @return the bundle instance (maybe <code>null</code> if inactive)
	 */
	static Bundle getBundle() {
		final GwtServiceActivator instance = GwtServiceActivator.instance;
		if (null == instance) {
			return null;
		}
		final BundleContext bundleContext = instance.context;
		if (null == bundleContext) {
			return null;
		}
		return bundleContext.getBundle();
	}

	private ServiceTracker httpServiceTracker;

	private BundleContext context;

	private Map<String, GwtServiceRegistry> registryByHttpServicePid;

	private synchronized void addHttpService(final HttpService service, final ServiceReference reference) {
		final String pid = getServiceId(reference);
		if (registryByHttpServicePid.containsKey(pid)) {
			throw new IllegalArgumentException("cannot add service twice; id " + pid);
		}

		final GwtServiceRegistry gwtServiceRegistry = new GwtServiceRegistry(context, service, reference);
		gwtServiceRegistry.open();
		registryByHttpServicePid.put(pid, gwtServiceRegistry);
	}

	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public Object addingService(final ServiceReference reference) {
		final Object service = context.getService(reference);

		if (service instanceof HttpService) {
			addHttpService((HttpService) service, reference);
		}

		return service;
	}

	private String getServiceId(final ServiceReference reference) {
		final Long pid = (Long) reference.getProperty(Constants.SERVICE_ID);
		if (null == pid) {
			throw new IllegalArgumentException("service pid must not be null");
		}
		return pid.toString();
	}

	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#modifiedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void modifiedService(final ServiceReference reference, final Object service) {
		if (service instanceof HttpService) {
			updateHttpService((HttpService) service, reference);
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void removedService(final ServiceReference reference, final Object service) {
		if (service instanceof HttpService) {
			removeHttpService((HttpService) service, reference);
		}
		context.ungetService(reference);
	}

	private synchronized void removeHttpService(final HttpService service, final ServiceReference reference) {
		final String pid = getServiceId(reference);

		// we don't be smart here but simple stop the registry and create a new
		if (registryByHttpServicePid.containsKey(pid)) {
			final GwtServiceRegistry old = registryByHttpServicePid.remove(pid);
			old.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		instance = this;
		this.context = context;

		// check state
		if ((registryByHttpServicePid != null) || (httpServiceTracker != null)) {
			throw new IllegalStateException("already started");
		}

		// create service factory
		registryByHttpServicePid = new HashMap<String, GwtServiceRegistry>(4);

		// track http services
		httpServiceTracker = new ServiceTracker(context, HttpService.class.getName(), this);
		httpServiceTracker.open();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(final BundleContext context) throws Exception {
		instance = null;
		this.context = null;

		// stop
		if (null != httpServiceTracker) {
			httpServiceTracker.close();
			httpServiceTracker = null;
		}

		// close registries
		if (null != registryByHttpServicePid) {
			for (final GwtServiceRegistry registry : registryByHttpServicePid.values()) {
				registry.close();
			}
			registryByHttpServicePid = null;
		}
	}

	private synchronized void updateHttpService(final HttpService service, final ServiceReference reference) {
		// we don't be smart here but simple stop the registry and create a new
		removeHttpService(service, reference);
		addHttpService(service, reference);
	}

}
