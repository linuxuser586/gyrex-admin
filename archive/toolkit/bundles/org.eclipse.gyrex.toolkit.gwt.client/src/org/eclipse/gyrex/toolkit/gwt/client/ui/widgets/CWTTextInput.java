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

import org.eclipse.gyrex.toolkit.gwt.client.ui.content.IContentAdapter;
import org.eclipse.gyrex.toolkit.gwt.client.ui.events.HasWidgetChangeHandlers;
import org.eclipse.gyrex.toolkit.gwt.client.ui.events.WidgetChangeEvent;
import org.eclipse.gyrex.toolkit.gwt.client.ui.events.WidgetChangeHandler;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.DialogFieldValidator;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.ValidationContext;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.validation.ValidationResult;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SContentEntry;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.STextEntry;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.STextInput;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Composite for <code>org.eclipse.gyrex.toolkit.widgets.TextInput</code>.
 */
public class CWTTextInput extends CWTDialogField {

	private final class ContentTracker implements HasWidgetChangeHandlers, ValueChangeHandler<String> {

		@Override
		public HandlerRegistration addWidgetChangeHandler(final WidgetChangeHandler handler) {
			return addHandler(handler, WidgetChangeEvent.TYPE);
		}

		@Override
		public void fireEvent(final GwtEvent<?> event) {
			CWTTextInput.this.fireEvent(event);
		}

		@Override
		public void onValueChange(final ValueChangeEvent<String> event) {
			WidgetChangeEvent.fire(this, CWTTextInput.this);
		}
	}

	static class TextInputPanel extends ComplexPanel {

		private static int uniqueId;

		private final TextBox textBox;
		private final Element labelElem;

		public TextInputPanel() {
			this(new TextBox(), "textInput");
		}

		protected TextInputPanel(final TextBox textBox, final String uidPrefix) {
			setElement(DOM.createSpan());

			labelElem = DOM.createLabel();
			DOM.appendChild(getElement(), labelElem);

			this.textBox = textBox;
			add(textBox, getElement());

			final String uid = uidPrefix + (++uniqueId);
			DOM.setElementProperty(textBox.getElement(), "id", uid);
			DOM.setElementProperty(labelElem, "htmlFor", uid);
		}

		/**
		 * Returns the textBox.
		 * 
		 * @return the textBox
		 */
		public TextBox getTextBox() {
			return textBox;
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
	}

	static final DialogFieldValidator textInputValidator = new DialogFieldValidator() {

		@Override
		public ValidationResult validate(final CWTDialogField dialogField, final ValidationContext context) {
			final CWTTextInput textInput = (CWTTextInput) dialogField;
			final STextInput sTextInput = textInput.getSTextInput();

			// required fields must contain some text
			if (sTextInput.required) {
				if (textInput.getTextBoxWidget().getText().length() <= 0) {
					return ValidationResult.ERROR;
				}
			}

			// text fields are valid
			return ValidationResult.OK;
		}
	};

	static final IContentAdapter textContentAdapter = new IContentAdapter() {

		public SContentEntry getContent(final CWTWidget widget) {
			final CWTTextInput textInput = (CWTTextInput) widget;
			final STextEntry textEntry = new STextEntry();
			textEntry.text = textInput.getTextBoxWidget().getText();
			return textEntry;
		}

		public boolean isSet(final CWTWidget widget) {
			final CWTTextInput textInput = (CWTTextInput) widget;
			final String text = textInput.getTextBoxWidget().getText();
			return (null != text) && (text.length() > 0);
		}

		public void setContent(final CWTWidget widget, final SContentEntry content) {
			final CWTTextInput textInput = (CWTTextInput) widget;
			final STextEntry textEntry = (STextEntry) content;
			final String text = null != textEntry.text ? textEntry.text : "";
			textInput.getTextBoxWidget().setText(text);
		}

	};

	private TextInputPanel textInputPanel;
	private ContentTracker contentTracker;

	protected TextInputPanel createTextInput() {
		final TextInputPanel textInputPanel = new TextInputPanel();
		textInputPanel.setStyleName("cwt-TextInput");
		return textInputPanel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(final Class<T> adapter) {
		if (HasWidgetChangeHandlers.class == adapter) {
			// see http://code.google.com/p/google-web-toolkit/issues/detail?id=2710
			return (T) contentTracker;
		}

		if (DialogFieldValidator.class == adapter) {
			// see http://code.google.com/p/google-web-toolkit/issues/detail?id=2710
			return (T) textInputValidator;
		}

		if (IContentAdapter.class == adapter) {
			// see http://code.google.com/p/google-web-toolkit/issues/detail?id=2710
			return (T) textContentAdapter;
		}

		// call super
		return super.getAdapter(adapter);
	}

	private STextInput getSTextInput() {
		return (STextInput) getSerializedWidget();
	}

	protected TextBox getTextBoxWidget() {
		if (null == textInputPanel) {
			return null;
		}
		return textInputPanel.textBox;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gyrex.toolkit.gwt.client.internal.ui.widgets.DialogFieldComposite#renderFieldWidget(org.eclipse.gyrex.toolkit.gwt.client.rwt.ISerializedWidget,
	 *      org.eclipse.gyrex.toolkit.gwt.client.ui.RenderingToolkit)
	 */
	@Override
	protected Widget renderFieldWidget(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final STextInput textInput = (STextInput) serializedWidget;

		String label = textInput.label;
		if (null == label) {
			label = "";
		}

		textInputPanel = createTextInput();
		textInputPanel.setLabelText(label);

		if (textInput.maxLength > 0) {
			textInputPanel.getTextBox().setMaxLength(textInput.maxLength);
		}

		if (null != textInput.toolTipText) {
			textInputPanel.getTextBox().setTitle(textInput.toolTipText);
		}

		textInputPanel.getTextBox().setReadOnly(textInput.readOnly);
		if (textInput.required) {
			textInputPanel.addStyleName("required");
		}

		contentTracker = new ContentTracker();
		textInputPanel.getTextBox().addValueChangeHandler(contentTracker);

		return textInputPanel;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if ((null == textInputPanel) || (null == textInputPanel.textBox)) {
			return;
		}

		textInputPanel.textBox.setEnabled(enabled);
	}
}
