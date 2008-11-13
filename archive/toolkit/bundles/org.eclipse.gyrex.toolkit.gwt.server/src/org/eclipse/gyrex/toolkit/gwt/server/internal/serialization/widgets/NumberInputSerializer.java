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
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogField;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SNumberInput;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SNumberInput.Type;
import org.eclipse.cloudfree.toolkit.widgets.DialogField;
import org.eclipse.cloudfree.toolkit.widgets.NumberInput;
import org.eclipse.cloudfree.toolkit.widgets.Widget;

/**
 * {@link NumberInput} serializer.
 */
public class NumberInputSerializer extends DialogFieldSerializer {

	@Override
	protected SDialogField createSDialogField(final DialogField dialogField, final SContainer parent) {
		return new SNumberInput();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization.widgets.DialogFieldSerializer#populateAttributes(org.eclipse.cloudfree.toolkit.widgets.Widget,
	 *      org.eclipse.cloudfree.toolkit.gwt.client.rwt.ISerializedWidget,
	 *      org.eclipse.cloudfree.toolkit.gwt.client.internal.stoolkit.widgets.SContainer)
	 */
	@Override
	protected ISerializedWidget populateAttributes(final Widget widget, final ISerializedWidget serializedWidget, final SContainer parent) {
		final NumberInput numberInput = (NumberInput) widget;
		final SNumberInput sNumberInput = (SNumberInput) serializedWidget;
		final Type type = Type.valueOf(numberInput.getType().name());
		if (null != type) {
			sNumberInput.type = type;
		}
		if (null != numberInput.getUpperLimit()) {
			sNumberInput.upperLimit = numberInput.getUpperLimit();
			sNumberInput.upperLimitInclusive = numberInput.isUpperLimitInclusive();
		}
		if (null != numberInput.getLowerLimit()) {
			sNumberInput.lowerLimit = numberInput.getLowerLimit();
			sNumberInput.lowerLimitInclusive = numberInput.isLowerLimitInclusive();
		}
		return super.populateAttributes(widget, serializedWidget, parent);
	}

}
