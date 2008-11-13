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
package org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.commands;


import com.google.gwt.user.client.rpc.IsSerializable;

import org.eclipse.cloudfree.gwt.common.status.IStatus;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.actions.SAction;

/**
 * Serializable CommandExecutionResult
 */
public class SCommandExecutionResult implements IsSerializable {
	public String id;
	public IStatus status;
	public SAction[] actions;
}
