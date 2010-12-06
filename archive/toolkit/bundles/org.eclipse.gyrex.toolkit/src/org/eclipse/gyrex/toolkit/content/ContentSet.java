/*******************************************************************************
 * Copyright (c) 2008, 2010 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.content;

import java.util.Collections;
import java.util.Map;

import org.eclipse.gyrex.toolkit.Toolkit;

/**
 * A collection of widget content.
 * <p>
 * Typically a widget content set is used to submit the actual widget content
 * between the rendered widgets and the application code defining the widget
 * (eg. your business code) back and forth.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ContentSet {

	private final Map<String, ContentObject> entries;

	/**
	 * Creates a new instance.
	 * 
	 * @param entryMap
	 *            a map of {@link ContentObject content objects} by widget id
	 */
	public ContentSet(final Map<String, ContentObject> entryMap) {
		if (null == entryMap) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "entry map must not be null");
		}
		entries = Collections.unmodifiableMap(entryMap);
	}

	/**
	 * Returns the content entries available in the set.
	 * 
	 * @return the content entries as a map of {@link ContentObject content
	 *         objects} by widget id
	 */
	public Map<String, ContentObject> getEntries() {
		return entries;
	}

	/**
	 * Returns the {@link ContentObject content object} for the specified widget
	 * id.
	 * 
	 * @param widgetId
	 *            the widget id (may not be <code>null</code>)
	 * @return the {@link ContentObject content object} or <code>null</code> if
	 *         none is available
	 */
	public ContentObject getEntry(final String widgetId) {
		return entries.get(widgetId);
	}

	@Override
	public String toString() {
		return String.format("ContentSet[%s]", null != entries ? entries.keySet().toArray() : null);
	}
}
