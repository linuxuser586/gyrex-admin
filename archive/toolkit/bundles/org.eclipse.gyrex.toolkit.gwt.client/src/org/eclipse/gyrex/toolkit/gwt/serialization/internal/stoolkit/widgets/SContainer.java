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
package org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets;

import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedLayout;
import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedWidget;

/**
 * Serializable Container implementation
 */
public class SContainer extends SWidget {

	public ISerializedWidget[] widgets;

	public ISerializedLayout layout;

	public String title, description;

}
