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
package org.eclipse.gyrex.toolkit.runtime.commands;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Handler for command invocations.
 * <p>
 * This interface may be implemented by clients.
 * </p>
 */
public interface ICommandHandler {

	/**
	 * Executed the command specified in the
	 * {@link CommandExecutionEvent execution event}.
	 * 
	 * @param executionEvent
	 *            the command execution event
	 * @param progressMonitor
	 *            the monitor for reporting progress and/or checking
	 *            cancellation
	 * @return a result object with details about the command execution and
	 *         actions to be applied in the environment where the command was
	 *         triggered (may not be <code>null</code>)
	 * @see CommandExecutionEvent
	 * @see CommandExecutionResult
	 */
	CommandExecutionResult execute(CommandExecutionEvent executionEvent, IProgressMonitor progressMonitor);
}
