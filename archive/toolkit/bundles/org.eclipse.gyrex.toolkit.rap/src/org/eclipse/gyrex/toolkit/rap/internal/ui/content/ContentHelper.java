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
package org.eclipse.gyrex.toolkit.rap.internal.ui.content;

import java.util.HashMap;
import java.util.Map;


import org.eclipse.gyrex.toolkit.content.ContentObject;
import org.eclipse.gyrex.toolkit.content.ContentSet;
import org.eclipse.gyrex.toolkit.rap.internal.ui.validation.DialogFieldRuleHelper;
import org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTContainer;
import org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTDialogField;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldRule;

/**
 * Utility class.
 */
public class ContentHelper {

	/**
	 * Builds a content set using the specified content selection rule and the
	 * given context.
	 * 
	 * @param contentSelectionRule
	 * @param context
	 *            the context
	 * @return the content set (maybe <code>null</code> if the rule results into
	 *         no content selection)
	 */
	public static ContentSet buildContentSet(final DialogFieldRule contentSelectionRule, final CWTContainer context) {
		if ((null == contentSelectionRule) || (null == context)) {
			return null;
		}

		final CWTDialogField<?>[] dialogFieldsToSubmit = DialogFieldRuleHelper.findAffectedDialogFields(contentSelectionRule, context);
		ContentSet contentSet = null;
		if (dialogFieldsToSubmit.length > 0) {
			final Map<String, ContentObject> content = new HashMap<String, ContentObject>(dialogFieldsToSubmit.length);
			for (int i = 0; i < dialogFieldsToSubmit.length; i++) {
				final CWTDialogField dialogField = dialogFieldsToSubmit[i];
				final IContentAdapter adapter = (IContentAdapter) dialogField.getAdapter(IContentAdapter.class);
				if (null != adapter) {
					content.put(dialogField.getWidgetId(), adapter.getContent(dialogField));
				}
			}
			if (!content.isEmpty()) {
				contentSet = new ContentSet(content);
			}
		}
		return contentSet;
	}

	private ContentHelper() {
		// empty
	}
}
