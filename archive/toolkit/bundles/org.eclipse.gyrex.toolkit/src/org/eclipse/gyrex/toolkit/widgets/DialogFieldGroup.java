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
package org.eclipse.cloudfree.toolkit.widgets;

import org.eclipse.cloudfree.toolkit.CWT;
import org.eclipse.cloudfree.toolkit.layout.Layout;

/**
 * A container to group and layout dialog fields.
 * <p>
 * In typical usage, the client instantiates this class and uses it as a parent
 * for a set of {@link DialogField dialog fields}. The dialog field group
 * orchestrates the presentation of its fields and also serves as a common
 * parent for dialog fields which require grouping.
 * </p>
 * <p>
 * The standard layout is roughly as follows: it has an area at the top
 * containing both the group's title, description, and image; the dialog fields
 * appear in the middle with their label on the beginning, their main control in
 * the middle, filling the remaining space, and their optional controls at the
 * end.
 * </p>
 * <p>
 * Note, although this class is a {@link Container container} it doesn't make
 * sense to add children other than {@link DialogField dialog fields} to it.
 * Also setting a layout is a no-op.
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
	 * @param parent
	 * @param style
	 */
	public DialogFieldGroup(final String id, final Container parent, final int style) {
		super(id, parent, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rwt.widgets.Container#checkChildWidget(org.eclipse.rwt.widgets.Widget)
	 */
	@Override
	protected void checkChildWidget(final Widget widget) {
		if (!DialogField.class.isAssignableFrom(widget.getClass())) {
			CWT.error(CWT.ERROR_INVALID_ARGUMENT, "not a dialog field");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rwt.widgets.Container#setLayout(org.eclipse.rwt.layout.Layout)
	 */
	@Override
	public void setLayout(final Layout layout) {
		// no-op
	}

}
