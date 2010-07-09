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
import org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.ToolkitSerialization;
import org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.WidgetSerializer;
import org.eclipse.gyrex.toolkit.layout.Layout;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.widgets.Widget;

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
	protected SContainer createSContainer(final Container container, final SContainer parent) {
		return new SContainer();
	}

	@Override
	protected ISerializedWidget populateAttributes(final Widget widget, final ISerializedWidget serializedWidget, final SContainer parent) {
		final Container container = (Container) widget;
		final SContainer sContainer = (SContainer) serializedWidget;
		sContainer.title = container.getLabel();
		sContainer.description = container.getDescription();
		return super.populateAttributes(container, sContainer, parent);
	}

	/**
	 * Serializes a {@link Container container widget} into a {@link SContainer
	 * serializable container} including all container children and attributes.
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
	public ISerializedWidget serialize(final Widget widget, final SContainer parent) {
		final Container container = (Container) widget;
		final SContainer sContainer = createSContainer(container, parent);

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
	protected void serializeContainerChildren(final Container container, final SContainer sContainer) {
		final Widget[] widgets = container.getWidgets();
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
	protected void serializeLayout(final Container container, final SContainer sContainer) {
		final Layout layout = container.getLayout();
		if (null == layout) {
			return;
		}

		sContainer.layout = ToolkitSerialization.serializeLayout(layout);
	}
}
