/*******************************************************************************
 * Copyright (c) 2010 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.gwt.client.ui.commands;

import org.eclipse.gyrex.gwt.common.status.IStatus;

/**
 * Event object containing information about command execution.
 */
public class CommandExecutedEvent {

	private final String commandId;
	private final IStatus status;
	private boolean processEvent;

	/**
	 * Creates a new instance.
	 * 
	 * @param commandId
	 */
	public CommandExecutedEvent(final String commandId, final IStatus status) {
		this.commandId = commandId;
		this.status = status;
	}

	/**
	 * Returns the id of the command which triggered the result.
	 * 
	 * @return the command id
	 */
	public String getCommandId() {
		return commandId;
	}

	/**
	 * Returns the status of the command execution.
	 * 
	 * @return the status
	 */
	public IStatus getStatus() {
		return status;
	}

	/**
	 * Indicates if the framework is allowed to perform any additional
	 * processing of the event.
	 * 
	 * @return <code>true</code> if the framework is allowed to perform further
	 *         event processing, <code>false</code> otherwise
	 */
	public boolean isContinueEventProcessing() {
		return processEvent;
	}

	/**
	 * Instructs the framework to not perform any additional processing on the
	 * event.
	 */
	public void stopEventProcessing() {
		processEvent = false;
	}
}
