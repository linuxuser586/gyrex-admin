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
package org.eclipse.gyrex.toolkit.commands;

import java.io.Serializable;

import org.eclipse.gyrex.toolkit.widgets.DialogFieldRule;

/**
 * A lightweight description of a command to be triggered through the user
 * interface.
 * <p>
 * Commands can be associated with widgets (eg. buttons), events, keystrokes,
 * etc. Usually, they trigger an action or a process in the application.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 */
public class Command implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -3348625346504463784L;

	/** command id */
	private final String id;

	/** content submit rule */
	private DialogFieldRule contentSubmitRule;

	/**
	 * Creates and returns a new command using the specified id.
	 * 
	 * @param id
	 *            the command id
	 */
	public Command(final String id) {
		this.id = id;
	}

	/**
	 * Creates and returns a new command using the specified id and sets the
	 * specified submit rule.
	 * 
	 * @param id
	 *            the command id
	 * @param contentSubmitRule
	 *            the submit rule to set (may be <code>null</code> to unset),
	 *            see {@link #setContentSubmitRule(DialogFieldRule)}
	 */
	public Command(final String id, final DialogFieldRule contentSubmitRule) {
		this(id);
		setContentSubmitRule(contentSubmitRule);
	}

	/**
	 * Returns the dialog field rule that will be used for selecting the fields
	 * to submit to the handler when this command is triggered.
	 * 
	 * @return the content submit rule (may be <code>null</code> if not set)
	 */
	public DialogFieldRule getContentSubmitRule() {
		return contentSubmitRule;
	}

	/**
	 * Returns the id of this command.
	 * 
	 * @return the command id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the dialog field rule that will be used for selecting the fields to
	 * submit to the handler when this command is triggered.
	 * 
	 * @param contentSubmitRule
	 *            the submit rule to set (may be <code>null</code> to unset)
	 */
	public void setContentSubmitRule(final DialogFieldRule contentSubmitRule) {
		this.contentSubmitRule = contentSubmitRule;
	}
}
