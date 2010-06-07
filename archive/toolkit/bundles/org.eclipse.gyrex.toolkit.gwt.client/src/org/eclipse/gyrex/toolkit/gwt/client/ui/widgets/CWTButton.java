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
package org.eclipse.gyrex.toolkit.gwt.client.ui.widgets;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import org.eclipse.gyrex.gwt.common.status.IStatus;
import org.eclipse.gyrex.toolkit.gwt.client.ExecuteCommandCallback;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactoryException;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.content.ContentHelper;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.commands.SCommandExecutionResult;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SContentSet;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SButton;

/**
 * Composite for <code>org.eclipse.gyrex.toolkit.widgets.Button</code>.
 */
public class CWTButton extends CWTDialogField {

	private Button button;

	private final ClickListener clickListener = new ClickListener() {
		public void onClick(final Widget sender) {
			if (sender == button) {
				buttonClicked();
			}
		}
	};

	void buttonClicked() {
		final SButton sButton = getSButton();
		if (null != sButton.command) {
			final String commandId = sButton.command.id;

			// build the content set
			final SContentSet contentSet = ContentHelper.buildContentSet(sButton.command.contentSubmitRule, getParentContainer());

			setEnabled(false);
			setButtonText("Please wait...");
			getToolkit().getWidgetFactory().executeCommand(commandId, getWidgetId(), contentSet, new ExecuteCommandCallback() {

				public void onFailure(final WidgetFactoryException caught) {
					resetButtonText();
					setEnabled(true);
				}

				public void onSuccess(final Object result) {
					final SCommandExecutionResult cmdResult = (SCommandExecutionResult) result;
					// if the status is not ok, we don't re-enable the button
					final IStatus status = null != cmdResult ? cmdResult.status : null;
					if ((null == status) || status.isOK()) {
						resetButtonText();
						setEnabled(true);
					} else {
						setButtonText(cmdResult.status.getMessage());
					}
				}

			});
		}

	}

	private SButton getSButton() {
		return (SButton) getSerializedWidget();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rep.web.gwt.client.internal.ui.widgets.DialogFieldComposite#renderFieldWidget(org.eclipse.rep.web.gwt.client.rwt.ISerializedWidget,
	 *      org.eclipse.rep.web.gwt.client.ui.RenderingToolkit)
	 */
	@Override
	protected Widget renderFieldWidget(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SButton sButton = (SButton) serializedWidget;

		button = new Button();
		setButtonText(sButton.label);

		if (null != sButton.toolTipText) {
			button.setTitle(sButton.toolTipText);
		}

		if (null != sButton.command) {
			button.addClickListener(clickListener);
		}

		return button;
	}

	void resetButtonText() {
		setButtonText(getSButton().label);
	}

	void setButtonText(final String label) {
		button.setText(null != label ? label : "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gyrex.toolkit.gwt.client.internal.ui.widgets.DialogFieldComposite#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		if (null == button) {
			return;
		}

		button.setEnabled(enabled);
	}

}
