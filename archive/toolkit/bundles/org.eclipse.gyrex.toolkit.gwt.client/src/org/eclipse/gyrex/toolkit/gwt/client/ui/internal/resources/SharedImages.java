/*******************************************************************************
 * Copyright (c) 2010 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/

package org.eclipse.gyrex.toolkit.gwt.client.ui.internal.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * shared images
 */
public interface SharedImages extends ClientBundle {

	public static final SharedImages INSTANCE = GWT.create(SharedImages.class);

	@Source("img/ico16/error.gif")
	ImageResource iconError();

	@Source("img/ico16/info.gif")
	ImageResource iconInformation();

	@Source("img/ico16/warn.gif")
	ImageResource iconWarning();
}
