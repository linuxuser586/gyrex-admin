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
package org.eclipse.gyrex.toolkit.resources;

import java.io.Serializable;
import java.net.URL;

/**
 * Base class for resources.
 * <p>
 * A resource is an object that needs to be made available to the UI in some
 * form. Typically, the resource is accessible by the user defining a resource.
 * The rendering technology will provide a way to access the resource from the
 * UI.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class Resource implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 5538527414764834703L;

	/**
	 * Returns a URL to the resource.
	 * <p>
	 * This method allows the toolkit to make a resource available to renderers
	 * from any source. Resources can be located on a local or remote file
	 * system, in a database, or in a <code>.jar</code> file.
	 * </p>
	 * <p>
	 * The toolkit must run in an environment which implements the URL handlers
	 * and <code>URLConnection</code> objects that are necessary to access the
	 * resource.
	 * </p>
	 * <p>
	 * This method returns <code>null</code> if the resource is not available.
	 * </p>
	 * <p>
	 * Some renderers may allow writing to the URL returned by this method using
	 * the methods of the URL class.
	 * </p>
	 * <p>
	 * The resource content is used directly.
	 * </p>
	 * 
	 * @return the resource or <code>null</code> if the resource is not
	 *         available (eg. does not exist)
	 */
	public abstract URL getUrl();

}
