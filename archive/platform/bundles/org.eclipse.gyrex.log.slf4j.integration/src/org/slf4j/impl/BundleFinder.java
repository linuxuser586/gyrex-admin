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
package org.slf4j.impl;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * Finds the calling bundle
 */
class BundleFinder implements PrivilegedAction<Bundle> {

	static final class Finder extends SecurityManager {
		@Override
		public Class[] getClassContext() {
			return super.getClassContext();
		}
	}

	static final Finder contextFinder = AccessController.doPrivileged(new PrivilegedAction<Finder>() {
		public Finder run() {
			return new Finder();
		}
	});

	static Bundle findCallingBundle(final String stopAfterFqcn) {
		return new BundleFinder(stopAfterFqcn).getCallingBundle();
	}

	private final String stopAfterFqcn;

	/**
	 * Hidden instance.
	 */
	private BundleFinder(final String stopAfterFqcn) {
		this.stopAfterFqcn = stopAfterFqcn;
	}

	public Bundle getCallingBundle() {
		if (System.getSecurityManager() == null) {
			return internalFindCallingBundle();
		}
		return AccessController.doPrivileged(this);
	}

	private Bundle internalFindCallingBundle() {
		final Class[] stack = contextFinder.getClassContext();
		for (int i = 0; i < stack.length; i++) {
			final String className = stack[i].getName();
			if (className.equals(stopAfterFqcn)) {
				// look ahead
				if ((i + 1 < stack.length) && !stopAfterFqcn.equals(stack[i + 1].getName())) {
					return FrameworkUtil.getBundle(stack[i + 1]);
				}
			}
		}
		// nothing found
		return null;
	}

	@Override
	public Bundle run() {
		return internalFindCallingBundle();
	}

}
