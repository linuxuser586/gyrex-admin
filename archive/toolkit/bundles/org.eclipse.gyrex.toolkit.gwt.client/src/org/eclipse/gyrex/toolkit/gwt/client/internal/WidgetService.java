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
package org.eclipse.gyrex.toolkit.gwt.client.internal;

import com.google.gwt.user.client.rpc.RemoteService;

import org.eclipse.gyrex.toolkit.gwt.client.WidgetClientEnvironment;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactoryException;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.commands.SCommandExecutionResult;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SContentSet;

/**
 * This service is used to locate and load widgets from the server.
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface WidgetService extends RemoteService {

	/**
	 * Triggers execution of the specified command.
	 * 
	 * @param commandId
	 *            the command id
	 * @param sourceWidgetId
	 *            the if of the widget which triggered the command
	 * @param sContentSet
	 *            the serializable content set
	 * @param environment
	 *            the widget factory environment
	 * @throws WidgetFactoryException
	 *             in case of errors
	 * @noreference This method is not intended to be referenced by clients.
	 */
	SCommandExecutionResult executeCommand(String commandId, String sourceWidgetId, SContentSet sContentSet, WidgetClientEnvironment environment) throws WidgetFactoryException;

	/**
	 * Returns the serialized widget for the specified widget id.
	 * 
	 * @param widgetId
	 *            the widget id
	 * @param environment
	 *            the widget factory environment
	 * @return the serialized widget
	 * @throws WidgetFactoryException
	 *             in case of errors
	 * @noreference This method is not intended to be referenced by clients.
	 */
	ISerializedWidget getWidget(String widgetId, WidgetClientEnvironment environment) throws WidgetFactoryException;
}
