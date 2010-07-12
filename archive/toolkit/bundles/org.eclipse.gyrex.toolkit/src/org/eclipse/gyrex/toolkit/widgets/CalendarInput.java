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
package org.eclipse.gyrex.toolkit.widgets;

import org.eclipse.gyrex.toolkit.Toolkit;

/**
 * A text dialog field specialized for date and time input.
 * <p>
 * A pattern described by an array of {@link CalendarFields} will be used to
 * apply locale specific formatting and basic input validation on the client
 * side.
 * </p>
 * <p>
 * The dialog field support the splitted input mode to allow seperate input
 * fields for each pattern field. The order of the fields in the pattern does
 * not determine the input order in splitted input mode. The input order depends
 * on the locale.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 * 
 * @see Toolkit#SPLITTED
 */
public class CalendarInput extends TextInput {

	/** serialVersionUID */
	private static final long serialVersionUID = 1896438317053713121L;

	/** DEFAULT_PATTERN */
	public static final CalendarFields[] DEFAULT_PATTERN = new CalendarFields[] { CalendarFields.DAY, CalendarFields.MONTH, CalendarFields.YEAR };

	/** pattern */
	private CalendarFields[] pattern = DEFAULT_PATTERN;

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 *            the widget id
	 * @param parent
	 *            the widget parent
	 * @param style
	 *            the widget style
	 */
	public CalendarInput(String id, Container parent, int style) {
		super(id, parent, style);
	}

	/**
	 * The date pattern.
	 * <p>
	 * Note: Depending on the pattern and the rendering technology some
	 * validation may already happen on the client side.
	 * </p>
	 * 
	 * @return the pattern (maynot be <code>null</code>)
	 */
	public CalendarFields[] getPattern() {
		return pattern;
	}

	/**
	 * Sets the pattern.
	 * 
	 * @param pattern
	 *            the pattern to set (maynot be <code>null</code>)
	 */
	public void setPattern(CalendarFields[] pattern) {
		if (null == pattern)
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "pattern");
		if (pattern.length == 0)
			Toolkit.error(Toolkit.ERROR_INVALID_ARGUMENT, "empty pattern");
		this.pattern = pattern;
	}
}
