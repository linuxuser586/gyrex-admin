/**
 * Copyright (c) 2011 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package org.eclipse.gyrex.admin.ui.internal.wizards.dialogfields;

import org.eclipse.rwt.widgets.UploadItem;

/**
 *
 */
public interface IUploadAdapter {

	/**
	 * @param uploadItem
	 */
	void uploadFinished(UploadItem uploadItem);

}