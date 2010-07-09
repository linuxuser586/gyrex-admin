/*******************************************************************************
 * Copyright (c) 2006, 2010 <enter-company-name-here> and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     <enter-company-name-here> - initial API and implementation
 *     Eclipse.org - ideas, concepts and code from existing Eclipse projects
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.widgets;

import org.eclipse.gyrex.toolkit.CWT;
import org.eclipse.gyrex.toolkit.layout.Layout;

/**
 * A container to host menu items.
 * <p>
 * Note, although this class is a {@link Container container} it doesn't make
 * sense to add children other than {@link Menu sub menus} or MenuItems to it.
 * Also setting a layout is a no-op.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public final class Menu extends Container {

	/** serialVersionUID */
	private static final long serialVersionUID = 8172073572025839760L;

	/**
	 * Creates a new menu instance.
	 * 
	 * @param id
	 *            the menu id
	 * @param parent
	 *            the menu parent
	 * @param style
	 *            the menu style
	 */
	public Menu(final String id, final Container parent, final int style) {
		super(id, parent, style);
	}

	/**
	 * Creates a new parent-less menu instance.
	 * 
	 * @param id
	 *            the menu id
	 * @param style
	 *            the menu style
	 */
	public Menu(final String id, final int style) {
		super(id, style);
	}

	/**
	 * Verifies that the specified widget is either a {@link Menu} or a
	 * MenuItem.
	 * 
	 * @see org.eclipse.gyrex.toolkit.widgets.Container#checkChildWidget(org.eclipse.gyrex.toolkit.widgets.Widget)
	 */
	@Override
	protected void checkChildWidget(final Widget widget) {
		if (!Menu.class.isAssignableFrom(widget.getClass())) {
			CWT.error(CWT.ERROR_INVALID_ARGUMENT, "only Menu or MenuItem allowed");
		}
	}

	/**
	 * Not supported by {@link Menu}.
	 */
	@Override
	public void setLayout(final Layout layout) {
		// no-op
	}

}
