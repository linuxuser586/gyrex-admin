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


import org.eclipse.gyrex.toolkit.commands.Command;
import org.eclipse.gyrex.toolkit.content.ContentSet;
import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;

/**
 * Data object passed to a {@link ICommandHandler command execution handler}
 * when a command execution is triggered.
 * <p>
 * This data object carries information about the triggered command, the trigger
 * and the current application context in which the command was triggered.
 * </p>
 * <p>
 * Clients may not instantiate this class. It is typically instantiated by the
 * Toolkit implementation.
 * </p>
 */
public final class CommandExecutionEvent {

	/** the id of the triggered command */
	private final String commandId;

	/** the widget id which triggered the command */
	private final String sourceWidgetId;

	/** the content set as requested by the command */
	private final ContentSet contentSet;

	/** the widget environment */
	private final IWidgetEnvironment environment;

	/**
	 * Creates a new instance.
	 * 
	 * @param commandId
	 *            the id of the command being triggered
	 * @param sourceWidgetId
	 *            the id of the widget which triggered the command
	 * @param contentSet
	 *            the submitted content set
	 * @param environment
	 *            the widget environment
	 */
	public CommandExecutionEvent(final String commandId, final String sourceWidgetId, final ContentSet contentSet, final IWidgetEnvironment environment) {
		this.commandId = commandId;
		this.sourceWidgetId = sourceWidgetId;
		this.contentSet = contentSet;
		this.environment = environment;
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
	 * Returns the content set as requested by the
	 * {@link Command#getContentSubmitRule() command}.
	 * 
	 * @return the content set as requested by the
	 *         {@link Command#getContentSubmitRule() command} (may be
	 *         <code>null</code> if not content was submitted)
	 */
	public ContentSet getContentSet() {
		return contentSet;
	}

	/**
	 * Returns the widget environment.
	 * 
	 * @return the widget environment
	 */
	public IWidgetEnvironment getEnvironment() {
		return environment;
	}

	/**
	 * Returns the id of widget which triggered the command.
	 * 
	 * @return the id of widget which triggered the command
	 */
	public String getSourceWidgetId() {
		return sourceWidgetId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("CommandExecutionEvent[command: %s, source: %s]", commandId, sourceWidgetId);
	}
}
