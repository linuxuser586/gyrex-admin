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
 * A content object indicating that content is not supported.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class NoContent extends StructuredContent {

	/** serialVersionUID */
	private static final long serialVersionUID = 5158570990007663698L;

	@Override
	public String toString() {
		return "NoContent";
	}
}