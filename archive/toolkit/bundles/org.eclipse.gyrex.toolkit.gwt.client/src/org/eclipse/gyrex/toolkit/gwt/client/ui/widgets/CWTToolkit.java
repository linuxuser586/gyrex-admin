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
import java.util.List;

import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactory;
import org.eclipse.gyrex.toolkit.gwt.client.ui.actions.IActionHandler;
import org.eclipse.gyrex.toolkit.gwt.client.ui.wizard.CWTWizardContainer;
import org.eclipse.gyrex.toolkit.gwt.client.ui.wizard.CWTWizardPage;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.actions.SRefreshAction;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.actions.SShowWidgetAction;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SButton;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SCheckbox;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogFieldGroup;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SEmailInput;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SNumberInput;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SPasswordInput;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SRadioButton;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SStyledText;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.STextInput;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.wizard.SWizardContainer;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.wizard.SWizardPage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The toolkit is responsible for creating GWT widgets from serialized Gyrex
 * widgets. In addition to applying default presentation properties (styles,
 * etc.), various listeners are attached to make them behave correctly in the
 * Gyrex context.
 * <p>
 * Typically, one toolkit object is created per widget factory and not shared
 * between factories.
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

	static class EmptyComposite extends CWTWidget {

		@Override
		protected Widget render(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
			return new Label(getWidgetId());
		}
	}

	private final class ToolkitChangeListener implements ChangeHandler {
		private final CWTWidget widget;

		private ToolkitChangeListener(final CWTWidget widget) {
			this.widget = widget;
		}

		@Override
		public void onChange(final ChangeEvent event) {
			fireWidgetChanged(widget);
		}
	}

	/** CHANGE_HANDLER */
	private static final String CHANGE_HANDLER = "__CHANGE_HANDLER";

	private static final IActionHandler DEFAULT_ACTION_HANDLER = new IActionHandler() {
		public void handleAction(final Object action) {
			if (action instanceof SShowWidgetAction) {
				final String widgetId = ((SShowWidgetAction) action).widgetId;
				if (null != widgetId) {
					History.newItem(widgetId);
				}
			} else if (action instanceof SRefreshAction) {
				final int delay = ((SRefreshAction) action).delay;
				final String token = History.getToken();
				final Timer timer = new Timer() {
					@Override
					public void run() {
						History.newItem(token);
					}
				};
				if (delay == 0) {
					timer.run();
				} else {
					timer.schedule(delay);
				}
			}
		}
	};

	/**
	 * CWTToolkit error constant indicating that no error number was specified
	 * (value is 1).
	 */
	public static final int ERROR_UNSPECIFIED = 1;

	/**
	 * CWTToolkit error constant indicating that a null argument was passed in
	 * (value is 2).
	 */
	public static final int ERROR_NULL_ARGUMENT = 2;

	/**
	 * CWTToolkit error constant indicating that an invalid argument was passed
	 * in (value is 3).
	 */
	public static final int ERROR_INVALID_ARGUMENT = 3;

	/**
	 * CWTToolkit error constant indicating that a particular feature has not
	 * been implemented on this platform (value is 4).
	 */
	public static final int ERROR_NOT_IMPLEMENTED = 4;

	/**
	 * CWTToolkit error constant indicating that the receiver's parent has
	 * already been set (value is 5).
	 */
	public static final int ERROR_PARENT_ALREADY_SET = 5;

	/**
	 * CWTToolkit error constant indicating that the receiver has already been
	 * initialized (value is 6).
	 */
	public static final int ERROR_ALREADY_INITIALIZED = 6;

	/**
	 * CWTToolkit error constant indicating that the receiver has not been
	 * initialized yet (value is 7).
	 */
	public static final int ERROR_NOT_INITIALIZED = 7;

	/** constant for a missing resource */
	private static final String NO_RESOURCE = "";

	/**
	 * Throws an appropriate exception based on the passed in error code.
	 * 
	 * @param code
	 *            the CWT error code
	 * @see CWTError
	 * @see CWTException
	 * @see IllegalArgumentException
	 */
	public static void error(final int code) throws IllegalArgumentException, CWTToolkitException {
		error(code, null, null);
	}

	/**
	 * Throws an appropriate exception based on the passed in error code and
	 * detail.
	 * <p>
	 * The <code>throwable</code> argument should be either null, or the
	 * throwable which caused CWT to throw an exception.
	 * </p>
	 * 
	 * @param code
	 *            the CWT error code
	 * @param detail
	 *            more information about error (maybe <code>null</code>)
	 * @see CWTError
	 * @see CWTException
	 * @see IllegalArgumentException
	 */
	public static void error(final int code, final String detail) throws IllegalArgumentException, CWTToolkitException {
		error(code, null, detail);
	}

	/**
	 * Throws an appropriate exception based on the passed in error code and
	 * throwable.
	 * <p>
	 * The <code>throwable</code> argument should be either null, or the
	 * throwable which caused CWT to throw an exception.
	 * </p>
	 * 
	 * @param code
	 *            the CWT error code
	 * @param throwable
	 *            the exception which caused the error to occur (maybe
	 *            <code>null</code>)
	 * @see CWTError
	 * @see CWTException
	 * @see IllegalArgumentException
	 */
	public static void error(final int code, final Throwable throwable) throws IllegalArgumentException, CWTToolkitException {
		error(code, throwable, null);
	}

	/**
	 * Throws an appropriate exception based on the passed in error code. The
	 * <code>throwable</code> argument should be either null, or the throwable
	 * which caused CWT to throw an exception.
	 * <p>
	 * In CWT, errors are reported by throwing one of three exceptions:
	 * <dl>
	 * <dd>IllegalArgumentException</dd>
	 * <dt>thrown whenever one of the API methods is invoked with an illegal
	 * argument</dt>
	 * <dd>{@link CWTException} (extends {@link java.lang.RuntimeException})</dd>
	 * <dt>thrown whenever a recoverable error happens internally in CWT</dt>
	 * <dd>{@link CWTError} (extends {@link java.lang.Error})</dd>
	 * <dt>thrown whenever a <b>non-recoverable</b> error happens internally in
	 * CWT</dt>
	 * </dl>
	 * This method provides the logic which maps between error codes and one of
	 * the above exceptions.
	 * </p>
	 * 
	 * @param code
	 *            the CWT error code.
	 * @param throwable
	 *            the exception which caused the error to occur (maybe
	 *            <code>null</code>)
	 * @param detail
	 *            more information about error (maybe <code>null</code>)
	 * @see CWTError
	 * @see CWTException
	 * @see IllegalArgumentException
	 */
	public static void error(final int code, final Throwable throwable, final String detail) throws IllegalArgumentException, CWTToolkitException {

		/*
		 * This code prevents the creation of "chains" of CWTToolkitException
		 * which in turn contain other CWTToolkitException as their throwable.
		 * This can occur when low level code throws an exception past a point
		 * where a higher layer is being "safe" and catching all exceptions.
		 * (Note that, this is _a_bad_thing_ which we always try to avoid.) On
		 * the theory that the low level code is closest to the original
		 * problem, we simply re-throw the original exception here.
		 */
		if (throwable instanceof CWTToolkitException) {
			throw (CWTToolkitException) throwable;
		}

		String message = findErrorText(code);
		if (detail != null) {
			message += "; " + detail;
		}
		switch (code) {

			/* Illegal Arguments (non-fatal) */
			case ERROR_NULL_ARGUMENT:
			case ERROR_INVALID_ARGUMENT:
				throw new IllegalArgumentException(message);

				/* CWT Errors (fatal, may occur only on some platforms) */
			case ERROR_NOT_IMPLEMENTED:
			case ERROR_UNSPECIFIED:
				throw new CWTToolkitException(code, message, throwable);
		}

		/* Unknown/Undefined Error */
		throw new CWTToolkitException(code, message, throwable);
	}

	/**
	 * Answers a concise, human readable description of the error code.
	 * 
	 * @param code
	 *            the CWT error code.
	 * @return a description of the error code.
	 * @see CWT
	 */
	static String findErrorText(final int code) {
		switch (code) {
			case ERROR_UNSPECIFIED:
				return "Unspecified error"; //$NON-NLS-1$
			case ERROR_NULL_ARGUMENT:
				return "Argument cannot be null"; //$NON-NLS-1$
			case ERROR_INVALID_ARGUMENT:
				return "Argument not valid"; //$NON-NLS-1$
			case ERROR_NOT_IMPLEMENTED:
				return "Not implemented"; //$NON-NLS-1$
			case ERROR_PARENT_ALREADY_SET:
				return "Parent already set"; //$NON-NLS-1$
			case ERROR_ALREADY_INITIALIZED:
				return "Already initialized"; //$NON-NLS-1$
			case ERROR_NOT_INITIALIZED:
				return "Not initialized"; //$NON-NLS-1$
		}
		return "Unknown error"; //$NON-NLS-1$
	}

	private List<CWTToolkitListener> changeListeners;
	private WidgetFactory widgetFactory;
	private IActionHandler actionHandler;

	/**
	 * Creates and returns a new toolkit.
	 */
	public CWTToolkit() {
		super();
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
			error(ERROR_NULL_ARGUMENT, "changeListener");
		}
		if (null == changeListeners) {
			changeListeners = new ArrayList<CWTToolkitListener>();
		}
		if (!changeListeners.contains(changeListener)) {
			changeListeners.add(changeListener);
		}
	}

	/**
	 * Creates an default widget.
	 * <p>
	 * The default widget is usually just a label that display the widget id.
	 * </p>
	 * 
	 * @param serializedWidget
	 *            the serialized widget
	 * @return a default widget wrapping the serialized widget
	 */
	public CWTWidget createDefaultWidget(final ISerializedWidget serializedWidget) {
		final EmptyComposite emptyComposite = new EmptyComposite();
		emptyComposite.init(serializedWidget, this);
		return emptyComposite;
	}

	CWTWidget createUninitializedWidgetInstance(final Class<? extends ISerializedWidget> serializedWidgetType) {
		if (null == serializedWidgetType) {
			throw new IllegalArgumentException("serializedWidgetType must not be null");
		}

		if (serializedWidgetType.equals(SContainer.class)) {
			return GWT.create(CWTContainer.class);
		}

		if (serializedWidgetType.equals(SStyledText.class)) {
			return GWT.create(CWTStyledText.class);
		}

		if (serializedWidgetType.equals(SDialogFieldGroup.class)) {
			return GWT.create(CWTDialogFieldGroup.class);
		}
		if (serializedWidgetType.equals(SRadioButton.class)) {
			return GWT.create(CWTRadioButton.class);
		}
		if (serializedWidgetType.equals(SCheckbox.class)) {
			return GWT.create(CWTCheckbox.class);
		}
		if (serializedWidgetType.equals(SButton.class)) {
			return GWT.create(CWTButton.class);
		}

		if (serializedWidgetType.equals(STextInput.class)) {
			return GWT.create(CWTTextInput.class);
		}
		if (serializedWidgetType.equals(SPasswordInput.class)) {
			return GWT.create(CWTPasswordInput.class);
		}
		if (serializedWidgetType.equals(SEmailInput.class)) {
			return GWT.create(CWTEmailInput.class);
		}
		if (serializedWidgetType.equals(SNumberInput.class)) {
			return GWT.create(CWTNumberInput.class);
		}

		if (serializedWidgetType.equals(SWizardContainer.class)) {
			return GWT.create(CWTWizardContainer.class);
		}
		if (serializedWidgetType.equals(SWizardPage.class)) {
			return GWT.create(CWTWizardPage.class);
		}

		throw new IllegalArgumentException(serializedWidgetType.getName() + " not mapped to a widget composite");

		// see: http://code.google.com/p/google-web-toolkit/issues/detail?id=1266
		//		Class widgetCompositeClass= (Class) compositeClassBySerializedWidgetTypeName.get(serializedWidgetTypeName);
		//		if (null == widgetCompositeClass)
		//			throw new IllegalArgumentException(serializedWidgetTypeName + " not mapped to a widgetCompositeClass");
		//		return (RenderedWidget) GWT.create(widgetCompositeClass);
	}

	/**
	 * Creates the GWT widget hierarchy for the specified serialized widget.
	 * 
	 * @param serializedWidget
	 *            the serialized widget
	 * @return the corresponding {@link CWTWidget} (maybe <code>null</code> if
	 *         unable to render)
	 */
	public CWTWidget createWidget(final ISerializedWidget serializedWidget) {
		final CWTWidget cwtWidget = createUninitializedWidgetInstance(serializedWidget.getClass());
		if (null != cwtWidget) {
			cwtWidget.init(serializedWidget, this);
		}
		return cwtWidget;
	}

	/**
	 * Fires a widget change event to all registered listener.
	 * 
	 * @param source
	 *            the widget which changed
	 */
	void fireWidgetChanged(final CWTWidget source) {
		if (null == source) {
			error(ERROR_NULL_ARGUMENT, "source");
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
	 * Returns a handler for processing actions.
	 * 
	 * @return
	 */
	public IActionHandler getActionHandler() {
		if (null != actionHandler) {
			return actionHandler;
		}

		return DEFAULT_ACTION_HANDLER;
	}

	/**
	 * Returns an URL for accessing the specified resource.
	 * <p>
	 * The default implementation uses the base resource URL provided by
	 * {@link WidgetFactory#getResourceBaseUrl()} to build the resource URL in
	 * the form
	 * <code>getWidgetFactory().getResourceBaseUrl().concat(reference)</code>.
	 * Therefore, the server side counterpart is required to export the widget
	 * resources under this URL.
	 * </p>
	 * <p>
	 * Subclasses may overwrite to prepare a more sophisticate approach which
	 * might also append authentication information to the URL.
	 * </p>
	 * 
	 * @param reference
	 *            the resource reference
	 * @return the URL to the resource reference on the server
	 */
	public String getResourceUrl(final String reference) {
		if (reference == null) {
			return getWidgetFactory().getResourceBaseUrl().concat(NO_RESOURCE);
		}
		return getWidgetFactory().getResourceBaseUrl().concat(reference);
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
			error(ERROR_NULL_ARGUMENT, "changeListener");
		}
		if (null != changeListeners) {
			changeListeners.remove(changeListener);
		}
	}

	//	/**
	//	 * Allows to set a custom action handler.
	//	 *
	//	 * @param actionHandler
	//	 *            the action handler to set (maybe <code>null</code> to unset)
	//	 */
	//	public void setActionHandler(final IActionHandler actionHandler) {
	//		// TODO: confirm API (come up with some better API)
	//		this.actionHandler = actionHandler;
	//	}

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
			CWTToolkit.error(CWTToolkit.ERROR_NULL_ARGUMENT, "widgetFactory");
		}
		if (null != this.widgetFactory) {
			CWTToolkit.error(CWTToolkit.ERROR_ALREADY_INITIALIZED);
		}
		this.widgetFactory = widgetFactory;
	}

	/**
	 * This method is called when an underlying GWT widget is attached to the
	 * browser's document.
	 * <p>
	 * Typically, the toolkit adds various listeners to the widget to observe
	 * its state and activate its specific behavior.
	 * </p>
	 * 
	 * @param widget
	 *            the widget to attach
	 */
	void widgetAttached(final CWTWidget widget) {
		if (null == widget) {
			error(ERROR_NULL_ARGUMENT, "widget");
		}

		final HasChangeHandlers changeEventSource = widget.getAdapter(HasChangeHandlers.class);
		if (null != changeEventSource) {
			final HandlerRegistration changeHandlerRegistration = changeEventSource.addChangeHandler(new ToolkitChangeListener(widget));
			widget.setData(CHANGE_HANDLER, changeHandlerRegistration);
		}
	}

	/**
	 * This method is called when an underlying GWT widget is detached from the
	 * browser's document.
	 * <p>
	 * Typically, the toolkit removes listeners registered in
	 * {@link #widgetAttached(CWTWidget)}.
	 * </p>
	 * 
	 * @param widget
	 *            the widget to detach
	 */
	void widgetDetached(final CWTWidget widget) {
		if (null == widget) {
			error(ERROR_NULL_ARGUMENT, "widget");
		}

		final HandlerRegistration changeHandlerRegistration = widget.getData(CHANGE_HANDLER);
		if (null != changeHandlerRegistration) {
			changeHandlerRegistration.removeHandler();
		}
	}
}
