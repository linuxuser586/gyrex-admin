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
package org.eclipse.gyrex.toolkit.gwt.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Exception used by the GWT {@link WidgetFactory}.
 * <p>
 * An {@link #getErrorCode() error code} is available which should provide more
 * detailed information about the problem.
 * </p>
 */
public class WidgetFactoryException extends Exception implements IsSerializable {

	/**
	 * error code indicating that an internal error occurred (value
	 * <code>1</code>); the server log might provide more details
	 */
	public static final int INTERNAL_ERROR = 1;

	/**
	 * error code indicating that the widget factory is invalid or unknown on
	 * the server (value <code>2</code>); the server log might provide more
	 * details
	 */
	public static final int INVALID_WIDGET_FACTORY = 2;

	/**
	 * error code indicating that the widget factory throw an exception while
	 * creating the widget (value <code>3</code>); the server log might provide
	 * more details
	 */
	public static final int WIDGET_FACTORY_EXCEPTION = 3;

	/**
	 * error code indicating that no widget was returned by the server (value
	 * <code>4</code>)
	 */
	public static final int NO_WIDGET_RETURNED = 4;

	/**
	 * error code indicating that the widget could not be found by the widget
	 * factory (value <code>5</code>)
	 */
	public static final int WIDGET_NOT_FOUND = 5;

	/**
	 * error code indicating that the widget could not be serialized on the
	 * server (value <code>6</code>); the server log might provide more details
	 */
	public static final int WIDGET_SERIALIZATION_FAILED = 6;

	/**
	 * error code indicating that the widget could not be rendered on the client
	 * (value <code>7</code>); the exception might provide more details
	 */
	public static final int WIDGET_RENDERING_FAILED = 7;

	/** serialVersionUID */
	private static final long serialVersionUID = 5272480503141136791L;

	/** the error code */
	private int errorCode;

	/** detail */
	private String detail;

	/** message */
	private String message;

	/**
	 * Creates a new instance.
	 * <p>
	 * Note, this constructor is only available for GWT serialization. Clients
	 * must not use this parameter-less constructor.
	 * </p>
	 * 
	 * @deprecated This API is required by GWT serialization. It is not intended
	 *             for regular usage.
	 */
	@Deprecated
	public WidgetFactoryException() {
		super();
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param code
	 */
	public WidgetFactoryException(final int code) {
		this(code, null, null);
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param code
	 * @param message
	 */
	public WidgetFactoryException(final int code, final String message) {
		this(code, message, null);
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param code
	 * @param message
	 * @param detail
	 */
	public WidgetFactoryException(final int code, final String message, final String detail, final Throwable cause) {
		super(message, cause);
		errorCode = code;
		this.message = null != message ? message : "";
		this.detail = null != detail ? detail : "";
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param code
	 * @param cause
	 */
	public WidgetFactoryException(final int code, final String message, final Throwable cause) {
		this(code, message, message, cause);
	}

	/**
	 * Returns the detail.
	 * 
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * Returns the error code.
	 * 
	 * @return the error code
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + "; error code: " + errorCode;
	}

}
