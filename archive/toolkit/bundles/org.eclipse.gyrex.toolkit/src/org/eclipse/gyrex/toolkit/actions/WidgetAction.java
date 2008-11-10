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
package org.eclipse.cloudfree.toolkit.actions;

/**
 * {@link Action} which operates on/with a single widget.
 */
public class WidgetAction extends Action {

	/** the widget id */
	private final String widgetId;

	/**
	 * Creates a new instance.
	 * 
	 * @param widgetId
	 *            the widget id
	 * @param hints
	 *            the action hints
	 */
	public WidgetAction(final String widgetId, final int hints) {
		super(hints);
		this.widgetId = widgetId;
	}

	/**
	 * Returns the id of the widget.
	 * 
	 * @return the widget id
	 */
	public String getWidgetId() {
		return widgetId;
	}
}
