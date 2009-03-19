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
package org.eclipse.gyrex.toolkit.runtime.lookup;


import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.gyrex.toolkit.widgets.Widget;

/**
 * A widget factory is responsible for creating widgets.
 * <p>
 * This abstraction provides a very flexible access to the widget. The idea is
 * that widgets can not only be created programmatically as Java objects but
 * also from files or database.
 * </p>
 * <p>
 * This interface is intended to be implemented by clients.
 * </p>
 */
public interface IWidgetFactory {

	/**
	 * Creates a widget with the specified id.
	 * <p>
	 * Implementors should check if the can provide a widget for the specified
	 * id. If they cann't they must return <code>null</code>.
	 * </p>
	 * 
	 * @param id
	 *            the widget id
	 * @param environment
	 *            the widget environment
	 * @return the widget or <code>null</code> if the factory does not have a
	 *         widget of the specified id
	 */
	Widget getWidget(String id, IWidgetEnvironment environment);
}
