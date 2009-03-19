/*******************************************************************************
 * Copyright (c) 2009 AGETO Service GmbH and others.
 * All rights reserved.
 *  
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/

/**
 * This package and its sub packages host common classes which are ment 
 * to be shared across Gyrex and its applications. 
 */
package org.eclipse.gyrex.log;

/**
 * This package and its sub packages host the Gyrex log system.
 * <p>
 * Gyrex takes logging seriously. It strictly separates logging
 * from debugging/tracing. Typically, logging may not be limited to technical
 * message logging about the system which usually targets developers or system
 * administrators. Logging is also suitable for logging application logic
 * specific messages targeted at a difference audience (eg. application users).
 * </p>
 * <p>
 * The Gyrex log system is based on <a href="http://www.slf4j.org/"
 * target="_blank">SLF4J</a>. It provides a native SLF4J implementation so that
 * any client code uses SLF4J works without modifications with the Gyrex log
 * system. Additionally, SLF4J provides re-implementations of other common
 * logging APIs. This allows any 3rd party code which depends on one of those
 * implementations to be redirected to SLF4J as well.
 * </p>
 * 
 * @see <a href="http://www.slf4j.org/" target="_blank">SLF4J</a>
 */
class Test {
}
