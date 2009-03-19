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
package org.eclipse.gyrex.toolkit.gwt.server.internal;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


import org.eclipse.gyrex.gwt.common.status.IStatus;
import org.eclipse.gyrex.gwt.common.status.MultiStatus;
import org.eclipse.gyrex.gwt.common.status.Status;
import org.eclipse.gyrex.toolkit.actions.Action;
import org.eclipse.gyrex.toolkit.actions.RefreshAction;
import org.eclipse.gyrex.toolkit.actions.ShowWidgetAction;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetClientEnvironment;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactoryException;
import org.eclipse.gyrex.toolkit.gwt.client.internal.WidgetService;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.actions.SAction;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.actions.SRefreshAction;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.actions.SShowWidgetAction;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.commands.SCommandExecutionResult;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SContentSet;
import org.eclipse.gyrex.toolkit.gwt.server.WidgetServiceAdvisor;
import org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.ContentSerialization;
import org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.ToolkitSerialization;
import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.gyrex.toolkit.runtime.commands.CommandExecutionEvent;
import org.eclipse.gyrex.toolkit.runtime.commands.CommandExecutionResult;
import org.eclipse.gyrex.toolkit.runtime.commands.ICommandHandler;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetAdapterFactory;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory;
import org.eclipse.gyrex.toolkit.widgets.Widget;

