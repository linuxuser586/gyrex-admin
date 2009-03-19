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
import static org.junit.Assert.assertNotNull;


import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.gyrex.configuration.preferences.PlatformScope;
import org.junit.Test;

public class PlatfromScopeTest {

	@Test
	public void testPlatformScope() throws Exception {
		final IEclipsePreferences node = new PlatformScope().getNode("test");
		assertNotNull(node);
		node.put("key", "value");
		node.flush();

		assertEquals("/platform/test", new PlatformScope().getNode("test").absolutePath());
		assertEquals("value", new PlatformScope().getNode("test").get("key", null));

		// note, this test may require a patch for Eclipse
		// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=220549
		//		if (!"value".equals(PlatformConfiguration.getConfigurationService().getString("test", "key", null, null))) {
		//			fail("The Eclipse preference service did not return the expected value. Please verify that you are using the correct bundle including the patch from Eclipse bug 220549 and set the lookup order in startup.ini.");
		//		}
	}
}
