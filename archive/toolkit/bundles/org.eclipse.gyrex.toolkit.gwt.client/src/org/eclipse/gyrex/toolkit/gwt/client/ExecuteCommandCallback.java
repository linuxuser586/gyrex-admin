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
package org.eclipse.cloudfree.toolkit.gwt.client;

/**
 * This is the callback interface to use when executing a command using the
 * {@link WidgetFactory widget factory}.
 */
public interface ExecuteCommandCallback {

	/**
	 * This method gets called when an error occurred.
	 * 
	 * @param caught
	 * @see WidgetFactoryException
	 */
	void onFailure(WidgetFactoryException caught);

	/**
	 * This method gets called when the command was executed.
	 * 
	 * @param result
	 *            the command execution result
	 */
	void onSuccess(Object result);
}
