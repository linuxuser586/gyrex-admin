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
package org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.wizard;


import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.wizard.SWizardPage;
import org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.widgets.ContainerSerializer;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.wizard.WizardPage;

/**
 * {@link WizardPage} serializer.
 */
public class WizardPageSerializer extends ContainerSerializer {

	@Override
	protected SContainer createSContainer(final Container container, final SContainer parent) {
		final WizardPage wizardPage = (WizardPage) container;
		final SWizardPage sWizardPage = new SWizardPage();
		sWizardPage.continueRule = serializeDialogFieldRule(wizardPage.getContinueRule(), wizardPage);
		return sWizardPage;
	}

}
