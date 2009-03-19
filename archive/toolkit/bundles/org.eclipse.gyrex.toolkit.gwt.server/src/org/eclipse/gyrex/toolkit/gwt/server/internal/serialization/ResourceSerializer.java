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


import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedResource;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.resources.SResource;
import org.eclipse.gyrex.toolkit.gwt.server.internal.ResourceUrlEncoder;
import org.eclipse.gyrex.toolkit.resources.Resource;

/**
 * Abstract base class for resource serializers.
 */
public abstract class ResourceSerializer {

	/**
	 * Fills the serialized resource base attributes.
	 * 
	 * @param resource
	 *            the resource to read the attributes from
	 * @param serializedResource
	 *            the SResource to write the attributes to
	 * @return the passed in serialized resource for convenience
	 */
	protected ISerializedResource populateAttributes(final Resource resource, final ISerializedResource serializedResource) {
		final SResource sResource = (SResource) serializedResource;
		sResource.reference = ResourceUrlEncoder.encodeResourceUrl(resource);
		return sResource;
	}

	/**
	 * Serializes the specified resource.
	 * 
	 * @param resource
	 *            the resource to serialize
	 * @return the serialized resource
	 */
	public abstract ISerializedResource serialize(Resource resource);
}
