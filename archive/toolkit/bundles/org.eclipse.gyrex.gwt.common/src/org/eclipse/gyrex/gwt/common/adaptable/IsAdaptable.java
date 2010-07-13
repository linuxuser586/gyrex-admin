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
package org.eclipse.gyrex.gwt.common.adaptable;

/**
 * An interface for an adaptable object.
 * <p>
 * Adaptable objects can be dynamically extended to provide different interfaces
 * (or "adapters"). Adapters are created by adapter factories, which are in turn
 * managed by type by adapter managers.
 * </p>
 * <p>
 * For example,
 * 
 * <pre>
 *     IAdaptable a = [some adaptable];
 *     IFoo x = (IFoo)a.getAdapter(IFoo.class);
 *     if (x != null)
 *         [do IFoo things with x]
 * </pre>
 * 
 * </p>
 * <p>
 * This interface is a clone of the Eclipse
 * <code>org.eclipse.core.runtime.IAdaptable</code> concept. The cloning was
 * introduced to allow adaption of this concept to the GWT world in a lighter
 * approach. Thus, the scope is much more limited to this application space.
 * </p>
 * <p>
 * Clients may implement this interface.
 * </p>
 * 
 * @see AdapterManager
 */
public interface IsAdaptable {

	/**
	 * Returns an object which is an instance of the given class associated with
	 * this object. Returns <code>null</code> if no such object can be found.
	 * 
	 * @param adapter
	 *            the adapter class to look up
	 * @return a object castable to the given class, or <code>null</code> if
	 *         this object does not have an adapter for the given class
	 */
	public <T> T getAdapter(Class<T> adapter);
}
