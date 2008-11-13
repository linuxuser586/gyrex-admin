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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Serializable Label implementation.
 */
public class SNumberInput extends SDialogField implements IsSerializable {
	public static enum Type {
		INTEGER, DECIMAL, CURRENY, PERCENTAGE
	}

	public Type type = Type.INTEGER;
	public Number upperLimit, lowerLimit;
	public boolean upperLimitInclusive, lowerLimitInclusive;

}
