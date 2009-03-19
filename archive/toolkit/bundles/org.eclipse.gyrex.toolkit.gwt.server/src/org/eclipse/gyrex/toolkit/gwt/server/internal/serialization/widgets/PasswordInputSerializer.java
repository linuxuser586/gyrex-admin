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
package org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.widgets;

import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogField;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SPasswordInput;
import org.eclipse.gyrex.toolkit.widgets.DialogField;
import org.eclipse.gyrex.toolkit.widgets.PasswordInput;

/**
 * {@link PasswordInput} serializer.
 */
public class PasswordInputSerializer extends TextInputSerializer {

	@Override
	protected SDialogField createSDialogField(final DialogField dialogField, final SContainer parent) {
		return new SPasswordInput();
	}

}
