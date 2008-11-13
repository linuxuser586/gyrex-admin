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
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import org.eclipse.cloudfree.toolkit.gwt.client.ui.content.IContentAdapter;
import org.eclipse.cloudfree.toolkit.gwt.client.ui.internal.validation.DialogFieldValidator;
import org.eclipse.cloudfree.toolkit.gwt.client.ui.internal.validation.ValidationContext;
import org.eclipse.cloudfree.toolkit.gwt.client.ui.internal.validation.ValidationResult;
import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.content.SContentEntry;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.content.SNumberEntry;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SNumberInput;

/**
 * Composite for <code>org.eclipse.cloudfree.toolkit.widgets.TextInput</code>.
 */
public class CWTNumberInput extends CWTDialogField {

	static class NumberInputPanel extends ComplexPanel {

		private static int uniqueId;

		private final TextBox textBox;
		private final Element labelElem;

		public NumberInputPanel() {
			setElement(DOM.createSpan());

			labelElem = DOM.createLabel();
			DOM.appendChild(getElement(), labelElem);

			textBox = new TextBox();
			add(textBox, getElement());

			final String uid = "numberInput" + (++uniqueId);
			DOM.setElementProperty(textBox.getElement(), "id", uid);
			DOM.setElementProperty(labelElem, "htmlFor", uid);
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.user.client.ui.UIObject#addStyleDependentName(java.lang.String)
		 */
		@Override
		public void addStyleDependentName(final String styleSuffix) {
			super.addStyleName(getStylePrimaryName() + '-' + styleSuffix);
			setStyleName(labelElem, getStylePrimaryName() + "-label-" + styleSuffix, true);
			textBox.addStyleName(getStylePrimaryName() + "-field-" + styleSuffix);
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.user.client.ui.UIObject#addStyleName(java.lang.String)
		 */
		@Override
		public void addStyleName(final String style) {
			super.addStyleName(style);
			setStyleName(labelElem, style + "-label", true);
			textBox.addStyleName(style + "-field");
		}

		/**
		 * Returns the textBox.
		 * 
		 * @return the textBox
		 */
		public TextBox getTextBox() {
			return textBox;
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.user.client.ui.UIObject#removeStyleDependentName(java.lang.String)
		 */
		@Override
		public void removeStyleDependentName(final String styleSuffix) {
			super.addStyleName(getStylePrimaryName() + '-' + styleSuffix);
			setStyleName(labelElem, getStylePrimaryName() + "-label-" + styleSuffix, false);
			textBox.removeStyleName(getStylePrimaryName() + "-field-" + styleSuffix);
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.user.client.ui.UIObject#removeStyleName(java.lang.String)
		 */
		@Override
		public void removeStyleName(final String style) {
			super.removeStyleName(style);
			setStyleName(labelElem, style + "-label", false);
			textBox.removeStyleName(style + "-field");
		}

		public void setLabelText(final String text) {
			DOM.setInnerText(labelElem, text);
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.user.client.ui.UIObject#setStyleName(java.lang.String)
		 */
		@Override
		public void setStyleName(final String style) {
			super.setStyleName(style);
			setStyleName(labelElem, style + "-label");
			textBox.setStyleName(style + "-field");
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.user.client.ui.UIObject#setStylePrimaryName(java.lang.String)
		 */
		@Override
		public void setStylePrimaryName(final String style) {
			super.setStylePrimaryName(style);
			setStylePrimaryName(labelElem, style + "-label");
			textBox.setStylePrimaryName(style + "-field");
		}
	}

	private static final DialogFieldValidator textInputValidator = new DialogFieldValidator() {

		@Override
		public ValidationResult validate(final CWTDialogField dialogField, final ValidationContext context) {
			final CWTNumberInput numberInput = (CWTNumberInput) dialogField;
			if (!numberInput.isValid()) {
				return ValidationResult.ERROR;
			}
			return ValidationResult.OK;
		}
	};

	private static final IContentAdapter numberContentAdapter = new IContentAdapter() {

		public SContentEntry getContent(final CWTWidget widget) {
			final CWTNumberInput numberInput = (CWTNumberInput) widget;
			final SNumberEntry numberEntry = new SNumberEntry();
			final String text = numberInput.getTextBoxWidget().getText();
			if (text.trim().length() > 0) {
				try {
					numberEntry.number = Integer.parseInt(text);
				} catch (final NumberFormatException e) {
					// invalid number;
				}
			}
			return numberEntry;
		}

		public boolean isSet(final CWTWidget widget) {
			final CWTNumberInput numberInput = (CWTNumberInput) widget;
			final String text = numberInput.getTextBoxWidget().getText();
			return (null != text) && (text.length() > 0);
		}

		public void setContent(final CWTWidget widget, final SContentEntry content) {
			final CWTNumberInput numberInput = (CWTNumberInput) widget;
			final SNumberEntry numberEntry = (SNumberEntry) content;
			final String text = null != numberEntry.number ? numberEntry.number.toString() : "";
			numberInput.getTextBoxWidget().setText(text);
		}

	};

	private final ChangeListener validationListener = new ChangeListener() {

		public void onChange(final Widget sender) {
			validate();
		}
	};

	private final KeyboardListener validationKeyboardListener = new KeyboardListener() {

		public void onKeyDown(final Widget sender, final char keyCode, final int modifiers) {
		}

		public void onKeyPress(final Widget sender, final char keyCode, final int modifiers) {
			validate();
		}

		public void onKeyUp(final Widget sender, final char keyCode, final int modifiers) {
			validate();
		}
	};

	private NumberInputPanel numberInputPanel;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets.CWTWidget#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(final Class<T> adapter) {
		if (SourcesChangeEvents.class == adapter) {
			// see http://code.google.com/p/google-web-toolkit/issues/detail?id=2710
			return (T) getTextBoxWidget();
		}

		if (DialogFieldValidator.class == adapter) {
			// see http://code.google.com/p/google-web-toolkit/issues/detail?id=2710
			return (T) textInputValidator;
		}

		if (IContentAdapter.class == adapter) {
			// see http://code.google.com/p/google-web-toolkit/issues/detail?id=2710
			return (T) numberContentAdapter;
		}

		// call super
		return super.getAdapter(adapter);
	}

	SNumberInput getSNumberInput() {
		return (SNumberInput) getSerializedWidget();
	}

	TextBox getTextBoxWidget() {
		if (null == numberInputPanel) {
			return null;
		}
		return numberInputPanel.textBox;
	}

	boolean isValid() {
		final SNumberInput sNumberInput = getSNumberInput();
		final String text = getTextBoxWidget().getText();

		// required fields must contain a number
		if (sNumberInput.required) {
			if (text.length() <= 0) {
				return false;
			}
		}

		if (text.length() > 0) {
			try {
				final Integer number = Integer.parseInt(text);

				// validate upper limit
				if (null != sNumberInput.upperLimit) {
					final int result = number.compareTo(new Integer(sNumberInput.upperLimit.intValue()));
					if (sNumberInput.upperLimitInclusive && (result > 0)) {
						return false;
					}
					if (!sNumberInput.upperLimitInclusive && (result >= 0)) {
						return false;
					}
				}

				// validate lower limit
				if (null != sNumberInput.lowerLimit) {
					final int result = number.compareTo(new Integer(sNumberInput.lowerLimit.intValue()));
					if (sNumberInput.lowerLimitInclusive && (result < 0)) {
						return false;
					}
					if (!sNumberInput.lowerLimitInclusive && (result <= 0)) {
						return false;
					}
				}

			} catch (final NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		numberInputPanel.textBox.addChangeListener(validationListener);
		numberInputPanel.textBox.addKeyboardListener(validationKeyboardListener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets.CWTDialogField#onLoad()
	 */
	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.gwt.client.ui.widgets.CWTDialogField#onUnload()
	 */
	@Override
	protected void onUnload() {
		numberInputPanel.textBox.removeKeyboardListener(validationKeyboardListener);
		numberInputPanel.textBox.removeChangeListener(validationListener);
		super.onUnload();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cloudfree.toolkit.gwt.client.internal.ui.widgets.DialogFieldComposite#renderFieldWidget(org.eclipse.cloudfree.toolkit.gwt.client.rwt.ISerializedWidget,
	 *      org.eclipse.cloudfree.toolkit.gwt.client.ui.RenderingToolkit)
	 */
	@Override
	protected Widget renderFieldWidget(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SNumberInput numberInput = (SNumberInput) serializedWidget;

		String label = numberInput.label;
		if (null == label) {
			label = "";
		}

		numberInputPanel = new NumberInputPanel();
		//numberInputPanel.setStyleName("cwt-NumberInput");
		numberInputPanel.setStylePrimaryName("cwt-NumberInput");
		numberInputPanel.setLabelText(label);

		if (null != numberInput.toolTipText) {
			numberInputPanel.getTextBox().setTitle(numberInput.toolTipText);
		}

		numberInputPanel.getTextBox().setReadOnly(numberInput.readOnly);
		if (numberInput.required) {
			numberInputPanel.addStyleName("required");
		}

		return numberInputPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cloudfree.toolkit.gwt.client.internal.ui.widgets.DialogFieldComposite#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		if ((null == numberInputPanel) || (null == numberInputPanel.textBox)) {
			return;
		}

		numberInputPanel.textBox.setEnabled(enabled);
	}

	/**
	 * 
	 */
	void validate() {
		if (isValid()) {
			numberInputPanel.removeStyleDependentName("error");
		} else {
			numberInputPanel.addStyleDependentName("error");
		}
	}
}
