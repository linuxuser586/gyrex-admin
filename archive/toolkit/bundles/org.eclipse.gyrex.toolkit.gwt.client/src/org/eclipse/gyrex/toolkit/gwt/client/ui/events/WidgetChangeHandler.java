/*******************************************************************************
 * Copyright (c) 2010 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.gwt.client.ui.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for {@link WidgetChangeEvent} events.
 */
public interface WidgetChangeHandler extends EventHandler {

	/**
	 * Called when {@link WidgetChangeEvent} is fired.
	 * 
	 * @param event
	 *            the {@link WidgetChangeEvent} that was fired
	 */
	void onWidgetChange(WidgetChangeEvent event);
}