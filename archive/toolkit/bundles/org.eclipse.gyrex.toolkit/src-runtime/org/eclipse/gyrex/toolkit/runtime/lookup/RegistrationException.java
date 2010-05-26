/*******************************************************************************
 * Copyright (c) 2008, 2009 Gunnar Wagenknecht and others.
 * All rights reserved.
 *  
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.runtime.lookup;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

/**
 * Thrown by {@link IWidgetRegistry widget registries} or
 * {@link IWidgetAdapterRegistry widget adapter registries} if a registration
 * failed.
 */
public class RegistrationException extends CoreException {

	/** serialVersionUID */
	private static final long serialVersionUID = -8722317998535952180L;

	/**
	 * Creates a new exception with the given status object. The message of the
	 * given status is used as the exception message.
	 * 
	 * @param status
	 *            the status object to be associated with this exception
	 */
	public RegistrationException(final IStatus status) {
		super(status);
	}
}
