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
package org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import org.eclipse.cloudfree.toolkit.gwt.client.ui.internal.validation.DialogFieldRuleEventHandler;
import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogField;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogFieldRule;

/**
 * Composite for <code>org.eclipse.cloudfree.toolkit.widgets.DialogField</code>.
 */
public abstract class CWTDialogField extends CWTWidget {

	static class DialogFieldPanel extends SimplePanel {

		private final Element descriptionDiv;
		private final Element fieldDiv;

		/**
		 * Creates a new instance.
		 */
		public DialogFieldPanel() {
			super(DOM.createDiv());

			fieldDiv = DOM.createDiv();
			descriptionDiv = DOM.createDiv();

			DOM.appendChild(getElement(), fieldDiv);
			DOM.appendChild(getElement(), descriptionDiv);

			setStyleName("cwt-DialogField");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.gwt.user.client.ui.SimplePanel#getContainerElement()
		 */
		@Override
		protected Element getContainerElement() {
			return fieldDiv;
		}

		/**
		 * Sets the description text.
		 * 
		 * @param description
		 */
		public void setDescription(final String description) {
			DOM.setInnerText(descriptionDiv, description);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.gwt.user.client.ui.UIObject#setStyleName(java.lang.String)
		 */
		@Override
		public void setStyleName(final String style) {
			// set main style name
			super.setStyleName(style);
			setStyleName(fieldDiv, style + "-field");
			setStyleName(descriptionDiv, style + "-description");
		}

	}

	private static final class EnablementHandler extends DialogFieldRuleEventHandler implements CWTToolkitListener {

		/** dialogField */
		private final CWTDialogField dialogField;

		/**
		 * Creates a new instance.
		 * 
		 * @param enablementRule
		 * @param dialogField
		 */
		private EnablementHandler(final SDialogFieldRule enablementRule, final CWTDialogField dialogField) {
			super(enablementRule, dialogField.getParentContainer()); // enablement is evaluated at the container root (for convenience reasons)
			this.dialogField = dialogField;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets.DialogFieldRuleHelper.RuleEventHandler#handleRuleEvaluationResult(boolean)
		 */
		@Override
		protected void handleRuleEvaluationResult(final boolean evaluationResult) {
			dialogField.setEnabled(evaluationResult);
		}
	}

	private DialogFieldPanel dialogFieldPanel;
	private EnablementHandler enablementHandler;

	private SDialogField getSDialogField() {
		return (SDialogField) getSerializedWidget();
	}

	/**
	 * Indicates if the dialog field is read-only, i.e. has the read-only flag
	 * set.
	 * 
	 * @return <code>true</code> if the read-only flag is set,
	 *         <code>false</code> otherwise
	 */
	public boolean isReadOnly() {
		return getSDialogField().readOnly;
	}

	/**
	 * Indicates if the dialog field is required, i.e. has the required flag
	 * set.
	 * 
	 * @return <code>true</code> if the required flag is set, <code>false</code>
	 *         otherwise
	 */
	public boolean isRequired() {
		return getSDialogField().required;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets.CWTWidget#onLoad()
	 */
	@Override
	protected void onLoad() {
		// call super
		super.onLoad();

		final SDialogFieldRule enablementRule = getSDialogField().enablementRule;
		if (null != enablementRule) {
			// add change listener
			enablementHandler = new EnablementHandler(enablementRule, this);
			getToolkit().addChangeListener(enablementHandler);

			// set initial enablement state
			enablementHandler.evaluate();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets.CWTWidget#onUnload()
	 */
	@Override
	protected void onUnload() {
		try {
			if (null != enablementHandler) {
				getToolkit().removeChangeListener(enablementHandler);
				enablementHandler = null;
			}
		} finally {
			// call super
			super.onUnload();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rep.web.gwt.client.ui.RenderedWidget#render(org.eclipse.rep.web.gwt.client.rwt.ISerializedWidget,
	 *      org.eclipse.rep.web.gwt.client.ui.RenderingToolkit)
	 */
	@Override
	protected final Widget render(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SDialogField dialogField = (SDialogField) serializedWidget;
		dialogFieldPanel = new DialogFieldPanel();
		if (null != dialogField.description) {
			dialogFieldPanel.setDescription(dialogField.description);
		}

		final Widget fieldWidget = renderFieldWidget(serializedWidget, toolkit);
		fieldWidget.addStyleName(serializedWidget.getId());
		dialogFieldPanel.setWidget(fieldWidget);
		return dialogFieldPanel;
	}

	/**
	 * Renders the field widget.
	 * 
	 * @param toolkit
	 * @param serializedWidget
	 * @return the rendered field widget
	 */
	protected abstract Widget renderFieldWidget(ISerializedWidget serializedWidget, CWTToolkit toolkit);

	/**
	 * Sets the dialog field enablement state.
	 * 
	 * @param enabled
	 *            the enablement state to set
	 */
	public abstract void setEnabled(boolean enabled);
}
