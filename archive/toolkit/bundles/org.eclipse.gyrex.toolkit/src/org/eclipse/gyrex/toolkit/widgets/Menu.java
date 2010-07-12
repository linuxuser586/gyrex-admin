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
package org.eclipse.gyrex.toolkit.widgets;

import org.eclipse.gyrex.toolkit.Toolkit;
import org.eclipse.gyrex.toolkit.commands.Command;
import org.eclipse.gyrex.toolkit.layout.Layout;

/**
 * A container to host menu items and sub menus.
 * <p>
 * A menu is a set of {@link Command commands} which can be triggered from the
 * UI. The Toolkit supports flat as well as structured menus of arbitrary levels
 * deep. However, recursion is not allowed within a menu tree branch.
 * </p>
 * <p>
 * Additional, menus can provide {@link #getFeaturedItems() <em>featured</em>}
 * items. This is useful in combination with very deep menus. A higher menu
 * level could provided shortcuts to common items for an improved usability.
 * </p>
 * <p>
 * Note, although this class is a {@link Container container} it doesn't make
 * sense to add children other than {@link Menu sub menus} or {@link MenuItem
 * items} to it. Also setting a layout is a no-op.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public final class Menu extends Container {

	/** serialVersionUID */
	private static final long serialVersionUID = 8172073572025839760L;
	private static final MenuItem[] NO_ITEMS = new MenuItem[0];

	private MenuItem[] featuredItems;

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
	 * {@link MenuItem} and that no recursion will occur when adding this item.
	 * 
	 * @see org.eclipse.gyrex.toolkit.widgets.Container#checkChildWidget(org.eclipse.gyrex.toolkit.widgets.Widget)
	 */
	@Override
	protected void checkChildWidget(final Widget widget) {
		// menu items are ok
		if (MenuItem.class.isAssignableFrom(widget.getClass())) {
			return;
		}
		// sub menus are ok as well if no recursion is involved
		if (Menu.class.isAssignableFrom(widget.getClass())) {
			// verify that no parent with same id exists
			/*
			 * note, the implementation can rely on just verifying the parents
			 * because the Toolkit API does not allow constructing trees
			 * bottom-up but only top-down
			 */
			final String id = widget.getId();
			Container parent = this;
			while (null != parent) {
				if (parent.getId().equals(id)) {
					Toolkit.error(Toolkit.ERROR_INVALID_ARGUMENT, "recursion detected within menu structure; id:" + id);
				}
				parent = parent.getParent();
			}
			return;
		}

		// invalid class
		Toolkit.error(Toolkit.ERROR_INVALID_ARGUMENT, "only Menu or MenuItem allowed");
	}

	/**
	 * Returns the featured items.
	 * 
	 * @return the featured items
	 */
	public MenuItem[] getFeaturedItems() {
		return featuredItems;
	}

	/**
	 * Sets the featured items.
	 * <p>
	 * Note, featured items do not need to have a parent. It's recommended to
	 * use parentless items here. But it's also allowed to re-use existing menu
	 * items from deeper levels in the menu tree.
	 * </p>
	 * 
	 * @param featuredItems
	 *            the featured items to set (maybe <code>null</code> to unset)
	 */
	public void setFeaturedItems(final MenuItem... featuredItems) {
		if (null == featuredItems) {
			this.featuredItems = NO_ITEMS;
			return;
		}
		for (int i = 0; i < featuredItems.length; i++) {
			if (null == featuredItems[i]) {
				Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "null element at index " + i);
			}
		}
		this.featuredItems = featuredItems;
	}

	/**
	 * Not supported by {@link Menu}.
	 */
	@Override
	public void setLayout(final Layout layout) {
		// no-op
	}

}
