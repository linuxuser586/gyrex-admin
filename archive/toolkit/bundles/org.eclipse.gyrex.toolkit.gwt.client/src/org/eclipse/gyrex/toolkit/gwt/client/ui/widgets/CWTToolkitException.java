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
package org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets;

/**
 * This exception is thrown whenever an exception occurred when rendering GWT
 * widgets.
 */
public class CWTToolkitException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 7545147060636270977L;

	/**
	 * The CWT error code, one of CWTToolkit.ERROR_*.
	 */
	public int code;

	/**
	 * Constructs a new instance of this class with its stack trace filled in.
	 * The error code is set to an unspecified value.
	 */
	public CWTToolkitException() {
		this(CWTToolkit.ERROR_UNSPECIFIED);
	}

	/**
	 * Constructs a new instance of this class with its stack trace and error
	 * code filled in.
	 * 
	 * @param code
	 *            the CWT error code
	 */
	public CWTToolkitException(final int code) {
		this(code, CWTToolkit.findErrorText(code), null);
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
	public CWTToolkitException(final int code, final String message) {
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
	public CWTToolkitException(final int code, final String message, final Throwable cause) {
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
	public CWTToolkitException(final int code, final Throwable cause) {
		this(code, CWTToolkit.findErrorText(code), cause);
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
	public CWTToolkitException(final String message) {
		this(CWTToolkit.ERROR_UNSPECIFIED, message, null);
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
	public CWTToolkitException(final String message, final Throwable cause) {
		this(CWTToolkit.ERROR_UNSPECIFIED, message, cause);
	}

	/**
	 * Constructs a new instance of this class with its stack trace filled in.
	 * The error code is set to an unspecified value.
	 * 
	 * @param cause
	 *            the underlying {@link Throwable} that caused the error
	 */
	public CWTToolkitException(final Throwable cause) {
		this(CWTToolkit.ERROR_UNSPECIFIED, cause);
	}

	/**
	 * Returns the CWT error code, one of CWTToolkit.ERROR_*..
	 * 
	 * @return the CWT error code
	 */
	public int getCode() {
		return code;
	}
}
