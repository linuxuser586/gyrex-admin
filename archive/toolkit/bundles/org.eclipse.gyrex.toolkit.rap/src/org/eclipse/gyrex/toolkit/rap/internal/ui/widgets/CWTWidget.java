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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gyrex.toolkit.CWT;
import org.eclipse.gyrex.toolkit.runtime.internal.fixme.AdapterManagerAccess;
import org.eclipse.gyrex.toolkit.widgets.Widget;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Base class that renders a {@link Widget CWT widget} for Eclipse
 * SWT/JFace/Forms UI.
 * <p>
 * Typically, rendered widgets are instantiated via from the rendering
 * framework. The rendering framework then calls
 * {@link #init(Widget, CWTToolkit)} with the widget and the rendering toolkit
 * to initiate the rendering process. The actual SWT control is created lazily
 * in a later step when {@link #createControl(Composite)} is called.
 * </p>
 * <p>
 * CWT widgets use the adaptable pattern to support optional features (event
 * sourcing, focus handling, etc). See {@link #getAdapter(Class)} for details.
 * </p>
 * <p>
 * Note, this class is intended to be subclassed by clients that want to provide
 * custom or customized widgets.
 * </p>
 */
public abstract class CWTWidget<T extends Widget> implements IAdaptable {

	private T widget;
	private CWTToolkit toolkit;
	private CWTContainer parentContainer;

	/** widget dispose notify */
	private final DisposeListener disposeListener = new DisposeListener() {

		@Override
		public void widgetDisposed(final DisposeEvent event) {
			CWTWidget.this.widgetDisposed(event);
		}
	};

	private Control control;

	private void checkInitialized() {
		if ((null == widget) || (null == toolkit)) {
			CWT.error(CWT.ERROR_NOT_INITIALIZED);
		}
	}

	/**
	 * Creates the top level control for this widget under the given parent
	 * composite.
	 * 
	 * @param parent
	 *            the parent composite
	 */
	public final void createControl(final Composite parent) {
		final Control control = createWidgetControl(parent);
		if (control == null) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "null control returned");
		}

		// set control
		this.control = control;

		// attach to the toolkit
		getToolkit().widgetCreated(this);

		// hook dispose listener
		this.control.addDisposeListener(disposeListener);

		// notify
		onWidgetControlCreated();
	}

	/**
	 * Creates and returns the top level control for this widget under the given
	 * parent composite.
	 * <p>
	 * This method is called by {@link #createControl(Composite)}. The returned
	 * control can be accessed via {@link #getControl()} later.
	 * </p>
	 * 
	 * @param parent
	 *            the parent composite
	 * @return the created control (may not be <code>null</code>)
	 */
	protected abstract Control createWidgetControl(final Composite parent);

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(final Class adapter) {
		return AdapterManagerAccess.getAdapterManager().getAdapter(this, adapter);
	}

	/**
	 * Returns the top level control for this widget.
	 * <p>
	 * May return <code>null</code> if the control has not been created yet. In
	 * this case, {@link #createControl(Composite)} may be used to create it.
	 * </p>
	 * 
	 * @return the top level control or <code>null</code>
	 */
	public Control getControl() {
		checkInitialized();
		return control;
	}

	/**
	 * Returns the parent container.
	 * 
	 * @return the parent container
	 */
	public final CWTContainer getParentContainer() {
		checkInitialized();
		return parentContainer;
	}

	/**
	 * Returns the rendering toolkit.
	 * 
	 * @return the rendering toolkit
	 */
	protected CWTToolkit getToolkit() {
		checkInitialized();
		return toolkit;
	}

	/**
	 * Returns the serialized widget.
	 * 
	 * @return the serialized widget
	 */
	public final T getWidget() {
		checkInitialized();
		return widget;
	}

	/**
	 * Returns the widget id.
	 * 
	 * @return the widget id
	 */
	public final String getWidgetId() {
		checkInitialized();
		return getWidget().getId();
	}

	/**
	 * Initializes the CWT widget.
	 * <p>
	 * This method will be called with the CWT widget. Subsequent calls will
	 * fail once the widget has been initialized.
	 * </p>
	 * 
	 * @param widget
	 *            the serialized widget
	 * @param toolkit
	 *            the rendering toolkit
	 */
	protected final void init(final T widget, final CWTToolkit toolkit) {
		if ((null != this.widget) || (null != this.toolkit)) {
			CWT.error(CWT.ERROR_ALREADY_INITIALIZED, getWidgetId());
		}

		if (null == widget) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "widget (in widget '" + getWidgetId() + "')");
		}
		this.widget = widget;

		if (null == toolkit) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "toolkit (in widget '" + getWidgetId() + "')");
		}
		this.toolkit = toolkit;
	}

	/**
	 * This method is called immediately after a widget control is created.
	 * <p>
	 * The default implementation does nothing. Subclasses may extend.
	 * </p>
	 */
	protected void onWidgetControlCreated() {
	}

	/**
	 * This method is called immediately after a widget control is disposed.
	 * <p>
	 * The default implementation does nothing. Subclasses may extend.
	 * </p>
	 */
	protected void onWidgetControlDisposed() {

	}

	/**
	 * Sets the parent.
	 * <p>
	 * This method will be called when the widget is added to a container.
	 * Subsequent calls will fail once the parent container has been set.
	 * </p>
	 * 
	 * @param parent
	 *            the parent to set
	 */
	final void setParentContainer(final CWTContainer parent) {
		if (null != parentContainer) {
			CWT.error(CWT.ERROR_PARENT_ALREADY_SET, "widget '" + getWidgetId() + "'");
		}
		if (null == parent) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "parent (in widget '" + getWidgetId() + "')");
		}
		parentContainer = parent;
	}

	final void widgetDisposed(final DisposeEvent event) {
		try {
			// notify
			onWidgetControlDisposed();
		} finally {
			// remove from toolkit
			getToolkit().widgetDisposed(CWTWidget.this);

			// free control
			this.control = null;
		}
	}

}
