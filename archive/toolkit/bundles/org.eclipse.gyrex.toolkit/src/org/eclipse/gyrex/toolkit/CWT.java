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
package org.eclipse.gyrex.toolkit;

import org.eclipse.gyrex.toolkit.actions.ShowWidgetAction;
import org.eclipse.gyrex.toolkit.widgets.CalendarInput;
import org.eclipse.gyrex.toolkit.widgets.DialogField;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldGroup;
import org.eclipse.gyrex.toolkit.widgets.StructuredDialogField;

/**
 * The Gyrex Widget Toolkit (CWT).
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 */
public class CWT {

	/**
	 * A constant known to be zero (0), used in operations which take bit flags
	 * to indicate that "no bits are set".
	 */
	public static final int NONE = 0;

	/**
	 * Style constant for read-only behavior (value is 1&lt;&lt;1).
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>{@link DialogField}</code> and subclasses</li>
	 * </ul>
	 * </p>
	 */
	public static final int READ_ONLY = 1 << 1;

	/**
	 * Style constant for required behavior (value is 1&lt;&lt;2). <br>
	 * Note that this is a <em>HINT</em>.
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>{@link DialogField}</code> and subclasses</li>
	 * <li><code>{@link DialogFieldGroup}</code> and subclasses</li>
	 * </ul>
	 * </p>
	 */
	public static final int REQUIRED = 1 << 2;

	/**
	 * Style constant for multi-selection behavior in lists (value is
	 * 1&lt;&lt;3).
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>{@link StructuredDialogField}</code> and subclasses</li>
	 * </ul>
	 * </p>
	 */
	public static final int SELECT_MULTI = 1 << 3;

	/**
	 * Style constant for single selection behavior in lists (value is
	 * 1&lt;&lt;4).
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>{@link StructuredDialogField}</code> and subclasses</li>
	 * </ul>
	 * </p>
	 */
	public static final int SELECT_SINGLE = 1 << 4;

	/**
	 * Style constant for single selection behavior in lists (value is
	 * 1&lt;&lt;5).
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>{@link StructuredDialogField}</code> and subclasses</li>
	 * </ul>
	 * </p>
	 */
	public static final int SELECT_ROW = 1 << 5;

	/**
	 * Style constant for single selection behavior in lists (value is
	 * 1&lt;&lt;6).
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>{@link StructuredDialogField}</code> and subclasses</li>
	 * </ul>
	 * </p>
	 */
	public static final int SELECT_CELL = 1 << 6;

	/**
	 * Style constant for splitted input behavior (value is 1&lt;&lt;7). <br>
	 * Note that this is a <em>HINT</em>.
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>{@link CalendarInput}</code></li>
	 * </ul>
	 * </p>
	 */
	public static final int SPLITTED = 1 << 7;

	/**
	 * Action hint for opening a new window (value is 1&lt;&lt;1).
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>{@link ShowWidgetAction}</code> and subclasses</li>
	 * </ul>
	 * </p>
	 */
	public static final int NEW_WINDOW = 1 << 1;

	/**
	 * CWT error constant indicating that no error number was specified (value
	 * is 1).
	 */
	public static final int ERROR_UNSPECIFIED = 1;
	/**
	 * CWT error constant indicating that a null argument was passed in (value
	 * is 2).
	 */
	public static final int ERROR_NULL_ARGUMENT = 2;
	/**
	 * CWT error constant indicating that an invalid argument was passed in
	 * (value is 3).
	 */
	public static final int ERROR_INVALID_ARGUMENT = 3;
	/**
	 * CWT error constant indicating that a particular feature has not been
	 * implemented on this platform (value is 4).
	 */
	public static final int ERROR_NOT_IMPLEMENTED = 4;
	/**
	 * CWT error constant indicating that a widget could not be found (value is
	 * 5).
	 */
	public static final int ERROR_WIDGET_NOT_FOUND = 5;
	/**
	 * CWT error constant indicating that a widget could not be initialized
	 * properly (value is 6).
	 */
	public static final int ERROR_WIDGET_INITIALIZATION_FAILED = 6;
	/**
	 * CWT error constant indicating that a class cannot be subclassed (value is
	 * 7).
	 */
	public static final int ERROR_INVALID_SUBCLASS = 7;

	/**
	 * CWT error constant indicating that an input/output operation failed
	 * during the execution of an CWT operation (value is 8).
	 */
	public static final int ERROR_IO = 8;

	/**
	 * CWT error constant indicating that an operation was attempted with a
	 * value having a valid but unsupported format (value is 9).
	 */
	public static final int ERROR_UNSUPPORTED_FORMAT = 9;

	/**
	 * CWT error constant indicating that a widget needs to be initialized
	 * before any operation is executed (value is 10).
	 */
	public static final int ERROR_NOT_INITIALIZED = 10;

	/**
	 * CWT error constant indicating that a widget can only be initialized once
	 * (value is 11).
	 */
	public static final int ERROR_ALREADY_INITIALIZED = 11;

	/**
	 * CWTToolkit error constant indicating that the receiver's parent has
	 * already been set (value is 12).
	 */
	public static final int ERROR_PARENT_ALREADY_SET = 12;

	/**
	 * Throws an appropriate exception based on the passed in error code.
	 * 
	 * @param code
	 *            the CWT error code
	 * @see CWTError
	 * @see CWTException
	 * @see IllegalArgumentException
	 */
	public static void error(final int code) throws IllegalArgumentException, CWTError, CWTException {
		error(code, null, null);
	}

