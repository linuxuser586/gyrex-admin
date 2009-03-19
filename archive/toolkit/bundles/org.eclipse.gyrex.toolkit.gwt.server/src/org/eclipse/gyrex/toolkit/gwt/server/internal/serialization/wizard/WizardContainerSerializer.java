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
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.wizard.SWizardContainer;
import org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.widgets.ContainerSerializer;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.wizard.WizardContainer;

/**
 * {@link WizardContainer} serializer.
 */
public class WizardContainerSerializer extends ContainerSerializer {

	@Override
	protected SContainer createSContainer(final Container container, final SContainer parent) {
		final WizardContainer wizardContainer = (WizardContainer) container;
		final SWizardContainer sWizardContainer = new SWizardContainer();

		sWizardContainer.cancelCommand = serializeCommand(wizardContainer.getCancelCommand(), wizardContainer);
		sWizardContainer.finishCommand = serializeCommand(wizardContainer.getFinishCommand(), wizardContainer);

		return sWizardContainer;
	}
}
