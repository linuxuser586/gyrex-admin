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
package org.slf4j.impl;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * The SLF4J logger factory implementation which delegates to the Gyrex log
 * system.
 */
class GyrexSlf4jLoggerFactory implements ILoggerFactory {

	/**
	 * A job that cleans up unused logger references (due to objects and class
	 * objects
	 */
	private final class CleanupJob extends Job {

		/**
		 * Creates a new instance.
		 */
		public CleanupJob() {
			super("SLF4J Logger References Clean-Up");
			setSystem(true);
			setRule(new MutexRule(GyrexSlf4jLoggerFactory.this));
			setPriority(SHORT);
		}

		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			final String[] names = loggersByName.keySet().toArray(new String[0]);
			final SubMonitor subMonitor = SubMonitor.convert(monitor, "Remove stale references", names.length);
			try {
				// note, we only look at the loggers, we never touch the locks!!!
				for (final String name : names) {
					// get a weak reference
					final WeakReference<GyrexSlf4jLogger> weakReference = loggersByName.get(name);
					// test if the weak reference is stale
					if ((null != weakReference) && (null == weakReference.get())) {
						// only clear the mapping for a reference if it is still mapped to the stale one!
						loggersByName.remove(name, weakReference);
					}
					subMonitor.worked(1);
				}
			} finally {
				if (null != monitor) {
					monitor.done();
				}
			}
			return Status.OK_STATUS;
		}
	}

	private static class MutexRule implements ISchedulingRule {

		private final Object object;

		public MutexRule(final Object object) {
			this.object = object;
		}

		public boolean contains(final ISchedulingRule rule) {
			return rule == this;
		}

		public boolean isConflicting(final ISchedulingRule rule) {
			if (rule instanceof MutexRule) {
				return object.equals(((MutexRule) rule).object);
			}
			return false;
		}
	}

	private static final String ROOT = "";

	private static final long CLEANUP_TIMEOUT = 120000; // 120 seconds
	final ConcurrentMap<String, WeakReference<GyrexSlf4jLogger>> loggersByName = new ConcurrentHashMap<String, WeakReference<GyrexSlf4jLogger>>();
	private final ConcurrentMap<String, Lock> loggerCreationLocks = new ConcurrentHashMap<String, Lock>();
	private final AtomicLong lastCleanup = new AtomicLong();
	private final CleanupJob cleanupJob = new CleanupJob();

	@Override
	public Logger getLogger(String name) {
		// check name
		if (null == name) {
			name = ROOT;
		}

		// lookup logger
		GyrexSlf4jLogger logger = getLoggerByName(name);
		if (null != logger) {
			return logger;
		}

		// from time to time it might be worth scheduling a cleanup
		final long lastCleanupTime = lastCleanup.get();
		final long currentTime = System.currentTimeMillis();
		if ((currentTime - lastCleanupTime > CLEANUP_TIMEOUT) && lastCleanup.compareAndSet(lastCleanupTime, currentTime)) {
			// only schedule when the job is not already operating
			if (cleanupJob.getState() == Job.NONE) {
				cleanupJob.schedule(200);
			}
		}

		final Lock lock = getLoggerCreationLock(name);
		lock.lock();
		try {
			// lookup logger again
			logger = getLoggerByName(name);
			if (null != logger) {
				return logger;
			}

			// create logger
			logger = new GyrexSlf4jLogger(name);

			// put logger
			loggersByName.put(name.intern(), new WeakReference<GyrexSlf4jLogger>(logger));

			// return
			return logger;
		} finally {
			lock.unlock();
		}
	}

	private GyrexSlf4jLogger getLoggerByName(final String name) {
		final WeakReference<GyrexSlf4jLogger> loggerReference = loggersByName.get(name);
		return null != loggerReference ? loggerReference.get() : null;
	}

	private Lock getLoggerCreationLock(final String name) {
		Lock lock = loggerCreationLocks.get(name);
		if (null == lock) {
			loggerCreationLocks.putIfAbsent(name.intern(), new ReentrantLock());
			lock = loggerCreationLocks.get(name);
		}
		return lock;
	}
}
