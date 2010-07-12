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

import java.util.ArrayList;

import org.eclipse.gyrex.toolkit.widgets.DialogFieldRule.FieldCondition;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldRule.FieldSelectionType;
import org.eclipse.gyrex.toolkit.widgets.MultiDialogFieldRule.Condition;

/**
 * A factory for {@link DialogFieldRule dialog field rules}.
 * <p>
 * Please use the static methods available in this class for creating rules.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 * 
 * @see DialogFieldRule
 */
public abstract class DialogFieldRules {

	/**
	 * A factory for {@link DialogFieldRule dialog field rules} involving
	 * multiple dialog fields sharing the same field condition but where one
	 * single field is sufficient to let the rule succeed.
	 */
	public static final class AnyOfMultipleFieldsRule extends DialogFieldRules {

		private final java.util.List<SingleFieldRule> rules;

		AnyOfMultipleFieldsRule(final DialogField... fields) {
			rules = new ArrayList<SingleFieldRule>(fields.length);
			for (int i = 0; i < fields.length; i++) {
				rules.add(new SingleFieldRule(fields[i]));
			}
		}

		protected DialogFieldRule generate() {
			final java.util.List<DialogFieldRule> fieldRules = new ArrayList<DialogFieldRule>(rules.size());
			for (final SingleFieldRule rule : rules) {
				fieldRules.add(rule.isSet());
			}

			return new MultiDialogFieldRule(Condition.OR, fieldRules.toArray(new DialogFieldRule[fieldRules.size()]));
		}

		/**
		 * Creates a rule that checks if any of the selected fields is set.
		 * 
		 * @return a rule that checks if any the selected fields is set
		 */
		public DialogFieldRule isSet() {
			return generate();
		}
	}

	/**
	 * A factory for {@link DialogFieldRule dialog field rules} involving
	 * multiple dialog fields.
	 */
	public static final class MultipleFieldsRule extends DialogFieldRules {

		MultipleFieldsRule(final DialogField... selectedFields) {
			this(FieldSelectionType.SPECIFIED_FIELDS);
			this.selectedFields = selectedFields;
		}

		MultipleFieldsRule(final FieldSelectionType selectionType) {
			this.selectionType = selectionType;
		}

		/**
		 * Creates a rule that checks if the selected fields are valid based on
		 * their validation policies.
		 * 
		 * @return a rule that checks if the selected fields are valid
		 */
		public DialogFieldRule areValid() {
			return generate(FieldCondition.IS_VALID);
		}

		/**
		 * Allows to set a scope for automatically selecting fields.
		 * <p>
		 * If no scope is set a default scope is determined from the context
		 * where the rule is applied.
		 * </p>
		 * <p>
		 * Note, setting a scope automatically changes the field selection type
		 * to <code>{@link FieldSelectionType#ALL_FIELDS}</code> and also resets
		 * any specified field.
		 * </p>
		 * 
		 * @param scope
		 *            the scope
		 * @return the multiple fields rule factory for convenience
		 * @see DialogFieldRule#getScope()
		 */
		public MultipleFieldsRule inScope(final Container scope) {
			this.scope = scope;
			selectionType = FieldSelectionType.ALL_FIELDS;
			selectedFields = null;
			return this;
		}

		/**
		 * Creates a rule that submits the content of the selected fields.
		 * 
		 * @return a rule that submits the content of the selected fields
		 */
		public DialogFieldRule submit() {
			return generate(FieldCondition.SUBMIT);
		}
	}

	/**
	 * A factory for {@link DialogFieldRule dialog field rules} involving a
	 * single dialog field.
	 */
	public static final class SingleFieldRule extends DialogFieldRules {

		SingleFieldRule(final DialogField selectedField) {
			selectionType = FieldSelectionType.SPECIFIED_FIELDS;
			selectedFields = new DialogField[] { selectedField };
		}

		/**
		 * Creates a rule that checks if the selected field is set.
		 * 
		 * @return a rule that checks if the selected field is set
		 */
		public DialogFieldRule isSet() {
			return generate(FieldCondition.IS_SET);
		}

		/**
		 * Creates a rule that checks if the selected field is valid based on
		 * its validation policies.
		 * 
		 * @return a rule that checks if the selected field is valid
		 */
		public DialogFieldRule isValid() {
			return generate(FieldCondition.IS_VALID);
		}

		/**
		 * Creates a rule that submits the content of the selected field.
		 * 
		 * @return a rule that submits the content of the selected field
		 */
		public DialogFieldRule submit() {
			return generate(FieldCondition.SUBMIT);
		}
	}

	/**
	 * Creates and returns a factory for generating a rule that automatically
	 * selects all available fields in a configured scope.
	 * 
	 * @return a multiple fields rule factory
	 * @see MultipleFieldsRule
	 */
	public static MultipleFieldsRule allFields() {
		return new MultipleFieldsRule(FieldSelectionType.ALL_FIELDS);
	}

	/**
	 * Creates and returns a factory for generating a rule that involves the
	 * specified fields but where only one single field is required to meet the
	 * field condition.
	 * 
	 * @param fields
	 *            the fields to select
	 * @return an any of multiple fields rule factory
	 */
	public static AnyOfMultipleFieldsRule anyOf(final DialogField... fields) {
		return new AnyOfMultipleFieldsRule(fields);
	}

	/**
	 * Creates and returns a factory for generating a rule that involves only a
	 * single field.
	 * 
	 * @param field
	 *            the field
	 * @return a single field rule factory
	 */
	public static SingleFieldRule field(final DialogField field) {
		return new SingleFieldRule(field);
	}

	/**
	 * Creates and returns a factory for generating a rule that involves the
	 * specified fields.
	 * 
	 * @param fields
	 *            the fields to select
	 * @return a multiple fields rule factory
	 */
	public static MultipleFieldsRule fields(final DialogField... fields) {
		return new MultipleFieldsRule(fields);
	}

	/**
	 * Creates and returns a rule which will always evaluate negatively, i.e.
	 * <code>false</code>.
	 * 
	 * @return a rule which will always evaluate to <code>false</code>
	 * @see FieldCondition#NEVER
	 */
	public static DialogFieldRule never() {
		return new DialogFieldRule(FieldSelectionType.NO_FIELDS, null, FieldCondition.NEVER, null);
	}

	FieldSelectionType selectionType;
	DialogField[] selectedFields;
	Container scope;

	DialogFieldRule generate(final FieldCondition condition) {
		return new DialogFieldRule(selectionType, selectedFields, condition, scope);
	}
}
