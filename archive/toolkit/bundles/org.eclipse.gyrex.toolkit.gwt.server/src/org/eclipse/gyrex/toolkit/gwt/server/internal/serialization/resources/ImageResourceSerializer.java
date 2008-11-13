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
package org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization.resources;


import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedResource;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.resources.SImageResource;
import org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization.ResourceSerializer;
import org.eclipse.cloudfree.toolkit.resources.ImageResource;
import org.eclipse.cloudfree.toolkit.resources.Resource;

/**
 * {@link ImageResource} serializer.
 */
public class ImageResourceSerializer extends ResourceSerializer {

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization.ResourceSerializer#serialize(org.eclipse.cloudfree.toolkit.resources.Resource)
	 */
	@Override
	public ISerializedResource serialize(final Resource resource) {
		final ImageResource imageResource = (ImageResource) resource;
		final SImageResource sImageResource = new SImageResource();
		return populateAttributes(imageResource, sImageResource);
	}

}
