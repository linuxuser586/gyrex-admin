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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gyrex.gwt.common.adaptable.AdapterManager;
import org.eclipse.gyrex.gwt.common.adaptable.IsAdaptable;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.DialogFieldRuleEventHandler;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogFieldRule;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SWidget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class that renders a {@link ISerializedWidget serialized widget} for
 * <code>org.eclipse.gyrex.toolkit.widgets.Widget</code>.
 * <p>
 * Typically, rendered widgets are instantiated from the rendering framework.
 * The rendering framework then calls
 * {@link #init(ISerializedWidget, CWTToolkit)} with the serialized widget and
 * the rendering toolkit to initiate the rendering process.
 * </p>
 * <p>
 * CWT widgets use the adaptable pattern to support optional features (event
 * sourcing, focus handling, etc). See {@link #getAdapter(Class)} for details.
 * </p>
 * <p>
 * Note, this class is intended to be subclassed by clients that want to provide
 * custom or customized widgets.
 * </p>
 * <p>
 * Although this class extends
 * <code>com.google.gwt.user.client.ui.Composite</code> it must be considered an
 * implementation detail. Clients must not rely on API provided by
 * {@link Composite}.
 * </p>
 */
public abstract class CWTWidget extends Composite implements IsAdaptable {

	private static final class VisibilityHandler extends DialogFieldRuleEventHandler implements CWTToolkitListener {

		/** widget */
		private final CWTWidget widget;

		/**
		 * Creates a new instance.
		 * 
		 * @param visibilityRule
		 * @param dialogField
		 */
		private VisibilityHandler(final SDialogFieldRule visibilityRule, final CWTWidget widget) {
			super(visibilityRule, widget.getParentContainer()); // enablement is evaluated at the container root (for convenience reasons)
			this.widget = widget;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.DialogFieldRuleHelper.RuleEventHandler#handleRuleEvaluationResult(boolean)
		 */
		@Override
		protected void handleRuleEvaluationResult(final boolean evaluationResult) {
			widget.setVisible(evaluationResult);
		}
	}

	/** the adaptable type hierarchy */
	private static final Class[] ADAPTABLE_TYPE_HIERARCHY = new Class[] { CWTWidget.class };

	/** the serialized widget */
	private ISerializedWidget serializedWidget;

	/** the rendering toolkit */
	private CWTToolkit toolkit;

	/** the rendering parent */
	private CWTContainer parentContainer;

	/** visibility handler */
	private VisibilityHandler visibilityHandler;

	private Map<String, Object> data;

	/**
	 * Checks if the widget has been properly initialized
	 */
	private void checkInitialized() {
		if ((null == serializedWidget) || (null == toolkit)) {
			CWTToolkit.error(CWTToolkit.ERROR_NOT_INITIALIZED);
		}
	}

	/**
	 * Returns the adaptable hierarchy of this widget for the adapter manager.
	 * 
	 * @return the adaptable hierarchy
	 */
	protected Class[] getAdaptableHierarchy() {
		return ADAPTABLE_TYPE_HIERARCHY;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.gwt.common.adaptable.IsAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAdapter(final Class<T> adapter) {
		if (Widget.class == adapter) {
			// see http://code.google.com/p/google-web-toolkit/issues/detail?id=2710
			return (T) getWidget();
		}

		return AdapterManager.getAdapterManager().getAdapter(adapter, getAdaptableHierarchy());
	}

	/**
	 * Returns a widget data attribute.
	 * 
	 * @param <T>
	 *            the attribute type
	 * @param name
	 *            the attribute name
	 * @return the attribute value
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData(final String name) {
		if (null == data) {
			return null;
		}
		return (T) data.get(name);
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
	 * Returns the serialized widget.
	 * 
	 * @return the serialized widget
	 */
	public final ISerializedWidget getSerializedWidget() {
		checkInitialized();
		return serializedWidget;
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
	 * Returns the widget id.
	 * 
	 * @return the widget id
	 */
	public final String getWidgetId() {
		return getSerializedWidget().getId();
	}

	/**
	 * Initializes the widget composite with the serialized widget.
	 * <p>
	 * This method will be called with the serialized widget. Subsequent calls
	 * will fail once the serializes widget has been initialized.
	 * </p>
	 * 
	 * @param serializedWidget
	 *            the serialized widget
	 * @param toolkit
	 *            the rendering toolkit
	 */
	protected final void init(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		if ((null != this.serializedWidget) || (null != this.toolkit)) {
			CWTToolkit.error(CWTToolkit.ERROR_ALREADY_INITIALIZED, getWidgetId());
		}

		if (null == serializedWidget) {
			CWTToolkit.error(CWTToolkit.ERROR_NULL_ARGUMENT, "serializedWidget (in widget '" + getWidgetId() + "')");
		}
		this.serializedWidget = serializedWidget;

		if (null == toolkit) {
			CWTToolkit.error(CWTToolkit.ERROR_NULL_ARGUMENT, "toolkit (in widget '" + getWidgetId() + "')");
		}
		this.toolkit = toolkit;

		Widget renderedWidget = null;
		try {
			renderedWidget = render(serializedWidget, toolkit);
			if (null == renderedWidget) {
				CWTToolkit.error(CWTToolkit.ERROR_NOT_IMPLEMENTED, "rendering " + serializedWidget.getClass().getName() + " returned null in " + this.getClass().getName());
			}
		} catch (final Throwable e) {
			CWTToolkit.error(CWTToolkit.ERROR_INVALID_ARGUMENT, e, "rendering error in " + this.getClass().getName() + ": " + e.getMessage());
		}
		initWidget(renderedWidget);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.google.gwt.user.client.ui.Widget#onLoad()
	 */
	@Override
	protected void onLoad() {
		// call super
		super.onLoad();

		// attach to the toolkit
		getToolkit().widgetAttached(this);

		// handle visibility
		final ISerializedWidget serializedWidget = getSerializedWidget();
		if (serializedWidget instanceof SWidget) {
			final SDialogFieldRule visibilityRule = ((SWidget) serializedWidget).visibilityRule;
			if (null != visibilityRule) {
				// add change listener
				visibilityHandler = new VisibilityHandler(visibilityRule, this);
				getToolkit().addChangeListener(visibilityHandler);

				// set initial enablement state
				visibilityHandler.evaluate();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.google.gwt.user.client.ui.Widget#onUnload()
	 */
	@Override
	protected void onUnload() {
		// detach from the toolkit
		try {
			getToolkit().widgetDetached(this);
		} finally {
			// call super
			super.onUnload();
		}
	}

	/**
	 * Renders the specified serialized widget into a GWT widget.
	 * <p>
	 * Subclasses must implement this method to render the serialized widget
	 * into the GWT widget. It will be called by
	 * {@link #init(ISerializedWidget, CWTToolkit)} during initialization of the
	 * serialized widget. The GWT widget returned is then forwarded to
	 * {@link #initWidget(Widget)} to initialize this composite. Thus,
	 * {@link #initWidget(Widget)} must not be called during rendering.
	 * </p>
	 * 
	 * @param serializedWidget
	 *            the serialized widget
	 * @param toolkit
	 *            the rendering toolkit
	 * @return the rendered GWT widget
	 */
	protected abstract Widget render(ISerializedWidget serializedWidget, CWTToolkit toolkit);

	/**
	 * Sets or unsets a widget data attribute.
	 * 
	 * @param <T>
	 *            the attribute type
	 * @param name
	 *            the attribute name
	 * @param value
	 *            the attribute value to set (maybe <code>null</code> to unset)
	 */
	public <T> void setData(final String name, final T value) {
		if (null == data) {
			data = new HashMap<String, Object>(3);
		}
		if (null != value) {
			data.put(name, value);
		} else {
			data.remove(name);
		}
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
			CWTToolkit.error(CWTToolkit.ERROR_PARENT_ALREADY_SET, "widget '" + getWidgetId() + "'");
		}
		if (null == parent) {
			CWTToolkit.error(CWTToolkit.ERROR_NULL_ARGUMENT, "parent (in widget '" + getWidgetId() + "')");
		}
		parentContainer = parent;
	}
}
