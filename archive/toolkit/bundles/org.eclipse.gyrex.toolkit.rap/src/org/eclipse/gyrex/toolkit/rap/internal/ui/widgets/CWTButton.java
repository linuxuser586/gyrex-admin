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
package org.eclipse.cloudfree.toolkit.rap.internal.ui.widgets;


import org.eclipse.cloudfree.toolkit.CWT;
import org.eclipse.cloudfree.toolkit.commands.Command;
import org.eclipse.cloudfree.toolkit.content.ContentSet;
import org.eclipse.cloudfree.toolkit.rap.internal.ui.commands.ExecuteCommandCallback;
import org.eclipse.cloudfree.toolkit.rap.internal.ui.content.ContentHelper;
import org.eclipse.cloudfree.toolkit.runtime.commands.CommandExecutionResult;
import org.eclipse.cloudfree.toolkit.widgets.Button;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Composite for <code>org.eclipse.cloudfree.toolkit.widgets.Button</code>.
 */
public class CWTButton extends CWTDialogField<Button> {

	private org.eclipse.swt.widgets.Button buttonControl;

	private final SelectionListener selectionListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(final SelectionEvent e) {
			buttonClicked();
		}
	};

	private Label emptyLabelControl;

	void buttonClicked() {
		final Command command = getWidget().getCommand();
		if (null != command) {
			// build the content set
			final ContentSet contentSet = ContentHelper.buildContentSet(command.getContentSubmitRule(), getParentContainer());

			setEnabled(false);
			buttonControl.setText("Please wait...");
			getToolkit().executeCommand(command, getWidget(), contentSet, new ExecuteCommandCallback() {

				@Override
				public void onFailure(final Throwable caught) {
					resetButtonText();
					setEnabled(true);
				}

				@Override
				public void onSuccess(final CommandExecutionResult result) {
					resetButtonText();
					setEnabled(true);
				}

			});
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.rap.internal.ui.widgets.CWTDialogField#createDescriptionControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createDescriptionControl(final Composite parent) {
		// no description
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.rap.internal.ui.widgets.CWTDialogField#createLabelControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createLabelControl(final Composite parent) {
		// no label
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.rap.internal.ui.widgets.CWTDialogField#createWidgetControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createWidgetControl(final Composite parent) {
		// empty label
		emptyLabelControl = getToolkit().getFormToolkit().createLabel(parent, EMTPY_STRING);

		// no label , no description, just button
		buttonControl = getToolkit().getFormToolkit().createButton(parent, getLabelText(), SWT.PUSH);
		buttonControl.addSelectionListener(selectionListener);
		setObservable(SWTObservables.observeSelection(buttonControl));

		return buttonControl;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.rap.internal.ui.widgets.CWTDialogField#fillIntoGrid(int)
	 */
	@Override
	protected void fillIntoGrid(final int columns) {
		if (columns < 2) {
			CWT.error(CWT.ERROR_INVALID_ARGUMENT, "at least 2 columns needed");
		}

		GridDataFactory.generate(emptyLabelControl, 1, 1);
		GridDataFactory.generate(getButtonControl(), columns - 1, 1);
	}

	/**
	 * Returns the button control.
	 * 
	 * @return the button control (maybe <code>null</code> if not created yet)
	 */
	protected org.eclipse.swt.widgets.Button getButtonControl() {
		return buttonControl;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.rap.internal.ui.widgets.CWTDialogField#getNumberOfColumns()
	 */
	@Override
	protected int getNumberOfColumns() {
		return 2;
	}

	void resetButtonText() {
		final org.eclipse.swt.widgets.Button button = getButtonControl();
		if ((null != button) && !button.isDisposed()) {
			button.setText(getLabelText());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cloudfree.toolkit.gwt.client.internal.ui.widgets.DialogFieldComposite#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		final org.eclipse.swt.widgets.Button button = getButtonControl();
		if ((null != button) && !button.isDisposed()) {
			button.setEnabled(enabled);
		}
	}

}
