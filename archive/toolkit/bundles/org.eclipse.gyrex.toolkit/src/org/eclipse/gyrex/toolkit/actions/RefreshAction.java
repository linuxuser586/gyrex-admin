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
package org.eclipse.gyrex.toolkit.actions;

import org.eclipse.gyrex.toolkit.CWT;

/**
 * Instructs the UI to refresh the current widget.
 * 
 * @noextend This class is intended to be subclassed <em>only</em> within the
 *           CWT implementation.
 */
public class RefreshAction extends Action {

	/** delay */
	private final int delay;

	/**
	 * Creates a new refresh action using a no delay.
	 */
	public RefreshAction() {
		this(0);
	}

	/**
	 * Creates a new refresh action using the specified delay.
	 * 
	 * @param delay
	 *            a delay in milliseconds to wait before the refresh should be
	 *            executed
	 * @see #getDelay()
	 */
	public RefreshAction(final int delay) {
		super(CWT.NONE);
		if (delay < 0) {
			CWT.error(CWT.ERROR_INVALID_ARGUMENT, "delay must be greater or equal to 0");
		}
		this.delay = delay;
	}

	/**
	 * Returns the delay in milliseconds to wait before the refresh should be
	 * executed.
	 * 
	 * @return the delay
	 */
	public int getDelay() {
		return delay;
	}

}
