/*******************************************************************************
 * Copyright (c) 2008, 2009 Gunnar Wagenknecht and others.
 * All rights reserved.
 *  
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit;

/**
 * This error is thrown whenever an unrecoverable error occurred in CWT.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 */
public class CWTError extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * The CWT error code, one of CWT.ERROR_*.
	 */
	public int code;

	/**
	 * Constructs a new instance of this class with its stack trace filled in.
	 * The error code is set to an unspecified value.
	 */
	public CWTError() {
		this(CWT.ERROR_UNSPECIFIED);
	}

	/**
	 * Constructs a new instance of this class with its stack trace and error
	 * code filled in.
	 * 
	 * @param code
	 *            the CWT error code
	 */
	public CWTError(final int code) {
		this(code, CWT.findErrorText(code), null);
	}

	/**
	 * Constructs a new instance of this class with its stack trace, error code
	 * and message filled in. Specifying <code>null</code> as the message is
	 * equivalent to specifying an empty string.
	 * 
	 * @param code
	 *            the CWT error code
	 * @param message
	 *            the detail message for the exception
	 */
	public CWTError(final int code, final String message) {
		this(code, message, null);
	}

	/**
	 * Constructs a new instance of this class with its stack trace, error code
	 * and message filled in. Specifying <code>null</code> as the message is
	 * equivalent to specifying an empty string.
	 * 
	 * @param code
	 *            the CWT error code
	 * @param message
	 *            the detail message for the exception
	 * @param cause
	 *            the underlying {@link Throwable} that caused the error
	 */
	public CWTError(final int code, final String message, final Throwable cause) {
		super(null == cause ? message : message + " (" + cause.toString() + ")", cause); //$NON-NLS-1$ //$NON-NLS-2$
		this.code = code;
	}

	/**
	 * Constructs a new instance of this class with its stack trace and error
	 * code filled in.
	 * 
	 * @param code
	 *            the CWT error code
	 * @param cause
	 *            the underlying {@link Throwable} that caused the error
	 */
	public CWTError(final int code, final Throwable cause) {
		this(code, CWT.findErrorText(code), cause);
	}

	/**
	 * Constructs a new instance of this class with its stack trace and message
	 * filled in. The error code is set to an unspecified value. Specifying
	 * <code>null</code> as the message is equivalent to specifying an empty
	 * string.
	 * 
	 * @param message
	 *            the detail message for the exception
	 */
	public CWTError(final String message) {
		this(CWT.ERROR_UNSPECIFIED, message, null);
	}

	/**
	 * Constructs a new instance of this class with its stack trace and message
	 * filled in. The error code is set to an unspecified value. Specifying
	 * <code>null</code> as the message is equivalent to specifying an empty
	 * string.
	 * 
	 * @param message
	 *            the detail message for the exception
	 * @param cause
	 *            the underlying {@link Throwable} that caused the error
	 */
	public CWTError(final String message, final Throwable cause) {
		this(CWT.ERROR_UNSPECIFIED, message, cause);
	}

	/**
	 * Constructs a new instance of this class with its stack trace filled in.
	 * The error code is set to an unspecified value.
	 * 
	 * @param cause
	 *            the underlying {@link Throwable} that caused the error
	 */
	public CWTError(final Throwable cause) {
		this(CWT.ERROR_UNSPECIFIED, cause);
	}

}
