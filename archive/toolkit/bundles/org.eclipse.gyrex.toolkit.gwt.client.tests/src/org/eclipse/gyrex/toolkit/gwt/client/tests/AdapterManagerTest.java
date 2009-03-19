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
package org.eclipse.gyrex.toolkit.gwt.client.tests;

import com.google.gwt.junit.client.GWTTestCase;

import org.eclipse.gyrex.gwt.common.adaptable.AdapterManager;
import org.eclipse.gyrex.gwt.common.adaptable.IsAdaptable;

/**
 * Test case for the {@link AdapterManager}
 */
public class AdapterManagerTest extends GWTTestCase {

	static class TestAdaptable implements IsAdaptable {
		public Object getAdapter(final Class adapter) {
			return AdapterManager.getAdapterManager().getAdapter(adapter, new Class[] { TestAdaptable.class });
		}
	}

	static class TestAdaptable2 implements IsAdaptable {
		public Object getAdapter(final Class adapter) {
			return AdapterManager.getAdapterManager().getAdapter(adapter, new Class[] { TestAdaptable2.class });
		}
	}

	static class TestAdaptableSub extends TestAdaptable {
		public Object getAdapter(final Class adapter) {
			return AdapterManager.getAdapterManager().getAdapter(adapter, new Class[] { TestAdaptableSub.class, TestAdaptable.class });
		}
	}

	static class TestAdapter {
	}

	private static final TestAdapter adapter1 = new TestAdapter();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
	 */
	public String getModuleName() {
		return "org.eclipse.gyrex.toolkit.gwt.Tests";
	}

	/**
	 * Tests adapter registration and retrieval.
	 */
	public void testRegisterAndGetAdapter1() {
		// register adapter
		AdapterManager.getAdapterManager().registerAdapter(TestAdaptable.class, TestAdapter.class, adapter1);

		// test get adapter from same class
		Object foundAdapter = new TestAdaptable().getAdapter(TestAdapter.class);
		assertNotNull("No adapter returned although one is registered.", foundAdapter);
		assertEquals("Returned adapter is not the registered adapter.", adapter1, foundAdapter);

		// test get adapter from subclass
		foundAdapter = new TestAdaptableSub().getAdapter(TestAdapter.class);
		assertNotNull("No adapter returned for subclass although one is registered for parent.", foundAdapter);
		assertEquals("Returned adapter is not the registered adapter.", adapter1, foundAdapter);
	}
}
