/*******************************************************************************
 * Copyright (c) 2010 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.admin.web.gwt.client.internal.console.widgets;

import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTWidget;
import org.eclipse.gyrex.toolkit.gwt.client.ui.wizard.CWTWizardContainer;
import org.eclipse.gyrex.toolkit.gwt.client.ui.wizard.CWTWizardContainer.PageChangeListener;
import org.eclipse.gyrex.toolkit.gwt.client.ui.wizard.CWTWizardPage;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 *
 */
public class WizardPopupPanel extends DecoratedPopupPanel implements CloseHandler<CWTWidget>, ResizeHandler, PageChangeListener {

	private final PopupPanel background;
	private HandlerRegistration closeHandlerRegistration;
	private HandlerRegistration resizeHandlerRegistration;

	public WizardPopupPanel() {
		super();
		background = new PopupPanel();
		background.setStyleName("admin-WizardPanelBackgroundLayer");
		addStyleName("admin-WizardPanel");
		setAnimationEnabled(true);
	}

	@Override
	public void center() {
		super.center();
	}

	@Override
	public void hide(final boolean autoClosed) {
		if (!isShowing()) {
			return;
		}

		if (null != closeHandlerRegistration) {
			closeHandlerRegistration.removeHandler();
			closeHandlerRegistration = null;
		}

		if (null != resizeHandlerRegistration) {
			resizeHandlerRegistration.removeHandler();
			resizeHandlerRegistration = null;
		}

		Window.enableScrolling(true);
		background.hide(autoClosed);
		super.hide(autoClosed);
	}

	@Override
	public void onClose(final CloseEvent<CWTWidget> event) {
		hide();
	}

	@Override
	public void onResize(final ResizeEvent event) {
		center();
	}

	@Override
	public void pageChanged(final CWTWizardPage wizardPage) {
		// jumping dialog is ugly
		//center();
	}

	public void setWizard(final CWTWizardContainer w) {
		closeHandlerRegistration = w.addCloseHandler(this);
		w.addPageChangeListener(this);

		super.setWidget(w);
	}

	@Override
	public void show() {
		if (!isShowing()) {
			Window.enableScrolling(false);
			resizeHandlerRegistration = Window.addResizeHandler(this);
			background.show();
		}
		super.show();
	}
}
