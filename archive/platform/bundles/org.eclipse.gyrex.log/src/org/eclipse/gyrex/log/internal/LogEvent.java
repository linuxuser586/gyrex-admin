/**
 * Copyright (c) 2009 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package org.eclipse.gyrex.log.internal;

import java.util.Map;
import java.util.Set;

/**
 * A log event
 */
public class LogEvent {

	private final long timestamp;
	private final LogEventLevel level;
	private final String message;
	private final Set<String> tags;
	private final Map<String, String> attributes;
	private final LogEventSourceData sourceData;
	private final Throwable exception;

	/**
	 * Creates a new instance.
	 * 
	 * @param level
	 * @param message
	 * @param tags
	 * @param attributes
	 * @param sourceData
	 * @param exception
	 */
	public LogEvent(final LogEventLevel level, final String message, final Set<String> tags, final Map<String, String> attributes, final LogEventSourceData sourceData, final Throwable exception) {
		this.level = level;
		this.message = message;
		this.tags = tags;
		this.attributes = attributes;
		this.sourceData = sourceData;
		this.exception = exception;
		timestamp = System.currentTimeMillis();
	}

	/**
	 * Returns the attributes.
	 * 
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * Returns the exception.
	 * 
	 * @return the exception
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * Returns the level.
	 * 
	 * @return the level
	 */
	public LogEventLevel getLevel() {
		return level;
	}

	/**
	 * Returns the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the sourceData.
	 * 
	 * @return the sourceData
	 */
	public LogEventSourceData getSourceData() {
		return sourceData;
	}

	/**
	 * Returns the tags.
	 * 
	 * @return the tags
	 */
	public Set<String> getTags() {
		return tags;
	}

	/**
	 * Returns the timestamp.
	 * 
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

}
