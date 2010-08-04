/*******************************************************************************
 * Copyright (c) 2008, 2010 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.actions;

import org.eclipse.gyrex.toolkit.Toolkit;

/**
 * Instructs the UI to show a widget.
 * <p>
 * This action can be used to instruct the UI to show a specific widget. The
 * decision where the widget will be shown is dependent on the UI technology and
 * the UI context. For example, if the UI context is an editing context and the
 * UI provides the notion of an editor the widget might be opened in a new
 * editor. It's possible to give hints to the UI technology which behavior is
 * desired.
 * </p>
 * 
 * @noextend This class is intended to be subclassed <em>only</em> within the
 *           Toolkit implementation.
 */
public class ShowWidgetAction extends WidgetAction {

	/**
	 * Creates a new instance.
	 * 
	 * @param widgetId
	 *            the widget id
	 */
	public ShowWidgetAction(final String widgetId) {
		super(widgetId, Toolkit.NONE);
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param widgetId
	 *            the widget id
	 * @param hints
	 *            the action hints
	 */
	public ShowWidgetAction(final String widgetId, final int hints) {
		super(widgetId, hints);
	}
}
