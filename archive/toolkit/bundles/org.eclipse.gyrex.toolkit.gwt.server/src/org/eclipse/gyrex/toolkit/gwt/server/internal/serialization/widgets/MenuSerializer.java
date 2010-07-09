/*******************************************************************************
 * Copyright (c) 2010 <enter-company-name-here> and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     <enter-developer-name-here> - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.gwt.server.internal.serialization.widgets;

import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets.SMenu;
import org.eclipse.gyrex.toolkit.widgets.Container;
import org.eclipse.gyrex.toolkit.widgets.Menu;

/**
 * {@link Menu} serializer.
 */
public class MenuSerializer extends ContainerSerializer {

	@Override
	protected SContainer createSContainer(final Container container, final SContainer parent) {
		return new SMenu();
	}
}
