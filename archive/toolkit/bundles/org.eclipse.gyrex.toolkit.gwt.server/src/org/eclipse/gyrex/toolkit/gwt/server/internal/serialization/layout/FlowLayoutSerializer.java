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
package org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization.layout;


import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedLayout;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.layout.SFlowLayout;
import org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization.LayoutSerializer;
import org.eclipse.cloudfree.toolkit.layout.FlowLayout;
import org.eclipse.cloudfree.toolkit.layout.Layout;

/**
 * {@link FlowLayout} serializer.
 */
public class FlowLayoutSerializer extends LayoutSerializer {

	@Override
	public ISerializedLayout serialize(final Layout layout) {
		final FlowLayout flowLayout = (FlowLayout) layout;
		final SFlowLayout sFlowLayout = new SFlowLayout();
		return fillBaseAttributes(flowLayout, sFlowLayout);
	}

}
