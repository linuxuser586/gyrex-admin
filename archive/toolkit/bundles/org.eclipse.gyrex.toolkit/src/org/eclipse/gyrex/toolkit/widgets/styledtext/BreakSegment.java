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
package org.eclipse.gyrex.toolkit.widgets.styledtext;

/**
 * This segment serves as break within a paragraph. It has no data - just starts
 * a new line.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class BreakSegment extends ParagraphSegment {

	/**
	 * Creates a new instance.
	 */
	BreakSegment() {
		super(true, null);
	}

}
