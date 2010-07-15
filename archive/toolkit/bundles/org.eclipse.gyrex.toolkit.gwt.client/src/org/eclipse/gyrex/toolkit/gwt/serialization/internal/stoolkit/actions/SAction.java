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
package org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.actions;

import java.io.Serializable;

import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedAction;

/**
 * Serializable Action
 */
public class SAction implements Serializable, ISerializedAction {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public int hints;
}
