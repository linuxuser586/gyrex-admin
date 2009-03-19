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
package org.eclipse.gyrex.gwt.service;

import java.util.Dictionary;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * The GWT Service allows other bundles in the OSGi environment to dynamically
 * register GWT modules and RPC service implementations into the URI namespace
 * of an {@link HttpService}. A bundle may later unregister its GWT modules or
 * RPC service implementations.
 * <p>
 * A GWT Service will be made available for every {@link HttpService} to provide
 * a one-to-one mapping between GWT Service and {@link HttpService}. The GWT
 * Service will inherit service properties from the {@link HttpService} to allow
 * filtering GWT Services based on {@link HttpService} properties.
 * </p>
 * 
 * @see HttpService
 */
public interface GwtService {

	/** prefix for service properties inherited from {@link HttpService} */
	String HTTP_SERVICE_PROPERTY_PREFIX = "http.service.";

	/**
	 * Registers GWT module resources into the URI namespace.
	 * <p>
	 * The alias is the name in the URI namespace of the Http Service at which
	 * the registration will be mapped.
	 * </p>
	 * <p>
	 * An alias must begin with slash ('/') and must not end with slash ('/'),
	 * with the exception that an alias of the form &quot;/&quot; is used to
	 * denote the root alias. The name parameter must also not end with slash
	 * ('/'). See the {@link HttpService} specification text for details on how
	 * HTTP requests are mapped to servlet and resource registrations.
	 * </p>
	 * 
	 * @param alias
	 *            name in the URI namespace at which the GWT module resources
	 *            are registered (eg. <code>&quot;/showcase&quot;</code>)
	 * @param moduleId
	 *            the GWT module id (eg.
	 *            <code>&quot;com.google.gwt.sample.showcase.Showcase&quot;</code>
	 *            )
	 * @param baseName
	 *            the name of the GWT module resources base that will be
	 *            registered (eg.
	 *            <code>&quot;/compiled-gwt-modules/com.google.gwt.sample.showcase.Showcase&quot;</code>
	 *            )
	 * @param defaultName
	 *            the name of the default resource that will be registered (eg.
	 *            <code>&quot;Showcase.html&quot;</code>) or <code>null</code>
	 *            if a default resource is not desired
	 * @param context
	 *            the <code>HttpContext</code> object for the registered
	 *            resources, or <code>null</code> if a default
	 *            <code>HttpContext</code> is to be created and used.
	 * @throws NamespaceException
	 *             if the registration fails because the alias is already in
	 *             use.
	 * @throws java.lang.IllegalArgumentException
	 *             if any of the parameters are invalid
	 * @see HttpService#registerResources(String, String, HttpContext)
	 */
	void registerModule(String alias, String moduleId, String baseName, String defaultName, HttpContext context) throws NamespaceException;

	/**
	 * Registers a GWT remote service into the URI namespace.
	 * <p>
	 * The alias is the name in the URI namespace of the Http Service at which
	 * the registration will be mapped.
	 * </p>
	 * <p>
	 * An alias must begin with slash ('/') and must not end with slash ('/'),
	 * with the exception that an alias of the form &quot;/&quot; is used to
	 * denote the root alias. See the {@link HttpService} specification text for
	 * details on how HTTP requests are mapped to servlet and resource
	 * registrations.
	 * </p>
	 * <p>
	 * The remote service will be exported using an extended
	 * <code>com.google.gwt.user.server.rpc.RemoteServiceServlet</code>. The
	 * extended servlet will make sure that the GWT RPC de-serialization
	 * mechanism uses the contributing bundle's class loader for resolving
	 * classes.
	 * </p>
	 * <p>
	 * Services registered with the same <code>HttpContext</code> object will
	 * share the same <code>ServletContext</code>. The Http Service will call
	 * the <code>context</code> argument to support the
	 * <code>ServletContext</code> methods <code>getResource</code>,
	 * <code>getResourceAsStream</code> and <code>getMimeType</code>, and to
	 * handle security for requests. If the <code>context</code> argument is
	 * <code>null</code>, a default <code>HttpContext</code> object is used (see
	 * {@link #createDefaultHttpContext}).
	 * </p>
	 * <p>
	 * The HttpContext will be proxied to allow accessing resources in the
	 * module.
	 * </p>
	 * 
	 * @param alias
	 *            name in the URI namespace at which the GWT service is
	 *            registered (eg.
	 *            <code>&quot;/showcase/sample-service&quot;</code>)
	 * @param moduleId
	 *            the GWT module id (eg.
	 *            <code>&quot;com.google.gwt.sample.showcase.Showcase&quot;</code>
	 *            )
	 * @param service
	 *            the GWT service object to register
	 * @param requestResponseListener
	 *            an adaptor for listening to GWT remote service communication
	 * @param context
	 *            the <code>HttpContext</code> object for the registered
	 *            servlet, or <code>null</code> if a default
	 *            <code>HttpContext</code> is to be created and used.
	 * @throws org.osgi.service.http.NamespaceException
	 *             if the registration fails because the alias is already in
	 *             use.
	 * @throws java.lang.IllegalStateException
	 *             if the given service object has already been registered at a
	 *             different alias.
	 * @throws java.lang.IllegalArgumentException
	 *             if any of the arguments are invalid
	 */
	void registerRemoteService(String alias, String moduleId, RemoteService service, GwtRequestResponseListener requestResponseListener, HttpContext context) throws ServletException, NamespaceException;

