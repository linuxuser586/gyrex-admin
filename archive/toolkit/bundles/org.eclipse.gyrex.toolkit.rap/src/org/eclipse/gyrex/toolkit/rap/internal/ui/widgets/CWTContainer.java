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
package org.eclipse.gyrex.toolkit.rap.internal.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.widgets.Widget;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * Renders a <code>org.eclipse.gyrex.toolkit.widgets.Container</code> into
 * SWT/JFace/Forms UI.
 */
public class CWTContainer<T extends Container> extends CWTWidget<T> {

	private static final CWTWidget[] NO_CHILDREN = new CWTWidget[0];

	private Composite composite;
	private List<CWTWidget<?>> children;

	/**
	 * Adds the widget to the composite.
	 * 
	 * @param composite
	 */
	protected void addToComposite(final CWTWidget<Widget> child) {
		if (child.getControl() == null) {
			child.createControl(getComposite());
		}

		//		if (getWidget().getWidgets().length > 1) {
		//		final LayoutHint[] layoutHints = child.getWidget().getLayoutHints();
		//		if (layoutHints.length > 0) {
		//			for (int i = 0; i < layoutHints.length; i++) {
		//				final LayoutHint layoutHint = layoutHints[i];
		//				if (layoutHint instanceof GridLayoutHint) {
		//					final GridLayoutHint gridLayoutHint = (GridLayoutHint) layoutHint;
		//					GridDataFactory.defaultsFor(child.getControl()).span(gridLayoutHint.spanColumns, gridLayoutHint.spanRows).applyTo(child.getControl());
		//				}
		//			}
		//		} else {
		//			GridDataFactory.defaultsFor(child.getControl()).grab(true, false).applyTo(child.getControl());
		//		}
		//		}
	}

	/**
	 * Adds a widget to this container.
	 * <p>
	 * This method sets the container as the widget's parent and calls
	 * {@link #addToComposite(CWTWidget)} to add the widget to the UI.
	 * </p>
	 * 
	 * @param widget
	 *            the child to add
	 */
	void addToContainer(final CWTWidget<Widget> widget) {
		if (null == children) {
			children = new ArrayList<CWTWidget<?>>(3);
		}

		widget.setParentContainer(this);
		children.add(widget);

		addToComposite(widget);
	}

	/**
	 * Creates and returns the composite to host the child widgets.
	 * <p>
	 * This method is called by {@link #createControl(Composite)} to create the
	 * composite for the child widgets. Subclasses must return a
	 * {@link Composite} that supports adding children to it.
	 * </p>
	 * 
	 * @param parent
	 *            the parent composite
	 * @return a composite to host the child widgets
	 */
	protected Composite createComposite(final Composite parent) {
		final FormToolkit formToolkit = getToolkit().getFormToolkit();

		final Composite composite = formToolkit.createComposite(parent);

		// GridLayout is problematic, we should try to get along with TableWrapLayout

		//		if (getWidget().getLayout() instanceof GridLayout) {
		//			final GridLayout gridLayout = (GridLayout) getWidget().getLayout();
		//			GridLayoutFactory.swtDefaults().numColumns(gridLayout.numberOfColumns).equalWidth(gridLayout.makeColumnsEqualWidth).applyTo(composite);
		//		} else {
		//			GridLayoutFactory.swtDefaults().applyTo(composite);
		//		}

		return composite;
	}

	/**
	 * Creates the top level control for this container under the given parent
	 * composite.
	 * <p>
	 * This container implementation creates a simple composite for "root"
	 * containers but a fully {@link Section} for non-root containers with a
	 * non-empty title.
	 * </p>
	 * 
	 * @param parent
	 *            the parent composite
	 * @return the created control
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTWidget#createWidgetControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createWidgetControl(final Composite parent) {
		final T container = getWidget();

		Control result = null;
		Composite composite = null;
		if ((container.getParent() != null) && (getContainerTitle().length() > 0)) {
			// wrap into composite into Section
			final FormToolkit formToolkit = getToolkit().getFormToolkit();
			final Section outer = formToolkit.createSection(parent, Section.DESCRIPTION | ExpandableComposite.SHORT_TITLE_BAR);
			outer.setText(getContainerTitle());
			outer.setDescription(getContainerDescription());
			composite = createComposite(outer);
			outer.setClient(composite);
			result = outer;
		} else {
			composite = createComposite(parent);
			result = composite;
		}
		assert composite != null;
		assert result != null;

		initComposite(composite);
		populateChildren(getComposite());

		// layout
		//		if (getChildren().length > 1) {
		//			GridLayoutFactory.swtDefaults().generateLayout(composite);
		//		} else {
		//			composite.setLayout(new FillLayout());
		//		}
		// GridLayout is problematic, we should try to get along with TableWrapLayout
		composite.setLayout(new TableWrapLayout());

		return result;
	}

	/**
	 * Returns the children of this container.
	 * 
	 * @return the children
	 */
	public CWTWidget<?>[] getChildren() {
		if (null == children) {
			return NO_CHILDREN;
		}
		return children.toArray(new CWTWidget[children.size()]);
	}

	/**
	 * Returns the composite to host the child widgets.
	 * 
	 * @return the composite to host the child widgets
	 */
	protected Composite getComposite() {
		return composite;
	}

	public String getContainerDescription() {
		final String description = getWidget().getDescription();
		return null != description ? description : "";
	}

	public String getContainerTitle() {
		final String title = getWidget().getLabel();
		return null != title ? title : "";
	}

	/**
	 * Sets the composite to host the child widgets.
	 * 
	 * @param composite
	 */
	protected void initComposite(final Composite composite) {
		if (this.composite != null) {
			throw new IllegalStateException("composite already set");
		}
		this.composite = composite;
	}

	/**
	 * Populates the container children.
	 * 
	 * @param container
	 * @param toolkit
	 */
	protected void populateChildren(final Composite parent) {
		final Widget[] widgets = getWidget().getWidgets();
		for (int i = 0; i < widgets.length; i++) {
			final Widget child = widgets[i];
			final CWTWidget<Widget> cwtWidget = getToolkit().createWidget(child);
			if (null != cwtWidget) {
				addToContainer(cwtWidget);
			}
		}
	}
}
