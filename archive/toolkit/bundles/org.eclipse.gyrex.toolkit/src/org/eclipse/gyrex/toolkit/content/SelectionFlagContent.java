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
public class SelectionFlagContent extends ContentObject {

	/** serialVersionUID */
	private static final long serialVersionUID = 3295485395967920248L;

	/** selection state */
	private final boolean selected;

	/**
	 * Creates a new instance.
	 * 
	 * @param selected
	 *            the selection status
	 */
	public SelectionFlagContent(final boolean selected) {
		super();
		this.selected = selected;
	}

	/**
	 * Returns the selection status.
	 * 
	 * @return <code>true</code> if selected, <code>false</code> otherwise
	 */
	public boolean isSelected() {
		return selected;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("SelectionFlag[%b]", selected);
	}
}
