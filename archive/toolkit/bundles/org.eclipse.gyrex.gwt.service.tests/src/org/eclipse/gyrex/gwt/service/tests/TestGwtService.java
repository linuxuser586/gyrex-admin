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
package org.eclipse.cloudfree.gwt.service.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.InetAddress;
import java.util.Dictionary;
import java.util.Hashtable;


import org.eclipse.cloudfree.gwt.service.GwtService;
import org.eclipse.equinox.http.jetty.JettyConfigurator;
import org.eclipse.equinox.http.jetty.JettyConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

public class TestGwtService {

	private static final String TESTSERVER = "testserver";

	private BundleContext getContext() {
		final Activator activator = Activator.getDefault();
		if (null == activator) {
			fail("test bundle is not started");
		}
		final BundleContext context = activator.getContext();
		if (context == null) {
			fail("test bundle context is not available");
		}
		return context;
	}

	@Before
	public void setUp() throws Exception {
		// make sure that we have one service running
		final Dictionary<String, Object> serverSettings = new Hashtable<String, Object>();
		serverSettings.put(JettyConstants.HTTP_ENABLED, new Boolean(true));
		serverSettings.put(JettyConstants.HTTP_HOST, InetAddress.getLocalHost().getHostAddress());
		serverSettings.put(JettyConstants.HTTP_PORT, new Integer(25001));
		serverSettings.put(JettyConstants.OTHER_INFO, TestGwtService.class.getName());
		JettyConfigurator.startServer(TESTSERVER, serverSettings);
	}

	@After
	public void tearDown() throws Exception {
		JettyConfigurator.stopServer(TESTSERVER);
	}

	@Test
	public void testGwtServiceRegistration() {
		final ServiceTracker tracker = new ServiceTracker(getContext(), GwtService.class.getName(), null);
		tracker.open();

		final int trackingCount = tracker.getTrackingCount();
		assertTrue("At least one service must be tracked here!", trackingCount >= 1);

		tracker.close();
	}

	@Test
	public void testGwtServiceRegistrationWithFilter() {
		final BundleContext context = getContext();
		Filter filter = null;
		try {
			filter = context.createFilter("(&(objectClass=" + GwtService.class.getName() + ")(http.service.other.info=" + TestGwtService.class.getName() + "))");
		} catch (final InvalidSyntaxException e) {
			fail("Please check test implelementation: " + e);
		}
		final ServiceTracker tracker = new ServiceTracker(context, filter, null);
		tracker.open();

		final int trackingCount = tracker.getTrackingCount();
		assertTrue("Only one service must be tracked here!", trackingCount == 1);

		tracker.close();
	}

}
