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

import org.eclipse.gyrex.toolkit.Toolkit;
import org.eclipse.gyrex.toolkit.commands.Command;
import org.eclipse.gyrex.toolkit.content.ContentSet;
import org.eclipse.gyrex.toolkit.rap.client.WidgetFactory;
import org.eclipse.gyrex.toolkit.rap.internal.ui.commands.ExecuteCommandCallback;
import org.eclipse.gyrex.toolkit.widgets.Widget;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * The toolkit is responsible for creating SWT/JFace/Forms UI widgets from Gyrex
 * widgets. In addition to applying default presentation properties (styles,
 * etc.), various listeners are attached to make them behave correctly in the
 * Gyrex context.
 * <p>
 * Typically, one toolkit object is created per widget services and not shared
 * between them.
 * </p>
 * <p>
 * {@link CWTToolkit} is normally instantiated, but can also be subclassed if
 * some of the methods needs to be modified. In those cases, <code>super</code>
 * must be called to preserve normal behaviour.
 * </p>
 * <p>
 * Note, this is experimental API. It will not be stable until version 1.0 is
 * officially released. Currently this API lacks support for localization,
 * content handling, resource handling and authorization.
 * </p>
 */
public class CWTToolkit {

	static class DefaultWidget<T extends Widget> extends CWTWidget<T> {

		/* (non-Javadoc)
		 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTWidget#createWidgetControl(org.eclipse.swt.widgets.Composite)
		 */
		@Override
		protected Control createWidgetControl(final Composite parent) {
			return getToolkit().getFormToolkit().createLabel(parent, getWidgetId());
		}

	}

	private final class ToolkitChangeListener implements IChangeListener {
		private final CWTWidget widget;

		private ToolkitChangeListener(final CWTWidget widget) {
			this.widget = widget;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final ToolkitChangeListener other = (ToolkitChangeListener) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (widget == null) {
				if (other.widget != null) {
					return false;
				}
			} else if (!widget.equals(other.widget)) {
				return false;
			}
			return true;
		}

		private CWTToolkit getOuterType() {
			return CWTToolkit.this;
		}

		@Override
		public void handleChange(final ChangeEvent event) {
			fireWidgetChanged(widget);
		}

		@Override
		public int hashCode() {
			final int prime = 41;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((widget == null) ? 0 : widget.hashCode());
			return result;
		}
	}

	private WidgetFactory widgetFactory;
	private final FormToolkit formToolkit;

	private List<CWTToolkitListener> changeListeners;

	/**
	 * Creates a new instance.
	 * 
	 * @param formToolkit
	 *            the {@link FormToolkit} to use
	 */
	public CWTToolkit(final FormToolkit formToolkit) {
		this.formToolkit = formToolkit;
	}

