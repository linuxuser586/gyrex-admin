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
package org.eclipse.gyrex.toolkit.content;

/**
 * A number content.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class NumberContent extends ContentObject {

	/** serialVersionUID */
	private static final long serialVersionUID = -5945936249354971412L;

	/** text */
	private final Number number;

	/**
	 * Creates a new instance.
	 * 
	 * @param number
	 *            the number
	 */
	public NumberContent(final Number number) {
		this.number = number;
	}

	/**
	 * Returns the number.
	 * 
	 * @return the number
	 */
	public Number getNumber() {
		return number;
	}

	@Override
	public String toString() {
		return String.format("Number[%s]", number);
	}
}