/**
 * This class is the internal {@link WidgetService} implementation.
 * <p>
 * It is considered an implementation detail. Clients may not rely on any public
 * API provided by this class directly or indirectly through framework classes
 * extending this class.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class WidgetServiceImpl implements WidgetService {

	public static boolean debugPerformance = true;

	private static String createDetailInfo(final Throwable caugth) {
		PrintStream printStream = null;
		try {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			printStream = new PrintStream(out, true, "UTF-8");
			caugth.printStackTrace(printStream);
			return out.toString("UTF-8");
		} catch (final UnsupportedEncodingException e1) {
			throw new IllegalStateException("UTF-8 not supported on this platform");
		} finally {
			if (null != printStream) {
				printStream.close();
			}
		}
	}

	/** the advisor */
	private final WidgetServiceAdvisor advisor;

	/**
	 * Creates a new instance.
	 * 
	 * @param advisor
	 *            the advisor
	 */
	public WidgetServiceImpl(final WidgetServiceAdvisor advisor) {
		if (null == advisor) {
			throw new IllegalStateException("advisor must not be null");
		}
		this.advisor = advisor;
	}

	private SCommandExecutionResult createSCommandExecutionResult(final CommandExecutionResult result) {
		if (result == null) {
			return null;
		}

		final SCommandExecutionResult sResult = new SCommandExecutionResult();
		sResult.id = result.getCommandId();
		sResult.status = serializeStatus(result.getStatus());

		final Action[] actions = result.getActions();
		if (actions != null) {
			sResult.actions = new SAction[actions.length];
			for (int i = 0; i < actions.length; i++) {
				final Action action = actions[i];
				sResult.actions[i] = serializeAction(action);
			}
		}

		return sResult;
	}

	private IWidgetEnvironment createWidgetEnvironment(final WidgetClientEnvironment environment) {
		return getWidgetServiceAdvisor().getWidgetEnvironment(environment);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.gwt.client.internal.WidgetService#executeCommand(java.lang.String, java.lang.String, org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SContentSet, org.eclipse.gyrex.toolkit.gwt.client.WidgetFactoryEnvironment)
	 */
	public final SCommandExecutionResult executeCommand(final String commandId, final String sourceWidgetId, final SContentSet contentSet, final WidgetClientEnvironment environment) throws WidgetFactoryException {
		// get adapter factory
		final IWidgetAdapterFactory widgetAdapterFactory = getWidgetAdapterFactory();
		if (null == widgetAdapterFactory) {
			return null; // TODO: should indicate that no factory is available
		}

		final IWidgetEnvironment widgetEnvironment = createWidgetEnvironment(environment);

		// TODO: we need to traverse the full widget hierarchy here in order to find an adapter!!!

		// get command handler
		final ICommandHandler commandHandler = widgetAdapterFactory.getAdapter(sourceWidgetId, ICommandHandler.class, widgetEnvironment);
		if (null == commandHandler) {
			return null; // TODO: should indicate that no handler is available
		}

		// prepare execution event
		final CommandExecutionEvent executionEvent = new CommandExecutionEvent(commandId, sourceWidgetId, ContentSerialization.deserializeContentSet(contentSet), widgetEnvironment);

		// execute command
		final CommandExecutionResult result = commandHandler.execute(executionEvent, null);

		return createSCommandExecutionResult(result);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.gwt.client.internal.WidgetService#getWidget(java.lang.String, org.eclipse.gyrex.toolkit.gwt.client.WidgetFactoryEnvironment)
	 */
	public final ISerializedWidget getWidget(final String widgetId, final WidgetClientEnvironment environment) throws WidgetFactoryException {
		// get the factory
		final IWidgetFactory widgetFactory = getWidgetFactory();
		if (null == widgetFactory) {
			throw new WidgetFactoryException(WidgetFactoryException.INVALID_WIDGET_FACTORY, "No widget factory available!");
		}

		// lookup view
		final Widget widget = lookupWidget(widgetId, widgetFactory, environment);
		if (null == widget) {
			throw new WidgetFactoryException(WidgetFactoryException.WIDGET_NOT_FOUND, "Widget not found: " + widgetId);
		}

		// serialize
		final ISerializedWidget serializedWidget = serializeWidget(widget);
		if (null == serializedWidget) {
			throw new WidgetFactoryException(WidgetFactoryException.WIDGET_SERIALIZATION_FAILED, "Unable to serialize widget: " + widget.getClass().getName());
		}

		// TODO: cache result

		return serializedWidget;
	}

	protected IWidgetAdapterFactory getWidgetAdapterFactory() {
		return getWidgetServiceAdvisor().getWidgetAdapterFactory();
	}

	protected IWidgetFactory getWidgetFactory() {
		final IWidgetFactory widgetFactory = getWidgetServiceAdvisor().getWidgetFactory();
		if (null == widgetFactory) {
			throw new IllegalStateException("invalid advisor: " + getWidgetServiceAdvisor().getClass().getName() + " (did not return a widget factory)");
		}
		return widgetFactory;
	}

	/**
	 * Returns the widget service advisor.
	 * 
	 * @return the widget service advisor
	 */
	protected WidgetServiceAdvisor getWidgetServiceAdvisor() {
		// null check happens in constructor
		//if (null == advisor) {
		//	throw new IllegalStateException("not initialized");
		//}
		return advisor;
	}

	private Widget lookupWidget(final String widgetId, final IWidgetFactory widgetFactory, final WidgetClientEnvironment environment) throws WidgetFactoryException {
		try {
			return widgetFactory.getWidget(widgetId, createWidgetEnvironment(environment));
		} catch (final RuntimeException e) {
			final String message = "Widget factory exception: " + e;
			throw new WidgetFactoryException(WidgetFactoryException.WIDGET_FACTORY_EXCEPTION, message, createDetailInfo(e), e);
		}
	}

	private SAction serializeAction(final Action action) {
		// TODO should be externalized
		if (action instanceof ShowWidgetAction) {
			final SShowWidgetAction showWidgetAction = new SShowWidgetAction();
			showWidgetAction.widgetId = ((ShowWidgetAction) action).getWidgetId();
			return showWidgetAction;
		} else if (action instanceof RefreshAction) {
			final SRefreshAction refreshAction = new SRefreshAction();
			refreshAction.delay = ((RefreshAction) action).getDelay();
			return refreshAction;
		}
		return null;
	}

	private org.eclipse.gyrex.gwt.common.status.IStatus serializeStatus(final org.eclipse.core.runtime.IStatus status) {
		if (status.isMultiStatus()) {
			final org.eclipse.core.runtime.IStatus[] children = status.getChildren();
			final List<IStatus> newChildren = new ArrayList<IStatus>(children.length);
			for (final org.eclipse.core.runtime.IStatus child : children) {
				newChildren.add(serializeStatus(child));
			}
			return new MultiStatus(status.getPlugin(), status.getCode(), newChildren.toArray(new IStatus[newChildren.size()]), status.getMessage(), status.getException());
		}
		return new Status(status.getSeverity(), status.getPlugin(), status.getCode(), status.getMessage(), status.getException());
	}

	private ISerializedWidget serializeWidget(final Widget widget) throws WidgetFactoryException {
		final long start = debugPerformance ? System.currentTimeMillis() : 0;
		try {
			return ToolkitSerialization.serializeWidget(widget);
		} catch (final RuntimeException e) {
			final String message = "Unable to serialize widget: " + widget.getClass().getName();
			throw new WidgetFactoryException(WidgetFactoryException.WIDGET_SERIALIZATION_FAILED, message, createDetailInfo(e), e);
		} finally {
			if (debugPerformance) {
				System.out.println(MessageFormat.format("[WidgetService] [PERFORMANCE] Widget ''{0}'': {1}ms", widget.getId(), System.currentTimeMillis() - start));
			}
		}
	}
}
