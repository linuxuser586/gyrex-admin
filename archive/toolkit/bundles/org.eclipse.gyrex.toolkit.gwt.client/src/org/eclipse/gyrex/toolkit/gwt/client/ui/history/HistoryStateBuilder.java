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
package org.eclipse.gyrex.toolkit.gwt.client.ui.history;

import com.google.gwt.http.client.URL;

/**
 * Utility class to build a history token.
 */
public class HistoryStateBuilder {

	private String widgetId;
	private String widgetState;

	/**
	 * Builds the history token and return it as an encoded string.
	 * 
	 * @return the encoded URL string
	 */
	public String buildString() {
		final StringBuilder token = new StringBuilder();

		// widgetId
		token.append(null != widgetId ? widgetId : "");

		// state separator
		token.append(':');

		// widget state
		if (null != widgetState) {
			token.append(widgetState);
		}

		return URL.encode(token.toString());
	}

	/**
	 * Returns the widgetId.
	 * 
	 * @return the widgetId
	 */
	public String getWidgetId() {
		return widgetId;
	}

	/**
	 * Returns the widgetState.
	 * 
	 * @return the widgetState
	 */
	public String getWidgetState() {
		return widgetState;
	}

	/**
	 * Parses an encoded history token previously generated with
	 * {@link #buildString()}.
	 * 
	 * @param token
	 *            the encoded history token
	 * @return the builder instance
	 */
	public HistoryStateBuilder parseString(String token) {
		token = URL.decode(token);

		final int separatorIndex = token.indexOf(':');
		if (separatorIndex < 0) {
			// widgetId
			widgetId = token;
		} else {
			// widgetId
			widgetId = token.substring(0, separatorIndex);

			// widget state
			if (token.length() > separatorIndex) {
				widgetState = token.substring(separatorIndex + 1);
			}
		}
		return this;
	}

	/**
	 * Sets the widget id
	 * 
	 * @param widgetId
	 *            the widget id
	 * @return the builder instance
	 */
	public HistoryStateBuilder setWidgetId(final String widgetId) {
		this.widgetId = widgetId;
		return this;
	}

	/**
	 * Sets the widget specific state info.
	 * 
	 * @param widgetState
	 *            the state info
	 * @return the builder instance
	 */
	public HistoryStateBuilder setWidgetState(final String widgetState) {
		this.widgetState = widgetState;
		return this;
	}

}