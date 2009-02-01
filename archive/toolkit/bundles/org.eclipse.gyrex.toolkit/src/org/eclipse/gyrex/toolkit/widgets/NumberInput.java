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
package org.eclipse.cloudfree.toolkit.widgets;

import org.eclipse.cloudfree.toolkit.CWT;
import org.eclipse.cloudfree.toolkit.content.NumberContent;

/**
 * A dialog field specialized for number input.
 * <p>
 * A {@link NumberType} will be used to apply locale specific formating and
 * basic input validation on the client side. The widget adapter can provide
 * extended validation support.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 */
public class NumberInput extends DialogField<NumberContent> {

	/** serialVersionUID */
	private static final long serialVersionUID = -6214143868079496250L;

	/** DEFAULT_TYPE */
	public static final NumberType DEFAULT_TYPE = NumberType.INTEGER;

	/** type */
	private NumberType type = DEFAULT_TYPE;

	/** limit to the number range; default is none (<code>null</code>) */
	private Number upperLimit, lowerLimit;
	private boolean upperLimitInclusive, lowerLimitInclusive;

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
	public NumberInput(final String id, final Container parent, final int style) {
		super(id, parent, style, NumberContent.class);
	}

	/**
	 * Returns the lower limit.
	 * 
	 * @return the lower limit
	 * @see #isLowerLimitInclusive()
	 */
	public Number getLowerLimit() {
		return lowerLimit;
	}

	/**
	 * The number type.
	 * <p>
	 * Note: Depending on the type and the rendering technology some validation
	 * may already happen on the client side.
	 * </p>
	 * 
	 * @return the number type (may not be <code>null</code>)
	 */
	public NumberType getType() {
		return type;
	}

	/**
	 * Returns the upper limit.
	 * 
	 * @return the upper limit
	 * @see #isUpperLimitInclusive()
	 */
	public Number getUpperLimit() {
		return upperLimit;
	}

	/**
	 * Indicates if the lower limit is inclusive.
	 * 
	 * @return <code>true</code> if inclusive, <code>false</code> otherwise
	 */
	public boolean isLowerLimitInclusive() {
		return lowerLimitInclusive;
	}

	/**
	 * Indicates if the upper limit is inclusive.
	 * 
	 * @return <code>true</code> if inclusive, <code>false</code> otherwise
	 */
	public boolean isUpperLimitInclusive() {
		return upperLimitInclusive;
	}

	/**
	 * Sets the lower limit.
	 * 
	 * @param lowerLimit
	 *            the lower limit to set (or <code>null</code> to unset)
	 * @param inclusive
	 *            <code>true</code> if the limit is incluse, <code>false</code>
	 *            if exclusive
	 */
	public void setLowerLimit(final Number lowerLimit, final boolean inclusive) {
		this.lowerLimit = lowerLimit;
		lowerLimitInclusive = inclusive;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the type to set (may not be <code>null</code>)
	 */
	public void setType(final NumberType type) {
		if (null == type) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "type");
		}
		this.type = type;
	}

	/**
	 * Sets the upper limit.
	 * 
	 * @param upperLimit
	 *            the upper limit to set (or <code>null</code> to unset)
	 * @param inclusive
	 *            <code>true</code> if the limit is incluse, <code>false</code>
	 *            if exclusive
	 */
	public void setUpperLimit(final Number upperLimit, final boolean inclusive) {
		this.upperLimit = upperLimit;
		upperLimitInclusive = inclusive;
	}
}
