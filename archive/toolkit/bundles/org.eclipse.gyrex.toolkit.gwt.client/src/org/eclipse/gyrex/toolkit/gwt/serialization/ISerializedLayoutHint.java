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
package org.eclipse.cloudfree.toolkit.gwt.serialization;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Marker interface for serialized widget layout hints.
 * <p>
 * This interface may be implemented by custom widget providers.
 * </p>
 */
public interface ISerializedLayoutHint extends IsSerializable {

}
