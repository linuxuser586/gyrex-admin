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
package org.eclipse.cloudfree.toolkit.content;

/**
 * A content object indicating a single selection status.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 */
public class BooleanContent extends ContentObject {

	/** serialVersionUID */
	private static final long serialVersionUID = 3295485395967920248L;

	/** selection state */
	private final boolean value;

	/**
	 * Creates a new instance.
	 * 
	 * @param value
	 *            the selection status
	 */
	public BooleanContent(final boolean value) {
		super();
		this.value = value;
	}

	/**
	 * Returns the boolean value.
	 * 
	 * @return <code>true</code> or <code>false</code>
	 */
	public boolean getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Boolean[%b]", value);
	}
}
