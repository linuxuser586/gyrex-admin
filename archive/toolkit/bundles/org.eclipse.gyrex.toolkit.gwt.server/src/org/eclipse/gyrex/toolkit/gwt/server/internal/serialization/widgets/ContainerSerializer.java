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
import org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization.ToolkitSerialization;
import org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization.WidgetSerializer;
import org.eclipse.cloudfree.toolkit.layout.Layout;
import org.eclipse.cloudfree.toolkit.widgets.Container;
import org.eclipse.cloudfree.toolkit.widgets.Widget;

/**
 * {@link Container} serializer.
 */
public class ContainerSerializer extends WidgetSerializer {

	/**
	 * Creates the {@link SContainer}.
	 * <p>
	 * Subclasses may overwrite to create a different {@link SContainer}
	 * instance.
	 * </p>
	 * 
	 * @param container
	 *            the container to serialize
	 * @param parent
	 *            the serialized parent
	 * @return a new {@link SContainer} instance
	 */
	protected SContainer createSContainer(Container container, SContainer parent) {
		return new SContainer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization.WidgetSerializer#populateAttributes(org.eclipse.cloudfree.toolkit.widgets.Widget,
	 *      org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedWidget,
	 *      org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer)
	 */
	@Override
	protected ISerializedWidget populateAttributes(Widget widget, ISerializedWidget serializedWidget, SContainer parent) {
		Container container = (Container) widget;
		SContainer sContainer = (SContainer) serializedWidget;
		sContainer.title = container.getTitle();
		sContainer.description = container.getDescription();
		return super.populateAttributes(container, sContainer, parent);
	}

	/**
	 * Serializes a {@link Container container widget} into a
	 * {@link SContainer serializable container} including all container
	 * children and attributes.
	 * <p>
	 * Typically, subclasses just overwrite {@link #createSContainer(Container)}
	 * instead of this method.
	 * </p>
	 * 
	 * @param widget
	 *            the container widget (must be a subclass of {@link Container})
	 * @param parent
	 *            the serialized parent.
	 * @return the serialized container
	 * @see WidgetSerializer#serialize(Widget, SContainer)
	 */
	@Override
	public ISerializedWidget serialize(Widget widget, SContainer parent) {
		Container container = (Container) widget;
		SContainer sContainer = createSContainer(container, parent);

		// children widgets
		serializeContainerChildren(container, sContainer);

		// layout
		serializeLayout(container, sContainer);

		// base attributes
		return populateAttributes(container, sContainer, parent);
	}

	/**
	 * Serializes the children of a container.
	 * 
	 * @param container
	 *            the container to read the children from
	 * @param sContainer
	 *            the SContainer to write the children to
	 */
	protected void serializeContainerChildren(Container container, SContainer sContainer) {
		Widget[] widgets = container.getWidgets();
		if (widgets.length > 0) {
			sContainer.widgets = new ISerializedWidget[widgets.length];
			for (int i = 0; i < sContainer.widgets.length; i++) {
				sContainer.widgets[i] = ToolkitSerialization.serializeWidget(widgets[i], sContainer);
			}
		}
	}

	/**
	 * Serializes the layout of a container.
	 * 
	 * @param container
	 *            the container to read the children from
	 * @param sContainer
	 *            the SContainer to write the children to
	 */
	protected void serializeLayout(Container container, SContainer sContainer) {
		Layout layout = container.getLayout();
		if (null == layout)
			return;

		sContainer.layout = ToolkitSerialization.serializeLayout(layout);
	}
}
