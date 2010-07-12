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
package org.eclipse.gyrex.toolkit.widgets;

import org.eclipse.gyrex.toolkit.Toolkit;
import org.eclipse.gyrex.toolkit.layout.Layout;

/**
 * A container to group and layout dialog fields.
 * <p>
 * In typical usage, the client instantiates this class and uses it as a parent
 * for a set of {@link DialogField dialog fields}. The dialog field group
 * orchestrates the presentation of its fields and also serves as a common
 * parent for dialog fields which require grouping.
 * </p>
 * <p>
 * The standard layout is roughly as follows: it has a header area containing
 * the group's title, description, and image; the dialog fields appear in the
 * main area with their label at the beginning, their main control in the
 * middle, filling the remaining space, and their optional controls at the end.
 * </p>
 * <p>
 * Note, although this class is a {@link Container container} it doesn't make
 * sense to add children other than {@link DialogField dialog fields} to it.
 * Also setting a layout is a no-op.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public final class DialogFieldGroup extends Container {

	/** serialVersionUID */
	private static final long serialVersionUID = -8509411634221932987L;

	/**
	 * Creates and returns a new dialog field group.
	 * 
	 * @param id
	 *            the group id
	 * @param parent
	 *            the group parent
	 * @param style
	 *            the group style (may be applied to all dialog fields in this
	 *            group)
	 * @see Toolkit#REQUIRED
	 */
	public DialogFieldGroup(final String id, final Container parent, final int style) {
		super(id, parent, style);
	}

	/**
	 * Verifies that the specified widget is a {@link DialogField dialog field}.
	 * 
	 * @see org.eclipse.gyrex.toolkit.widgets.Container#checkChildWidget(org.eclipse.gyrex.toolkit.widgets.Widget)
	 */
	@Override
	protected void checkChildWidget(final Widget widget) {
		if (!DialogField.class.isAssignableFrom(widget.getClass())) {
			Toolkit.error(Toolkit.ERROR_INVALID_ARGUMENT, "not a dialog field");
		}
	}

	/**
	 * Not supported by {@link DialogFieldGroup}.
	 */
	@Override
	public void setLayout(final Layout layout) {
		// no-op
	}

}
