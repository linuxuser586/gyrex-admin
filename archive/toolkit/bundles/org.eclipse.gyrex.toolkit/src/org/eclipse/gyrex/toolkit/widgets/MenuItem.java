/*******************************************************************************
 * Copyright (c) 2010 AGETO and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.toolkit.widgets;

import org.eclipse.gyrex.toolkit.commands.Command;
import org.eclipse.gyrex.toolkit.content.NoContent;
import org.eclipse.gyrex.toolkit.resources.ImageResource;

/**
 * A menu item.
 * <p>
 * A menu item has a label, a description and two images (one representing
 * enabled and one for disabled state). A menu item triggers a {@link Command}
 * when invoked.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public final class MenuItem extends DialogField<NoContent> {

	/** serialVersionUID */
	private static final long serialVersionUID = -4047852361081616989L;

	private Command command;
	private ImageResource image;
	private ImageResource disabledImage;

	/**
	 * A menu item.
	 * 
	 * @param id
	 *            the item id
	 * @param parent
	 *            the parent menu
	 * @param style
	 *            the item style
	 */
	public MenuItem(final String id, final Menu parent, final int style) {
		super(id, parent, style, NoContent.class);
	}

	/**
	 * Returns the command to be triggered when this item is invoked.
	 * 
	 * @return the command (maybe <code>null</code>)
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * Returns the image when the item is disabled.
	 * 
	 * @return the disabled image
	 */
	public ImageResource getDisabledImage() {
		return disabledImage;
	}

	/**
	 * Returns the item image.
	 * 
	 * @return the item image
	 */
	public ImageResource getImage() {
		return image;
	}

	/**
	 * Sets the command to be triggered when this item is invoked.
	 * 
	 * @param command
	 *            the command to trigger
	 */
	public void setCommand(final Command command) {
		this.command = command;
	}

	/**
	 * Sets the image to use when the item is disabled.
	 * 
	 * @param disabledImage
	 *            the disabled image to set
	 */
	public void setDisabledImage(final ImageResource disabledImage) {
		this.disabledImage = disabledImage;
	}

	/**
	 * Sets the item image.
	 * 
	 * @param image
	 *            the image to set
	 */
	public void setImage(final ImageResource image) {
		this.image = image;
	}
}
