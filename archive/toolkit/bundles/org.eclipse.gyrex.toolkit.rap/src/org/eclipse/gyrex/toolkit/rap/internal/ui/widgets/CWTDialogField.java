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
package org.eclipse.gyrex.toolkit.rap.internal.ui.widgets;

import org.eclipse.gyrex.toolkit.Toolkit;
import org.eclipse.gyrex.toolkit.rap.internal.ui.content.IContentAdapter;
import org.eclipse.gyrex.toolkit.rap.internal.ui.validation.DialogFieldRuleEventHandler;
import org.eclipse.gyrex.toolkit.rap.internal.ui.validation.DialogFieldValidator;
import org.eclipse.gyrex.toolkit.widgets.DialogField;
import org.eclipse.gyrex.toolkit.widgets.DialogFieldRule;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Renders a <code>org.eclipse.gyrex.toolkit.widgets.DialogField</code> into
 * SWT/JFace/Forms UI.
 * <p>
 * A dialog field may consist of multiple controls which are arranged in a
 * common way typically found in forms. Note, it doesn't make sense to have a
 * parent other than a {@link CWTDialogFieldGroup}.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class CWTDialogField<T extends DialogField> extends CWTWidget<T> {

	private static final class EnablementHandler extends DialogFieldRuleEventHandler implements CWTToolkitListener {
		/** dialogField */
		private final CWTDialogField dialogField;

		/**
		 * Creates a new instance.
		 * 
		 * @param enablementRule
		 * @param dialogField
		 */
		private EnablementHandler(final DialogFieldRule enablementRule, final CWTDialogField dialogField) {
			super(enablementRule, dialogField.getParentContainer());
			this.dialogField = dialogField;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.DialogFieldRuleHelper.RuleEventHandler#handleRuleEvaluationResult(boolean)
		 */
		@Override
		protected void handleRuleEvaluationResult(final boolean evaluationResult) {
			dialogField.setEnabled(evaluationResult);
		}
	}

	static final String EMTPY_STRING = "";

	private EnablementHandler enablementHandler;

	private Label labelControl;
	private Text descriptionControl;

	private IObservable observable;
	private IContentAdapter contentAdapter;
	private DialogFieldValidator dialogFieldValidator;

	/**
	 * Creates the dialog field's description control if necessary. No control
	 * will be created if the {@link #getDescriptionText() description} is
	 * empty.
	 * <p>
	 * The created control can be accessed via {@link #getDescriptionControl()}.
	 * </p>
	 * 
	 * @param parent
	 *            the parent composite
	 */
	protected void createDescriptionControl(final Composite parent) {
		final String descriptionText = getDescriptionText();
		if (descriptionText.length() > 0) {
			descriptionControl = new Text(parent, SWT.READ_ONLY);
			descriptionControl.setText(descriptionText);
			getToolkit().getFormToolkit().adapt(descriptionControl, false, false);
		}
	}

	/**
	 * Creates the dialog field's label control.
	 * <p>
	 * The created control can be accessed via {@link #getLabelControl()}.
	 * </p>
	 * 
	 * @param parent
	 *            the parent control
	 */
	protected void createLabelControl(final Composite parent) {
		labelControl = getToolkit().getFormToolkit().createLabel(parent, getLabelText());
	}

	/**
	 * Creates and returns the top level control for this dialog field under the
	 * given parent composite.
	 * <p>
	 * This implementation creates a label and optionally a description control.
	 * Subclasses must re-implement to create additional controls. They should
	 * call {@link #createLabelControl(Composite)} and
	 * {@link #createDescriptionControl(Composite)} at appropriate times to
	 * create the default controls.
	 * </p>
	 * 
	 * @param parent
	 *            the parent composite
	 * @return the created control (may not be <code>null</code>)
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTWidget#createWidgetControl(org.eclipse.swt.widgets.Composite)
	 * @see #getLabelControl()
	 * @see #getDescriptionControl()
	 */
	@Override
	protected Control createWidgetControl(final Composite parent) {
		// create label
		createLabelControl(parent);

		// create optional description if provided
		createDescriptionControl(parent);

		return labelControl;
	}

	/**
	 * Fills the dialog field controls into a grid with the given number of
	 * columns.
	 * <p>
	 * The parent is assumed to have a SWT {@link GridLayout} as layout. The
	 * dialog field will adjust its controls' spans to the number of columns
	 * given. This method is called after the dialog field controls have been
	 * created.
	 * </p>
	 * <p>
	 * Subclasses will need to reimplemented this method in order to layout the
	 * dialog field correctly.
	 * </p>
	 * 
	 * @param columns
	 *            the number of columns to span in the grid
	 * @see #getNumberOfColumns()
	 */
	protected void fillIntoGrid(final int columns) {
		// apply a default layout
		if (null != getDescriptionControl()) {
			if (columns < 2) {
				Toolkit.error(Toolkit.ERROR_INVALID_ARGUMENT, "at least 2 columns needed");
			}
			GridDataFactory.generate(getLabelControl(), 1, 1);
			GridDataFactory.generate(getDescriptionControl(), columns - 1, 1);
		} else {
			if (columns < 1) {
				Toolkit.error(Toolkit.ERROR_INVALID_ARGUMENT, "at least 1 column needed");
			}
			GridDataFactory.generate(getLabelControl(), columns, 1);
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTWidget#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(final Class adapter) {
		if (DialogFieldValidator.class.equals(adapter) && (null != getDialogFieldValidator())) {
			return getDialogFieldValidator();
		}
		if (IContentAdapter.class.equals(adapter) && (null != getContentAdapter())) {
			return getContentAdapter();
		}
		if (IObservable.class.equals(adapter) && (null != getObservable())) {
			return getObservable();
		}

		return super.getAdapter(adapter);
	}

	/**
	 * Returns the content adapter.
	 * 
	 * @return the content adapter
	 */
	protected IContentAdapter getContentAdapter() {
		return contentAdapter;
	}

	/**
	 * Returns the dialog field's description control.
	 * <p>
	 * The description control is optional.
	 * </p>
	 * 
	 * @return the description control (maybe <code>null</code>)
	 */
	protected Control getDescriptionControl() {
		return descriptionControl;
	}

	/**
	 * Returns the dialog field description text.
	 * 
	 * @return the description text
	 */
	protected String getDescriptionText() {
		final String desc = getWidget().getDescription();
		return null != desc ? desc : EMTPY_STRING;
	}

	/**
	 * Returns the dialog field validator.
	 * 
	 * @return the dialog field validator
	 */
	protected DialogFieldValidator getDialogFieldValidator() {
		return dialogFieldValidator;
	}

	/**
	 * Returns the dialog field's label control.
	 * 
	 * @return the label control (maybe <code>null</code> if not created yet)
	 */
	protected Control getLabelControl() {
		return labelControl;
	}

	/**
	 * Returns the dialog field label text.
	 * 
	 * @return the label text
	 */
	protected String getLabelText() {
		final String label = getWidget().getLabel();
		return null != label ? label : EMTPY_STRING;
	}

	/**
	 * Returns the number of columns in a grid required by this dialog field.
	 * <p>
	 * The default implementation returns <code>1</code> if the dialog field's
	 * description is empty, otherwise <code>2</code>. Subclasses will need to
	 * reimplemented this method in order to provide the number of columns
	 * required for proper layout in an SWT {@link GridLayout}.
	 * </p>
	 * 
	 * @return the number of columns
	 * @see #fillIntoGrid(int)
	 */
	protected int getNumberOfColumns() {
		return null == getDescriptionControl() ? 1 : 2;
	}

	/**
	 * Returns the observable for listening to changes of the dialog field.
	 * 
	 * @return the observable
	 */
	protected IObservable getObservable() {
		return observable;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTWidget#onWidgetControlCreated()
	 */
	@Override
	protected void onWidgetControlCreated() {
		final DialogFieldRule enablementRule = getWidget().getEnablementRule();
		if (null != enablementRule) {
			// add change listener
			enablementHandler = new EnablementHandler(enablementRule, this);
			getToolkit().addChangeListener(enablementHandler);

			// set initial enablement state
			enablementHandler.evaluate();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTWidget#onWidgetControlDisposed()
	 */
	@Override
	protected void onWidgetControlDisposed() {
		if (null != observable) {
			observable.dispose();
			observable = null;
		}
		if (null != enablementHandler) {
			getToolkit().removeChangeListener(enablementHandler);
			enablementHandler = null;
		}
	}

	/**
	 * Sets the content adapter.
	 * 
	 * @param contentAdapter
	 *            the content adapter to set
	 */
	protected void setContentAdapter(final IContentAdapter contentAdapter) {
		this.contentAdapter = contentAdapter;
	}

	/**
	 * Sets the dialog field validator.
	 * 
	 * @param dialogFieldValidator
	 *            the validator to set
	 */
	protected void setDialogFieldValidator(final DialogFieldValidator dialogFieldValidator) {
		this.dialogFieldValidator = dialogFieldValidator;
	}

	/**
	 * Sets the dialog field enablement state.
	 * <p>
	 * Subclasses must re-implement this method to set the enablement state of
	 * the dialog field controls.
	 * </p>
	 * 
	 * @param enabled
	 *            the enablement state to set
	 */
	public void setEnabled(final boolean enabled) {
		if ((null != getLabelControl()) && !getLabelControl().isDisposed()) {
			getLabelControl().setEnabled(enabled);
		}
		if ((null != getDescriptionControl()) && !getDescriptionControl().isDisposed()) {
			getDescriptionControl().setEnabled(enabled);
		}
	}

	/**
	 * Sets the observable for monitoring changes to the dialog field.
	 * 
	 * @param observable
	 *            the observable to set
	 */
	protected void setObservable(final IObservable observable) {
		if (null != this.observable) {
			this.observable.dispose();
		}
		this.observable = observable;
	}
}
