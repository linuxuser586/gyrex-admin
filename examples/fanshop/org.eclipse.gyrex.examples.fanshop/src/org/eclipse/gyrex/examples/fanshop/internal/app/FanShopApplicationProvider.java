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
package org.eclipse.gyrex.examples.fanshop.internal.app;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.gyrex.common.context.IContext;
import org.eclipse.gyrex.examples.fanshop.internal.FanShopActivator;
import org.eclipse.gyrex.http.application.Application;
import org.eclipse.gyrex.http.application.provider.ApplicationProvider;

/**
 * The application provider for the extensible Fan Shop application.
 */
public class FanShopApplicationProvider extends ApplicationProvider {

	public static final String ID = FanShopActivator.PLUGIN_ID + ".application.provider";

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 */
	public FanShopApplicationProvider() {
		super(ID);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.http.application.provider.ApplicationProvider#createApplication(java.lang.String, org.eclipse.gyrex.common.context.IContext)
	 */
	@Override
	public Application createApplication(final String applicationId, final IContext context) throws CoreException {
		return new FanShopApplication(applicationId, context);
	}

}
