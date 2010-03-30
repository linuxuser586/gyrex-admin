/**
 * Copyright (c) 2009 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package org.eclipse.gyrex.examples.bugsearch.internal;

import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.context.preferences.IRuntimeContextPreferences;
import org.eclipse.gyrex.context.preferences.PreferencesUtil;
import org.eclipse.gyrex.context.registry.IRuntimeContextRegistry;
import org.eclipse.gyrex.examples.bugsearch.internal.indexing.BugSearchIndexJob;
import org.eclipse.gyrex.examples.bugsearch.internal.indexing.DocumentsPublisher;
import org.eclipse.gyrex.examples.bugsearch.internal.indexing.OptimizeIndexJob;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import org.osgi.service.component.ComponentContext;

import org.apache.commons.lang.math.NumberUtils;

/**
 * Console commands
 */
@SuppressWarnings("restriction")
public class BugSearchCommandComponent implements CommandProvider {

	private static String getJobStateAsString(final int state) {
		switch (state) {
			case Job.RUNNING:
				return "RUNNING";
			case Job.WAITING:
				return "WAITING";
			case Job.SLEEPING:
				return "SLEEPING";
			case Job.NONE:
				return "NONE";
			default:
				return "(unknown)";
		}
	}

	private IRuntimeContextRegistry contextRegistry;

	public void _bsCancelImport(final CommandInterpreter ci) {
		BugzillaUpdateScheduler.cancelImportJob();
		ci.println("Canceld update job.");
	}

	public void _bsConcurrency(final CommandInterpreter ci) {
		// get (optional) fetch length
		final int concurrencyLevel = NumberUtils.toInt(ci.nextArgument(), 8);
		BugSearchIndexJob.PARALLEL_THREADS = concurrencyLevel;
		ci.println("Concurrency set to " + concurrencyLevel + " threads for next scheduled indexing job.");
	}

	public void _bsIndex(final CommandInterpreter ci) {
		final IRuntimeContext eclipseBugSearchContext = contextRegistry.get(IEclipseBugSearchConstants.CONTEXT_PATH);
		if (null == eclipseBugSearchContext) {
			ci.println("Eclipse bug search context not found!");
			return;
		}

		// get bug number
		final int startId = NumberUtils.toInt(ci.nextArgument());
		if (startId == 0) {
			ci.println("Bug number must be greater 0!");
			return;
		}

		// get (optional) fetch length
		final int length = NumberUtils.toInt(ci.nextArgument(), 1);

		// schedule indexing
		new BugSearchIndexJob("bug indexing", eclipseBugSearchContext) {
			@Override
			protected void doIndex(final IProgressMonitor monitor, final TaskRepository repository, final BugzillaRepositoryConnector connector, final DocumentsPublisher publisher) {
				queryForBugsRange(monitor, repository, connector, publisher, startId, length);
			}
		}.schedule(5000);
		ci.println("Scheduled indexing bug " + startId + ".");
	}

	public void _bsOptimize(final CommandInterpreter ci) {
		final IRuntimeContext eclipseBugSearchContext = contextRegistry.get(IEclipseBugSearchConstants.CONTEXT_PATH);
		if (null == eclipseBugSearchContext) {
			ci.println("Eclipse bug search context not found!");
			return;
		}

		// schedule optimization
		new OptimizeIndexJob(eclipseBugSearchContext).schedule(5000);
		ci.println("Scheduled index optimization.");
	}

	public void _bsReindex(final CommandInterpreter ci) {
		final IRuntimeContext eclipseBugSearchContext = contextRegistry.get(IEclipseBugSearchConstants.CONTEXT_PATH);
		if (null == eclipseBugSearchContext) {
			ci.println("Eclipse bug search context not found!");
			return;
		}

		// reset the index counter if we created a new index
		final IRuntimeContextPreferences preferences = PreferencesUtil.getPreferences(eclipseBugSearchContext);
		preferences.remove(BugSearchActivator.PLUGIN_ID, "import.start");
		try {
			preferences.flush(BugSearchActivator.PLUGIN_ID);
		} catch (final Exception e) {
			ci.println("Error while flushing preferences after resetting the index counter: " + e);
			// but continue
		}

		// re-schedule initial indexing
		BugzillaUpdateScheduler.rescheduleInitialImportFollowedByUpdate();
		ci.println("Rescheduled indexing.");
	}

	public void _bsStatus(final CommandInterpreter ci) {
		final Job[] jobs = Job.getJobManager().find(BugSearchIndexJob.FAMILY);
		if ((null == jobs) || (jobs.length == 0)) {
			ci.println("No jobs scheduled.");
			return;
		}

		for (final Job job : jobs) {
			ci.println(job + " " + getJobStateAsString(job.getState()));
		}
	}

	protected void activate(final ComponentContext context) {
		contextRegistry = (IRuntimeContextRegistry) context.locateService("IRuntimeContextRegistry");

	}

	protected void deactivate(final ComponentContext context) {
		// release references
		contextRegistry = null;
	}

	@Override
	public String getHelp() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append("---BugSearch---\n");
		buffer.append("\tbsIndex <bug id> - indexes a specific bug\n");
		buffer.append("\tbsOptimize - kicks off indexing optimization\n");
		buffer.append("\tbsReindex  - kicks off re-indexing of the whole bugs index\n");
		buffer.append("\tbsCancelImport\n");
		buffer.append("\tbsConcurrency\n");
		buffer.append("\tbsStatus\n");
		return buffer.toString();
	}

}
