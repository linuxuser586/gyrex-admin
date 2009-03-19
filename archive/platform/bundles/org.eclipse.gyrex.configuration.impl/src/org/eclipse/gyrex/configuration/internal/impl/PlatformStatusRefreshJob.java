/*******************************************************************************
 * Copyright (c) 2008 Gunnar Wagenknecht and others.
 * All rights reserved.
 *  
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.configuration.internal.impl;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gyrex.configuration.constraints.PlatformConfigurationConstraint;
import org.eclipse.gyrex.configuration.internal.ConfigurationActivator;
import org.eclipse.gyrex.configuration.internal.holders.PlatformStatusHolder;

/**
 * This job refreshes the configuration status of Gyrex.
 * <p>
 * It must always be scheduled through {@link #scheduleRefreshIfPermitted()}.
 * </p>
 */
public final class PlatformStatusRefreshJob extends Job {

	private static final AtomicBoolean inactive = new AtomicBoolean(false);

	/**
	 * Scheduling rule used for the initialization job.
	 */
	private static final ISchedulingRule schedulingRule = new ISchedulingRule() {
		public boolean contains(final ISchedulingRule rule) {
			return rule == this;
		}

		public boolean isConflicting(final ISchedulingRule rule) {
			return rule == this;
		}
	};

	/** indicates if the platform status is initializing */
	private static final Semaphore refreshSemaphore = new Semaphore(1);

	static void activate() {
		inactive.set(false);
	}

	/**
	 * Disables any refresh activity.
	 */
	/*package*/static void disable() {
		inactive.set(true);
	}

	/**
	 * Schedules a refresh if no refresh is already scheduled.
	 */
	public static void scheduleRefreshIfPermitted() {
		// skip any activity if inactive
		if (inactive.get()) {
			return;
		}

		// don't initialize concurrently
		if (!refreshSemaphore.tryAcquire()) {
			return;
		}

		// start the initialization job with some delay
		new PlatformStatusRefreshJob().schedule(500);
	}

	/**
	 * Waits for a scheduled refresh to finish.
	 * 
	 * @param timeout
	 *            the maximum time to wait
	 * @param unit
	 *            the time unit of the {@code timeout} argument
	 * @return {@code true} if a scheduled refresh finished and {@code false} if
	 *         the waiting time elapsed before a scheduled refresh finished
	 * @throws InterruptedException
	 *             if the current thread is interrupted
	 */
	public static boolean waitForScheduledRefresh(final long timeout, final TimeUnit unit) throws InterruptedException {
		// skip any activity if inactive
		if (inactive.get()) {
			return false;
		}

		if (refreshSemaphore.tryAcquire(timeout, unit)) {
			refreshSemaphore.release();
			return true;
		}
		return false;
	}

	/**
	 * Private constructor.
	 */
	private PlatformStatusRefreshJob() {
		super("Platform Status Initialization Job");
		setPriority(LONG);
		setSystem(true);
		setRule(schedulingRule);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(final IProgressMonitor monitor) {
		// wrap into try/finally to guarantee release of the semaphore
		try {
			if (inactive.get()) {
				return Status.CANCEL_STATUS;
			}

			// get checks
			final PlatformConfigurationConstraint[] platformChecks = ConfigImplActivator.getInstance().getPlatformConfigurationConstraint();

			// return ok if we have no further checks
			if (platformChecks.length == 0) {
				PlatformStatusHolder.setCurrentPlatformStatus(Status.OK_STATUS);
				return Status.OK_STATUS;
			}

			// evaluate all constraints
			monitor.beginTask("Analyzing platform status", platformChecks.length);
			final MultiStatus platformStatus = new MultiStatus(ConfigurationActivator.PLUGIN_ID, IStatus.OK, null, null);
			for (final PlatformConfigurationConstraint check : platformChecks) {

				// check if canceled
				if (inactive.get()) {
					return Status.CANCEL_STATUS;
				}

				// evaluate constraint
				final IStatus status = check.evaluateConfiguration(new SubProgressMonitor(monitor, 1));
				if (null != status) {
					platformStatus.add(status);
				}
			}

			// update platform status
			if (!inactive.get()) {
				PlatformStatusHolder.setCurrentPlatformStatus(platformStatus);
			} else {
				return Status.CANCEL_STATUS;
			}
		} catch (final IllegalStateException e) {
			// stopped
			return Status.CANCEL_STATUS;
		} finally {
			// release semaphore
			refreshSemaphore.release();

			// done
			monitor.done();
		}

		// ok
		return Status.OK_STATUS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.Job#shouldRun()
	 */
	@Override
	public boolean shouldRun() {
		// check if inactive
		if (inactive.get()) {
			// at this point, the semaphore was already acquired 
			refreshSemaphore.release();
			// don't run
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.Job#shouldSchedule()
	 */
	@Override
	public boolean shouldSchedule() {
		// same as should run
		return shouldRun();
	}

}
