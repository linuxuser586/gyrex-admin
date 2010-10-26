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
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogField;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SMenuItem;
import org.eclipse.gyrex.toolkit.widgets.DialogField;
import org.eclipse.gyrex.toolkit.widgets.Menu;
import org.eclipse.gyrex.toolkit.widgets.MenuItem;
import org.eclipse.gyrex.toolkit.widgets.Widget;

/**
 * {@link Menu} serializer.
 */
public class MenuItemSerializer extends DialogFieldSerializer {

	@Override
	protected SDialogField createSDialogField(final DialogField dialogField, final SContainer parent) {
		return new SMenuItem();
	}

	@Override
	protected ISerializedWidget populateAttributes(final Widget widget, final ISerializedWidget serializedWidget, final SContainer parent) {
		final MenuItem item = (MenuItem) widget;
		final SMenuItem sItem = (SMenuItem) serializedWidget;
		sItem.command = serializeCommand(item.getCommand(), item);
		sItem.image = serializeImageResource(item.getImage());
		sItem.disabledImage = serializeImageResource(item.getDisabledImage());
		return super.populateAttributes(item, sItem, parent);
	}
}
