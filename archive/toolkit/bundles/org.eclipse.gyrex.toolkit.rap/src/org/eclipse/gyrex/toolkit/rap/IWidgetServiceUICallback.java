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
package org.eclipse.cloudfree.toolkit.rap;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * A callback for the widget service to provide details when creating controls.
 */
public interface IWidgetServiceUICallback {

	/**
	 * Returns the parent control to embed widgets.
	 * 
	 * @return the parent control
	 */
	Control getParentControl();

	/**
	 * Returns the parent shell for opening dialogs.
	 * 
	 * @return the parent shell
	 */
	Shell getParentShell();
}
