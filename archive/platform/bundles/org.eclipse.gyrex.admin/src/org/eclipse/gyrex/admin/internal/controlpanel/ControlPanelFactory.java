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
package org.eclipse.gyrex.admin.internal.controlpanel;

import org.eclipse.gyrex.toolkit.Toolkit;
import org.eclipse.gyrex.toolkit.resources.ImageResource;
import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory;
import org.eclipse.gyrex.toolkit.widgets.Menu;
import org.eclipse.gyrex.toolkit.widgets.MenuItem;
import org.eclipse.gyrex.toolkit.widgets.Widget;

/**
 * Factory for the control panel.
 */
public class ControlPanelFactory implements IWidgetFactory {

	private static final String IMG_MENU_SYSTEM_SECURITY = "platform:/plugin/org.eclipse.gyrex.admin/images/icons/48/eclipse.gif";
	private static final String IMG_MENU_NETWORK_CLOUD = "platform:/plugin/org.eclipse.gyrex.admin/images/icons/48/eclipse.gif";
	private static final String IMG_ITEM_SYSTEM_STATUS = "platform:/plugin/org.eclipse.gyrex.admin/images/icons/48/eclipse.gif";
	private static final String IMG_MENU_MAINTENANCE_CENTER = "platform:/plugin/org.eclipse.gyrex.admin/images/icons/48/eclipse.gif";
	private static final String IMG_MENU_PLATFORM_UPDATE = "platform:/plugin/org.eclipse.gyrex.admin/images/48/eclipse.gif";
	private static final String IMG_ITEM_TERMINAL = "platform:/plugin/org.eclipse.gyrex.admin/images/icons/terminal.png";
	private static final String IMG_MENU_ADMINISTRATION = "platform:/plugin/org.eclipse.gyrex.admin/images/icons/48/eclipse.gif";
	private static final String IMG_MENU_SOFTWARE = "platform:/plugin/org.eclipse.gyrex.admin/images/icons/48/eclipse.gif";
	private static final String IMG_MENU_CONTROL_PANEL = "platform:/plugin/org.eclipse.gyrex.admin/images/icons/48/eclipse.gif";

	private static final String CONTROL_PANEL = "control-panel";
	public static final String[] IDS = new String[] { CONTROL_PANEL };

	private void administration(final Menu systemAndSecurity) {
		final Menu administration = new Menu("administration", systemAndSecurity, Toolkit.NONE);
		administration.setLabel("Administration");
		administration.setImage(ImageResource.createFromUrl(IMG_MENU_ADMINISTRATION));

		final MenuItem webTerminal = new MenuItem("web-terminal", administration, Toolkit.NONE);
		webTerminal.setLabel("Open Terminal");
		webTerminal.setImage(ImageResource.createFromUrl(IMG_ITEM_TERMINAL));
	}

	private Widget createControlPanel(final IWidgetEnvironment environment) {
		final Menu controlPanel = new Menu(CONTROL_PANEL, Toolkit.NONE);
		controlPanel.setLabel("Control Panel");
		controlPanel.setDescription("Configure settings of the system.");
		controlPanel.setImage(ImageResource.createFromUrl(IMG_MENU_CONTROL_PANEL));
		systemAndSecurity(controlPanel);
		networkAndCloud(controlPanel);
		software(controlPanel);
		return controlPanel;
	}

	@Override
	public Widget getWidget(final String id, final IWidgetEnvironment environment) {
		if (CONTROL_PANEL.equals(id)) {
			return createControlPanel(environment);
		}
		return null;
	}

	private void gyrexUpdate(final Menu systemAndSecurity) {
		final Menu gyrexUpdate = new Menu("gyrex-update", systemAndSecurity, Toolkit.NONE);
		gyrexUpdate.setLabel("Gyrex Update");
		gyrexUpdate.setImage(ImageResource.createFromUrl(IMG_MENU_PLATFORM_UPDATE));

		new MenuItem("gyrex-update-settings", gyrexUpdate, Toolkit.NONE);
		new MenuItem("gyrex-update-check", gyrexUpdate, Toolkit.NONE);
		new MenuItem("gyrex-update-show", gyrexUpdate, Toolkit.NONE);
	}

	private void maintenanceCenter(final Menu systemAndSecurity) {
		final Menu maintenance = new Menu("maintenance-center", systemAndSecurity, Toolkit.NONE);
		maintenance.setLabel("Maintenance Center");
		maintenance.setImage(ImageResource.createFromUrl(IMG_MENU_MAINTENANCE_CENTER));

		final MenuItem systemStatus = new MenuItem("system-status", maintenance, Toolkit.NONE);
		systemStatus.setLabel("System Status");
		systemStatus.setImage(ImageResource.createFromUrl(IMG_ITEM_SYSTEM_STATUS));
	}

	private void networkAndCloud(final Menu controlPanel) {
		final Menu networkAndCloud = new Menu("network-cloud", controlPanel, Toolkit.NONE);
		networkAndCloud.setLabel("Network And Cloud");
		networkAndCloud.setImage(ImageResource.createFromUrl(IMG_MENU_NETWORK_CLOUD));
	}

	private void software(final Menu controlPanel) {
		final Menu software = new Menu("software", controlPanel, Toolkit.NONE);
		software.setLabel("Software");
		software.setImage(ImageResource.createFromUrl(IMG_MENU_SOFTWARE));
	}

	private void systemAndSecurity(final Menu controlPanel) {
		final Menu systemAndSecurity = new Menu("system-security", controlPanel, Toolkit.NONE);
		systemAndSecurity.setLabel("System and Security");
		systemAndSecurity.setImage(ImageResource.createFromUrl(IMG_MENU_SYSTEM_SECURITY));

		maintenanceCenter(systemAndSecurity);
		gyrexUpdate(systemAndSecurity);
		administration(systemAndSecurity);

		final MenuItem featured1 = new MenuItem("system-status", Toolkit.NONE);
		featured1.setLabel("Check system status");
		final MenuItem featured2 = new MenuItem("web-terminal", Toolkit.NONE);
		featured2.setLabel("Administrate system through terminal");
		systemAndSecurity.setFeaturedItems(featured1, featured2);
	}
}
