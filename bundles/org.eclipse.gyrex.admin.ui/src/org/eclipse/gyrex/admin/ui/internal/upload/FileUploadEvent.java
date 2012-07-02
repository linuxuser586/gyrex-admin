/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 *    Gunnar Wagenknecht - copied from RAP Incubator
 ******************************************************************************/
package org.eclipse.gyrex.admin.ui.internal.upload;

import java.util.EventObject;

/**
 * Event object that provides information on a file upload. The source of this
 * kind of events is always a file upload handler.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @see IFileUploadListener
 */
public abstract class FileUploadEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	protected FileUploadEvent(final FileUploadHandler source) {
		super(source);
	}

	protected void dispatchFailed() {
		((FileUploadHandler) source).getListeners().notifyUploadFailed(this);
	}

	protected void dispatchFinished() {
		((FileUploadHandler) source).getListeners().notifyUploadFinished(this);
	}

	protected void dispatchProgress() {
		((FileUploadHandler) source).getListeners().notifyUploadProgress(this);
	}

	/**
	 * The number of bytes that have been received so far.
	 * 
	 * @return the number of bytes received
	 */
	public abstract long getBytesRead();

	/**
	 * The total number of bytes which are expected in total, as transmitted by
	 * the uploading client. May be unknown.
	 * 
	 * @return the content length in bytes or -1 if unknown
	 */
	public abstract long getContentLength();

	/**
	 * The content type as transmitted by the uploading client.
	 * 
	 * @return the content type or <code>null</code> if unknown
	 */
	public abstract String getContentType();

	/**
	 * If the upload has failed, this method will return the exception that has
	 * occurred.
	 * 
	 * @return the exception if the upload has failed, <code>null</code>
	 *         otherwise
	 */
	public abstract Exception getException();

	/**
	 * The original file name of the uploaded file, as transmitted by the
	 * client. If a path was included in the request, it is stripped off.
	 * 
	 * @return the plain file name without any path segments
	 */
	public abstract String getFileName();
}
