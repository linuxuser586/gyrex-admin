/*******************************************************************************
 * Copyright (c) 2008, 2010 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.admin.web.gwt.client.internal.console.widgets;

import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;

public class SError implements ISerializedWidget {
	/** serialVersionUID */
	private static final long serialVersionUID = -6991449610745415913L;

	public String getId() {
		return "error";
	}

	public ISerializedWidget getParent() {
		return null;
	}
}