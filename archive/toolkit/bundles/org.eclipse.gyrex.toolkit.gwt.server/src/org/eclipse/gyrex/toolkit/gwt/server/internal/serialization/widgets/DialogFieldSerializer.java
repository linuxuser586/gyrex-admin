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
import org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization.WidgetSerializer;
import org.eclipse.cloudfree.toolkit.widgets.DialogField;
import org.eclipse.cloudfree.toolkit.widgets.Widget;

/**
 * {@link DialogField} serializer.
 */
public abstract class DialogFieldSerializer extends WidgetSerializer {

	/**
	 * Creates the {@link SDialogField}.
	 * <p>
	 * Subclasses must implement to create the {@link SDialogField} instance.
	 * </p>
	 * 
	 * @param dialogField
	 *            the dialog field to serialize
	 * @param parent
	 *            the serialized parent
	 * @return a new {@link SDialogField} instance
	 */
	protected abstract SDialogField createSDialogField(DialogField dialogField, SContainer parent);

	@Override
	protected ISerializedWidget populateAttributes(final Widget widget, final ISerializedWidget serializedWidget, final SContainer parent) {
		final DialogField dialogField = (DialogField) widget;
		final SDialogField sDialogField = (SDialogField) serializedWidget;
		sDialogField.label = dialogField.getLabel();
		sDialogField.description = dialogField.getDescription();
		sDialogField.required = dialogField.isRequired();
		sDialogField.readOnly = dialogField.isReadOnly();
		sDialogField.enablementRule = serializeDialogFieldRule(dialogField.getEnablementRule(), dialogField);
		return super.populateAttributes(widget, serializedWidget, parent);
	}

	/**
	 * Serializes a {@link DialogField dialog field} into a
	 * {@link SDialogField serializable dialog field} including all attributes.
	 * <p>
	 * Typically, subclasses don't need to overwrite this method but implement
	 * {@link #createSDialogField(DialogField, SContainer)}.
	 * </p>
	 * 
	 * @param widget
	 *            the dialog field widget (must be a subclass of
	 *            {@link DialogField})
	 * @param parent
	 *            the serialized parent.
	 * @return the serialized dialog field
	 * @see WidgetSerializer#serialize(Widget, SContainer)
	 */
	@Override
	public ISerializedWidget serialize(final Widget widget, final SContainer parent) {
		final DialogField dialogField = (DialogField) widget;
		final SDialogField sDialogField = createSDialogField(dialogField, parent);
		return populateAttributes(dialogField, sDialogField, parent);
	}

}
