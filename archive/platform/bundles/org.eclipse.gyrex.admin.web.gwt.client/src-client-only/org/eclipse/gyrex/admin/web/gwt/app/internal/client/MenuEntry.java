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
package org.eclipse.gyrex.admin.web.gwt.app.internal.client;

/**
 * A menu entry
 */
public class MenuEntry {

	private final String id;
	private final String name;

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 * @param name
	 */
	public MenuEntry(String name, String id) {
		super();
		this.id= id;
		this.name= name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
