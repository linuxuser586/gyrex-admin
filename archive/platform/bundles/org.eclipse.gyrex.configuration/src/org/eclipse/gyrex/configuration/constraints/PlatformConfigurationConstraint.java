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
package org.eclipse.cloudfree.configuration.constraints;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

/**
 * A constraint for determining a platform configuration status.
 * <p>
 * Configuration constraints can be contributed by clients very easily by simply
 * registering them as OSGi services using this class.
 * </p>
 * <p>
 * This class is intended to be subclassed by clients that wish to add
 * constraints to the platform configuration.
 * </p>
 */
public abstract class PlatformConfigurationConstraint {

	/**
	 * Called by the configuration framework to perform a configuration
	 * evaluation.
	 * 
	 * @param progressMonitor
	 *            a progress monitor for reporting progress feedback and
	 *            checking cancelation for long running operations
	 * @return a status indicating the result (may not be <code>null</code>)
	 */
	public abstract IStatus evaluateConfiguration(IProgressMonitor progressMonitor);
}
