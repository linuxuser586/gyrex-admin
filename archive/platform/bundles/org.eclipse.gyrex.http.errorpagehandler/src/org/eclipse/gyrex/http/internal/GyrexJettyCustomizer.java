/*******************************************************************************
 * Copyright (c) 2008, 2010 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.http.internal;

import java.util.Dictionary;

import org.eclipse.equinox.http.jetty.JettyCustomizer;
import org.mortbay.jetty.servlet.Context;

/**
 * Customizer for Jetty
 */
public class GyrexJettyCustomizer extends JettyCustomizer {

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.http.jetty.JettyCustomizer#customizeContext(java.lang.Object, java.util.Dictionary)
	 */
	@Override
	public Object customizeContext(final Object contex, final Dictionary settings) {
		if (!(contex instanceof Context)) {
			return contex;
		}

		final Context jettyContext = (Context) contex;

		// set error handler
		jettyContext.setErrorHandler(new GyrexErrorHandler());

		return jettyContext;
	}

}
