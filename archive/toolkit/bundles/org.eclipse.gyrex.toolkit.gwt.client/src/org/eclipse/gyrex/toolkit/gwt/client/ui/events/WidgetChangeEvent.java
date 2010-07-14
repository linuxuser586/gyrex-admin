/*******************************************************************************
 * Copyright (c) 2010 AGETO and others.
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

import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Represents a widget change event.
 */
public class WidgetChangeEvent extends GwtEvent<WidgetChangeHandler> {

	public static Type<WidgetChangeHandler> TYPE = new Type<WidgetChangeHandler>();

	/**
	 * Fires a value change event on all registered handlers in the handler
	 * manager.If no such handlers exist, this method will do nothing.
	 * 
	 * @param source
	 *            the source of the handlers
	 * @param widget
	 *            the value
	 */
	public static void fire(final HasWidgetChangeHandlers source, final CWTWidget widget) {
		final WidgetChangeEvent event = new WidgetChangeEvent(widget);
		source.fireEvent(event);
	}

	private final CWTWidget widget;

	protected WidgetChangeEvent(final CWTWidget widget) {
		this.widget = widget;
	}

	@Override
	protected void dispatch(final WidgetChangeHandler handler) {
		handler.onWidgetChange(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<WidgetChangeHandler> getAssociatedType() {
		return TYPE;
	}

	/**
	 * Returns the widget.
	 * 
	 * @return the widget
	 */
	public CWTWidget getWidget() {
		return widget;
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + widget.getWidgetId();
	}

}
