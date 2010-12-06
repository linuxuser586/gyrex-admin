/*******************************************************************************
 * Copyright (c) 2010 <enter-company-name-here> and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     <enter-developer-name-here> - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.runtime.content;

/**
 * Provider of widget content.
 * <p>
 * This interface may be implemented by clients.
 * </p>
 */
public interface IContentHandler {

	/**
	 * Populates the specified widget with content.
	 * 
	 * @param widgetId
	 */
	void populate(String widgetId);

}
