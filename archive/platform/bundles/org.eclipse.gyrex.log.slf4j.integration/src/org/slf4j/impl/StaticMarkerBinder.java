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

import org.slf4j.IMarkerFactory;
import org.slf4j.spi.MarkerFactoryBinder;

/**
 * Hook in the SLF4J API to bind our {@link IMarkerFactory}.
 */
public class StaticMarkerBinder implements MarkerFactoryBinder {

	@Override
	public IMarkerFactory getMarkerFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMarkerFactoryClassStr() {
		// TODO Auto-generated method stub
		return null;
	}

}
