/*******************************************************************************
 * Copyright (c) 2008, 2009 Gunnar Wagenknecht and others.
 * All rights reserved.
 *  
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/

package org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.resources;

import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTToolkit;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedResource;

/**
 * Serializable Resource.
 * <p>
 * Note, serialization of resources is different. Typically, a resource is
 * "exported" by the server and only a reference to the resource is given to the
 * GWT base client. The client that passes that reference to the
 * {@link CWTToolkit} which knows how to translate that into an URL where the
 * resource can be accessed by the client.
 * </p>
 */
public class SResource implements ISerializedResource {
	/**
	 * the resource's references to be passed to the
	 * {@link CWTToolkit#getResourceUrl(String)}
	 */
	public String reference;
}
