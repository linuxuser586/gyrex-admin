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
package org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.layout;


import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedLayout;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.layout.SGridLayout;
import org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.LayoutSerializer;
import org.eclipse.gyrex.toolkit.layout.GridLayout;
import org.eclipse.gyrex.toolkit.layout.Layout;

/**
 * {@link GridLayout} serializer.
 */
public class GridLayoutSerializer extends LayoutSerializer {

	@Override
	public ISerializedLayout serialize(Layout layout) {
		GridLayout gridLayout = (GridLayout) layout;
		SGridLayout sGridLayout = new SGridLayout();
		sGridLayout.numberOfColumns = gridLayout.numberOfColumns;
		sGridLayout.makeColumnsEqualWidth = gridLayout.makeColumnsEqualWidth;
		return fillBaseAttributes(gridLayout, sGridLayout);
	}

}
