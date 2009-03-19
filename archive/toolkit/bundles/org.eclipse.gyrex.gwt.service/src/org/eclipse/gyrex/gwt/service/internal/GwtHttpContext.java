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
package org.eclipse.gyrex.gwt.service.internal;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.http.HttpContext;

/**
 * Specialized <code>{@link HttpContext}</code> implementation that wraps a
 * default context and provides support for additional features like mapping and
 * resource sharing between GWT remote service and its GWT module.
 */
public class GwtHttpContext implements HttpContext {

	/** a wrapped default context */
	private final HttpContext defaultContext;

	private String defaultResourceName;
	private String baseFolderName;

	private final GwtServiceImpl gwtService;
	private final boolean isRemoteServiceContext;

	private final String moduleId;

	/**
	 * Creates a new GWT remote service servlet context.
	 * 
	 * @param gwtService
	 * @param defaultContext
	 * @param baseName
	 * @param defaultName
	 */
	GwtHttpContext(final GwtServiceImpl gwtService, final String moduleId, final HttpContext defaultContext) {
		this(gwtService, moduleId, defaultContext, true);
	}

	private GwtHttpContext(final GwtServiceImpl gwtService, final String moduleId, final HttpContext defaultContext, final boolean isRemoteServiceContext) {
		this.moduleId = moduleId;
		this.isRemoteServiceContext = isRemoteServiceContext;
		this.gwtService = gwtService;
		this.defaultContext = defaultContext;
	}

	/**
	 * Creates a new GWT module context.
	 * 
	 * @param gwtService
	 * @param defaultContext
	 * @param baseName
	 * @param defaultName
	 */
	GwtHttpContext(final GwtServiceImpl gwtService, final String moduleId, final HttpContext defaultContext, final String baseName, final String defaultName) {
		this(gwtService, moduleId, defaultContext, false);

		if ((null != baseName) && (null != defaultName)) {
			baseFolderName = baseName.concat("/");
			defaultResourceName = baseFolderName.concat(defaultName);
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.http.HttpContext#getMimeType(java.lang.String)
	 */
	public String getMimeType(final String name) {
		final String mimeType = null != defaultContext ? defaultContext.getMimeType(mapName(name)) : null;

		// check the module context if this is a remote service
		if ((mimeType == null) && isRemoteServiceContext) {
			final GwtHttpContext context = gwtService.getModuleContext(moduleId);
			if (context != null) {
				return context.getMimeType(name);
			}
		}

		return mimeType;
	}

	public URL getModuleResource(final String moduleResourceName) {
		if (isRemoteServiceContext || (baseFolderName == null)) {
			return null;
		}

		return getResource(baseFolderName.concat(moduleResourceName));
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.http.HttpContext#getResource(java.lang.String)
	 */
	public URL getResource(final String name) {
		final String internalName = mapName(name);

		// get resource
		URL resource;
		if (defaultContext != null) {
			// get resource from default context if available
			resource = defaultContext.getResource(internalName);
		} else {
			// get resource from bundle which contributed the GWT resource
			resource = gwtService.getBundle().getResource(internalName);
		}

		// also check the module context if this is a remote service
		if ((resource == null) && isRemoteServiceContext) {
			final GwtHttpContext context = gwtService.getModuleContext(moduleId);
			if (context != null) {
				return context.getResource(name); // don't use internal name here
			}
		}

		return resource;
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.http.HttpContext#handleSecurity(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public boolean handleSecurity(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		if (null != defaultContext) {
			return defaultContext.handleSecurity(request, response);
		}

		// default behaviour assumes the container has already performed authentication
		return true;
	}

	private String mapName(final String name) {
		if (isRemoteServiceContext || (null == baseFolderName) || (defaultResourceName == null)) {
			return name;
		}

		if (null == name) {
			return "";
		}

		// note, we compare the exact name here
		// baseFolderName ends with "/" and name must end with "/" too
		// GWT requires that the name ends with a "/", 
		// otherwise it calculates the module base URL wrong 
		if ((null != name) && name.equals(baseFolderName)) {
			return defaultResourceName;
		}

		return name;
	}

}
