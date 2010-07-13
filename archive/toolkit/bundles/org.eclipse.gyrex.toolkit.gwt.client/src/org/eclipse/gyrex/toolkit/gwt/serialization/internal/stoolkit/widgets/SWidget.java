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
package org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets;

import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedLayoutHint;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;

/**
 * Serializable Widget implementation.
 */
public class SWidget implements ISerializedWidget {
	/** serialVersionUID */
	private static final long serialVersionUID = 7744263489911409109L;
	public String id;
	public int style;
	public String toolTipText;
	public ISerializedLayoutHint[] layoutHints;
	public ISerializedWidget parent;
	public SDialogFieldRule visibilityRule;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rep.web.gwt.client.rwt.ISerializedWidget#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rep.web.gwt.client.rwt.ISerializedWidget#getParent()
	 */
	@Override
	public ISerializedWidget getParent() {
		return parent;
	}
}
