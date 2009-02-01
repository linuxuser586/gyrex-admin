/*******************************************************************************
 * Copyright (c) 2009 Gunnar Wagenknecht and others.
 * All rights reserved.
 *  
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization.widgets;

import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogField;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SEmailInput;
import org.eclipse.cloudfree.toolkit.widgets.DialogField;
import org.eclipse.cloudfree.toolkit.widgets.EmailInput;

/**
 * {@link EmailInput} serializer.
 */
public class EmailInputSerializer extends TextInputSerializer {

	@Override
	protected SDialogField createSDialogField(final DialogField dialogField, final SContainer parent) {
		return new SEmailInput();
	}

}
