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
package org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization;


import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedLayout;
import org.eclipse.cloudfree.toolkit.layout.Layout;

/**
 * Abstract base class for layout serializers.
 */
public abstract class LayoutSerializer {

	/**
	 * Fills the serialized layout base attributes.
	 * 
	 * @param layout
	 *            the layout to read the attributes from
	 * @param serializedLayout
	 *            the ISerializedLayout to write the attributes to
	 * @return the passed in SWidget for convenience
	 */
	protected <T extends ISerializedLayout> T fillBaseAttributes(Layout layout, T serializedLayout) {
		return serializedLayout;
	}

	/**
	 * Serializes the specified layout.
	 * 
	 * @param layout
	 *            the layout to serialize
	 * @return the serialized layout
	 */
	public abstract ISerializedLayout serialize(Layout layout);

}
