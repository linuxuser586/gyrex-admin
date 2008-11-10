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


import org.eclipse.cloudfree.toolkit.widgets.DialogFieldGroup;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * Composite for <code>org.eclipse.cloudfree.toolkit.widgets.DialogFieldGroup</code>.
 */
public class CWTDialogFieldGroup extends CWTContainer<DialogFieldGroup> {

	//	/*
	//	 * (non-Javadoc)
	//	 * 
	//	 * @see org.eclipse.rep.web.gwt.client.internal.ui.widgets.ContainerComposite#createPanel(org.eclipse.rep.web.gwt.client.rwt.ISerializedWidget,
	//	 *      org.eclipse.rep.web.gwt.client.ui.RenderingToolkit)
	//	 */
	//	@Override
	//	protected Panel createPanel(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
	//		final SDialogFieldGroup group = (SDialogFieldGroup) serializedWidget;
	//
	//		final DialogFieldGroupPanel dialogFieldGroupPanel = new DialogFieldGroupPanel();
	//
	//		if (null != group.title) {
	//			dialogFieldGroupPanel.setName(group.title);
	//		}
	//
	//		if (null != group.description) {
	//			dialogFieldGroupPanel.setDescription(group.description);
	//		}
	//
	//		return dialogFieldGroupPanel;
	//	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.rap.internal.ui.widgets.CWTContainer#createWidgetControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createWidgetControl(final Composite parent) {
		final FormToolkit formToolkit = getToolkit().getFormToolkit();
		final Section outer = formToolkit.createSection(parent, Section.DESCRIPTION | ExpandableComposite.SHORT_TITLE_BAR);
		outer.setLayout(new TableWrapLayout());
		outer.setText(getContainerTitle());
		outer.setDescription(getContainerDescription());

		final Composite composite = createComposite(outer);
		assert composite != null;
		outer.setClient(composite);

		initComposite(composite);
		populateChildren(getComposite());

		// layout the children
		layoutDialogFields();

		return outer;
	}

	/**
	 * Performs the layout of the dialog fields within this dialog field group.
	 */
	private void layoutDialogFields() {
		// determine number of columns
		int columns = 1;
		final CWTWidget<?>[] children = getChildren();
		for (final CWTWidget child : children) {
			if (child instanceof CWTDialogField) {
				columns = Math.max(((CWTDialogField) child).getNumberOfColumns(), columns);
			}
		}

		// set composite layout
		GridLayoutFactory.swtDefaults().numColumns(columns).applyTo(getComposite());

		// layout all children
		for (final CWTWidget<?> child : children) {
			if (child instanceof CWTDialogField) {
				// let dialog field layout itself
				((CWTDialogField) child).fillIntoGrid(columns);
			} else {
				// apply a default layout
				GridDataFactory.generate(child.getControl(), columns, 1);
			}
		}
	}
}