	/**
	 * Registers a servlet into the URI namespace.
	 * <p>
	 * The alias is the name in the URI namespace of the Http Service at which
	 * the registration will be mapped.
	 * </p>
	 * <p>
	 * An alias must begin with slash ('/') and must not end with slash ('/'),
	 * with the exception that an alias of the form &quot;/&quot; is used to
	 * denote the root alias. See the specification text for details on how HTTP
	 * requests are mapped to servlet and resource registrations.
	 * </p>
	 * <p>
	 * The Http Service will call the servlet's <code>init</code> method before
	 * returning.
	 * </p>
	 * 
	 * <pre>
	 * httpService.registerServlet(&quot;/myservlet&quot;, servlet, initparams, context);
	 * </pre>
	 * <p>
	 * Servlets registered with the same <code>HttpContext</code> object will
	 * share the same <code>ServletContext</code>. The Http Service will call
	 * the <code>context</code> argument to support the
	 * <code>ServletContext</code> methods <code>getResource</code>,
	 * <code>getResourceAsStream</code> and <code>getMimeType</code>, and to
	 * handle security for requests. If the <code>context</code> argument is
	 * <code>null</code>, a default <code>HttpContext</code> object is used (see
	 * {@link #createDefaultHttpContext}).
	 * </p>
	 * <p>
	 * The HttpContext will be proxied to allow accessing resources in the
	 * module.
	 * </p>
	 * 
	 * @param alias
	 *            name in the URI namespace at which the servlet is registered
	 * @param moduleId
	 *            the GWT module id (eg.
	 *            <code>&quot;com.google.gwt.sample.showcase.Showcase&quot;</code>
	 *            )
	 * @param servlet
	 *            the servlet object to register
	 * @param initparams
	 *            initialization arguments for the servlet or <code>null</code>
	 *            if there are none. This argument is used by the servlet's
	 *            <code>ServletConfig</code> object.
	 * @param context
	 *            the <code>HttpContext</code> object for the registered
	 *            servlet, or <code>null</code> if a default
	 *            <code>HttpContext</code> is to be created and used.
	 * @throws NamespaceException
	 *             if the registration fails because the alias is already in
	 *             use.
	 * @throws javax.servlet.ServletException
	 *             if the servlet's <code>init</code> method throws an
	 *             exception, or the given servlet object has already been
	 *             registered at a different alias.
	 * @throws java.lang.IllegalArgumentException
	 *             if any of the arguments are invalid
	 */
	public void registerServlet(String alias, String moduleId, Servlet servlet, Dictionary initparams, HttpContext context) throws ServletException, NamespaceException;

	/**
	 * Unregisters a previous registration done by <code>registerService</code>
	 * or <code>registerModule</code> methods.
	 * <p>
	 * After this call, the registered alias in the URI name-space will no
	 * longer be available.
	 * </p>
	 * <p>
	 * If the bundle which performed the registration is stopped or otherwise
	 * "unget"s the Gwt Service without calling {@link #unregister} then Gwt
	 * Service must automatically unregister the registration.
	 * </p>
	 * 
	 * @param alias
	 *            name in the URI name-space of the registration to unregister
	 * @throws java.lang.IllegalArgumentException
	 *             if there is no registration for the alias or the calling
	 *             bundle was not the bundle which registered the alias.
	 */
	void unregister(String alias);

}
