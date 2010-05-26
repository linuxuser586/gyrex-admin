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
package org.eclipse.gyrex.gwt.service.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.gyrex.gwt.service.GwtRequestResponseListener;

import org.osgi.framework.Bundle;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;

public class OSGiRemoteServiceServlet extends RemoteServiceServlet {

	/** serialVersionUID */
	private static final long serialVersionUID = 4829836240562916507L;
	private final GwtServiceImpl gwtService;
	private final String moduleId;
	private final RemoteService remoteService;
	private final GwtRequestResponseListener requestResponseAdapter;

	private final ClassLoader remoteServiceClassLoader = new ClassLoader() {

		@Override
		protected Class<?> findClass(final String name) throws ClassNotFoundException {
			// try the bundle which defined the remote service
			try {
				return remoteService.getClass().getClassLoader().loadClass(name);
			} catch (final Exception e) {
				// ignore
			}

			// try the bundle which registered the remote service
			try {
				return gwtService.getBundle().loadClass(name);
			} catch (final Exception e) {
				// ignore
			}

			// try our bundle (we import the default GWT custom serializers)
			try {
				final Bundle bundle = GwtServiceActivator.getBundle();
				if (null != bundle) {
					return bundle.loadClass(name);
				}
			} catch (final Exception e) {
				// ignore
			}

			// note, we should ideally try the old TCCL here as well
			// we should investigate if this is worthwhile (eg. performance)

			// give up
			return super.findClass(name);
		}

		@Override
		protected URL findResource(final String name) {
			// try the bundle which defined the remote service
			try {
				final URL resource = remoteService.getClass().getClassLoader().getResource(name);
				if (null != resource) {
					return resource;
				}
			} catch (final Exception e) {
				// ignore
			}

			// try the bundle which registered the remote service
			try {
				final URL resource = gwtService.getBundle().getResource(name);
				if (null != resource) {
					return resource;
				}
			} catch (final Exception e) {
				// ignore
			}

			// try our bundle (we import the default GWT custom serializers)
			try {
				final Bundle bundle = GwtServiceActivator.getBundle();
				if (null != bundle) {
					return bundle.getResource(name);
				}
			} catch (final Exception e) {
				// ignore
			}

			// note, we should ideally try the old TCCL here as well
			// we should investigate if this is worthwhile (eg. performance)

			// give up
			return super.findResource(name);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Enumeration<URL> findResources(final String name) throws IOException {
			// try the bundle which defined the remote service
			try {
				final Enumeration<URL> resources = remoteService.getClass().getClassLoader().getResources(name);
				if (resources.hasMoreElements()) {
					return resources;
				}
			} catch (final Exception e) {
				// ignore
			}

			// try the bundle which registered the remote service
			try {
				final Enumeration<URL> resources = gwtService.getBundle().getResources(name);
				if (resources.hasMoreElements()) {
					return resources;
				}
			} catch (final Exception e) {
				// ignore
			}

			// try our bundle (we import the default GWT custom serializers)
			try {
				final Bundle bundle = GwtServiceActivator.getBundle();
				if (null != bundle) {
					final Enumeration<URL> resources = bundle.getResources(name);
					if (resources.hasMoreElements()) {
						return resources;
					}
				}
			} catch (final Exception e) {
				// ignore
			}

			// note, we should ideally try the old TCCL here as well
			// we should investigate if this is worthwhile (eg. performance)

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
					final SerializationPolicy policy = SerializationPolicyLoader.loadFromStream(is, exceptions);

					// return policy if there were no errors
					if (exceptions.isEmpty() && (null != policy)) {
						return policy;
					}

					// this may indicate a deployment problem, i.e. some unsatisfied version dependencies.
					for (final ClassNotFoundException classNotFoundException : exceptions) {
						getServletContext().log(MessageFormat.format("Could not load class \"{0}\" using bundle \"{1}\".", classNotFoundException.getMessage(), gwtService.getBundle().getSymbolicName()));
					}
				} catch (final Exception e) {
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
		return RPC.getDefaultSerializationPolicy();
	}

	@Override
	protected void onAfterResponseSerialized(final String serializedResponse) {
		if (null != requestResponseAdapter) {
			requestResponseAdapter.onAfterResponseSerialized(serializedResponse);
		}
	}

	@Override
	protected void onBeforeRequestDeserialized(final String serializedRequest) {
		if (null != requestResponseAdapter) {
			requestResponseAdapter.onBeforeRequestDeserialized(serializedRequest);
		}
	}

	@Override
	public String processCall(final String payload) throws SerializationException {
		// we override the TCCL to ensure proper GWT (de-)serialization
		final ClassLoader oldTccl = Thread.currentThread().getContextClassLoader();
		try {
			// allow listeners to see request and response
			if (null != requestResponseAdapter) {
				requestResponseAdapter.onBeforeProcessCall(getThreadLocalRequest(), getThreadLocalResponse());
			}

			// set custom TCCL
			Thread.currentThread().setContextClassLoader(remoteServiceClassLoader);

			// process call using GWT implementation
			return super.processCall(payload);
		} finally {
			// restore TCCL
			Thread.currentThread().setContextClassLoader(oldTccl);

			// allow listeners to see request and response
			if (null != requestResponseAdapter) {
				requestResponseAdapter.onAfterProcessCall(getThreadLocalRequest(), getThreadLocalResponse());
			}
		}
	}
}
