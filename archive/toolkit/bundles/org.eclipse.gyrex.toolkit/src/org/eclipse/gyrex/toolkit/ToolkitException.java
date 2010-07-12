/*******************************************************************************
 * Copyright (c) 2008, 2010 Gunnar Wagenknecht and others.
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
 * This exception is thrown whenever a recoverable exception occurred in Toolkit.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class ToolkitException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 1068650795350240832L;

	/**
	 * The Toolkit error code, one of Toolkit.ERROR_*.
	 */
	public final int code;

	/**
	 * Constructs a new instance of this class with its stack trace filled in.
	 * The error code is set to an unspecified value.
	 */
	public ToolkitException() {
		this(Toolkit.ERROR_UNSPECIFIED);
	}

	/**
	 * Constructs a new instance of this class with its stack trace and error
	 * code filled in.
	 * 
	 * @param code
	 *            the Toolkit error code
	 */
	public ToolkitException(final int code) {
		this(code, Toolkit.findErrorText(code), null);
	}

	/**
	 * Constructs a new instance of this class with its stack trace, error code
	 * and message filled in. Specifying <code>null</code> as the message is
	 * equivalent to specifying an empty string.
	 * 
	 * @param code
	 *            the Toolkit error code
	 * @param message
	 *            the detail message for the exception
	 */
	public ToolkitException(final int code, final String message) {
		this(code, message, null);
	}

	/**
	 * Constructs a new instance of this class with its stack trace, error code
	 * and message filled in. Specifying <code>null</code> as the message is
	 * equivalent to specifying an empty string.
	 * 
	 * @param code
	 *            the Toolkit error code
	 * @param message
	 *            the detail message for the exception
	 * @param cause
	 *            the underlying {@link Throwable} that caused the error
	 */
	public ToolkitException(final int code, final String message, final Throwable cause) {
		super(null == cause ? message : message + " (" + cause.toString() + ")", cause); //$NON-NLS-1$ //$NON-NLS-2$
		this.code = code;
	}

	/**
	 * Constructs a new instance of this class with its stack trace and error
	 * code filled in.
	 * 
	 * @param code
	 *            the Toolkit error code
	 * @param cause
	 *            the underlying {@link Throwable} that caused the error
	 */
	public ToolkitException(final int code, final Throwable cause) {
		this(code, Toolkit.findErrorText(code), cause);
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
	public ToolkitException(final String message) {
		this(Toolkit.ERROR_UNSPECIFIED, message, null);
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
	public ToolkitException(final String message, final Throwable cause) {
		this(Toolkit.ERROR_UNSPECIFIED, message, cause);
	}

	/**
	 * Constructs a new instance of this class with its stack trace filled in.
	 * The error code is set to an unspecified value.
	 * 
	 * @param cause
	 *            the underlying {@link Throwable} that caused the error
	 */
	public ToolkitException(final Throwable cause) {
		this(Toolkit.ERROR_UNSPECIFIED, cause);
	}

}
