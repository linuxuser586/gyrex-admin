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
package org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation;

import java.util.HashMap;
import java.util.Map;

public class ValidationContext {
	private final Map attributes = new HashMap();

	ValidationContext() {
		super();
	}

	public boolean getBoolean(final String key) {
		final Boolean result = (Boolean) attributes.get(key);
		return null != result ? result.booleanValue() : false;
	}

	public String getString(final String key) {
		return (String) attributes.get(key);
	}

	public void set(final String key, final boolean value) {
		attributes.put(key, new Boolean(value));
	}

	public void set(final String key, final String value) {
		attributes.put(key, value);
	}
}
