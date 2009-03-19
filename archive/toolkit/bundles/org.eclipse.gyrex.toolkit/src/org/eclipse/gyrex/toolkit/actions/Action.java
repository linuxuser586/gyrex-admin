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

import org.eclipse.gyrex.toolkit.commands.Command;

/**
 * A lightweight description of an action to be performed in the user interface.
 * <p>
 * {@link Action actions} are used to change the UI. Typically, actions are a
 * direct result of a {@link Command command} execution.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 * 
 * @noextend This class is intended to be subclassed <em>only</em> within the
 *           CWT implementation.
 */
public class Action {

	/** hint bits */
	private final int hints;

	/**
	 * Instantiates a new action using the specified hints.
	 * 
	 * @param hints
	 *            the action hints
	 */
	public Action(final int hints) {
		this.hints = hints;
	}

	/**
	 * Returns the receiver's hints.
	 * 
	 * @return the hint bits
	 */
	public int getHints() {
		return hints;
	}

}
