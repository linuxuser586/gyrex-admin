/*******************************************************************************
 * Copyright (c) 2008,2010 Gunnar Wagenknecht and others.
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

import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactory;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactoryException;

/**
 * This is the callback interface to use when executing a command using the
 * {@link WidgetFactory widget factory}.
 */
public interface CommandExecutionCallback {

	/**
	 * This method gets called when the command was executed.
	 * 
	 * @param event
	 *            the command execution event
	 */
	void onExecuted(CommandExecutedEvent event);

	/**
	 * This method gets called when an error occurred.
	 * 
	 * @param caught
	 *            the error
	 * @see WidgetFactoryException
	 */
	void onFailure(WidgetFactoryException caught);
}
