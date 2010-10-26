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
package org.eclipse.gyrex.toolkit.gwt.client.ui.widgets;

import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SMenuItem;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 */
public class CWTMenuItem extends CWTDialogField {

	@Override
	protected Widget renderFieldWidget(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SMenuItem sMenuItem = (SMenuItem) serializedWidget;

		final Image image = null != sMenuItem.image ? new Image(toolkit.getResourceUrl(sMenuItem.image.reference)) : null;

		final HorizontalPanel panel = new HorizontalPanel();
		if (null != image) {
			panel.add(image);
		} else {
			panel.add(new HTML("&nbsp;"));
		}

		final VerticalPanel right = new VerticalPanel();
		right.add(new Label(sMenuItem.label));

		panel.add(right);
		return panel;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		// TODO Auto-generated method stub

	}

}
