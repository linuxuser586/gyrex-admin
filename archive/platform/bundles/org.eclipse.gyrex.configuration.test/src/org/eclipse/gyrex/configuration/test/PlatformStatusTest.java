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

import static org.junit.Assert.assertNotNull;


import org.eclipse.core.runtime.IStatus;
import org.eclipse.gyrex.configuration.PlatformConfiguration;
import org.junit.Test;

public class PlatformStatusTest {

	@Test
	public void testPlatformConfigurationStatus() {
		final IStatus platformStatus = PlatformConfiguration.getPlatformStatus();
		assertNotNull("platform status must not be null", platformStatus);
	}
}
