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
package org.eclipse.gyrex.log.internal;

import org.eclipse.equinox.log.SynchronousLogListener;
import org.eclipse.gyrex.log.internal.firephp.FirePHPLogger;
import org.osgi.service.log.LogEntry;

/**
 *
 */
public class ConsoleLogger implements SynchronousLogListener {

	/* (non-Javadoc)
	 * @see org.osgi.service.log.LogListener#logged(org.osgi.service.log.LogEntry)
	 */
	@Override
	public void logged(final LogEntry entry) {
		//		final String symbolicName = entry.getBundle().getSymbolicName();
		//		// get the trace
		//		DebugTrace trace;
		//		try {
		//			final FrameworkDebugOptions frameworkDebugOptions = FrameworkDebugOptions.getDefault();
		//			final Field field = frameworkDebugOptions.getClass().getDeclaredField("debugTraceCache");
		//			if (!field.isAccessible()) {
		//				field.setAccessible(true);
		//			}
		//			trace = (DebugTrace) ((Map) field.get(frameworkDebugOptions)).get(symbolicName);
		//		} catch (final Exception e) {
		//			e.printStackTrace();
		//			trace = null;
		//		}
		//
		//		if (null != trace) {
		//			trace.trace(null, entry.getMessage());
		//		}
		System.out.println(FirePHPLogger.getLabel(entry));
	}

}
