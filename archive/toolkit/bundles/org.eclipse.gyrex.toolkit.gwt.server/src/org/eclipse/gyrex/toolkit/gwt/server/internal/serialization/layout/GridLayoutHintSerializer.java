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


import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedLayoutHint;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.layout.SGridLayoutHint;
import org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.LayoutHintSerializer;
import org.eclipse.gyrex.toolkit.layout.GridLayoutHint;
import org.eclipse.gyrex.toolkit.layout.LayoutHint;

/**
 * {@link GridLayoutHint} serializer.
 */
public class GridLayoutHintSerializer extends LayoutHintSerializer {

	@Override
	public ISerializedLayoutHint serialize(LayoutHint layoutHint) {
		GridLayoutHint gridLayoutHint = (GridLayoutHint) layoutHint;
		SGridLayoutHint sGridLayoutHint = new SGridLayoutHint();
		sGridLayoutHint.spanColumns = gridLayoutHint.spanColumns;
		sGridLayoutHint.spanRows = gridLayoutHint.spanRows;
		return fillBaseAttributes(gridLayoutHint, sGridLayoutHint);
	}

}