	/**
	 * Throws an appropriate exception based on the passed in error code and
	 * detail.
	 * <p>
	 * The <code>throwable</code> argument should be either null, or the
	 * throwable which caused CWT to throw an exception.
	 * </p>
	 * 
	 * @param code
	 *            the CWT error code
	 * @param detail
	 *            more information about error (maybe <code>null</code>)
	 * @see CWTError
	 * @see CWTException
	 * @see IllegalArgumentException
	 */
	public static void error(final int code, final String detail) throws IllegalArgumentException, CWTError, CWTException {
		error(code, null, detail);
	}

	/**
	 * Throws an appropriate exception based on the passed in error code and
	 * throwable.
	 * <p>
	 * The <code>throwable</code> argument should be either null, or the
	 * throwable which caused CWT to throw an exception.
	 * </p>
	 * 
	 * @param code
	 *            the CWT error code
	 * @param throwable
	 *            the exception which caused the error to occur (maybe
	 *            <code>null</code>)
	 * @see CWTError
	 * @see CWTException
	 * @see IllegalArgumentException
	 */
	public static void error(final int code, final Throwable throwable) throws IllegalArgumentException, CWTError, CWTException {
		error(code, throwable, null);
	}

	/**
	 * Throws an appropriate exception based on the passed in error code. The
	 * <code>throwable</code> argument should be either null, or the throwable
	 * which caused CWT to throw an exception.
	 * <p>
	 * In CWT, errors are reported by throwing one of three exceptions:
	 * <dl>
	 * <dd>IllegalArgumentException</dd>
	 * <dt>thrown whenever one of the API methods is invoked with an illegal
	 * argument</dt>
	 * <dd>{@link CWTException} (extends {@link java.lang.RuntimeException})</dd>
	 * <dt>thrown whenever a recoverable error happens internally in CWT</dt>
	 * <dd>{@link CWTError} (extends {@link java.lang.Error})</dd>
	 * <dt>thrown whenever a <b>non-recoverable</b> error happens internally in
	 * CWT</dt>
	 * </dl>
	 * This method provides the logic which maps between error codes and one of
	 * the above exceptions.
	 * </p>
	 * 
	 * @param code
	 *            the CWT error code.
	 * @param throwable
	 *            the exception which caused the error to occur (maybe
	 *            <code>null</code>)
	 * @param detail
	 *            more information about error (maybe <code>null</code>)
	 * @see CWTError
	 * @see CWTException
	 * @see IllegalArgumentException
	 */
	public static void error(final int code, final Throwable throwable, final String detail) throws IllegalArgumentException, CWTError, CWTException {

		/*
		 * This code prevents the creation of "chains" of RWTErrors and
		 * RWTExceptions which in turn contain other RWTErrors and RWTExceptions
		 * as their throwable. This can occur when low level code throws an
		 * exception past a point where a higher layer is being "safe" and
		 * catching all exceptions. (Note that, this is _a_bad_thing_ which we
		 * always try to avoid.)
		 * 
		 * On the theory that the low level code is closest to the original
		 * problem, we simply re-throw the original exception here.
		 */
		if (throwable instanceof CWTError) {
			throw (CWTError) throwable;
		}
		if (throwable instanceof CWTException) {
			throw (CWTException) throwable;
		}

		String message = findErrorText(code);
		if (detail != null) {
			message += "; " + detail;
		}
		switch (code) {

			/* Illegal Arguments (non-fatal) */
			case ERROR_NULL_ARGUMENT:
			case ERROR_INVALID_ARGUMENT:
				throw new IllegalArgumentException(message);

				/* CWT Exceptions (non-fatal) */
			case ERROR_WIDGET_NOT_FOUND:
			case ERROR_WIDGET_INITIALIZATION_FAILED:
			case ERROR_IO:
			case ERROR_UNSUPPORTED_FORMAT:
			case ERROR_NOT_INITIALIZED:
			case ERROR_ALREADY_INITIALIZED:
				throw new CWTException(code, message, throwable);

				/* CWT Errors (fatal, may occur only on some platforms) */
			case ERROR_NOT_IMPLEMENTED:
			case ERROR_UNSPECIFIED:
				throw new CWTError(code, message, throwable);
		}

		/* Unknown/Undefined Error */
		throw new CWTError(code, message, throwable);
	}

	/**
	 * Answers a concise, human readable description of the error code.
	 * 
	 * @param code
	 *            the CWT error code.
	 * @return a description of the error code.
	 * @see CWT
	 */
	static String findErrorText(final int code) {
		switch (code) {
			case ERROR_UNSPECIFIED:
				return "Unspecified error"; //$NON-NLS-1$
			case ERROR_NULL_ARGUMENT:
				return "Argument cannot be null"; //$NON-NLS-1$
			case ERROR_INVALID_ARGUMENT:
				return "Argument not valid"; //$NON-NLS-1$
			case ERROR_NOT_IMPLEMENTED:
				return "Not implemented"; //$NON-NLS-1$
			case ERROR_WIDGET_NOT_FOUND:
				return "Widget not found"; //$NON-NLS-1$
			case ERROR_WIDGET_INITIALIZATION_FAILED:
				return "Widget initialization failed"; //$NON-NLS-1$
			case ERROR_INVALID_SUBCLASS:
				return "Invalid subclass";
		}
		return "Unknown error"; //$NON-NLS-1$
	}

	/**
	 * No need to instanciate.
	 */
	private CWT() {
		// no need to instanciate
	}
}
