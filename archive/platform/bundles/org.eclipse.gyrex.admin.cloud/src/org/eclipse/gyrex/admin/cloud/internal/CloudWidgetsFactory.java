/**
 * Copyright (c) 2010 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package org.eclipse.gyrex.admin.cloud.internal;

import org.eclipse.gyrex.toolkit.Toolkit;
import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.widgets.Widget;

public class CloudWidgetsFactory implements IWidgetFactory {

	private Container cloudMembership(final String id, final IWidgetEnvironment environment) {
		final Container container = new Container(id, Toolkit.NONE);
		container.setLabel("Cloud Membership");
		container.setDescription("Approve new cloud members, assign responsibilities or retire dead nodes.");

		return container;
	}

	@Override
	public Widget getWidget(final String id, final IWidgetEnvironment environment) {
		if ("cloud-membership".equals(id)) {
			return cloudMembership(id, environment);
		}
		return null;
	}

}
