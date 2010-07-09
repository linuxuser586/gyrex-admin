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
package org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.widgets;


import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogFieldGroup;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldGroup;
import org.eclipse.gyrex.toolkit.widgets.Widget;

/**
 * {@link DialogFieldGroup} serializer
 */
public class DialogFieldGroupSerializer extends ContainerSerializer {

	@Override
	protected SContainer createSContainer(final Container container, final SContainer parent) {
		return new SDialogFieldGroup();
	}

	@Override
	protected ISerializedWidget populateAttributes(final Widget widget, final ISerializedWidget serializedWidget, final SContainer parent) {
		final DialogFieldGroup dialogFieldGroup = (DialogFieldGroup) widget;
		final SDialogFieldGroup sDialogFieldGroup = (SDialogFieldGroup) serializedWidget;
		sDialogFieldGroup.title = dialogFieldGroup.getLabel();
		sDialogFieldGroup.description = dialogFieldGroup.getDescription();
		return super.populateAttributes(widget, serializedWidget, parent);
	}
}
