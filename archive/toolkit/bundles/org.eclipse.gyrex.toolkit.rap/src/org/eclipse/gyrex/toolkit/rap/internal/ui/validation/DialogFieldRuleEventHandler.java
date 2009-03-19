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
package org.eclipse.gyrex.toolkit.rap.internal.ui.validation;

import java.util.concurrent.atomic.AtomicBoolean;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTContainer;
import org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTToolkitListener;
import org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTWidget;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.widgets.DialogField;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldRule;

/**
 * A generic {@link CWTToolkitListener toolkit listener} to evaluate dialog
 * field rules on toolkit events.
 */
public abstract class DialogFieldRuleEventHandler implements CWTToolkitListener {

	private final class EvaluationJob extends Job {

		/**
		 * Creates a new instance.
		 */
		public EvaluationJob() {
			super("Rule Evaluation");
			setSystem(true);
			setPriority(INTERACTIVE);
		}

		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			evaluationScheduled.set(false);
			handleRuleEvaluationResult(DialogFieldRuleHelper.evaluateRule(rule, evaluationContext));
			return Status.OK_STATUS;
		}
	}

	/** the rule */
	protected final DialogFieldRule rule;

	/** the evaluation context */
	protected final CWTContainer evaluationContext;

	/**
	 * indicates if there is an evaluation currently scheduled to prevent
	 * unnecessary parallel evaluations
	 */
	private final AtomicBoolean evaluationScheduled = new AtomicBoolean();

	/**
	 * Creates a new instance for the specified rule and owner.
	 * 
	 * @param rule
	 *            the rule to evaluate on events
	 * @param evaluationContext
	 *            the evaluation context
	 */
	public DialogFieldRuleEventHandler(final DialogFieldRule rule, final CWTContainer evaluationContext) {
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
		evaluationScheduled.set(true);
		final Job evaluationJob = new EvaluationJob();
		evaluationJob.schedule();
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

	private boolean isWidgetAnchorOf(CWTWidget widget, final Container container) {
		if (null == container) {
			return false;
		}

		while (null != widget) {
			// the widget id must match
			if (container.getId().equals(widget.getWidgetId())) {
				return true;
			}

			// walk up the hierarchy
			widget = widget.getParentContainer();
		}
		return false;
	}

	private boolean isWidgetInList(final CWTWidget widget, final DialogField[] dialogFields) {
		if (null == widget) {
			return false;
		}

		if ((null != dialogFields) && (dialogFields.length > 0)) {
			for (int i = 0; i < dialogFields.length; i++) {
				// the widget id must match
				final String widgetId = dialogFields[i].getId();
				if (widgetId.equals(widget.getWidgetId())) {
					return true;
				}
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
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.gwt.client.ui.widgets.CWTToolkitListener#widgetChanged(org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget)
	 */
	public void widgetChanged(final CWTWidget source) {
		// performance: don't evaluate twice
		if (evaluationScheduled.get()) {
			return;
		}

		// check if evaluation is necessary
		switch (rule.getFieldSelectionType()) {
			case ALL_FIELDS:
			case REQUIRED_FIELDS:
				// check the scope
				if (!isWidgetAnchorOf(source, rule.getScope())) {
					return;
				}
				break;

			case SPECIFIED_FIELDS:
				// check the specified fields
				if (!isWidgetInList(source, rule.getSelectedFields())) {
					return;
				}
				break;

			default:
				// evaluate
				break;
		}

		// evaluate the rule
		evaluate();
	}
}
