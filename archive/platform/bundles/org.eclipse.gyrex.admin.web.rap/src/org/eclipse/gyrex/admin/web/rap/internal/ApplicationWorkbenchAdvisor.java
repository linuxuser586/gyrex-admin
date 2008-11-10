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
package org.eclipse.cloudfree.admin.web.rap.internal;


import org.eclipse.cloudfree.toolkit.rap.client.editor.WidgetFormEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * This workbench advisor creates the window advisor, and specifies the
 * perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "org.eclipse.cloudfree.admin.web.rap.perspective";

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#postStartup()
	 */
	@Override
	public void postStartup() {
		// always open the default admin view
		final IWorkbenchWindow workbenchWindow = getWorkbenchConfigurer().getWorkbench().getActiveWorkbenchWindow();
		if (null != workbenchWindow) {
			final IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
			if (null != workbenchPage) {
				try {
					workbenchPage.openEditor(new WidgetFormEditorInput(""), CloudFreeAdminEditorFactory.EDITOR_ID);
				} catch (final PartInitException e) {
					// TODO should log
					e.printStackTrace();
				}
			}
		}
	}
}
