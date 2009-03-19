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
package org.eclipse.gyrex.toolkit.wizard;

import org.eclipse.gyrex.toolkit.CWT;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldRule;

/**
 * A wizard page.
 */
public final class WizardPage extends Container {

	/** serialVersionUID */
	private static final long serialVersionUID = 5223482176399360469L;

	private DialogFieldRule continueRule;

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 * @param parent
	 * @param style
	 */
	public WizardPage(final String id, final WizardContainer parent) {
		super(id, parent, CWT.NONE);
	}

	/**
	 * Returns the continuing rule.
	 * 
	 * @return the continuing rule for this dialog field (may be
	 *         <code>null</code>)
	 * @see #setContinueRule(DialogFieldRule)
	 */
	public DialogFieldRule getContinueRule() {
		return continueRule;
	}

	/**
	 * Sets a continuing rule for this dialog field.
	 * <p>
	 * If a continuing rule is set it will be used to determine the continuing
	 * state for the wizard page. The rule must evaluate positive before the
	 * wizard can continue to the next page.
	 * <p>
	 * <p>
	 * The rule is evaluated at runtime.
	 * </p>
	 * 
	 * @param rule
	 *            the continue rule to evaluate (may be <code>null</code> to
	 *            unset)
	 */
	public void setContinueRule(final DialogFieldRule rule) {
		continueRule = rule;
	}
}
