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
package org.eclipse.cloudfree.toolkit.widgets.styledtext;

/**
 * A hyperlink segment.
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IHyperlinkSegment {

	/**
	 * Returns the hyperlink target URL.
	 * 
	 * @return the hyperlink
	 */
	String getHref();

	/**
	 * Returns the hyperlink text
	 * 
	 * @return the hyperlink text
	 */
	String getText();
}
