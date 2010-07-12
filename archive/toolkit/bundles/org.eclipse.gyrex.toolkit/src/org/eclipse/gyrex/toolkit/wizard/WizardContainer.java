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

package org.eclipse.gyrex.toolkit.wizard;

import java.util.Arrays;

import org.eclipse.gyrex.toolkit.Toolkit;
import org.eclipse.gyrex.toolkit.commands.Command;
import org.eclipse.gyrex.toolkit.layout.Layout;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.widgets.Widget;

/**
 * A container to show a wizard to the end user.
 * <p>
 * In typical usage, the client instantiates this class and uses it as a parent
 * for a set {@link WizardPage wizard pages}. The wizard container orchestrates
 * the presentation of its pages.
 * </p>
 * <p>
 * The standard layout is roughly as follows: it has an area at the top
 * containing both the wizard's title, description, and image; the actual wizard
 * page appears in the middle; below that is a progress indicator (which is made
 * visible if needed); and at the bottom of the page is message line and a
 * button bar containing Help, Next, Back, Finish, and Cancel buttons (or some
 * subset).
 * </p>
 * <p>
 * Note, although the wizard container is a {@link Container container} it
 * doesn't make sense to add children other than {@link WizardPage wizard pages}
 * to it. Also setting a layout is a no-op.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 */
public final class WizardContainer extends Container {

	/** serialVersionUID */
	private static final long serialVersionUID = -8573074961533222970L;

	/** the cancel command */
	private Command cancelCommand;

	/** the finish command */
	private Command finishCommand;

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 * @param parent
	 * @param style
	 */
	public WizardContainer(final String id, final Container parent, final int style) {
		super(id, parent, style);
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 * @param style
	 */
	public WizardContainer(final String id, final int style) {
		super(id, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rwt.widgets.Container#checkChildWidget(org.eclipse.rwt.widgets.Widget)
	 */
	@Override
	protected void checkChildWidget(final Widget widget) {
		if (WizardPage.class != widget.getClass()) {
			Toolkit.error(Toolkit.ERROR_INVALID_ARGUMENT, "not a wizard page");
		}
	}

	/**
	 * Returns the command to be triggered when the wizard is canceled.
	 * 
	 * @return the command (maybe <code>null</code>)
	 */
	public Command getCancelCommand() {
		return cancelCommand;
	}

	/**
	 * Returns the command to be triggered when the wizard is finished.
	 * 
	 * @return the command (maybe <code>null</code>)
	 */
	public Command getFinishCommand() {
		return finishCommand;
	}

	/**
	 * Convenience method which returns all child widgets as wizard pages.
	 * 
	 * @return the container children as wizard pages
	 */
	public WizardPage[] getPages() {
		final Widget[] widgets = getWidgets();
		return Arrays.asList(widgets).toArray(new WizardPage[widgets.length]);
	}

	/**
	 * Sets the command to be triggered when the wizard is canceled.
	 * 
	 * @param cancelCommand
	 *            the command to be triggered
	 */
	public void setCancelCommand(final Command cancelCommand) {
		this.cancelCommand = cancelCommand;
	}

	/**
	 * Sets the command to be triggered when the wizard is finished.
	 * 
	 * @param finishCommand
	 *            the command to be triggered
	 */
	public void setFinishCommand(final Command finishCommand) {
		this.finishCommand = finishCommand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rwt.widgets.Container#setLayout(org.eclipse.rwt.layout.Layout)
	 */
	@Override
	public void setLayout(final Layout layout) {
		// no-op
	}
}
