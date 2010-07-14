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
package org.eclipse.gyrex.toolkit.gwt.client.ui.wizard;

import com.google.gwt.user.client.ui.Panel;

import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.DialogFieldRuleEventHandler;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTContainer;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTToolkit;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTToolkitListener;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogFieldRule;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.wizard.SWizardPage;

/**
 * Composite for <code>org.eclipse.gyrex.toolkit.wizard.WizardPage</code>.
 */
public class CWTWizardPage extends CWTContainer {

	private static final class ContinueHandler extends DialogFieldRuleEventHandler implements CWTToolkitListener {
		/** dialogField */
		private final CWTWizardPage wizardPage;

		/**
		 * Creates a new instance.
		 * 
		 * @param enablementRule
		 * @param wizardPage
		 */
		private ContinueHandler(final SDialogFieldRule enablementRule, final CWTWizardPage wizardPage) {
			super(enablementRule, wizardPage);
			this.wizardPage = wizardPage;
		}

		@Override
		protected void handleRuleEvaluationResult(final boolean evaluationResult) {
			wizardPage.setPageComplete(evaluationResult);
		}
	}

	private boolean pageComplete;
	private ContinueHandler continueHandler;

	@Override
	protected Panel createPanel(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final Panel panel = super.createPanel(serializedWidget, toolkit);
		panel.setStyleName("cwt-WizardPage");
		return panel;
	}

	public String getPageDescription() {
		final String description = getSWizardPage().description;
		return null != description ? description : "";
	}

	public String getPageTitle() {
		final String title = getSWizardPage().label;
		return null != title ? title : "";
	}

	SWizardPage getSWizardPage() {
		return (SWizardPage) getSerializedWidget();
	}

	/**
	 * Returns the wizard container the page is contained in.
	 * 
	 * @return the wizard container
	 */
	CWTWizardContainer getWizardContainer() {
		return (CWTWizardContainer) getParentContainer();
	}

	/**
	 * Returns whether this page is complete or not.
	 * <p>
	 * This information is typically used by the wizard to decide when it is
	 * okay to finish.
	 * </p>
	 * 
	 * @return <code>true</code> if this page is complete, and
	 *         <code>false</code> otherwise
	 */
	boolean isPageComplete() {
		// always return true if no evaluation rule is set
		if (null == getSWizardPage().continueRule) {
			return true;
		}

		return pageComplete;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget#onLoad()
	 */
	@Override
	protected void onLoad() {
		// call super
		super.onLoad();

		final SDialogFieldRule continueRule = getSWizardPage().continueRule;
		if (null != continueRule) {
			// add change listener
			continueHandler = new ContinueHandler(continueRule, this);
			getToolkit().addChangeListener(continueHandler);

			// set initial enablement state
			continueHandler.evaluate();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget#onUnload()
	 */
	@Override
	protected void onUnload() {
		try {
			if (null != continueHandler) {
				getToolkit().removeChangeListener(continueHandler);
				continueHandler = null;
			}
		} finally {
			// call super
			super.onUnload();
		}
	}

	void setPageComplete(final boolean pageComplete) {
		if (this.pageComplete == pageComplete) {
			return;
		}

		this.pageComplete = pageComplete;
		getWizardContainer().updateButtons();
	}
}
