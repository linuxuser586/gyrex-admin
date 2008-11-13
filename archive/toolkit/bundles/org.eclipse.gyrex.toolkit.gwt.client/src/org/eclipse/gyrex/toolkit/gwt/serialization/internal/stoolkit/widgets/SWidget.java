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

import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedLayoutHint;
import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedWidget;

/**
 * Serializable Widget implementation.
 */
public class SWidget implements ISerializedWidget {
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
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rep.web.gwt.client.rwt.ISerializedWidget#getParent()
	 */
	public ISerializedWidget getParent() {
		return parent;
	}
}
