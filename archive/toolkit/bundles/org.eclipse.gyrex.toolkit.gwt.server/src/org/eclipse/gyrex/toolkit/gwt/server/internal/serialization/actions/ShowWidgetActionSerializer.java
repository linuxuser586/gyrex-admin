/*******************************************************************************
 * Copyright (c) 2010 AGETO and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.actions;

import org.eclipse.gyrex.toolkit.actions.ShowWidgetAction;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedAction;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.actions.SShowWidgetAction;

/**
 * {@link ShowWidgetAction} serializer
 */
public class ShowWidgetActionSerializer extends WidgetActionSerializer<ShowWidgetAction> {

	@Override
	public ISerializedAction serialize(final ShowWidgetAction action) {
		final SShowWidgetAction sShowWidgetAction = new SShowWidgetAction();
		return populateAttributes(action, sShowWidgetAction);
	}
}
