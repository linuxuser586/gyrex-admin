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
package org.eclipse.gyrex.toolkit.widgets;

import org.eclipse.gyrex.toolkit.Toolkit;

/**
 * A dialog field with a radio button.
 * <p>
 * Note: All radio dialog fields belonging to the same container (just that
 * container level) are considered to be within the same radio group. That means
 * that only one can be active at the same time. Additionally, although the
 * style attribute {@link Toolkit#REQUIRED} is supported, it does not apply to an
 * individual radio button directly but to the whole group.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 * 
 * @see Toolkit#REQUIRED
 */
public class RadioButton extends Checkbox {

	/** serialVersionUID */
	private static final long serialVersionUID = 251838487425522865L;

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 *            the widget id
	 * @param parent
	 *            the widget parent
	 * @param style
	 *            the widget style
	 */
	public RadioButton(final String id, final Container parent, final int style) {
		super(id, parent, style);
	}
}
