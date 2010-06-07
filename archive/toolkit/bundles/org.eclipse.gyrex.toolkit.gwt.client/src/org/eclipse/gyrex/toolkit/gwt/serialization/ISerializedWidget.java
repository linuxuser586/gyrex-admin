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
package org.eclipse.gyrex.toolkit.gwt.serialization;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A serialized CWT widget.
 * <p>
 * This interface may be implemented by custom widget providers.
 * </p>
 */
public interface ISerializedWidget extends IsSerializable {

	/**
	 * Returns the widget id.
	 * 
	 * @return the widget id
	 */
	String getId();

	/**
	 * Returns the parent widget.
	 * 
	 * @return the parent widget
	 */
	ISerializedWidget getParent();

}
