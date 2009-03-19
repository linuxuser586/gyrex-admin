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
package org.eclipse.gyrex.toolkit.rap.internal.ui.widgets;

import java.util.EventListener;


/**
 * A listener for toolkit events.
 */
public interface CWTToolkitListener extends EventListener {

	/**
	 * Called when a widget changed.
	 * <p>
	 * The meaning of <em>change</em> is defined by the source widget.
	 * </p>
	 * 
	 * @param source
	 *            the widget which changed
	 */
	void widgetChanged(CWTWidget source);
}
