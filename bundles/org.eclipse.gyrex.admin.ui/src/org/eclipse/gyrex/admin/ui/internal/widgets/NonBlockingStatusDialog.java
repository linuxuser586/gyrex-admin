/*******************************************************************************
 * Copyright (c) 2012 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.admin.ui.internal.widgets;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.gyrex.admin.ui.internal.AdminUiActivator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.util.Policy;
import org.eclipse.rap.rwt.widgets.DialogCallback;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

/**
 * A {@link StatusDialog} that supports a callback pattern in a non-blocking
 * environment.
 * <p>
 * 
 * <pre>
 * final NonBlockingStatusDialog dialog = ...;
 * dialog.<strong>openNonBlocking</strong>(new DialogCallback() {
 *     public void dialogClosed(final int returnCode) {
 *         if (returnCode == Window.OK) {
 *             // execute logic on success
 *         }
 *     }
 * });
 * </pre>
 * 
 * </p>
 */
public class NonBlockingStatusDialog extends StatusDialog {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	private final AtomicReference<DialogCallback> callbackRef = new AtomicReference<DialogCallback>();

	/**
	 * Creates an instance of a status dialog.
	 * 
	 * @param parent
	 *            the parent Shell of the dialog
	 */
	public NonBlockingStatusDialog(final Shell parent) {
		super(parent);
	}

	@Override
	public boolean close() {
		final boolean closed = super.close();
		if (closed) {
			final DialogCallback callback = callbackRef.getAndSet(null);
			if (null != callback) {
				callback.dialogClosed(getReturnCode());
			}
		}
		return closed;
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		final Shell shell = getParentShell();
		if (shell != null) {
			// center dialog
			final Rectangle displayBounds = shell.getDisplay().getBounds();
			final Point size = getShell().getSize();
			final int x = (displayBounds.width - size.x) / 2;
			final int y = (displayBounds.height - size.y) / 2;
			getShell().setLocation(x, y);
		}
	}

	@Override
	public final int open() {
		try {
			return super.open();
		} catch (final Exception | LinkageError | AssertionError e) {
			Policy.getStatusHandler().show(e instanceof CoreException ? ((CoreException) e).getStatus() : new Status(IStatus.ERROR, AdminUiActivator.SYMBOLIC_NAME, "Unable to open dialog. Please check the server logs.", e), "Error Opening Dialog");
			return CANCEL;
		}
	}

	/**
	 * Opens this window, creating it first if it has not yet been created.
	 * <p>
	 * The window will be configured to not block on open. The specified
	 * callback will be set and (if not <code>null</code>) will be called when
	 * the windows is closed. Clients may use {@link #getReturnCode()} to obtain
	 * the return code that {@link #open()} returns in blocking mode.
	 * </p>
	 * 
	 * @see #create()
	 */
	public void openNonBlocking(final DialogCallback callback) {
		if (!callbackRef.compareAndSet(null, callback))
			throw new IllegalStateException("Concurrent operation not supported!");
		setBlockOnOpen(false);
		open();
	}
}
