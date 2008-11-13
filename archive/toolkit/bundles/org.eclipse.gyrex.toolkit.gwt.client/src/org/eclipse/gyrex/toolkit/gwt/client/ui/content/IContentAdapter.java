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
package org.eclipse.cloudfree.toolkit.gwt.client.ui.content;

import org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets.CWTWidget;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.content.SContentEntry;

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
	SContentEntry getContent(CWTWidget widget);

	/**
	 * Indicates if the specified widget has content.
	 * <p>
	 * This will be used by the dialog rules framework to evaluate if a widget
	 * has content, i.e. "is set".
	 * </p>
	 * 
	 * @param widget
	 *            the widget
	 * @return <code>true</code> if the widget has content, <code>false</code>
	 *         otherwise
	 */
	boolean isSet(CWTWidget widget);

	/**
	 * Sets the widget content.
	 * 
	 * @param widget
	 *            the widget
	 * @param content
	 *            the content to set
	 */
	void setContent(CWTWidget widget, SContentEntry content);
}
