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
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Enumeration;

import org.eclipse.gyrex.gwt.service.GwtRequestResponseListener;

import org.osgi.framework.Bundle;

import com.google.gwt.rpc.client.impl.RemoteException;
import com.google.gwt.rpc.server.ClientOracle;
import com.google.gwt.rpc.server.RPC;
import com.google.gwt.rpc.server.RpcServlet;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPCRequest;

public class OSGiRpcServlet extends RpcServlet {

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

	OSGiRpcServlet(final GwtServiceImpl gwtService, final String moduleId, final RemoteService remoteService, final GwtRequestResponseListener requestResponseAdapter) {
		this.gwtService = gwtService;
		this.moduleId = moduleId;
		this.remoteService = remoteService;
		this.requestResponseAdapter = requestResponseAdapter;
	}

	@Override
	protected InputStream findClientOracleData(String requestModuleBasePath, final String permutationStrongName) throws SerializationException {
		// the oracle can usually be found in the GWT client module
		final GwtHttpContext moduleContext = gwtService.getModuleContext(moduleId);
		if (null == moduleContext) {
			throw new SerializationException(MessageFormat.format("GWT module \"{0}\" not found!", moduleId));
		}

		// the module base path may contain a module alias which must be removes
		final String alias = moduleContext.getAlias();
		if ((null != alias) && requestModuleBasePath.startsWith(alias)) {
			requestModuleBasePath = requestModuleBasePath.substring(alias.length());
		}

		final String clientOracleDataFile = requestModuleBasePath + permutationStrongName + CLIENT_ORACLE_EXTENSION;

		final URL resource = moduleContext.getModuleResource(clientOracleDataFile);
		if (null != resource) {
			// open stream for reading policy
			try {
				return resource.openStream();
			} catch (final IOException e) {
				// log
				getServletContext().log(MessageFormat.format("Error while reading clientOracle data \"{0}\" in module \"{1}\". {2}", clientOracleDataFile, moduleId, e.getMessage()), e);
			}
		}

		// log a message
		getServletContext().log(MessageFormat.format("ClientOracle data \"{0}\" not available in module \"{1}\".", clientOracleDataFile, moduleId));

		return super.findClientOracleData(requestModuleBasePath, permutationStrongName);
	}

	@Override
	public void processCall(final ClientOracle clientOracle, final String payload, final OutputStream stream) throws SerializationException {
		// we override the TCCL to ensure proper GWT (de-)serialization
		final ClassLoader oldTccl = Thread.currentThread().getContextClassLoader();
		try {
			// allow listeners to see request and response
			if (null != requestResponseAdapter) {
				requestResponseAdapter.onBeforeProcessCall(getThreadLocalRequest(), getThreadLocalResponse());
			}

			// set custom TCCL
			Thread.currentThread().setContextClassLoader(remoteServiceClassLoader);

			// process call using GWT logic but with our remote service class
			final RPCRequest rpcRequest = RPC.decodeRequest(payload, remoteService.getClass(), clientOracle);
			onAfterRequestDeserialized(rpcRequest);
			RPC.invokeAndStreamResponse(remoteService, rpcRequest.getMethod(), rpcRequest.getParameters(), clientOracle, stream);
		} catch (final RemoteException ex) {
			throw new SerializationException("An exception was sent from the client", ex.getCause());
		} catch (final IncompatibleRemoteServiceException ex) {
			// this indicated an outdated client or an invalid client (hack attempt?)
			// TODO we should log this into some metric
			//log("An IncompatibleRemoteServiceException was thrown while processing this call.", ex);
			RPC.streamResponseForFailure(clientOracle, stream, ex);
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
