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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An adapter for receiving request and response information during GWT remote
 * service communication.
 * <p>
 * This class can be instantiated and subclassed by clients.
 * </p>
 */
public class GwtRequestResponseListener {

	private final ThreadLocal<HttpServletRequest> perThreadRequest = new ThreadLocal<HttpServletRequest>();
	private final ThreadLocal<HttpServletResponse> perThreadResponse = new ThreadLocal<HttpServletResponse>();

	/**
	 * Gets the <code>HttpServletRequest</code> object for the current call.
	 * <p>
	 * It is stored thread-locally so that simultaneous invocations can have
	 * different request objects.
	 * </p>
	 * 
	 * @return the <code>HttpServletRequest</code> object for the current call
	 */
	public final HttpServletRequest getThreadLocalRequest() {
		return perThreadRequest.get();
	}

	/**
	 * Gets the <code>HttpServletResponse</code> object for the current call.
	 * <p>
	 * It is stored thread-locally so that simultaneous invocations can have
	 * different response objects.
	 * </p>
	 * 
	 * @return the <code>HttpServletResponse</code> object for the current call
	 */
	public final HttpServletResponse getThreadLocalResponse() {
		return perThreadResponse.get();
	}

	/**
	 * Override this method to receive the serialized response that will be
	 * returned to the client. The default implementation does nothing and need
	 * not be called by subclasses.
	 */
	public final void onAfterProcessCall(final HttpServletRequest request, final HttpServletResponse response) {
		// null the thread-locals to avoid holding request/response
		perThreadRequest.set(null);
		perThreadResponse.set(null);
	}

	/**
	 * Override this method to examine the serialized response that will be
	 * returned to the client. The default implementation does nothing and need
	 * not be called by subclasses.
	 */
	public void onAfterResponseSerialized(final String serializedResponse) {
	}

	/**
	 * Override this method to examine the serialized response that will be
	 * returned to the client. The default implementation does nothing and need
	 * not be called by subclasses.
	 */
	public final void onBeforeProcessCall(final HttpServletRequest request, final HttpServletResponse response) {
		// store the request & response objects in thread-local storage.
		perThreadRequest.set(request);
		perThreadResponse.set(response);
	}

	/**
	 * Override this method to examine the serialized version of the request
	 * payload before it is deserialized into objects. The default
	 * implementation does nothing and need not be called by subclasses.
	 */
	public void onBeforeRequestDeserialized(final String serializedRequest) {
	}

}
