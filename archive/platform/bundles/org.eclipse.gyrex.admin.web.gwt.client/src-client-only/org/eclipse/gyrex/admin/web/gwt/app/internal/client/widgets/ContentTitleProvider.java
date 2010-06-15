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
package org.eclipse.gyrex.admin.web.gwt.app.internal.client.widgets;

import com.google.gwt.user.client.ui.Widget;

import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTContainer;

/**
 * A title and description provider for the {@link Content} widget.
 */
public class ContentTitleProvider {

	public String getDescription(Widget widget) {
		if (widget instanceof CWTContainer)
			return ((CWTContainer) widget).getContainerDescriptionText();
		return "";
	}

	public String getTitle(Widget widget) {
		if (widget instanceof CWTContainer)
			return ((CWTContainer) widget).getContainerTitleText();
		return "";
	}

}
