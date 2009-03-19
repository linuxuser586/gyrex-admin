/**
 * Copyright (c) 2009 AGETO Service GmbH and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package org.slf4j.impl;

import org.slf4j.helpers.BasicMDCAdapter;
import org.slf4j.spi.MDCAdapter;

/**
 * Hook in the SLF4J API to bind our MDC support.
 */
public class StaticMDCBinder {

	/**
	 * The singleton instance of this {@link StaticMDCBinder}.
	 * <p>
	 * Note, while all other SLF4J API hooks use a common approach this one
	 * requires a <code>public</code> <code>static</code> <code>final</code>
	 * field.
	 * </p>
	 */
	public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

	/**
	 * Hidden constructor.
	 */
	private StaticMDCBinder() {
		// empty
	}

	// XXX: this should be better captured within SLF4J API
	public MDCAdapter getMDCA() {
		// TODO write our own adapter which integrates with several Eclipse/Equinox APIs (eg. Jobs)
		return new BasicMDCAdapter();
	}

	// XXX: this should be better captured within SLF4J API
	public String getMDCAdapterClassStr() {
		return BasicMDCAdapter.class.getName();
	}
}
