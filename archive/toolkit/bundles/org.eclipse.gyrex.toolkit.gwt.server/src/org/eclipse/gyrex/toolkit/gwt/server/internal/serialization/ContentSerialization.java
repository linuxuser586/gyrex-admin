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
package org.eclipse.gyrex.toolkit.gwt.server.internal.serialization;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import org.eclipse.gyrex.toolkit.content.BooleanContent;
import org.eclipse.gyrex.toolkit.content.ContentObject;
import org.eclipse.gyrex.toolkit.content.ContentSet;
import org.eclipse.gyrex.toolkit.content.NumberContent;
import org.eclipse.gyrex.toolkit.content.TextContent;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SContentEntry;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SContentSet;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SNumberEntry;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SSelectionFlagEntry;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.STextEntry;

/**
 * This is a util for translating CWT content elements to the serializable GWT
 * equivalent that gets sent by GWT's RPC implementation across to wire.
 * <p>
 * Right now the translation is static because it's simple and doesn't introduce
 * any dependency to yet another library. This could change in the future if
 * there is a more performant way available or the model becomes too complex.
 * </p>
 */
public class ContentSerialization {

	private static ContentObject deserializeContentObject(final SContentEntry value) {
		if (value instanceof STextEntry) {
			return new TextContent(((STextEntry) value).text);
		} else if (value instanceof SNumberEntry) {
			return new NumberContent(((SNumberEntry) value).number);
		} else if (value instanceof SSelectionFlagEntry) {
			return new BooleanContent(((SSelectionFlagEntry) value).selected);
		}
		return null;
	}

	public static ContentSet deserializeContentSet(final SContentSet sContentSet) {
		if ((null == sContentSet) || (null == sContentSet.entries)) {
			return null;
		}

		final Map<String, ContentObject> entries = new HashMap<String, ContentObject>(sContentSet.entries.size());
		for (final Iterator stream = sContentSet.entries.entrySet().iterator(); stream.hasNext();) {
			final Map.Entry entry = (Map.Entry) stream.next();
			entries.put((String) entry.getKey(), deserializeContentObject((SContentEntry) entry.getValue()));
		}
		return new ContentSet(entries);
	}

}
