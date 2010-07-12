/*******************************************************************************
 * Copyright (c) 2008, 2009 Gunnar Wagenknecht and others.
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
 * A {@link DialogFieldRule} extension which allows to combine multiple rules
 * with a condition.
 * <p>
 * Note, the {@link DialogFieldRule} is special because it does not work with
 * fields. Any call to {@link #getFieldCondition()},
 * {@link #getFieldSelectionType()}, {@link #getScope()},
 * {@link #getSelectedFields()} will return <code>null</code>. Instead this rule
 * only uses sub rules.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation. Instances can only be created by using the factory
 * methods provided in {@link DialogFieldRules}.
 * </p>
 * 
 * @see MultiDialogFieldRule
 * @see DialogFieldRules
 */
public class MultiDialogFieldRule extends DialogFieldRule {

	/**
	 * The possible field conditions for dialog field rules.
	 */
	public static enum Condition {
		/** OR condition */
		OR,
		/** AND condition */
		AND
	}

	/** serialVersionUID */
	private static final long serialVersionUID = -3695663147091599728L;

	private final Condition condition;
	private final DialogFieldRule[] rules;

	/**
	 * Creates a new instance.
	 * 
	 * @param condition
	 * @param rules
	 */
	MultiDialogFieldRule(final Condition condition, final DialogFieldRule[] rules) {
		super();
		this.condition = condition;
		this.rules = rules;
		validateRule();
	}

	/**
	 * Returns the condition.
	 * 
	 * @return the condition
	 */
	public Condition getCondition() {
		return condition;
	}

	/**
	 * Returns the rules.
	 * 
	 * @return the rules
	 */
	public DialogFieldRule[] getRules() {
		return rules;
	}

	/**
	 * Returns <code>true</code> to indicated that this is a
	 * {@link MultiDialogFieldRule}.
	 * 
	 * @return <code>true</code>
	 * @see org.eclipse.gyrex.toolkit.widgets.DialogFieldRule#isMulti()
	 */
	@Override
	public boolean isMulti() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.widgets.DialogFieldRule#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder toString = new StringBuilder(64);
		final DialogFieldRule[] rules = getRules();
		for (int i = 0; i < rules.length; i++) {
			if (i > 0) {
				toString.append(getCondition());
			}
			toString.append(rules[i]);
		}
		return toString.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.widgets.DialogFieldRule#validateRule()
	 */
	@Override
	void validateRule() {
		if (null == condition) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "condition");
		}
		if (null == rules) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "rules");
		}
		if (rules.length == 0) {
			Toolkit.error(Toolkit.ERROR_INVALID_ARGUMENT, "rules is empty");
		}

	}
}
