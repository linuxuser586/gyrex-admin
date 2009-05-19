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

import java.io.Serializable;

/**
 * The source data of the log event.
 * <p>
 * This class implements {@link #hashCode()} and {@link #equals(Object)} so that
 * it can be used to detect duplicate locations.
 * </p>
 */
public final class LogEventSourceData implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -4725728220360034898L;

	private final String className;
	private final String methodName;
	private final String fileName;
	private final int lineNumber;

	/**
	 * Creates a new instance.
	 * 
	 * @param className
	 * @param methodName
	 * @param fileName
	 * @param lineNumber
	 */
	public LogEventSourceData(final String className, final String methodName, final String fileName, final int lineNumber) {
		this.className = className;
		this.methodName = methodName;
		this.fileName = fileName;
		this.lineNumber = lineNumber;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final LogEventSourceData other = (LogEventSourceData) obj;
		if (className == null) {
			if (other.className != null) {
				return false;
			}
		} else if (!className.equals(other.className)) {
			return false;
		}
		if (methodName == null) {
			if (other.methodName != null) {
				return false;
			}
		} else if (!methodName.equals(other.methodName)) {
			return false;
		}
		if (fileName == null) {
			if (other.fileName != null) {
				return false;
			}
		} else if (!fileName.equals(other.fileName)) {
			return false;
		}
		if (lineNumber != other.lineNumber) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the className.
	 * 
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Returns the fileName.
	 * 
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Returns the lineNumber.
	 * 
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * Returns the methodName.
	 * 
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Returns the className as simple class name. This is the name of the class
	 * without the package name.
	 * 
	 * @return the simple class name
	 */
	public String getSimpleClassName() {
		String string = getClassName();
		final int index = string.lastIndexOf('.');
		if (index != -1) {
			string = string.substring(index + 1, string.length());
		}
		return string;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + lineNumber;
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("LogEventSourceData [className=").append(className).append(", methodName=").append(methodName).append(", fileName=").append(fileName).append(", lineNumber=").append(lineNumber).append("]");
		return builder.toString();
	}

}
