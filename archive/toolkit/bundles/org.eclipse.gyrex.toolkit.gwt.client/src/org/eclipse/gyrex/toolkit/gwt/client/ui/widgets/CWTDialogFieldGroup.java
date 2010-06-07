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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SDialogFieldGroup;

/**
 * Composite for
 * <code>org.eclipse.gyrex.toolkit.widgets.DialogFieldGroup</code>.
 */
public class CWTDialogFieldGroup extends CWTContainer {

	static class DialogFieldGroupPanel extends ComplexPanel {
		private final Element fieldSetElement;
		private final Element legendElement;
		private final Element notesDiv;

		/**
		 * Creates a new instance.
		 */
		public DialogFieldGroupPanel() {

			fieldSetElement = DOM.createElement("fieldset");
			legendElement = DOM.createElement("legend");
			notesDiv = DOM.createDiv();

			DOM.appendChild(fieldSetElement, legendElement);
			DOM.appendChild(fieldSetElement, notesDiv);

			setElement(fieldSetElement);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.gwt.user.client.ui.Panel#add(com.google.gwt.user.client.ui.Widget)
		 */
		@Override
		public void add(final Widget w) {
			add(w, getElement());
		}

		public void setDescription(final String description) {
			DOM.setInnerText(notesDiv, description);
		}

		public void setName(final String name) {
			DOM.setInnerText(legendElement, name);
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.user.client.ui.UIObject#setStyleName(java.lang.String)
		 */
		@Override
		public void setStyleName(final String style) {
			super.setStyleName(style);
			setStyleName(fieldSetElement, style + "-fields");
			setStyleName(legendElement, style + "-legend");
			setStyleName(notesDiv, style + "-description");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rep.web.gwt.client.internal.ui.widgets.ContainerComposite#createPanel(org.eclipse.rep.web.gwt.client.rwt.ISerializedWidget,
	 *      org.eclipse.rep.web.gwt.client.ui.RenderingToolkit)
	 */
	@Override
	protected Panel createPanel(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SDialogFieldGroup group = (SDialogFieldGroup) serializedWidget;

		final DialogFieldGroupPanel dialogFieldGroupPanel = new DialogFieldGroupPanel();
		dialogFieldGroupPanel.setStyleName("cwt-DialogFieldGroup");

		if (null != group.title) {
			dialogFieldGroupPanel.setName(group.title);
		}

		if (null != group.description) {
			dialogFieldGroupPanel.setDescription(group.description);
		}

		return dialogFieldGroupPanel;
	}
}
