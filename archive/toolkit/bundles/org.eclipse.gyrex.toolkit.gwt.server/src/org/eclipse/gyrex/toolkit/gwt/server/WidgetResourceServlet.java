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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.gyrex.toolkit.gwt.server.internal.ResourceUrlEncoder;

/**
 * This servlet is responsible for delivering Gyrex widget resources to any
 * GWT client.
 * <p>
 * This class is intended to be instantiated and/or extended by clients. It
 * serves as a base implementation for accessing widget resource.
 * </p>
 */
public class WidgetResourceServlet extends HttpServlet {

	/** serialVersionUID */
	private static final long serialVersionUID = 1868031957254891199L;

	private static final String LAST_MODIFIED = "Last-Modified"; //$NON-NLS-1$
	private static final String IF_MODIFIED_SINCE = "If-Modified-Since"; //$NON-NLS-1$
	private static final String IF_NONE_MATCH = "If-None-Match"; //$NON-NLS-1$
	private static final String ETAG = "ETag"; //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		final String method = req.getMethod();
		if (method.equals("GET") || method.equals("POST") || method.equals("HEAD")) { //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			final String pathInfo = req.getPathInfo();
			final String resourcePath = ((pathInfo != null) && pathInfo.startsWith("/")) ? pathInfo.substring(1) : pathInfo;
			if (!writeResource(req, resp, resourcePath)) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} else {
			resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}

	/**
	 * Writes the specified resource to the response.
	 * <p>
	 * (This method was copied and adapted from
	 * <code>org.eclipse.equinox.http.helper.ResourceServlet</code>).
	 * </p>
	 * 
	 * @param req
	 *            the request
	 * @param resp
	 *            the response
	 * @param resourceReference
	 *            the resource reference
	 * @return <code>true</code> if the resource was written successfully,
	 *         <code>false</code> otherwise
	 * @throws IOException
	 */
	protected boolean writeResource(final HttpServletRequest req, final HttpServletResponse resp, final String resourceReference) throws IOException {
		final URL url = (resourceReference != null) ? ResourceUrlEncoder.decodeResourceUrl(resourceReference) : null;
		if (url == null) {
			return false;
		}

		final URLConnection connection = url.openConnection();
		final long lastModified = connection.getLastModified();
		final int contentLength = connection.getContentLength();
		final String contentType = connection.getContentType();

		String etag = null;
		if ((lastModified != -1) && (contentLength != -1)) {
			etag = "W/\"" + contentLength + "-" + lastModified + "\""; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		}

		// Check for cache revalidation.
		// We should prefer ETag validation as the guarantees are stronger and all HTTP 1.1 clients should be using it
		final String ifNoneMatch = req.getHeader(IF_NONE_MATCH);
		if ((ifNoneMatch != null) && (etag != null) && (ifNoneMatch.indexOf(etag) != -1)) {
			resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return true;
		} else {
			final long ifModifiedSince = req.getDateHeader(IF_MODIFIED_SINCE);
			// for purposes of comparison we add 999 to ifModifiedSince since the fidelity
			// of the IMS header generally doesn't include milliseconds
			if ((ifModifiedSince > -1) && (lastModified > 0) && (lastModified <= (ifModifiedSince + 999))) {
				resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return true;
			}
		}

		// return the full contents regularly
		if (contentLength != -1) {
			resp.setContentLength(contentLength);
		}

		if (contentType != null) {
			resp.setContentType(contentType);
		}

		if (lastModified > 0) {
			resp.setDateHeader(LAST_MODIFIED, lastModified);
		}

		if (etag != null) {
			resp.setHeader(ETAG, etag);
		}

		InputStream is = null;
		try {
			is = connection.getInputStream();
			final OutputStream os = resp.getOutputStream();
			final byte[] buffer = new byte[8192];
			int bytesRead = is.read(buffer);
			int writtenContentLength = 0;
			while (bytesRead != -1) {
				os.write(buffer, 0, bytesRead);
				writtenContentLength += bytesRead;
				bytesRead = is.read(buffer);
			}
			if ((contentLength == -1) || (contentLength != writtenContentLength)) {
				resp.setContentLength(writtenContentLength);
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return true;
	}
}
