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
package org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.widgets;

import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SMenu;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SMenuItem;
import org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.ToolkitSerialization;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.widgets.Menu;
import org.eclipse.gyrex.toolkit.widgets.MenuItem;
import org.eclipse.gyrex.toolkit.widgets.Widget;

/**
 * {@link Menu} serializer.
 */
public class MenuSerializer extends ContainerSerializer {

	@Override
	protected SContainer createSContainer(final Container container, final SContainer parent) {
		return new SMenu();
	}

	@Override
	protected ISerializedWidget populateAttributes(final Widget widget, final ISerializedWidget serializedWidget, final SContainer parent) {
		final Menu menu = (Menu) widget;
		final SMenu sMenu = (SMenu) serializedWidget;

		sMenu.image = serializeImageResource(menu.getImage());
		final MenuItem[] featuredItems = menu.getFeaturedItems();
		if (featuredItems.length > 0) {
			sMenu.featuresItems = new SMenuItem[featuredItems.length];
			for (int i = 0; i < featuredItems.length; i++) {
				sMenu.featuresItems[i] = (SMenuItem) ToolkitSerialization.serializeWidget(featuredItems[i]);
			}
		}

		return super.populateAttributes(menu, sMenu, parent);
	}

}
