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
package org.eclipse.cloudfree.toolkit.gwt.examples.simple1;


import org.eclipse.cloudfree.toolkit.examples.simple.Simple1WidgetAdapterFactory;
import org.eclipse.cloudfree.toolkit.examples.simple.Simple1WidgetFactory;
import org.eclipse.cloudfree.toolkit.gwt.server.WidgetService;
import org.eclipse.cloudfree.toolkit.gwt.server.WidgetServiceServlet;

/**
 * {@link WidgetServiceServlet} for example Simple1
 */
public class Simple1WidgetService extends WidgetService {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance.
	 */
	public Simple1WidgetService() {
		super(new Simple1WidgetFactory(), new Simple1WidgetAdapterFactory());
	}
}
