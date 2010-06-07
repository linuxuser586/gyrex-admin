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
package org.eclipse.gyrex.toolkit.gwt.client.ui.wizard;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

import org.eclipse.gyrex.toolkit.gwt.client.ExecuteCommandCallback;
import org.eclipse.gyrex.toolkit.gwt.client.WidgetFactoryException;
import org.eclipse.gyrex.toolkit.gwt.client.ui.internal.content.ContentHelper;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTContainer;
import org.eclipse.gyrex.toolkit.gwt.client.ui.widgets.CWTToolkit;
import org.eclipse.gyrex.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.commands.SCommand;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.content.SContentSet;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.wizard.SWizardContainer;
import org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.wizard.SWizardPage;

/**
 * Composite for
 * <code>org.eclipse.gyrex.toolkit.wizard.WizardContainer</code>.
 */
public class CWTWizardContainer extends CWTContainer {

	public static interface PageChangeListener {

		void pageChanged(CWTWizardPage wizardPage);

	}

	private static final class PageDescription extends Widget {

		/**
		 * Creates a new instance.
		 */
		public PageDescription() {
			setElement(DOM.createElement("h2"));
		}

		public void setText(final String text) {
			DOM.setInnerText(getElement(), text);
		}
	}

	private static final class PageTitle extends Widget {

		/**
		 * Creates a new instance.
		 */
		public PageTitle() {
			setElement(DOM.createElement("h1"));
		}

		public void setText(final String text) {
			DOM.setInnerText(getElement(), text);
		}
	}

	public static final int BUTTON_ID_HELP = 4;
	public static final int BUTTON_ID_BACK = 3;
	public static final int BUTTON_ID_NEXT = 2;

	public static final int BUTTON_ID_FINISH = 1;
	public static final int BUTTON_ID_CANCEL = 0;
	private DeckPanel pagesPanel;
	private Button backButton;
	private Button nextButton;
	private Button finishButton;
	private Button cancelButton;

	private Panel headerPanel;
	private Panel buttonsPanel;
	private boolean allButtonsDisabled = false;

	private boolean renderPageTitleAndDescription = true;
	private PageTitle pageTitle;
	private PageDescription pageDescription;

	private ArrayList<PageChangeListener> pageChangeListeners;

	public void addPageChangeListener(final PageChangeListener listener) {
		if (null == pageChangeListeners) {
			pageChangeListeners = new ArrayList<PageChangeListener>(1);
		}
		if (!pageChangeListeners.contains(listener)) {
			pageChangeListeners.add(listener);
		}
	}

	/**
	 * The Back button has been pressed.
	 */
	protected void backPressed() {
		final int previousPage = pagesPanel.getVisibleWidget() - 1;
		if (previousPage < 0) {
			// should not happen because the back button would be disabled
			return;
		}
		showPage(previousPage);
	}

	/**
	 * Notifies that this wizard's button with the given id has been pressed.
	 * <p>
	 * The default implementation of this framework method calls
	 * <code>{@link #helpPressed()}</code> if the help button is the pressed,
	 * <code>{@link #backPressed()}</code> if the back button is the pressed,
	 * <code>{@link #nextPressed()}</code> if the next button is the pressed,
	 * <code>{@link #finishPressed()}</code> if the finish button is the pressed
	 * and <code>{@link #cancelPressed()}</code> if the cancel button is the
	 * pressed. All other button presses are ignored. Subclasses may override to
	 * handle other buttons, but should call <code>super.buttonPressed</code> if
	 * the default handling of the buttons mentioned above is desired.
	 * </p>
	 * 
	 * @param buttonId
	 *            the id of the button that was pressed (see
	 *            <code>CWTWizardContainer.BUTTON_ID_*</code> constants)
	 */
	protected void buttonPressed(final int buttonId) {

		switch (buttonId) {
			case BUTTON_ID_HELP: {
				helpPressed();
				break;
			}
			case BUTTON_ID_BACK: {
				backPressed();
				break;
			}
			case BUTTON_ID_NEXT: {
				nextPressed();
				break;
			}
			case BUTTON_ID_FINISH: {
				finishPressed();
				break;
			}
			case BUTTON_ID_CANCEL: {
				cancelPressed();
				break;
			}
		}
	}

	/**
	 * The Cancel button has been pressed.
	 */
	protected void cancelPressed() {
		triggerCommand(getSWizardContainer().cancelCommand);
	}

