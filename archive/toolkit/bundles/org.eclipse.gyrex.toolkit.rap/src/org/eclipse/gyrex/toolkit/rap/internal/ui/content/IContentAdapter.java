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
package org.eclipse.gyrex.toolkit.rap.internal.ui.content;


import org.eclipse.gyrex.toolkit.content.ContentObject;
import org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTWidget;

/**
 * The content adapter is responsible for reading and writing content to a
 * widget.
 */
public interface IContentAdapter {

	/**
	 * Returns the widget content as a content object.
	 * 
	 * @param widget
	 *            the widget
	 * @return the content object (maybe <code>null</code>)
	 */
	ContentObject getContent(CWTWidget widget);

	/**
	 * Indicates if the specified widget has content.
	 * 
	 * @param widget
	 *            the widget
	 * @return <code>true</code> if the widget has content, <code>false</code>
	 *         otherwise
	 */
	boolean hasContent(CWTWidget widget);

	/**
	 * Sets the widget content.
	 * 
	 * @param widget
	 *            the widget
	 * @param content
	 *            the content to set
	 */
	void setContent(CWTWidget widget, ContentObject content);
}
