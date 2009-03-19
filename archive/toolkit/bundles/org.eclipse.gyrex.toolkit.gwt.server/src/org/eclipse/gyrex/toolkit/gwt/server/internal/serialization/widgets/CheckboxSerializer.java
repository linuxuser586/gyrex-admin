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
package org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.widgets;


import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SCheckbox;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogField;
import org.eclipse.gyrex.toolkit.widgets.Checkbox;
import org.eclipse.gyrex.toolkit.widgets.DialogField;

/**
 * {@link Checkbox} serializer.
 */
public class CheckboxSerializer extends DialogFieldSerializer {

	@Override
	protected SDialogField createSDialogField(DialogField dialogField, SContainer parent) {
		return new SCheckbox();
	}

}
