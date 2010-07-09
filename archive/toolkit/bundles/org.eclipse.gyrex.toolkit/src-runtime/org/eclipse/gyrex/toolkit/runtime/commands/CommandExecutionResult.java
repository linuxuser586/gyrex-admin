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
package org.eclipse.gyrex.toolkit.runtime.commands;

import org.eclipse.gyrex.toolkit.actions.Action;

import org.eclipse.core.runtime.IStatus;

/**
 * Data object returned by a {@link ICommandHandler command execution handler}
 * after executing a command.
 * <p>
 * This data object carries information about a command execution, its result
 * and a set of actions to be applied to the environment which triggered a
 * command execution.
 * </p>
 * <p>
 * Clients implementing a {@link ICommandHandler command handler} may
 * instantiate this class.
 * </p>
 */
public final class CommandExecutionResult {

	/** the id of the command */
	private final String commandId;

	/** the command execution result status */
	private final IStatus status;

	/** the actions to execute as a result */
	private final Action[] actions;

	/**
	 * Creates a new result.
	 * 
	 * @param commandId
	 *            the id of the command that was triggered
	 * @param status
	 *            the result status
	 * @param actions
	 *            a set of actions to be executed in the given order
	 */
	public CommandExecutionResult(final String commandId, final IStatus status, final Action... actions) {
		this.commandId = commandId;
		this.status = status;
		this.actions = actions;
	}

	/**
	 * Returns the actions to execute.
	 * 
	 * @return the actions to execute
	 */
	public Action[] getActions() {
		return actions;
	}

	/**
	 * Returns the id of the triggered command.
	 * 
	 * @return the id of the triggered command
	 */
	public String getCommandId() {
		return commandId;
	}

	/**
	 * Returns the result status of the command execution.
	 * 
	 * @return the result status of the command execution
	 */
	public IStatus getStatus() {
		return status;
	}

}
