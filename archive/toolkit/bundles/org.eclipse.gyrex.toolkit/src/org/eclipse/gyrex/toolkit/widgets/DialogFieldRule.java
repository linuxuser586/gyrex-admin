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

import java.io.Serializable;
import java.util.Arrays;

import org.eclipse.gyrex.toolkit.CWT;

/**
 * A multi purpose rule for runtime aspects.
 * <p>
 * The idea of using dialog field rules is to automated certain usability
 * aspects at runtime by the rendering technology and to avoid potential
 * expensive server roundtrips in a client-server rendering model. For example,
 * a dialog field rule could be defined for some button that should only be
 * enabled if all required fields have their content and the content is valid.
 * The rule would be evaluated at runtime in the UI layer without going back to
 * the application back-end.
 * </p>
 * <p>
 * Dialog field rules can be also used to select fields for arbitrary
 * operations. For example, a dialog field rule can be set to select a set of
 * fields whose content should be submitted to the server when a command is
 * executed.
 * </p>
 * <p>
 * It's also possible to combine multiple rules into a single rule. Support for
 * this is provided via {@link MultiDialogFieldRule}.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation. Instances can only be created by using the factory
 * methods provided in {@link DialogFieldRules}.
 * </p>
 * 
 * @see #isMulti()
 * @see MultiDialogFieldRule
 * @see DialogFieldRules
 */
public class DialogFieldRule implements Serializable {

	/**
	 * The possible field conditions for dialog field rules.
	 */
	public static enum FieldCondition {
		/** a field must be valid according to its validation policies */
		IS_VALID,
		/** a field must be set */
		IS_SET,
		/** a field's content will be submitted */
		SUBMIT,
		/**
		 * A field condition which will never validate making the rule
		 * effectively static (always <code>false</code>).
		 */
		NEVER
	}

	/**
	 * The possible field selection modes for dialog field rules.
	 */
	public static enum FieldSelectionType {
		/**
		 * Instructs the runtime to automatically select all possible fields in
		 * a given scope.
		 */
		ALL_FIELDS,

		/**
		 * Instructs the runtime to use the manually specified fields when
		 * evaluating the rule.
		 */
		SPECIFIED_FIELDS,

		/**
		 * Instructs the runtime to automatically select all fields marked
		 * {@link CWT#REQUIRED required} in a given scope.
		 */
		REQUIRED_FIELDS,

		/**
		 * Indicates that no fields are necessary to evaluate the rule.
		 */
		NO_FIELDS
	};

	/** serialVersionUID */
	private static final long serialVersionUID = 1971591103070922803L;;

	private final FieldSelectionType fieldSelectionType;
	private final DialogField[] selectedFields;
	private final FieldCondition fieldCondition;
	private final Container scope;

	/**
	 * Constructor for subclasses. Does not validate, sets all fields to
	 * <code>null</code>.
	 */
	DialogFieldRule() {
		fieldSelectionType = null;
		selectedFields = null;
		fieldCondition = null;
		scope = null;
	}

	/**
	 * Creates a new rule using the specified parameters.
	 * 
	 * @param fieldSelectionType
	 * @param selectedFields
	 */
	DialogFieldRule(final FieldSelectionType fieldSelectionType, final DialogField[] selectedFields, final FieldCondition fieldCondition, final Container scope) {
		this.fieldSelectionType = fieldSelectionType;
		this.selectedFields = null != selectedFields ? selectedFields : new DialogField[0];
		this.fieldCondition = fieldCondition;
		this.scope = scope;

		// validate the rule
		validateRule();
	}

	/**
	 * Returns the fieldCondition.
	 * 
	 * @return the fieldCondition
	 * @see FieldCondition
	 */
	public FieldCondition getFieldCondition() {
		return fieldCondition;
	}

	/**
	 * Returns the fieldSelectionType.
	 * 
	 * @return the fieldSelectionType
	 * @see FieldSelectionType
	 */
	public FieldSelectionType getFieldSelectionType() {
		return fieldSelectionType;
	}

	/**
	 * Returns the scope of this rule.
	 * <p>
	 * The sope will only be evaluated if the field selection mode is an
	 * automated selection mode. Automated selection modes are
	 * <code>{@link FieldSelectionType#ALL_FIELDS}</code></li> and
	 * <code>{@link FieldSelectionType#REQUIRED_FIELDS}</code>.
	 * </p>
	 * <p>
	 * If no scope is set a default scope is determined from the context where
	 * the rule is applied. Usually, this will be the parent of the dialog field
	 * widget where this rule is attached to.
	 * </p>
	 * 
	 * @return the scope
	 * @see FieldSelectionType#ALL_FIELDS
	 * @see FieldSelectionType#REQUIRED_FIELDS
	 */
	public Container getScope() {
		return scope;
	}

	/**
	 * Returns the selectedFields.
	 * 
	 * @return the selectedFields
	 */
	public DialogField[] getSelectedFields() {
		return selectedFields;
	}

	/**
	 * Indicates if this {@link DialogFieldRule} is a
	 * {@link MultiDialogFieldRule}.
	 * 
	 * @return <code>true</code> if this is a {@link MultiDialogFieldRule},
	 *         <code>false</code> otherwise
	 * @see #isMulti()
	 */
	public boolean isMulti() {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		switch (fieldSelectionType) {
			case NO_FIELDS:
				return fieldCondition.toString();

			case SPECIFIED_FIELDS:
				return fieldCondition + " {" + Arrays.asList(selectedFields) + "}";

			default:
				return fieldCondition + " {" + fieldSelectionType + " in " + scope + "}";
		}
	}

	/**
	 * Validates the rule.
	 */
	void validateRule() {
		switch (fieldSelectionType) {
			case ALL_FIELDS:
			case REQUIRED_FIELDS:
				break;

			case SPECIFIED_FIELDS:
				if (selectedFields.length == 0) {
					CWT.error(CWT.ERROR_INVALID_ARGUMENT, "selectedFields must not be empty (fieldSelectionType " + fieldSelectionType + ")");
				}
				break;
			case NO_FIELDS:
				if (fieldCondition != FieldCondition.NEVER) {
					CWT.error(CWT.ERROR_INVALID_ARGUMENT, "fieldCondition " + fieldCondition + " not supported for fieldSelectionType " + fieldSelectionType);
				}
				break;
			default:
				CWT.error(CWT.ERROR_INVALID_ARGUMENT, "fieldSelectionType " + fieldSelectionType + " not supported");
				break;
		}

	}
}
