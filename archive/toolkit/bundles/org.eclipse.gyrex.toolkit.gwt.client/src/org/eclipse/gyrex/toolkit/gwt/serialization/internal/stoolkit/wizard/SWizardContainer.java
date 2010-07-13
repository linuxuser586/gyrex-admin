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
package org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.wizard;

import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.commands.SCommand;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Serializable WizardContainer
 */
public class SWizardContainer extends SContainer implements IsSerializable {
	/** serialVersionUID */
	private static final long serialVersionUID = -4619449141522321919L;
	public SCommand cancelCommand, finishCommand;
}
