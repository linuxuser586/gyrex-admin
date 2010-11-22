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
package org.eclipse.gyrex.admin.internal.configuration.wizard;

import org.eclipse.gyrex.admin.internal.AdminActivator;
import org.eclipse.gyrex.admin.internal.RunConfigWizardConfigConstraint;
import org.eclipse.gyrex.admin.setupwizard.SetupWizardStep;
import org.eclipse.gyrex.boot.internal.app.ServerApplication;
import org.eclipse.gyrex.toolkit.actions.RefreshAction;
import org.eclipse.gyrex.toolkit.actions.ShowWidgetAction;
import org.eclipse.gyrex.toolkit.runtime.commands.CommandExecutionEvent;
import org.eclipse.gyrex.toolkit.runtime.commands.CommandExecutionResult;
import org.eclipse.gyrex.toolkit.runtime.commands.ICommandHandler;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class ConfigurationWizardCommandHandler implements ICommandHandler {
	private final class RelaunchJob extends Job {
		private RelaunchJob() {
			super("Relaunch Platform");
			setSystem(true);
			setPriority(SHORT);
		}

		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			ServerApplication.signalRelaunch();
			return Status.OK_STATUS;
		}
	}

	public CommandExecutionResult execute(final CommandExecutionEvent executionEvent, final IProgressMonitor progressMonitor) {
		final String commandId = executionEvent.getCommandId();
		if (commandId.equals(ConfigurationWizardFactory.CMD_FINISH)) {
			return finish(executionEvent);
		} else if (commandId.equals(ConfigurationWizardFactory.CMD_RESTART)) {
			return restart(executionEvent);
		}
		return new CommandExecutionResult(executionEvent.getCommandId(), Status.CANCEL_STATUS);
	}

	/**
	 * @param executionEvent
	 * @return
	 */
	private CommandExecutionResult finish(final CommandExecutionEvent executionEvent) {
		final MultiStatus wizardStatus = new MultiStatus(AdminActivator.SYMBOLIC_NAME, 0, null, null);
		final SetupWizardStep[] steps = AdminActivator.getInstance().getConfigurationWizardService().getSteps();
		for (final SetupWizardStep step : steps) {
			final IStatus status = step.wizardFinished(executionEvent, null);
			if (status.isOK()) {
				RunConfigWizardConfigConstraint.addStepToExecutedList(step.getId());
			} else {
				RunConfigWizardConfigConstraint.removeStepFromExecutedList(step.getId());
			}
			wizardStatus.add(status);
		}

		if (!wizardStatus.isOK()) {
			return new CommandExecutionResult(executionEvent.getCommandId(), wizardStatus);
		}

		// register constraint to restart platform
		AdminActivator.getInstance().setShouldRestartServer(true);

		// show RESTART page
		final CommandExecutionResult result = new CommandExecutionResult(executionEvent.getCommandId(), Status.OK_STATUS, new ShowWidgetAction(ConfigurationWizardFactory.ID_CONFIGURATION_WIZARD_FINISHED));

		// TODO at the end we should schedule a shutdown of the platform

		return result;
	}

	private CommandExecutionResult restart(final CommandExecutionEvent executionEvent) {
		// schedule relaunch
		new RelaunchJob().schedule(500);

		// instruct the client to refresh the current view in 60 seconds ...
		return new CommandExecutionResult(executionEvent.getCommandId(), AdminActivator.getInstance().getStatusUtil().createWarning(0, "Shutting down..."), new RefreshAction(60000));
	}
}