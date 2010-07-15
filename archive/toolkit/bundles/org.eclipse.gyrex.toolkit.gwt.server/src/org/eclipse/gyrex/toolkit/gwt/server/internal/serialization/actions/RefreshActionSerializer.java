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

import org.eclipse.gyrex.toolkit.actions.RefreshAction;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedAction;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.actions.SRefreshAction;
import org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.ActionSerializer;

/**
 * {@link RefreshAction} serializer
 */
public class RefreshActionSerializer extends ActionSerializer<RefreshAction> {

	@Override
	protected ISerializedAction populateAttributes(final RefreshAction action, final ISerializedAction serializedAction) {
		final SRefreshAction sRefreshAction = (SRefreshAction) serializedAction;
		sRefreshAction.delay = action.getDelay();
		return super.populateAttributes(action, serializedAction);
	}

	@Override
	public ISerializedAction serialize(final RefreshAction action) {
		final SRefreshAction sAction = new SRefreshAction();
		return populateAttributes(action, sAction);
	}
}
