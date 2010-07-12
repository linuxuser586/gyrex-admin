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
package org.eclipse.gyrex.admin.internal.widgets;

import org.eclipse.gyrex.admin.internal.configuration.wizard.ConfigurationWizardFactory;
import org.eclipse.gyrex.toolkit.Toolkit;
import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.widgets.StyledText;
import org.eclipse.gyrex.toolkit.widgets.Widget;

/**
 *
 */
public class DefaultAdminWidgetFactory implements IWidgetFactory {

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory#getWidget(java.lang.String, org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment)
	 */
	@Override
	public Widget getWidget(final String id, final IWidgetEnvironment environment) {

		// create a default page
		final Container container = new Container("default", Toolkit.NONE);
		container.setLabel("Gyrex System Admin");
		container.setDescription("Welcome to Gyrex.");

		final StyledText styledText = new StyledText("intro", container, Toolkit.NONE);
		styledText.setText("<text>\r\n" + "<p>There is currently not much content here. The platform is running and that's what we wanted to show you. After all, it's a showcase which demonstrates some concepts. If you like our vision, please follow our <a href=\"http://www.eclipse.org/gyrex/\" alt=\"Open the Gyrex blog.\">blog</a>.</p></text>", true, true);

		ConfigurationWizardFactory.createPlatformStatusInfo(container);

		return container;
	}
}
