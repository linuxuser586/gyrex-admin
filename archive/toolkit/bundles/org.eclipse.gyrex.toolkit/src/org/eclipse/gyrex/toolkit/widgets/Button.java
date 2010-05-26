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

package org.eclipse.gyrex.toolkit.widgets;

import org.eclipse.gyrex.toolkit.commands.Command;
import org.eclipse.gyrex.toolkit.content.NoContent;

/**
 * A dialog field with a button to press.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Button extends DialogField<NoContent> {

	/** serialVersionUID */
	private static final long serialVersionUID = -4310058729473403373L;

	/** the command */
	private Command command;

	/**
	 * Creates a new button dialog field.
	 * 
	 * @param id
	 * @param parent
	 * @param style
	 */
	public Button(final String id, final Container parent, final int style) {
		super(id, parent, style, NoContent.class);
	}

	/**
	 * Returns the command to be triggered when this button is pressed.
	 * 
	 * @return the command (maybe <code>null</code>)
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * Sets the command to be triggered when the button is pressed.
	 * 
	 * @param command
	 *            the command to trigger
	 */
	public void setCommand(final Command command) {
		this.command = command;
	}
}
