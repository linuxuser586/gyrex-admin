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
package org.eclipse.gyrex.http.internal;

import java.io.IOException;
import java.util.Dictionary;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.equinox.http.jetty.JettyCustomizer;
import org.eclipse.gyrex.log.internal.firephp.FirePHPLogger;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHandler;

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

		// set servlet handler
		jettyContext.setServletHandler(new ServletHandler() {

			@Override
			public void handle(final String target, final HttpServletRequest request, final HttpServletResponse response, final int type) throws IOException, ServletException {
				try {
					// hook with the logging system
					// TODO: this should be generalized with some extensible API (i.e. the other way around)
					FirePHPLogger.setResponse(response);

					// handle the request
					super.handle(target, request, response, type);
				} finally {
					// remove from logging
					FirePHPLogger.setResponse(null);
				}

			}
		});
		return jettyContext;
	}

}
