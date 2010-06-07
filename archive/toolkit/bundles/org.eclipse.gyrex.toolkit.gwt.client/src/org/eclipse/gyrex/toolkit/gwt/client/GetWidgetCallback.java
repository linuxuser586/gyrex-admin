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
package org.eclipse.gyrex.toolkit.gwt.client;

import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget;

/**
 * This is the callback interface for receiving a {@link CWTWidget widget} from
 * the {@link WidgetFactory widget factory}.
 */
public interface GetWidgetCallback {
	/**
	 * This method gets called when an error occurred.
	 * 
	 * @param caught
	 * @see WidgetFactoryException
	 */
	void onFailure(WidgetFactoryException caught);

	/**
	 * This method gets called when the requested {@link CWTWidget} is
	 * available.
	 * 
	 * @param widget
	 *            the requested widget
	 */
	void onSuccess(CWTWidget widget);
}
