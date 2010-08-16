/*******************************************************************************
 * Copyright (c) 2010 AGETO and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets;

import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.resources.SImageResource;

/**
 * Serializable Menu implementation.
 */
public class SMenu extends SContainer {
	/** serialVersionUID */
	private static final long serialVersionUID = 287774337633359336L;
	public SImageResource image;
	public SMenuItem[] featuresItems;
}