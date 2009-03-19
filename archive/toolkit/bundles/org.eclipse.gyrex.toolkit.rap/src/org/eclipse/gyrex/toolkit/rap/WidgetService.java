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
package org.eclipse.gyrex.toolkit.rap;


import org.eclipse.gyrex.toolkit.commands.Command;
import org.eclipse.gyrex.toolkit.content.ContentSet;
import org.eclipse.gyrex.toolkit.rap.internal.WidgetServiceImpl;
import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.gyrex.toolkit.runtime.commands.CommandExecutionResult;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetAdapterFactory;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory;
import org.eclipse.gyrex.toolkit.widgets.Widget;

/**
 * This service is responsible for delivering Gyrex widgets to any RAP
 * client.
 * <p>
 * A {@link IWidgetFactory} is used to lookup widgets be their ids. The
 * {@link IWidgetAdapterFactory} is used to lookup widget adapters.
 * </p>
 * <p>
 * This class is intended to be instantiated and/or extended by clients.
 * </p>
 */
public class WidgetService extends WidgetServiceImpl {

	/**
	 * Convenience constructor to create a new service instance.
	 * <p>
	 * Calling this constructor is equivalent to calling
	 * <code>new WidgetService(widgetFactory, null)</code>.
	 * </p>
	 * 
	 * @param widgetFactory
	 *            the widget factory (may not be <code>null</code>)
	 * @see #WidgetService(IWidgetFactory, IWidgetAdapterFactory)
	 */
	public WidgetService(final IWidgetFactory widgetFactory) {
		this(widgetFactory, null);
	}

	/**
	 * Convenience constructor to create a new service instance using a default
	 * widget environment.
	 * 
	 * @param widgetFactory
	 *            the widget factory (may not be <code>null</code>)
	 * @param widgetAdapterFactory
	 *            the widget adapter factory ()
	 * @see WidgetServiceAdvisor#WidgetServiceAdvisor(IWidgetFactory,
	 *      IWidgetAdapterFactory)
	 */
	public WidgetService(final IWidgetFactory widgetFactory, final IWidgetAdapterFactory widgetAdapterFactory) {
		this(new WidgetServiceAdvisor(widgetFactory, widgetAdapterFactory));
	}

	/**
	 * Creates a new instance using the specified service advisor.
	 * 
	 * @param widgetServiceAdvisor
	 *            the widget service advisor (may not be <code>null</code>)
	 */
	public WidgetService(final WidgetServiceAdvisor widgetServiceAdvisor) {
		super(widgetServiceAdvisor);
	}

	/**
	 * Triggers execution of the specified command.
	 * 
	 * @param command
	 *            the command
	 * @param source
	 *            the if of the widget which triggered the command
	 * @param contentSet
	 *            the content set
	 * @param environment
	 *            the widget factory environment
	 */
	public CommandExecutionResult executeCommand(final Command command, final Widget source, final ContentSet contentSet, final IWidgetEnvironment environment) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the widget for the specified widget id.
	 * 
	 * @param widgetId
	 *            the widget id
	 * @param environment
	 *            the widget environment
	 * @return the widget
	 */
	public Widget getWidget(final String widgetId, final IWidgetEnvironment environment) {
		return getWidgetFactory().getWidget(widgetId, environment);
	}

	/**
	 * Returns the widget adapter factory used by this service.
	 * 
	 * @return the widget adapter factory
	 */
	@Override
	public final IWidgetAdapterFactory getWidgetAdapterFactory() {
		return super.getWidgetAdapterFactory();
	}

	/**
	 * Returns the widget factory used by this service.
	 * 
	 * @return the widget factory
	 */
	@Override
	public final IWidgetFactory getWidgetFactory() {
		return super.getWidgetFactory();
	}

	/**
	 * Returns the widget service advisor used by this service.
	 * 
	 * @return the widget service advisor
	 */
	@Override
	public WidgetServiceAdvisor getWidgetServiceAdvisor() {
		return super.getWidgetServiceAdvisor();
	}

	/**
	 * Opens the widget with the specified id.
	 * 
	 * @param widgetId
	 */
	@Override
	public void openWidget(final String widgetId, final IWidgetServiceUICallback callback) {
		super.openWidget(widgetId, callback);
	}

}
