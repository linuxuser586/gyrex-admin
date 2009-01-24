/*******************************************************************************
 * Copyright (c) 2008 Gunnar Wagenknecht and others.
 * All rights reserved.
 *  
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * This file uses content originating from Google Web Toolkit.
 * 
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *     Google Inc. - code from RemoteServiceServlet
 *******************************************************************************/
/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.eclipse.cloudfree.toolkit.gwt.server.internal;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.cloudfree.toolkit.gwt.server.WidgetServiceServlet;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;
import com.google.gwt.user.server.rpc.SerializationPolicyProvider;

/**
 * This is the internal {@link WidgetServiceServlet} implementation.
 * <p>
 * This class is not intended to be instantiated or subclassed outside the
 * CloudFree GWT rendering framework.
 * </p>
 */
public class TargetedRemoteServiceServlet extends RemoteServiceServlet {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** remote service */
	private final RemoteService remoteService;

	/** the class loader to use for RPC de-serialization */
	private final ClassLoader classLoader;

	/** a custom {@link SerializationPolicyProvider} */
	private final SerializationPolicyProvider serializationPolicyProvider;

	/**
	 * Creates a new instance.
	 * 
	 * @param remoteService
	 * @param classLoader
	 * @param serializationPolicyProvider
	 */
	public TargetedRemoteServiceServlet(final RemoteService remoteService, final ClassLoader classLoader, final SerializationPolicyProvider serializationPolicyProvider) {
		this.remoteService = remoteService;
		this.classLoader = classLoader;
		this.serializationPolicyProvider = serializationPolicyProvider;
	}

	private RPCRequest decodeRequest(final String payload, final RemoteService target, final ClassLoader classLoader) {
		// we use reflection (the patch for GWT issue 1888 may eventually be available in 1.6)
		try {
			final Method method = RPC.class.getMethod("decodeRequest", String.class, Class.class, SerializationPolicyProvider.class, ClassLoader.class);
			return (RPCRequest) method.invoke(null, payload, target.getClass(), this, classLoader);
		} catch (final Exception e) {
			// ignore, fallback bellow
		}

		// as a fallback we override the TCCL 
		final ClassLoader contextFinder = Thread.currentThread().getContextClassLoader();
		try {
			if (null != classLoader) {
				Thread.currentThread().setContextClassLoader(classLoader);
			}
			return RPC.decodeRequest(payload, target.getClass(), this);
		} finally {
			Thread.currentThread().setContextClassLoader(contextFinder);
		}
	}

	/* 
	 * Overridden to support custom class loading.
	 * 
	 * @see com.google.gwt.user.server.rpc.RemoteServiceServlet#doGetSerializationPolicy(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
	 */
	@Override
	protected SerializationPolicy doGetSerializationPolicy(final HttpServletRequest request, final String moduleBaseURL, final String strongName) {
		// try custom provider first (note, we also check for != this to prevent recursion)
		final SerializationPolicyProvider customProvider = getSerializationPolicyProvider();
		if ((null != customProvider) && (customProvider != this)) {
			final SerializationPolicy serializationPolicy = customProvider.getSerializationPolicy(moduleBaseURL, strongName);
			// return policy if not null, otherwise fallback to default loading below
			if (null != serializationPolicy) {
				return serializationPolicy;
			}
		}

		// The request can tell you the path of the web app relative to the
		// container root.
		final String contextPath = request.getContextPath();

		String modulePath = null;
		if (moduleBaseURL != null) {
			try {
				modulePath = new URL(moduleBaseURL).getPath();
			} catch (final MalformedURLException ex) {
				// log the information, we will default
				getServletContext().log("Malformed moduleBaseURL: " + moduleBaseURL, ex);
			}
		}

		SerializationPolicy serializationPolicy = null;

		/*
		 * Check that the module path must be in the same web app as the servlet
		 * itself. If you need to implement a scheme different than this, override
		 * this method.
		 */
		if ((modulePath == null) || !modulePath.startsWith(contextPath)) {
			final String message = "ERROR: The module path requested, " + modulePath + ", is not in the same web application as this servlet, " + contextPath + ".  Your module may not be properly configured or your client and server code maybe out of date.";
			getServletContext().log(message);
		} else {
			// Strip off the context path from the module base URL. It should be a
			// strict prefix.
			final String contextRelativePath = modulePath.substring(contextPath.length());

			final String serializationPolicyFilePath = SerializationPolicyLoader.getSerializationPolicyFileName(contextRelativePath + strongName);

			// Open the RPC resource file read its contents.
			final InputStream is = getServletContext().getResourceAsStream(serializationPolicyFilePath);
			try {
				if (is != null) {
					try {
						serializationPolicy = loadSerializationPolicy(is, null, getClassLoader());
					} catch (final ParseException e) {
						getServletContext().log("ERROR: Failed to parse the policy file '" + serializationPolicyFilePath + "'", e);
					} catch (final IOException e) {
						getServletContext().log("ERROR: Could not read the policy file '" + serializationPolicyFilePath + "'", e);
					}
				} else {
					final String message = "ERROR: The serialization policy file '" + serializationPolicyFilePath + "' was not found; did you forget to include it in this deployment?";
					getServletContext().log(message);
				}
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (final IOException e) {
						// Ignore this error
					}
				}
			}
		}

		return serializationPolicy;
	}

	/**
	 * Returns the classLoader.
	 * 
	 * @return the classLoader
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * Returns the remote service.
	 * 
	 * @return the remote service
	 */
	public RemoteService getRemoteService() {
		return remoteService;
	}

	/**
	 * Returns the custom SerializationPolicyProvider.
	 * 
	 * @return the custom SerializationPolicyProvider
	 */
	public SerializationPolicyProvider getSerializationPolicyProvider() {
		return serializationPolicyProvider;
	}

	private SerializationPolicy loadSerializationPolicy(final InputStream is, final List<ClassNotFoundException> exceptions, final ClassLoader classLoader) throws IOException, ParseException {
		// we use reflection (the patch for GWT issue 1888 may eventually be available in 1.6)
		try {
			final Method method = SerializationPolicyLoader.class.getMethod("loadFromStream", InputStream.class, List.class, ClassLoader.class);
			return (SerializationPolicy) method.invoke(null, is, exceptions, classLoader);
		} catch (final Exception e) {
			// ignore, fallback bellow
		}

		// as a fallback we override the TCCL 
		final ClassLoader contextFinder = Thread.currentThread().getContextClassLoader();
		try {
			if (null != classLoader) {
				Thread.currentThread().setContextClassLoader(classLoader);
			}
			return SerializationPolicyLoader.loadFromStream(is, exceptions);
		} finally {
			Thread.currentThread().setContextClassLoader(contextFinder);
		}
	}

	/*
	 * Overridden to support custom class loading.
	 * 
	 * @see com.google.gwt.user.server.rpc.RemoteServiceServlet#processCall(java.lang.String)
	 */
	@Override
	public String processCall(final String payload) throws SerializationException {
		try {
			final ClassLoader classLoader = getClassLoader();
			final RemoteService target = getRemoteService();
			if (null == target) {
				throw new IllegalStateException("remote service is null");
			}
			final RPCRequest rpcRequest = decodeRequest(payload, target, classLoader);
			return RPC.invokeAndEncodeResponse(target, rpcRequest.getMethod(), rpcRequest.getParameters(), rpcRequest.getSerializationPolicy());
		} catch (final IncompatibleRemoteServiceException ex) {
			return RPC.encodeResponseForFailure(null, ex);
		}
	}
}
