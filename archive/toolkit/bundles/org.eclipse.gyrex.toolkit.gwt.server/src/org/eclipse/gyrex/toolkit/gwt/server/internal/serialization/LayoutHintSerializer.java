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
package org.eclipse.gyrex.toolkit.gwt.server.internal.serialization;


import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedLayoutHint;
import org.eclipse.gyrex.toolkit.layout.LayoutHint;

/**
 * Abstract base class for layout hint serializers.
 */
public abstract class LayoutHintSerializer {

	/**
	 * Fills the serialized layout hint base attributes.
	 * 
	 * @param layoutHint
	 *            the layout hint to read the attributes from
	 * @param serializedLayoutHint
	 *            the {@link ISerializedLayoutHint} to write the attributes to
	 * @return the passed in SWidget for convenience
	 */
	protected <T extends ISerializedLayoutHint> T fillBaseAttributes(LayoutHint layoutHint, T serializedLayoutHint) {
		return serializedLayoutHint;
	}

	/**
	 * Serializes the specified layout hint.
	 * 
	 * @param layoutHint
	 *            the layout hint to serialize
	 * @return the serialized layout hint
	 */
	public abstract ISerializedLayoutHint serialize(LayoutHint layoutHint);

}
