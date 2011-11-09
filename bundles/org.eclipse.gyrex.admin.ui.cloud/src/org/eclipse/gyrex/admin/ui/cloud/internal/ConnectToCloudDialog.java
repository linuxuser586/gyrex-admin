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
package org.eclipse.gyrex.admin.ui.cloud.internal;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.gyrex.admin.ui.internal.wizards.dialogfields.DialogField;
import org.eclipse.gyrex.admin.ui.internal.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.gyrex.admin.ui.internal.wizards.dialogfields.LayoutUtil;
import org.eclipse.gyrex.admin.ui.internal.wizards.dialogfields.Separator;
import org.eclipse.gyrex.admin.ui.internal.wizards.dialogfields.StringDialogField;
import org.eclipse.gyrex.cloud.admin.ICloudManager;
import org.eclipse.gyrex.cloud.admin.INodeConfigurer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.apache.commons.lang.StringUtils;

public class ConnectToCloudDialog extends StatusDialog {

	private final StringDialogField urlField = new StringDialogField();
	private URL url;
	private final ICloudManager cloudManager;

	/**
	 * Creates a new instance.
	 * 
	 * @param cloudManager
	 * @param parent
	 */
	public ConnectToCloudDialog(final ICloudManager cloudManager, final Shell parent) {
		super(parent);
		this.cloudManager = cloudManager;
		setTitle("New Mount");
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		final GridData gd = (GridData) composite.getLayoutData();
		gd.minimumHeight = convertVerticalDLUsToPixels(60);
		gd.minimumWidth = convertHorizontalDLUsToPixels(400);

		urlField.setLabelText("URL");

		final IDialogFieldListener validateListener = new IDialogFieldListener() {
			@Override
			public void dialogFieldChanged(final DialogField field) {
				validate();
			}
		};

		urlField.setDialogFieldListener(validateListener);

		LayoutUtil.doDefaultLayout(composite, new DialogField[] { new Separator(), urlField }, false);
		LayoutUtil.setHorizontalGrabbing(urlField.getTextControl(null));

		urlField.setFocus();

		return composite;
	}

	/**
	 * Returns the url.
	 * 
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}

	@Override
	protected void okPressed() {
		validate();
		if (!getStatus().isOK()) {
			return;
		}

		final INodeConfigurer nodeConfigurer = cloudManager.getNodeConfigurer(cloudManager.getLocalInfo().getNodeId());

//		IStatus status;
//		if (cloudMemberButton.getSelection()) {
//			status = nodeConfigurer.configureConnection(connectStringText.getText());
//		} else {
//			status = nodeConfigurer.configureConnection(null);
//		}
//		if (!status.isOK()) {
//			Policy.getStatusHandler().show(status, "Error Configuring Node");
//		}

		super.okPressed();
	}

	void setError(final String message) {
		updateStatus(new Status(IStatus.ERROR, CloudUiActivator.SYMBOLIC_NAME, message));
		getShell().pack(true);
	}

	void setInfo(final String message) {
		updateStatus(new Status(IStatus.INFO, CloudUiActivator.SYMBOLIC_NAME, message));
	}

	void setWarning(final String message) {
		updateStatus(new Status(IStatus.WARNING, CloudUiActivator.SYMBOLIC_NAME, message));
	}

	void validate() {
		url = null;
		final String urlStr = urlField.getText();
		if (StringUtils.isBlank(urlStr)) {
			setInfo("Please enter an url.");
			return;
		}

		try {
			url = new URL(urlStr);
		} catch (final MalformedURLException e) {
			setError(String.format("The entered url is invalid. %s", e.getMessage()));
			return;
		}

		updateStatus(Status.OK_STATUS);
	}
}
