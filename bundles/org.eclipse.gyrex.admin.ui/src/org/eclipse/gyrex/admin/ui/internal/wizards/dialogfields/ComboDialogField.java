/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.admin.ui.internal.wizards.dialogfields;

import org.eclipse.gyrex.admin.ui.internal.helper.SwtUtil;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Dialog field containing a label and a combo control.
 */
public class ComboDialogField extends DialogField {

	protected static GridData gridDataForCombo(final int span) {
		final GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = false;
		gd.horizontalSpan = span;
		return gd;
	}

	private String fText;
	private int fSelectionIndex;
	private String[] fItems;
	private Combo fComboControl;
	private ModifyListener fModifyListener;

	private final int fFlags;

	// ------- layout helpers

	public ComboDialogField(final int flags) {
		super();
		fText = ""; //$NON-NLS-1$
		fItems = new String[0];
		fFlags = flags;
		fSelectionIndex = -1;
	}

	/*
	 * @see DialogField#doFillIntoGrid
	 */
	@Override
	public Control[] doFillIntoGrid(final Composite parent, final int nColumns) {
		assertEnoughColumns(nColumns);

		final Label label = getLabelControl(parent);
		label.setLayoutData(gridDataForLabel(1));
		final Combo combo = getComboControl(parent);
		combo.setLayoutData(gridDataForCombo(nColumns - 1));

		return new Control[] { label, combo };
	}

	private void doModifyText(final ModifyEvent e) {
		if (isOkToUse(fComboControl)) {
			fText = fComboControl.getText();
			fSelectionIndex = fComboControl.getSelectionIndex();
		}
		dialogFieldChanged();
	}

	// ------- focus methods

	private void doSelectionChanged(final SelectionEvent e) {
		if (isOkToUse(fComboControl)) {
			fItems = fComboControl.getItems();
			fText = fComboControl.getText();
			fSelectionIndex = fComboControl.getSelectionIndex();
		}
		dialogFieldChanged();
	}

	// ------- ui creation

	/**
	 * Creates or returns the created combo control.
	 * 
	 * @param parent
	 *            The parent composite or <code>null</code> when the widget has
	 *            already been created.
	 */
	public Combo getComboControl(final Composite parent) {
		if (fComboControl == null) {
			assertCompositeNotNull(parent);
			fModifyListener = new ModifyListener() {
				/** serialVersionUID */
				private static final long serialVersionUID = 1L;

				@Override
				public void modifyText(final ModifyEvent e) {
					doModifyText(e);
				}
			};
			final SelectionListener selectionListener = new SelectionListener() {
				/** serialVersionUID */
				private static final long serialVersionUID = 1L;

				@Override
				public void widgetDefaultSelected(final SelectionEvent e) {
				}

				@Override
				public void widgetSelected(final SelectionEvent e) {
					doSelectionChanged(e);
				}
			};

			fComboControl = new Combo(parent, fFlags);
			// moved up due to 1GEUNW2
			fComboControl.setItems(fItems);
			if (fSelectionIndex != -1) {
				fComboControl.select(fSelectionIndex);
			} else {
				fComboControl.setText(fText);
			}
			fComboControl.setFont(parent.getFont());
			SwtUtil.setDefaultVisibleItemCount(fComboControl);
			fComboControl.addModifyListener(fModifyListener);
			fComboControl.addSelectionListener(selectionListener);
			fComboControl.setEnabled(isEnabled());
		}
		return fComboControl;
	}

	/**
	 * Gets the combo items.
	 */
	public String[] getItems() {
		return fItems;
	}

	/*
	 * @see DialogField#getNumberOfControls
	 */
	@Override
	public int getNumberOfControls() {
		return 2;
	}

	// ------ enable / disable management

	public int getSelectionIndex() {
		return fSelectionIndex;
	}

	// ------ text access

	/**
	 * Gets the text.
	 */
	public String getText() {
		return fText;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField#refresh()
	 */
	@Override
	public void refresh() {
		super.refresh();
		setTextWithoutUpdate(fText);
	}

	/**
	 * Selects an item.
	 */
	public boolean selectItem(final int index) {
		boolean success = false;
		if (isOkToUse(fComboControl)) {
			fComboControl.select(index);
			success = fComboControl.getSelectionIndex() == index;
		} else {
			if (index >= 0 && index < fItems.length) {
				fText = fItems[index];
				fSelectionIndex = index;
				success = true;
			}
		}
		if (success) {
			dialogFieldChanged();
		}
		return success;
	}

	/**
	 * Selects an item.
	 */
	public boolean selectItem(final String name) {
		for (int i = 0; i < fItems.length; i++) {
			if (fItems[i].equals(name)) {
				return selectItem(i);
			}
		}
		return false;
	}

	/*
	 * @see DialogField#setFocus
	 */
	@Override
	public boolean setFocus() {
		if (isOkToUse(fComboControl)) {
			fComboControl.setFocus();
		}
		return true;
	}

	/**
	 * Sets the combo items. Triggers a dialog-changed event.
	 */
	public void setItems(final String[] items) {
		fItems = items;
		if (isOkToUse(fComboControl)) {
			fComboControl.setItems(items);
		}
		dialogFieldChanged();
	}

	/**
	 * Sets the text. Triggers a dialog-changed event.
	 */
	public void setText(final String text) {
		fText = text;
		if (isOkToUse(fComboControl)) {
			fComboControl.setText(text);
		} else {
			dialogFieldChanged();
		}
	}

	/**
	 * Sets the text without triggering a dialog-changed event.
	 */
	public void setTextWithoutUpdate(final String text) {
		fText = text;
		if (isOkToUse(fComboControl)) {
			fComboControl.removeModifyListener(fModifyListener);
			fComboControl.setText(text);
			fComboControl.addModifyListener(fModifyListener);
		}
	}

	/*
	 * @see DialogField#updateEnableState
	 */
	@Override
	protected void updateEnableState() {
		super.updateEnableState();
		if (isOkToUse(fComboControl)) {
			fComboControl.setEnabled(isEnabled());
		}
	}

}
