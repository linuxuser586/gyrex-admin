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
package org.eclipse.gyrex.toolkit.gwt.server.internal.serialization;

import org.eclipse.gyrex.toolkit.actions.Action;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedAction;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.actions.SAction;

/**
 * Abstract base class for action serializers.
 */
public abstract class ActionSerializer<T extends Action> {

	/**
	 * Fills the serialized action base attributes.
	 * 
	 * @param action
	 *            the action to read the attributes from
	 * @param serializedAction
	 *            the serialized action to write the attributes to
	 * @return the passed in serialized action for convenience
	 */
	protected ISerializedAction populateAttributes(final T action, final ISerializedAction serializedAction) {
		final SAction sAction = (SAction) serializedAction;
		sAction.hints = action.getHints();
		return sAction;
	}

	/**
	 * Serializes the specified action.
	 * 
	 * @param action
	 *            the action to serialize
	 * @return the serialized resource
	 */
	public abstract ISerializedAction serialize(T action);
}
