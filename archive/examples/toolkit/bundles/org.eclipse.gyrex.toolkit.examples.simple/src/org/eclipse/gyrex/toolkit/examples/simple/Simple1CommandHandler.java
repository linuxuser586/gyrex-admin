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
package org.eclipse.cloudfree.toolkit.examples.simple;


import org.eclipse.cloudfree.toolkit.content.ContentSet;
import org.eclipse.cloudfree.toolkit.content.TextContent;
import org.eclipse.cloudfree.toolkit.runtime.commands.CommandExecutionEvent;
import org.eclipse.cloudfree.toolkit.runtime.commands.CommandExecutionResult;
import org.eclipse.cloudfree.toolkit.runtime.commands.ICommandHandler;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * 
 */
public class Simple1CommandHandler implements ICommandHandler {

	public static final Simple1CommandHandler INSTANCE = new Simple1CommandHandler();

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.runtime.commands.ICommandHandler#execute(org.eclipse.cloudfree.toolkit.runtime.commands.CommandExecutionEvent, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public CommandExecutionResult execute(final CommandExecutionEvent executionEvent, final IProgressMonitor progressMonitor) {
		if (isGenerateCommand(executionEvent)) {
			return generateKey(executionEvent, progressMonitor);
		}
		System.out.println("Command: " + executionEvent.getCommandId());
		return null;
	}

	private CommandExecutionResult generateKey(final CommandExecutionEvent executionEvent, final IProgressMonitor progressMonitor) {
		final ContentSet contentSet = executionEvent.getContentSet();
		final TextContent entry = (TextContent) contentSet.getEntry(Simple1Constants.ID_EMAIL);
		System.out.println("[Generate Key] " + entry.getText());
		return null;
	}

	private boolean isGenerateCommand(final CommandExecutionEvent executionEvent) {
		return Simple1Constants.CMD_GENERATE.equals(executionEvent.getCommandId());
	}

}