	/**
	 * Returns whether this wizard could be finished without further user
	 * interaction.
	 * <p>
	 * The result of this method is typically used by the wizard container to
	 * enable or disable the Finish button.
	 * </p>
	 * 
	 * @return <code>true</code> if the wizard could be finished, and
	 *         <code>false</code> otherwise
	 */
	boolean canFinish() {
		for (int i = 0; i < pagesPanel.getWidgetCount(); i++) {
			final CWTWizardPage page = (CWTWizardPage) pagesPanel.getWidget(i);
			if (!page.isPageComplete()) {
				return false;
			}
		}
		return true;
	}

	private Button createButton(final int id, final String label, final Panel buttonBar) {
		final Button button = new Button();
		button.setText(label);
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				buttonPressed(id);
			}
		});
		if (null != buttonBar) {
			buttonBar.add(button);
		}
		return button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rep.web.gwt.client.internal.ui.widgets.ContainerComposite#createPanel(org.eclipse.rep.web.gwt.client.rwt.ISerializedWidget,
	 *      org.eclipse.rep.web.gwt.client.ui.RenderingToolkit)
	 */
	@Override
	protected Panel createPanel(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		throw new IllegalStateException("not allowed");
	}

	/**
	 * The Finish button has been pressed.
	 */
	protected void finishPressed() {
		triggerCommand(getSWizardContainer().finishCommand);
	}

	private void firePageChangedEvent() {
		final CWTWizardPage wizardPage = getCurrentPage();
		if (null != wizardPage) {
			final PageChangeListener[] changeListeners = null != pageChangeListeners ? pageChangeListeners.toArray(new PageChangeListener[0]) : null;
			if ((null != changeListeners) && (changeListeners.length > 0)) {
				for (final PageChangeListener pageChangeListener : changeListeners) {
					pageChangeListener.pageChanged(wizardPage);
				}
			}
		}
	}

	public CWTWizardPage getCurrentPage() {
		final DeckPanel pagesPanel = getPagesPanel();
		if (null == pagesPanel) {
			return null;
		}

		final CWTWizardPage page = (CWTWizardPage) pagesPanel.getWidget(pagesPanel.getVisibleWidget());
		return page;
	}

	private DeckPanel getPagesPanel() {
		return (DeckPanel) getPanel();
	}

	private SWizardContainer getSWizardContainer() {
		return (SWizardContainer) getSerializedWidget();
	}

	/**
	 * The Help button has been pressed.
	 */
	protected void helpPressed() {
		// TODO Auto-generated method stub

	}

	/**
	 * The Next button has been pressed.
	 */
	protected void nextPressed() {
		final int nextPage = pagesPanel.getVisibleWidget() + 1;
		if (nextPage >= pagesPanel.getWidgetCount()) {
			// should not happen because the next button would be disabled
			return;
		}
		showPage(nextPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rep.web.gwt.client.internal.ui.widgets.ContainerComposite#populateChildren(org.eclipse.rep.web.gwt.client.rwt.ISerializedWidget,
	 *      org.eclipse.rep.web.gwt.client.ui.RenderingToolkit)
	 */
	@Override
	protected void populateChildren(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		super.populateChildren(serializedWidget, toolkit);
		showPage(0);
	}

	public void removePageChangeListener(final PageChangeListener listener) {
		if (null == pageChangeListeners) {
			return;
		}
		pageChangeListeners.remove(listener);
		if (pageChangeListeners.isEmpty()) {
			pageChangeListeners = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rep.web.gwt.client.internal.ui.widgets.ContainerComposite#render(org.eclipse.rep.web.gwt.client.rwt.ISerializedWidget,
	 *      org.eclipse.rep.web.gwt.client.ui.RenderingToolkit)
	 */
	@Override
	protected Widget render(final ISerializedWidget serializedWidget, final CWTToolkit toolkit) {
		final SWizardContainer sWizardContainer = (SWizardContainer) serializedWidget;

		headerPanel = new FlowPanel();
		headerPanel.setStyleName("cwt-WizardContainer-Header");
		pagesPanel = new DeckPanel();
		pagesPanel.setStyleName("cwt-WizardContainer-Pages");
		buttonsPanel = new FlowPanel();
		buttonsPanel.setStyleName("cwt-WizardContainer-Buttons");

		if (renderPageTitleAndDescription) {
			pageTitle = new PageTitle();
			pageDescription = new PageDescription();
		}

		if (null != pageTitle) {
			headerPanel.add(pageTitle);
		}
		if (null != pageDescription) {
			headerPanel.add(pageDescription);
		}

		if ((null != sWizardContainer.widgets) && (sWizardContainer.widgets.length > 1)) {
			backButton = createButton(BUTTON_ID_BACK, "< Back", buttonsPanel);
			nextButton = createButton(BUTTON_ID_NEXT, "Next >", buttonsPanel);
		}

		finishButton = createButton(BUTTON_ID_FINISH, "Finish", buttonsPanel);
		cancelButton = createButton(BUTTON_ID_CANCEL, "Cancel", buttonsPanel);

		final DockPanel outer = new DockPanel();
		outer.setStyleName("cwt-WizardContainer");
		outer.add(headerPanel, DockPanel.NORTH);
		outer.add(pagesPanel, DockPanel.CENTER);
		outer.add(buttonsPanel, DockPanel.SOUTH);

		initPanel(pagesPanel);
		populateChildren(serializedWidget, toolkit);

		return outer;
	}

	private void setPageDescription(final String description) {
		if (null != pageDescription) {
			pageDescription.setText(description);
		}
	}

	private void setPageTitle(final String title) {
		if (null != pageTitle) {
			pageTitle.setText(title);
		}
	}

	/**
	 * Sets the renderPageTitleAndDescription.
	 * 
	 * @param renderPageTitleAndDescription
	 *            the renderPageTitleAndDescription to set
	 */
	public void setRenderPageTitleAndDescription(final boolean renderPageTitleAndDescription) {
		this.renderPageTitleAndDescription = renderPageTitleAndDescription;
		if (!renderPageTitleAndDescription) {
			if (null != pageTitle) {
				headerPanel.remove(pageTitle);
				pageTitle = null;
			}
			if (null != pageDescription) {
				headerPanel.remove(pageDescription);
				pageDescription = null;
			}
		}
	}

	private void showPage(final int pageIndex) {
		if ((pageIndex < 0) || (pageIndex >= pagesPanel.getWidgetCount())) {
			throw new IndexOutOfBoundsException("invalid page index");
		}
		pagesPanel.showWidget(pageIndex);

		// update title
		updatePageTitle();

		// update buttons
		updateButtons();

		// fire event
		firePageChangedEvent();
	}

	private void triggerCommand(final SCommand command) {
		if (null == command) {
			return;
		}

		// build the content set
		final String commandId = command.id;
		final SContentSet contentSet = ContentHelper.buildContentSet(command.contentSubmitRule, this);

		allButtonsDisabled = true;
		updateButtons();
		//button.setText("Please wait...");
		getToolkit().getWidgetFactory().executeCommand(commandId, getWidgetId(), contentSet, new ExecuteCommandCallback() {

			public void onFailure(final WidgetFactoryException caught) {
				allButtonsDisabled = false;
				updateButtons();
			}

			public void onSuccess(final Object result) {
				allButtonsDisabled = false;
				updateButtons();
			}

		});
	}

	/**
	 * Adjusts the enable state of the Back, Next, and Finish buttons to reflect
	 * the state of the currently active page in this container.
	 * <p>
	 * This method is called by the container itself when its wizard page
	 * changes and may be called by the page at other times to force a button
	 * state update.
	 * </p>
	 */
	void updateButtons() {
		final DeckPanel pagesPanel = getPagesPanel();
		if (null == pagesPanel) {
			return;
		}

		if (allButtonsDisabled) {
			if (null != backButton) {
				backButton.setEnabled(false);
			}
			if (null != nextButton) {
				nextButton.setEnabled(false);
			}
			if (null != finishButton) {
				finishButton.setEnabled(false);
			}
			if (null != cancelButton) {
				cancelButton.setEnabled(false);
			}
			return;
		}

		final int pages = pagesPanel.getWidgetCount();
		final int currentPageIndex = pagesPanel.getVisibleWidget();
		final CWTWizardPage currentPage = (CWTWizardPage) pagesPanel.getWidget(currentPageIndex);

		final boolean canFlipToNextPage = ((currentPageIndex + 1) < pages) && currentPage.isPageComplete();

		if (null != backButton) {
			backButton.setEnabled(currentPageIndex > 0);
		}

		if (null != nextButton) {
			nextButton.setEnabled(canFlipToNextPage);
		}

		if (null != finishButton) {
			finishButton.setEnabled(canFinish());
		}

		if (null != cancelButton) {
			cancelButton.setEnabled(true);
		}
	}

	void updatePageTitle() {
		final CWTWizardPage page = getCurrentPage();
		if (null != page) {
			final SWizardPage sWizardPage = page.getSWizardPage();
			setPageTitle(null != sWizardPage.title ? sWizardPage.title : sWizardPage.id);
			setPageDescription(null != sWizardPage.description ? sWizardPage.description : "");
		}
	}

}
