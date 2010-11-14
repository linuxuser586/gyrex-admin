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
package org.eclipse.gyrex.toolkit.rap.client.editor;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.gyrex.toolkit.Toolkit;
import org.eclipse.gyrex.toolkit.rap.client.WidgetFactory;
import org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTContainer;
import org.eclipse.gyrex.toolkit.rap.internal.ui.widgets.CWTWidget;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.EditorPart;

/**
 * This class forms a base of form editor that typically shows a Gyrex widget of
 * the editor input.
 */
public class WidgetFormEditor extends EditorPart {

	private WidgetFactory widgetFactory;
	private CWTWidget widget;

	/**
	 * Creates a new instance.
	 * 
	 * @param widgetFactory
	 *            the widget factory
	 */
	public WidgetFormEditor(final WidgetFactory widgetFactory) {
		this.widgetFactory = widgetFactory;
	}

	protected void createFormContent(final ScrolledForm form) {
		// the parent
		final Composite parent = form.getBody();
		parent.setLayout(new FillLayout());

		widget = getWidgetFactory().getWidget(getWidgetId());
		try {
			if (null != widget) {
				if (null != widget.getControl()) {
					Toolkit.error(Toolkit.ERROR_ALREADY_INITIALIZED, "widget");
				}

				// create widget control
				widget.createControl(parent);

				// form heading
				if (widget instanceof CWTContainer) {
					form.setText(((CWTContainer) widget).getContainerTitle());
				}

			} else {
				getFormToolkit().createLabel(parent, NLS.bind("widget \"{0}\" not found", getWidgetId()));
			}
		} catch (final RuntimeException e) {
			getFormToolkit().createLabel(parent, NLS.bind("error while initializing widget \"{0}\"", getWidgetId()));
			final StringWriter stringWriter = new StringWriter(400);
			e.printStackTrace(new PrintWriter(stringWriter));
			getFormToolkit().createText(parent, stringWriter.toString(), SWT.READ_ONLY);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {
		final ScrolledForm form = getFormToolkit().createScrolledForm(parent);
		BusyIndicator.showWhile(parent.getDisplay(), new Runnable() {
			public void run() {
				createFormContent(form);
			}
		});

		getFormToolkit().decorateFormHeading(form.getForm());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		// release factory
		widgetFactory = null;

		// super
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(final IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	FormToolkit getFormToolkit() {
		return getWidgetFactory().getToolkit().getFormToolkit();
	}

	/**
	 * Returns the widget factory.
	 * 
	 * @return the widget factory
	 */
	public WidgetFactory getWidgetFactory() {
		return widgetFactory;
	}

	/**
	 * Returns the widget id
	 * 
	 * @return the widget id
	 */
	String getWidgetId() {
		final IEditorInput editorInput = getEditorInput();
		if (null == editorInput) {
			Toolkit.error(Toolkit.ERROR_NOT_INITIALIZED, "missing input");
		}
		if (!WidgetFormEditorInput.class.isAssignableFrom(editorInput.getClass())) {
			Toolkit.error(Toolkit.ERROR_INVALID_ARGUMENT, "invalid input");
		}
		final WidgetFormEditorInput input = (WidgetFormEditorInput) editorInput;
		final String widgetId = input.getWidgetId();
		if (null == widgetId) {
			Toolkit.error(Toolkit.ERROR_NOT_INITIALIZED, "missing widget id");
		}
		return widgetId;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		if (!WidgetFormEditorInput.class.isAssignableFrom(input.getClass())) {
			throw new PartInitException("Invalid input.");
		}

		setSite(site);
		setInput(input);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
