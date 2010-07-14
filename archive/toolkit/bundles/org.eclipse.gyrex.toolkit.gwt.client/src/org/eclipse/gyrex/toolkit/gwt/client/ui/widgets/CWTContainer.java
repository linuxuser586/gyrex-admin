/*******************************************************************************
 * Copyright (c) 2008, 2009 Gunnar Wagenknecht and others.
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedLayoutHint;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.layout.SGridLayout;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.layout.SGridLayoutHint;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SWidget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Composite for <code>org.eclipse.gyrex.toolkit.widgets.Container</code>.
 */
public class CWTContainer extends CWTWidget {

	private static final class ContainerWidget extends SimplePanel {

		private final Element content;

		/**
		 * Creates a new instance.
		 */
		public ContainerWidget(final String title, final String description) {
			super(DOM.createDiv());
			final Element div = getElement();

			if ((title != null) && (title.length() > 0)) {
				final Element titleElement = DOM.createElement("h3");
				titleElement.setInnerText(title);
				titleElement.setClassName("cwt-Container-Title");
				div.appendChild(titleElement);
			}

			if ((description != null) && (description.length() > 0)) {
				final Element descriptionElement = DOM.createDiv();
				descriptionElement.setInnerText(description);
				descriptionElement.setClassName("cwt-Container-Description");
				div.appendChild(descriptionElement);
			}

			content = DOM.createDiv();
			div.appendChild(content);
			setStyleName("cwt-Container");
		}

		@Override
		protected Element getContainerElement() {
			return content;
		}
	}

	private class NonRemovableIterator implements Iterator {

		private final Iterator delegete;

		public NonRemovableIterator(final Iterator delegete) {
			this.delegete = delegete;
		}

		public boolean hasNext() {
			if (null == delegete) {
				return false;
			}
			return delegete.hasNext();
		}

		public Object next() {
			if (null == delegete) {
				throw new NoSuchElementException();
			}
			return delegete.next();
		}

		public void remove() {
			throw new UnsupportedOperationException("remove not supported");
		}
	}

	/** the {@link CWTContainer} adaptable hierarchy */
	private static final Class[] CONTAINER_ADAPTABLE_HIERARCHY = new Class[] { CWTContainer.class, CWTWidget.class };
	private Panel panel;
	private int currentRow;
	private int currentCol;

	private int numColumns = 1;

	private List<CWTWidget> children;

	/**
	 * Adds a widget to this container.
	 * <p>
	 * This method sets the container as the widget's parent and calls
	 * {@link #addToPanel(CWTWidget)} to add the widget to the UI.
	 * </p>
	 * 
	 * @param widget
	 *            the child to add
	 */
	void addToContainer(final CWTWidget widget) {
		if (null == children) {
			children = new ArrayList<CWTWidget>(3);
		}

		widget.setParentContainer(this);
		children.add(widget);

		addToPanel(widget);
	}

	protected void addToPanel(final CWTWidget composite) {
		if (panel instanceof FlexTable) {
			final FlexTable flexTable = (FlexTable) panel;
			flexTable.setWidget(currentRow, currentCol, composite);
			if (null != composite) {
				final SGridLayoutHint sGridLayoutHint = getGridLayoutHint(composite.getSerializedWidget());
				if (null != sGridLayoutHint) {
					if (sGridLayoutHint.spanColumns > 1) {
						flexTable.getFlexCellFormatter().setColSpan(currentRow, currentCol, sGridLayoutHint.spanColumns);
						for (int i = 1; i < sGridLayoutHint.spanColumns; i++) {
							nextColumn();
						}
					}
				}
			}
			nextColumn();
		} else if (null != composite) {
			panel.add(composite);
		}
	}

	/**
	 * Returns an iterator for the container children.
	 * <p>
	 * Note, the returned iterator does not support removal.
	 * </p>
	 * 
	 * @return the iterator of the container children
	 */
	public Iterator childrenIterator() {
		return new NonRemovableIterator(null != children ? children.iterator() : null);
	}

