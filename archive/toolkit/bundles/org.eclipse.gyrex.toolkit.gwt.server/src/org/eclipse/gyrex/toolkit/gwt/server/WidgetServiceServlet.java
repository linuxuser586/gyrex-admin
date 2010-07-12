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
package org.eclipse.gyrex.toolkit.gwt.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.eclipse.gyrex.toolkit.gwt.server.internal.TargetedRemoteServiceServlet;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetAdapterFactory;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyProvider;

/**
 * This servlet is responsible for delivering Gyrex widgets to any GWT
 * client.
 * <p>
 * A {@link IWidgetFactory} is used to lookup containers be their ids. The
 * {@link IWidgetFactory} must be set before the widget service can be
 * initialized. Thus, it's not possible to use the widget service out of the box
 * in a servlet container. It must either be configured by a subclass or by a
 * container that allows injection of the {@link IWidgetFactory}.
 * </p>
 * <p>
 * This class is intended to be instantiated and/or extended by clients.
 * However, clients may also use the {@link WidgetService} directly in case they
 * don't need a full servlet.
 * </p>
 */
public class WidgetServiceServlet extends HttpServlet {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** the internal GWT remote service servlet */
	private TargetedRemoteServiceServlet remoteServiceServlet;

	/** the widget service */
	private WidgetService widgetService;

	/** the widget factory */
	private IWidgetFactory widgetFactory;

	/** the widget adapter factory */
	private IWidgetAdapterFactory widgetAdapterFactory;

	/** delegate class loader */
	private ClassLoader classLoader;

	/** delegate serialization policy provider */
	private SerializationPolicyProvider serializationPolicyProvider;

	/**
	 * Destroys the widget service.
	 * <p>
	 * This releases any resources acquired by the widget service (such as
	 * caches, etc.).
	 * </p>
	 * <p>
	 * When overriding this form of the method, call
	 * <code>super.destroy()</code>.
	 * </p>
	 * 
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	@Override
	public void destroy() {
		if (null != remoteServiceServlet) {
			remoteServiceServlet.destroy();
			remoteServiceServlet = null;
		}
		super.destroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		if (null == remoteServiceServlet) {
			throw new ServletException("The widget service servlet was not initialized properly!");
		}

		remoteServiceServlet.doPost(req, resp);
	}

	private ClassLoader getDefaultClassLoader() {
		return this.getClass().getClassLoader();
	}

	/**
	 * Initializes the widget service.
	 * <p>
	 * This is final to enforce a proper API. Subclasses can overwrite
	 * {@link #initialize()} to perform sub class specific implementation
	 * behavior.
	 * </p>
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public final void init() throws ServletException {
		initialize();

		if (null == widgetFactory) {
			throw new ServletException("The widget service servlet has no widget factory!");
		}

		// create internal service
		widgetService = new WidgetService(widgetFactory, widgetAdapterFactory);

		// determine class loader
		ClassLoader classLoader = this.classLoader;
		if (null == classLoader) {
			classLoader = getDefaultClassLoader();
		}

		// create remote service servlet
		remoteServiceServlet = new TargetedRemoteServiceServlet(widgetService, classLoader, serializationPolicyProvider);

		// initialize internal servlet
		remoteServiceServlet.init(getServletConfig());
	}

	/**
	 * Called by {@link #init()} to allow sub class specific initialization
	 * behavior.
	 * <p>
	 * Typically a {@link IWidgetFactory} and {@link IWidgetAdapterFactory} will
	 * be configured via {@link #setWidgetFactory(IWidgetFactory)} and
	 * {@link #setWidgetAdapterFactory(IWidgetAdapterFactory)}.
	 * </p>
	 */
	protected void initialize() throws ServletException {
		// empty
	}

	/**
	 * Sets the class loader to use for loading classes during deserialization.
	 * <p>
	 * This may be used in an OSGi environment to delegate class loading to a
	 * specific bundle class loader.
	 * </p>
	 * <p>
	 * This method has no effect after the servlet has been initialized. It must
	 * be calles before {@link #init()} is called.
	 * </p>
	 * 
	 * @param classLoader
	 *            the class loader to set (use <code>null</code> to use a
	 *            default class loader)
	 */
	public void setClassLoader(final ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * Sets the {@link SerializationPolicyProvider} to use for reading the
	 * serialization policy.
	 * <p>
	 * This may be used in an OSGi environment to delegate
	 * {@link SerializationPolicy}. This can be useful for example if the
	 * compiled GWT client is located in a different bundle than the service
	 * implementation and/or the URL (alias in OSGi) to the service does not
	 * start with the module base URL.
	 * </p>
	 * <p>
	 * This method has no effect after the servlet has been initialized. It must
	 * be calles before {@link #init()} is called.
	 * </p>
	 * 
	 * @param serializationPolicyProvider
	 *            the {@link SerializationPolicyProvider} to set (use
	 *            <code>null</code> to use a default behavior implemented in
	 *            {@link RemoteServiceServlet}).
	 */
	public void setSerializationPolicyProvider(final SerializationPolicyProvider serializationPolicyProvider) {
		this.serializationPolicyProvider = serializationPolicyProvider;
	}

	/**
	 * Sets the {@link IWidgetAdapterFactory} for resolving widget adapters.
	 * <p>
	 * This method has no effect after the servlet has been initialized. It must
	 * be calles before {@link #init()} is called.
	 * </p>
	 * 
	 * @param widgetAdapterFactory
	 *            the widget adapter factory to set
	 */
	public void setWidgetAdapterFactory(final IWidgetAdapterFactory widgetAdapterFactory) {
		this.widgetAdapterFactory = widgetAdapterFactory;
	}

	/**
	 * Sets the {@link IWidgetFactory} for resolving Toolkit widgets.
	 * <p>
	 * This method has no effect after the servlet has been initialized. It must
	 * be calles before {@link #init()} is called.
	 * </p>
	 * 
	 * @param widgetFactory
	 *            the widget factory to set
	 */
	public void setWidgetFactory(final IWidgetFactory widgetFactory) {
		this.widgetFactory = widgetFactory;
	}
}
