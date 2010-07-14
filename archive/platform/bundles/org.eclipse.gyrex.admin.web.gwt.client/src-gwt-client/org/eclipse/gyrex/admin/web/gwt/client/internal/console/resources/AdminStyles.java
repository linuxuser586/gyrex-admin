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
package org.eclipse.gyrex.admin.web.gwt.client.internal.console.resources;

import org.eclipse.gyrex.admin.web.gwt.client.internal.console.resources.css.AdminCss;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

/**
 *
 */
public interface AdminStyles extends ClientBundle {

	AdminStyles ADMIN_STYLES = GWT.create(AdminStyles.class);

	@Source("css/admin.css")
	AdminCss getAdminCss();
}
