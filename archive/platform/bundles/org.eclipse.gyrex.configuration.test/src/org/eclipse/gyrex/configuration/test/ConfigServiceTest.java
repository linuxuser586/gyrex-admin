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
package org.eclipse.gyrex.configuration.test;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.equinox.security.storage.EncodingUtils;
import org.eclipse.gyrex.configuration.internal.impl.ConfigurationServiceImpl;
import org.eclipse.gyrex.configuration.internal.impl.PreferencesUtil;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.preferences.PlatformScope;
import org.junit.Test;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * 
 */
public class ConfigServiceTest {

	private static class TestContext extends PlatformObject implements IRuntimeContext {
		private final IPath path;

		/**
		 * Creates a new instance.
		 * 
		 * @param path
		 */
		public TestContext(final IPath path) {
			this.path = path;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gyrex.context.IRuntimeContext#get(java.lang.Class)
		 */
		@Override
		public <T> T get(final Class<T> type) throws IllegalArgumentException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gyrex.context.IRuntimeContext#getContextPath()
		 */
		@Override
		public IPath getContextPath() {
			return path;
		}
	}

	private static final String SIMPLE_KEY = "test";
	private static final String COMPLEX_KEY_1 = "complex//key/something";
	private static final String COMPLEX_KEY_2 = "complex.key/some/more//key/something";

	private static final TestContext TEST_CONTEXT = new TestContext(new Path("/test/context"));
	private static final TestContext EMPTY = new TestContext(Path.EMPTY);
	private static final TestContext ROOT = new TestContext(Path.ROOT);

	private static final Random random = new Random(System.currentTimeMillis());

	@Test
	public void defaultPreferenceEmptyContext() throws Exception {
		testDefault(EMPTY);
	}

	@Test
	public void defaultPreferenceRootContext() throws Exception {
		testDefault(ROOT);
	}

	@Test
	public void defaultPreferenceWithoutContext() throws Exception {
		testDefault(null);
	}

	@Test
	public void defaultPreferenceWithTestContext() throws Exception {
		testDefault(TEST_CONTEXT);
	}

	private String generateValue() {
		final byte[] randomBytes = new byte[40];
		random.nextBytes(randomBytes);
		final StringBuilder builder = new StringBuilder(120);
		builder.append("[test] ");
		builder.append(Long.toHexString(System.currentTimeMillis()));
		builder.append(" (").append(EncodingUtils.encodeBase64(randomBytes)).append(")");
		return builder.toString();
	}

	@Test
	public void putPreferenceEmptyContext() throws Exception {
		testPut(EMPTY);
	}

	@Test
	public void putPreferenceRootContext() throws Exception {
		testPut(ROOT);
	}

	@Test
	public void putPreferenceWithoutContext() throws Exception {
		testPut(null);
	}

	@Test
	public void putPreferenceWithTestContext() throws Exception {
		testPut(TEST_CONTEXT);
	}

	@Test
	public void removePreferenceEmptyContext() throws Exception {
		testRemove(EMPTY);
	}

	@Test
	public void removePreferenceRootContext() throws Exception {
		testRemove(ROOT);
	}

	@Test
	public void removePreferenceWithoutContext() throws Exception {
		testRemove(null);
	}

	@Test
	public void removePreferenceWithTestContext() throws Exception {
		testRemove(TEST_CONTEXT);
	}

	private void testDefault(final IRuntimeContext context) {
		testDefault(context, SIMPLE_KEY);
		testDefault(context, COMPLEX_KEY_1);
		testDefault(context, COMPLEX_KEY_2);
	}

	private void testDefault(final IRuntimeContext context, final String key) {
		final ConfigurationServiceImpl serviceImpl = new ConfigurationServiceImpl();
		final String VALUE = generateValue();
		final String childPath = PreferencesUtil.makeRelative(PreferencesUtil.decodePath(key)[0]);
		Preferences defaultNode = new DefaultScope().getNode(Activator.PLUGIN_ID);
		if (childPath.length() > 0) {
			defaultNode = defaultNode.node(childPath);
		}
		defaultNode.put(PreferencesUtil.decodePath(key)[1], VALUE);
		serviceImpl.remove(Activator.PLUGIN_ID, key, context);
		assertEquals(VALUE, serviceImpl.getString(Activator.PLUGIN_ID, key, null, context));
	}

	private void testPut(final IRuntimeContext context) throws BackingStoreException {
		testPut(context, SIMPLE_KEY);
		testPut(context, COMPLEX_KEY_1);
		testPut(context, COMPLEX_KEY_2);
	}

	private void testPut(final IRuntimeContext context, final String key) throws BackingStoreException {
		final String VALUE = generateValue();
		new DefaultScope().getNode(Activator.PLUGIN_ID).removeNode();
		final ConfigurationServiceImpl serviceImpl = new ConfigurationServiceImpl();
		serviceImpl.putString(Activator.PLUGIN_ID, key, VALUE, context, false);
		new PlatformScope().getNode(Activator.PLUGIN_ID).flush();
		assertEquals(VALUE, serviceImpl.getString(Activator.PLUGIN_ID, key, null, context));
	}

	private void testRemove(final IRuntimeContext context) throws BackingStoreException {
		testRemove(context, SIMPLE_KEY);
		testRemove(context, COMPLEX_KEY_1);
		testRemove(context, COMPLEX_KEY_2);
	}

	private void testRemove(final IRuntimeContext context, final String key) throws BackingStoreException {
		new DefaultScope().getNode(Activator.PLUGIN_ID).removeNode();
		final ConfigurationServiceImpl serviceImpl = new ConfigurationServiceImpl();
		serviceImpl.remove(Activator.PLUGIN_ID, key, context);
		assertEquals(null, serviceImpl.getString(Activator.PLUGIN_ID, key, null, context));
	}
}
