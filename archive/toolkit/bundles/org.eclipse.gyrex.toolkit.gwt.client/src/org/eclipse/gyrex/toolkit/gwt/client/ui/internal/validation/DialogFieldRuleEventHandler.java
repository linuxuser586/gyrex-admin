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
package org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTContainer;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTToolkitListener;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogFieldRule;


/**
 * A generic {@link CWTToolkitListener toolkit listener} to evaluate dialog
 * field rules on toolkit events.
 */
public abstract class DialogFieldRuleEventHandler implements CWTToolkitListener {

	/** the rule */
	protected final SDialogFieldRule rule;

	/** the evaluation context */
	protected final CWTContainer evaluationContext;

	/**
	 * indicates if there is an evaluation currently scheduled to prevent
	 * unnecessary parallel evaluations
	 */
	private boolean evaluationScheduled = false;

	/**
	 * Creates a new instance for the specified rule and owner.
	 * 
	 * @param rule
	 *            the rule to evaluate on events
	 * @param evaluationContext
	 *            the evaluation context
	 */
	public DialogFieldRuleEventHandler(final SDialogFieldRule rule, final CWTContainer evaluationContext) {
		this.rule = rule;
		this.evaluationContext = evaluationContext;
	}

	/**
	 * Triggers an evaluation of the event handler's rule.
	 * <p>
	 * This method is typically called if a relevant event was detected.
	 * </p>
	 * <p>
	 * Note, the evaluation is performed as a deferred command.
	 * </p>
	 */
	public void evaluate() {
		// we don't evaluate directly but schedule a deferred command
		evaluationScheduled = true;
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				evaluationScheduled = false;
				handleRuleEvaluationResult(DialogFieldRuleHelper.evaluateRule(rule, evaluationContext));
			}
		});
	}

	/**
	 * Called whenever the rule has been evaluated as a result of an event
	 * triggered by the toolkit.
	 * <p>
	 * Subclasses must implement to perform actions depending on the evaluation
	 * result (for example, enable or disable a button).
	 * </p>
	 * 
	 * @param evaluationResult
	 *            the evaluation result
	 */
	protected abstract void handleRuleEvaluationResult(boolean evaluationResult);

	private boolean isWidgetAnchorOf(CWTWidget widget, final String containerId) {
		if (null == containerId)
			return false;

		while (null != widget) {
			// the widget id must match
			if (containerId.equals(widget.getWidgetId()))
				return true;

			// walk up the hierarchy
			widget = widget.getParentContainer();
		}
		return false;
	}

	private boolean isWidgetInList(final CWTWidget widget, final String[] widgetIds) {
		if (null == widget)
			return false;

		if ((null != widgetIds) && (widgetIds.length > 0)) {
			for (int i = 0; i < widgetIds.length; i++) {
				// the widget id must match
				final String widgetId = widgetIds[i];
				if ((null != widgetId) && widgetId.equals(widget.getWidgetId()))
					return true;
			}
		}
		return false;
	}

	/**
	 * Handles a widget change event by triggering a rule evaluation if the
	 * source widget is relevant to the rule.
	 * <p>
	 * Relevancy is determined by checking the rule scope and the rule specific
	 * fields if any.
	 * </p>
	 * 
	 * @see org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTToolkitListener#widgetChanged(org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget)
	 */
	public void widgetChanged(final CWTWidget source) {
		// performance: don't evaluate twice
		if (evaluationScheduled)
			return;

		// check the scope if necessary
		if (null != rule.scopeContainerId) {
			if (!isWidgetAnchorOf(source, rule.scopeContainerId))
				return;
		}

		// check the specified fields if necessary
		if (null != rule.selectedFieldIds) {
			if (!isWidgetInList(source, rule.selectedFieldIds))
				return;
		}

		// evaluate the rule
		evaluate();
	}
}
