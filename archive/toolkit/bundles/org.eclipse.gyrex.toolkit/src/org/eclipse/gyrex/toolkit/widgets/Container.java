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
package org.eclipse.gyrex.toolkit.widgets;

import java.util.ArrayList;

import org.eclipse.gyrex.toolkit.CWT;
import org.eclipse.gyrex.toolkit.layout.Layout;

/**
 * A container hosts widgets.
 * <p>
 * The children of the container will be arraigned by a particular
 * {@link Layout layout}.
 * </p>
 * <p>
 * A container may optionally have a title and a description.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Container extends Widget {

	/** serialVersionUID */
	private static final long serialVersionUID = 1004283376850123624L;

	/** NO_WIDGETS */
	static final Widget[] NO_WIDGETS = new Widget[0];

	/** widgets */
	private java.util.List<Widget> widgets;

	/** layout */
	private Layout layout;

	/** label */
	private String label;

	/** description * */
	private String description;

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 *            the widget id
	 * @param parent
	 *            the widget parent
	 * @param style
	 *            the widget style
	 */
	public Container(final String id, final Container parent, final int style) {
		super(id, parent, style);
	}

	/**
	 * Creates a new root container instance.
	 * 
	 * @param id
	 *            the widget id
	 * @param style
	 *            the widget style
	 */
	public Container(final String id, final int style) {
		this(id, null, style);
	}

	/**
	 * Adds a widget to the list of widgets.
	 * 
	 * @param widget
	 *            the widget to add
	 */
	void addWidget(final Widget widget) {
		if (null == widget) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "widget");
		}

		checkChildWidget(widget);

		if (null == widgets) {
			widgets = new ArrayList<Widget>(2);
		}
		widgets.add(widget);
	}

	@Override
	void addWidgetToParent() {
		// overwritten to allow null parent
		if (null != getParent()) {
			super.addWidgetToParent();
		}
	}

	/**
	 * Checks the specified widget if it's a valid child of this container.
	 * <p>
	 * The default implementation does nothing. Subclasses my overwrite to check
	 * specifics constraints before a a widget is added as a child to this
	 * container.
	 * </p>
	 * 
	 * @param widget
	 *            the widget that will be added to the container
	 */
	protected void checkChildWidget(final Widget widget) {
		// empty
	}

	@Override
	protected void checkParent(final Container parent) {
		// overwritten to allow null parent
	}

	/**
	 * Returns the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the label.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Returns the layout for aligning children in the container.
	 * 
	 * @return the layout (may be <code>null</code>)
	 */
	public Layout getLayout() {
		return layout;
	}

	/**
	 * Returns the list of widgets contained in this container.
	 * 
	 * @return the widgets
	 */
	public Widget[] getWidgets() {
		if (null == widgets) {
			return NO_WIDGETS;
		}
		return widgets.toArray(new Widget[widgets.size()]);
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Sets the container's label.
	 * 
	 * @param label
	 *            the label to set
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * Sets a layout for aligning children in the container.
	 * <p>
	 * If no layout is set a default layout is used which may vary depending on
	 * the underlying rendering technology for the benefit of a good out-of-the
	 * box layout.
	 * </p>
	 * 
	 * @param layout
	 *            the layout to set (maybe <code>null</code> to unset)
	 */
	public void setLayout(final Layout layout) {
		this.layout = layout;
	}

}
