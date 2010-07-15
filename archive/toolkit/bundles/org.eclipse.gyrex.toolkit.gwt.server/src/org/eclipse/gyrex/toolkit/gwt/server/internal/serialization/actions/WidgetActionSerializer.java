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

import org.eclipse.gyrex.toolkit.actions.WidgetAction;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedAction;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.actions.SWidgetAction;
import org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.ActionSerializer;

/**
 * {@link WidgetAction} serializer
 */
public abstract class WidgetActionSerializer<T extends WidgetAction> extends ActionSerializer<T> {

	@Override
	protected ISerializedAction populateAttributes(final T action, final ISerializedAction serializedAction) {
		final SWidgetAction sWidgetAction = (SWidgetAction) serializedAction;
		sWidgetAction.widgetId = action.getWidgetId();
		return super.populateAttributes(action, sWidgetAction);
	}

}
