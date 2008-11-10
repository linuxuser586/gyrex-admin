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
package org.eclipse.cloudfree.http.internal.apps.dummy;


import org.eclipse.cloudfree.common.context.IContext;
import org.eclipse.cloudfree.http.application.Application;
import org.eclipse.cloudfree.http.application.provider.ApplicationProvider;
import org.eclipse.core.runtime.CoreException;

/**
 * 
 */
public class DummyAppProvider extends ApplicationProvider {

	public static final String ID = "org.eclipse.cloudfree.http.internal.apps.dummy";

	public DummyAppProvider() {
		super(ID);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.http.application.provider.ApplicationProvider#createApplication(java.lang.String, org.eclipse.cloudfree.common.context.IContext, java.util.Map)
	 */
	@Override
	public Application createApplication(final String applicationId, final IContext context) throws CoreException {
		return new DummyApp(applicationId, context);
	}

}
