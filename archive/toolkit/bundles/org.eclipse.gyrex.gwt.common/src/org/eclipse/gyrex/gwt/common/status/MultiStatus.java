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
package org.eclipse.gyrex.gwt.common.status;

/**
 * A concrete multi-status implementation, suitable either for instantiating or
 * subclassing.
 * <p>
 * This class was copied from <code>org.eclipse.core.runtime.MultiStatus</code>
 * and adapted to the GWT world.
 * </p>
 */
public class MultiStatus extends Status {

	/** serialVersionUID */
	private static final long serialVersionUID = 6430029223661148877L;

	/**
	 * List of child statuses.
	 */
	private IStatus[] children;

	/**
	 * Creates a new uninitialized instance.
	 * <p>
	 * Note, this constructor only exists to satisfy GWT serialization. It must
	 * not be used directly by any client.
	 * </p>
	 * 
	 * @noreference This constructor is not intended to be referenced by
	 *              clients.
	 */
	protected MultiStatus() {
		// empty
	}

	/**
	 * Creates and returns a new multi-status object with the given children.
	 * 
	 * @param pluginId
	 *            the unique identifier of the relevant plug-in
	 * @param code
	 *            the plug-in-specific status code
	 * @param newChildren
	 *            the list of children status objects
	 * @param message
	 *            a human-readable message, localized to the current locale
	 * @param exception
	 *            a low-level exception, or <code>null</code> if not applicable
	 */
	public MultiStatus(final String pluginId, final int code, final IStatus[] newChildren, final String message, final Throwable exception) {
		this(pluginId, code, message, exception);
		if (newChildren == null) {
			throw new IllegalArgumentException("children must not be null");
		}
		int maxSeverity = getSeverity();
		for (int i = 0; i < newChildren.length; i++) {
			if (newChildren[i] == null) {
				throw new IllegalArgumentException("child " + i + " must not be null");
			}
			final int severity = newChildren[i].getSeverity();
			if (severity > maxSeverity) {
				maxSeverity = severity;
			}
		}
		children = new IStatus[newChildren.length];
		setSeverity(maxSeverity);
		System.arraycopy(newChildren, 0, children, 0, newChildren.length);
	}

	/**
	 * Creates and returns a new multi-status object with no children.
	 * 
	 * @param pluginId
	 *            the unique identifier of the relevant plug-in
	 * @param code
	 *            the plug-in-specific status code
	 * @param message
	 *            a human-readable message, localized to the current locale
	 * @param exception
	 *            a low-level exception, or <code>null</code> if not applicable
	 */
	public MultiStatus(final String pluginId, final int code, final String message, final Throwable exception) {
		super(OK, pluginId, code, message, exception);
		children = new IStatus[0];
	}

	/**
	 * Adds the given status to this multi-status.
	 * 
	 * @param status
	 *            the new child status
	 */
	public void add(final IStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("status must not be null");
		}

		final IStatus[] result = new IStatus[children.length + 1];
		System.arraycopy(children, 0, result, 0, children.length);
		result[result.length - 1] = status;
		children = result;
		final int newSev = status.getSeverity();
		if (newSev > getSeverity()) {
			setSeverity(newSev);
		}
	}

	/**
	 * Adds all of the children of the given status to this multi-status. Does
	 * nothing if the given status has no children (which includes the case
	 * where it is not a multi-status).
	 * 
	 * @param status
	 *            the status whose children are to be added to this one
	 */
	public void addAll(final IStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("status must not be null");
		}

		final IStatus[] statuses = status.getChildren();
		for (int i = 0; i < statuses.length; i++) {
			add(statuses[i]);
		}
	}

	/* (Intentionally not javadoc'd)
	 * Implements the corresponding method on <code>IStatus</code>.
	 */
	@Override
	public IStatus[] getChildren() {
		return children;
	}

	/* (Intentionally not javadoc'd)
	 * Implements the corresponding method on <code>IStatus</code>.
	 */
	@Override
	public boolean isMultiStatus() {
		return true;
	}

	/**
	 * Merges the given status into this multi-status. Equivalent to
	 * <code>add(status)</code> if the given status is not a multi-status.
	 * Equivalent to <code>addAll(status)</code> if the given status is a
	 * multi-status.
	 * 
	 * @param status
	 *            the status to merge into this one
	 * @see #add(IStatus)
	 * @see #addAll(IStatus)
	 */
	public void merge(final IStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("status must not be null");
		}

		if (!status.isMultiStatus()) {
			add(status);
		} else {
			addAll(status);
		}
	}

	/**
	 * Returns a string representation of the status, suitable for debugging
	 * purposes only.
	 */
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer(super.toString());
		buf.append(" children=["); //$NON-NLS-1$
		for (int i = 0; i < children.length; i++) {
			if (i != 0) {
				buf.append(" "); //$NON-NLS-1$
			}
			buf.append(children[i].toString());
		}
		buf.append("]"); //$NON-NLS-1$
		return buf.toString();
	}
}