	/**
	 * Adds a change listener to the list of listeners.
	 * <p>
	 * This method has no effect if an identical listener is already registered.
	 * </p>
	 * 
	 * @param changeListener
	 *            the change listener to add
	 */
	public final void addChangeListener(final CWTToolkitListener changeListener) {
		if (null == changeListener) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "changeListener");
		}
		if (null == changeListeners) {
			changeListeners = new ArrayList<CWTToolkitListener>();
		}
		if (!changeListeners.contains(changeListener)) {
			changeListeners.add(changeListener);
		}
	}

	/**
	 * Creates a default widget if the specified widget could not be rendered.
	 * 
	 * @param widget
	 * @return the default widget
	 */
	public <T extends Widget> CWTWidget<T> createDefaultWidget(final T widget) {
		final DefaultWidget<T> emptyComposite = new DefaultWidget<T>();
		emptyComposite.init(widget, this);
		return emptyComposite;
	}

	/**
	 * Creates the {@link CWTWidget} hierarchy for the specified widget.
	 * 
	 * @param widget
	 *            the widget
	 * @return the corresponding {@link CWTWidget} (maybe <code>null</code> if
	 *         unable to render)
	 */
	@SuppressWarnings("unchecked")
	public <T extends Widget> CWTWidget<T> createWidget(final T widget) {

		String widgetClassName = widget.getClass().getPackage().getName();
		if (widgetClassName.startsWith("org.eclipse.gyrex.toolkit.")) {
			widgetClassName = widgetClassName.substring("org.eclipse.gyrex.toolkit.".length());
		}

		widgetClassName = "org.eclipse.gyrex.toolkit.rap.internal.ui.".concat(widgetClassName);
		widgetClassName = widgetClassName.concat(".Toolkit").concat(widget.getClass().getSimpleName());

		try {
			final CWTWidget<T> cwtWidget = (CWTWidget<T>) getClass().getClassLoader().loadClass(widgetClassName).newInstance();
			cwtWidget.init(widget, this);
			return cwtWidget;
		} catch (final ClassNotFoundException e) {
			// not found
		} catch (final Exception e) {
			Toolkit.error(Toolkit.ERROR_WIDGET_INITIALIZATION_FAILED, e, NLS.bind("class{0} for widget type {1}", widgetClassName, widget.getClass().getName()));
		}

		// TODO support extensible widget creation
		return null;
	}

	/**
	 * Executes the specified command
	 * 
	 * @param command
	 * @param sourceWidget
	 * @param contentSet
	 * @param executeCommandCallback
	 */
	public void executeCommand(final Command command, final Widget sourceWidget, final ContentSet contentSet, final ExecuteCommandCallback executeCommandCallback) {
		// TODO Auto-generated method stub

	}

	/**
	 * Fires a widget change event to all registered listener.
	 * 
	 * @param source
	 *            the widget which changed
	 */
	void fireWidgetChanged(final CWTWidget source) {
		if (null == source) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "source");
		}

		if (null == changeListeners) {
			return;
		}

		final Object[] listeners = changeListeners.toArray();
		for (int i = 0; i < listeners.length; i++) {
			((CWTToolkitListener) listeners[i]).widgetChanged(source);
		}
	}

	/**
	 * Returns the {@link FormToolkit}.
	 * <p>
	 * TODO: We should support non-forms UI eventually.
	 * </p>
	 * 
	 * @return the form toolkit
	 */
	public FormToolkit getFormToolkit() {
		return formToolkit;
	}

	/**
	 * Returns the widget factory.
	 * 
	 * @return the widget factory
	 */
	public WidgetFactory getWidgetFactory() {
		return widgetFactory;
	}

	/**
	 * Removes a change listener from the list of listeners.
	 * <p>
	 * This method has no effect if the listener wasn't registered before.
	 * </p>
	 * 
	 * @param changeListener
	 *            the change listener to remove
	 */
	public final void removeChangeListener(final CWTToolkitListener changeListener) {
		if (null == changeListener) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "changeListener");
		}
		if (null != changeListeners) {
			changeListeners.remove(changeListener);
		}
	}

	/**
	 * Sets the widget factory that owns this toolkit instance.
	 * <p>
	 * This method is called by the {@link WidgetFactory} and must not be called
	 * by clients directly.
	 * </p>
	 * 
	 * @param widgetFactory
	 *            the widget factory to set
	 */
	public void setWidgetFactory(final WidgetFactory widgetFactory) {
		if (null == widgetFactory) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "widgetFactory");
		}
		if (null != this.widgetFactory) {
			Toolkit.error(Toolkit.ERROR_ALREADY_INITIALIZED);
		}
		this.widgetFactory = widgetFactory;
	}

	/**
	 * This method is called when an underlying SWT control is created.
	 * <p>
	 * Typically, the toolkit adds various listeners to the widget to observe
	 * its state and activate its specific behavior.
	 * </p>
	 * 
	 * @param widget
	 *            the widget to attach
	 */
	void widgetCreated(final CWTWidget widget) {
		if (null == widget) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "widget");
		}

		// listen for changes
		final IObservable observable = (IObservable) widget.getAdapter(IObservable.class);
		if (null != observable) {
			observable.addChangeListener(new ToolkitChangeListener(widget));
		}
	}

	/**
	 * This method is called when an underlying SWT widget is disposed.
	 * <p>
	 * Typically, the toolkit removes listeners registered in
	 * {@link #widgetCreated(CWTWidget)}.
	 * </p>
	 * 
	 * @param widget
	 *            the widget to detach
	 */
	void widgetDisposed(final CWTWidget widget) {
		if (null == widget) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "widget");
		}

		// remove change listener
		final IObservable observable = (IObservable) widget.getAdapter(IObservable.class);
		if (null != observable) {
			observable.removeChangeListener(new ToolkitChangeListener(widget));
		}
	}
}
