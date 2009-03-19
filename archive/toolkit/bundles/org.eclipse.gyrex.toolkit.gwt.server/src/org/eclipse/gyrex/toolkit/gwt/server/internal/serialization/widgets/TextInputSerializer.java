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
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogField;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.STextInput;
import org.eclipse.gyrex.toolkit.widgets.DialogField;
import org.eclipse.gyrex.toolkit.widgets.TextInput;
import org.eclipse.gyrex.toolkit.widgets.Widget;

/**
 * {@link TextInput} serializer.
 */
public class TextInputSerializer extends DialogFieldSerializer {

	@Override
	protected SDialogField createSDialogField(DialogField dialogField, SContainer parent) {
		return new STextInput();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.widgets.DialogFieldSerializer#populateAttributes(org.eclipse.gyrex.toolkit.widgets.Widget,
	 *      org.eclipse.gyrex.toolkit.gwt.client.rwt.ISerializedWidget,
	 *      org.eclipse.gyrex.toolkit.gwt.client.internal.stoolkit.widgets.SContainer)
	 */
	@Override
	protected ISerializedWidget populateAttributes(Widget widget, ISerializedWidget serializedWidget, SContainer parent) {
		TextInput textInput = (TextInput) widget;
		STextInput sTextInput = (STextInput) serializedWidget;
		sTextInput.maxLength = textInput.getMaxLength();
		return super.populateAttributes(widget, serializedWidget, parent);
	}

}
