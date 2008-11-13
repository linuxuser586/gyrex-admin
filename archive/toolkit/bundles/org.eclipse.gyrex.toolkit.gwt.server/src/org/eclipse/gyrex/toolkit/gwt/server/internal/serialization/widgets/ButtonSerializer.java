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
package org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization.widgets;


import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SButton;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogField;
import org.eclipse.cloudfree.toolkit.widgets.Button;
import org.eclipse.cloudfree.toolkit.widgets.DialogField;
import org.eclipse.cloudfree.toolkit.widgets.Widget;

/**
 * {@link Button} serializer.
 */
public class ButtonSerializer extends DialogFieldSerializer {

	@Override
	protected SDialogField createSDialogField(final DialogField dialogField, final SContainer parent) {
		return new SButton();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization.widgets.DialogFieldSerializer#populateAttributes(org.eclipse.cloudfree.toolkit.widgets.Widget, org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedWidget, org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer)
	 */
	@Override
	protected ISerializedWidget populateAttributes(final Widget widget, final ISerializedWidget serializedWidget, final SContainer parent) {
		final Button button = (Button) widget;
		final SButton sButton = (SButton) serializedWidget;
		sButton.command = serializeCommand(button.getCommand(), button);
		return super.populateAttributes(button, sButton, parent);
	}
}