	/**
	 * Creates and returns the panel to host the child widgets.
	 * <p>
	 * This method is called by {@link #render(ISerializedWidget, CWTToolkit)}
	 * to create the panel for the child widgets. Subclasses must return a
	 * {@link Panel} that supports {@link Panel#add(Widget)}.
	 * </p>
	 * 
	 * @param serializedWidget
	 *            the serialized widget
	 * @param toolkit
	 *            the toolkit
	 * @return a panel to host the child widgets
	 */
	protected Panel createPanel(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SContainer container = (SContainer) serializedWidget;
		if (container.layout instanceof SGridLayout) {
			final SGridLayout gridLayout = (SGridLayout) container.layout;
			final FlexTable table = new FlexTable();
			numColumns = gridLayout.numberOfColumns;
			return table;
		}

		// FlowPanel is the default
		return new FlowPanel();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget#getAdaptableHierarchy()
	 */
	@Override
	protected Class[] getAdaptableHierarchy() {
		return CONTAINER_ADAPTABLE_HIERARCHY;
	}

	/**
	 * Returns the list of child widgets.
	 * 
	 * @return the child widgets
	 */
	List<CWTWidget> getChildWidgets() {
		return children;
	}

	/**
	 * Returns the container description text.
	 * 
	 * @return the container description text
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public String getContainerDescriptionText() {
		final String description = getSContainer().description;
		return null != description ? description : "";
	}

	/**
	 * Returns the container title text.
	 * 
	 * @return the container title text
	 */
	public String getContainerTitleText() {
		final String title = getSContainer().label;
		return null != title ? title : "";
	}

	private SGridLayoutHint getGridLayoutHint(final ISerializedWidget widget) {
		if (!(widget instanceof SWidget)) {
			return null;
		}

		final SWidget sWidget = (SWidget) widget;
		final ISerializedLayoutHint[] layoutHints = sWidget.layoutHints;
		if ((null == layoutHints) || (layoutHints.length == 0)) {
			return null;
		}

		for (int i = 0; i < layoutHints.length; i++) {
			final ISerializedLayoutHint serializedLayoutHint = layoutHints[i];
			if (serializedLayoutHint instanceof SGridLayoutHint) {
				return (SGridLayoutHint) serializedLayoutHint;
			}
		}
		return null;
	}

	/**
	 * Returns the panel.
	 * 
	 * @return the panel
	 */
	protected Panel getPanel() {
		return panel;
	}

	private SContainer getSContainer() {
		return (SContainer) getSerializedWidget();
	}

	/**
	 * Sets the panel to host the child widgets.
	 * 
	 * @param panel
	 */
	protected void initPanel(final Panel panel) {
		if (this.panel != null) {
			throw new IllegalStateException("panel already set");
		}
		this.panel = panel;
	}

	private void nextColumn() {
		if (currentCol < (numColumns - 1)) {
			currentCol++;
		} else {
			currentRow++;
			currentCol = 0;
		}
	}

	protected void populateChildren(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SContainer container = (SContainer) serializedWidget;
		final ISerializedWidget[] widgets = container.widgets;
		if (null != widgets) {
			for (int i = 0; i < widgets.length; i++) {
				final ISerializedWidget child = widgets[i];
				final CWTWidget composite = toolkit.createWidget(child);
				addToContainer(composite);
			}
		}
	}

	@Override
	protected Widget render(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SContainer container = (SContainer) serializedWidget;

		final Panel panel = createPanel(serializedWidget, toolkit);
		assert panel != null;

		// set container id as style if not empty
		final String id = container.id;
		if ((null != id) && (id.trim().length() > 0)) {
			panel.addStyleName(id);
		}

		initPanel(panel);
		populateChildren(serializedWidget, toolkit);

		// wrap the panel into a container widget if desired
		if (shouldRenderTitleAndDescription(serializedWidget, toolkit)) {
			final ContainerWidget containerWidget = new ContainerWidget(container.label, container.description);
			containerWidget.setWidget(panel);
			return containerWidget;
		} else {
			return panel;
		}
	}

	/**
	 * Indicates if the container class should wrap the panel into a custom
	 * widget which also displays title and description if available.
	 * 
	 * @param serializedWidget
	 * @param toolkit
	 * @return <code>true</code> if it should be wrapped, <code>false</code>
	 *         otherwise
	 */
	private boolean shouldRenderTitleAndDescription(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		return serializedWidget.getClass().equals(SContainer.class);
	}
}
