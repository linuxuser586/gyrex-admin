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
package org.eclipse.cloudfree.gwt.service.internal;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.cloudfree.gwt.service.GwtRequestResponseListener;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;
import com.google.gwt.user.server.rpc.SerializationPolicyProvider;

public class OSGiRemoteServiceServlet extends RemoteServiceServlet {

	/** serialVersionUID */
	private static final long serialVersionUID = 4829836240562916507L;
	private final GwtServiceImpl gwtService;
	private final String moduleId;
	private final RemoteService remoteService;
	private final GwtRequestResponseListener requestResponseAdapter;

	private final ClassLoader remoteServiceClassLoader = new ClassLoader() {

		/* (non-Javadoc)
		 * @see java.lang.ClassLoader#findClass(java.lang.String)
		 */
		@Override
		protected Class<?> findClass(final String name) throws ClassNotFoundException {
			// try the bundle which defined the service
			try {
				return remoteService.getClass().getClassLoader().loadClass(name);
			} catch (final Exception e) {
				// ignore
			}

			// try the bundle which registered the service
			try {
				return gwtService.getBundle().loadClass(name);
			} catch (final Exception e) {
				// ignore
			}

			// try the context finder
			try {
				final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
				if (null != contextClassLoader) {
					return contextClassLoader.loadClass(name);
				}
			} catch (final Exception e) {
				// ignore
			}

			// fail
			return super.findClass(name);
		}

		/* (non-Javadoc)
		 * @see java.lang.ClassLoader#findResource(java.lang.String)
		 */
		@Override
		protected URL findResource(final String name) {
			// try the bundle which defined the service
			try {
				final URL resource = remoteService.getClass().getClassLoader().getResource(name);
				if (null != resource) {
					return resource;
				}
			} catch (final Exception e) {
				// ignore
			}

			// try the bundle which registered the service
			try {
				final URL resource = gwtService.getBundle().getResource(name);
				if (null != resource) {
					return resource;
				}
			} catch (final Exception e) {
				// ignore
			}

			// try the context finder
			try {
				final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
				if (null != contextClassLoader) {
					final URL resource = contextClassLoader.getResource(name);
					if (null != resource) {
						return resource;
					}
				}
			} catch (final Exception e) {
				// ignore
			}

			// fail
			return super.findResource(name);
		}

		/* (non-Javadoc)
		 * @see java.lang.ClassLoader#findResources(java.lang.String)
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected Enumeration<URL> findResources(final String name) throws IOException {
			// try the bundle which defined the service
			try {
				final Enumeration<URL> resources = remoteService.getClass().getClassLoader().getResources(name);
				if (resources.hasMoreElements()) {
					return resources;
				}
			} catch (final Exception e) {
				// ignore
			}

			// try the bundle which registered the service
			try {
				final Enumeration<URL> resources = gwtService.getBundle().getResources(name);
				if (resources.hasMoreElements()) {
					return resources;
				}
			} catch (final Exception e) {
				// ignore
			}

			// try the context finder
			try {
				final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
				if (null != contextClassLoader) {
					final Enumeration<URL> resources = contextClassLoader.getResources(name);
					if (resources.hasMoreElements()) {
						return resources;
					}
				}
			} catch (final Exception e) {
				// ignore
			}

			// fail
			return super.findResources(name);
		}
	};

	OSGiRemoteServiceServlet(final GwtServiceImpl gwtService, final String moduleId, final RemoteService remoteService, final GwtRequestResponseListener requestResponseAdapter) {
		this.gwtService = gwtService;
		this.moduleId = moduleId;
		this.remoteService = remoteService;
		this.requestResponseAdapter = requestResponseAdapter;
	}

	private RPCRequest decodeRequest(final String payload) {
		// we use reflection (the patch for GWT issue 1888 may eventually be available in 1.6)
		try {
			final Method method = RPC.class.getMethod("decodeRequest", String.class, Class.class, SerializationPolicyProvider.class, ClassLoader.class);
			return (RPCRequest) method.invoke(null, payload, remoteService.getClass(), this, remoteServiceClassLoader);
		} catch (final Exception e) {
			// ignore, fallback bellow
		}

		// as a fallback we override the TCCL 
		final ClassLoader contextFinder = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(remoteServiceClassLoader);
			return RPC.decodeRequest(payload, remoteService.getClass(), this);
		} finally {
			Thread.currentThread().setContextClassLoader(contextFinder);
		}
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.RemoteServiceServlet#doGetSerializationPolicy(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
	 */
	@Override
	protected SerializationPolicy doGetSerializationPolicy(final HttpServletRequest request, final String moduleBaseURL, final String strongName) {
		// the serialization policy file
		final String serializationPolicyFile = SerializationPolicyLoader.getSerializationPolicyFileName(strongName);

		// the serialization policy can be found in the GWT client module
		final GwtHttpContext moduleContext = gwtService.getModuleContext(moduleId);
		if (moduleContext != null) {
			final URL resource = moduleContext.getModuleResource(serializationPolicyFile);
			if (null != resource) {
				InputStream is = null;
				try {
					// open stream for reading policy
					is = resource.openStream();

					// read policy
					final List<ClassNotFoundException> exceptions = new ArrayList<ClassNotFoundException>();
					final SerializationPolicy policy = loadSerializationPolicy(is, exceptions);

					// return policy if there were no errors
					if (exceptions.isEmpty() && (null != policy)) {
						return policy;
					}

					// TODO: consider logging not accessible classes
					// this may indicate a deployment problem, i.e. some unsatisfied version dependencies.
					for (final ClassNotFoundException classNotFoundException : exceptions) {
						getServletContext().log(MessageFormat.format("Could not load class \"{0}\" using bundle \"{1}\"", classNotFoundException.getMessage(), gwtService.getBundle().getSymbolicName()));
					}
				} catch (final Exception e) {
					// TODO consider logging this
					getServletContext().log(MessageFormat.format("Error while reading serialization policy for module \"{0}\".", moduleId), e);
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
		}

		// log a message
		getServletContext().log(MessageFormat.format("Serialization policy file \"{0}\" not available in module \"{1}\". Falling back to legacy policy.", serializationPolicyFile, moduleId));

		// fallback to a legacy policy
		// TODO: consider logging this
		return RPC.getDefaultSerializationPolicy();
	}

	private SerializationPolicy loadSerializationPolicy(final InputStream is, final List<ClassNotFoundException> exceptions) throws IOException, ParseException {
		// we use reflection (the patch for GWT issue 1888 may eventually be available in 1.6)
		try {
			final Method method = SerializationPolicyLoader.class.getMethod("loadFromStream", InputStream.class, List.class, ClassLoader.class);
			return (SerializationPolicy) method.invoke(null, is, exceptions, remoteServiceClassLoader);
		} catch (final Exception e) {
			// ignore, fallback bellow
		}

		// as a fallback we override the TCCL 
		final ClassLoader contextFinder = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(remoteServiceClassLoader);
			return SerializationPolicyLoader.loadFromStream(is, exceptions);
		} finally {
			Thread.currentThread().setContextClassLoader(contextFinder);
		}
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.RemoteServiceServlet#onAfterResponseSerialized(java.lang.String)
	 */
	@Override
	protected void onAfterResponseSerialized(final String serializedResponse) {
		if (null != requestResponseAdapter) {
			requestResponseAdapter.onAfterResponseSerialized(serializedResponse);
		}
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.RemoteServiceServlet#onBeforeRequestDeserialized(java.lang.String)
	 */
	@Override
	protected void onBeforeRequestDeserialized(final String serializedRequest) {
		if (null != requestResponseAdapter) {
			requestResponseAdapter.onBeforeRequestDeserialized(serializedRequest);
		}
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.RemoteServiceServlet#processCall(java.lang.String)
	 */
	@Override
	public String processCall(final String payload) throws SerializationException {
		try {
			if (null != requestResponseAdapter) {
				requestResponseAdapter.onBeforeProcessCall(getThreadLocalRequest(), getThreadLocalResponse());
			}
			final RPCRequest rpcRequest = decodeRequest(payload);
			return RPC.invokeAndEncodeResponse(remoteService, rpcRequest.getMethod(), rpcRequest.getParameters(), rpcRequest.getSerializationPolicy());
		} catch (final IncompatibleRemoteServiceException ex) {
			// TODO consider logging this
			//getServletContext().log("An IncompatibleRemoteServiceException was thrown while processing this call.", ex);
			return RPC.encodeResponseForFailure(null, ex);
		} finally {
			if (null != requestResponseAdapter) {
				requestResponseAdapter.onAfterProcessCall(getThreadLocalRequest(), getThreadLocalResponse());
			}
		}
	}
}
