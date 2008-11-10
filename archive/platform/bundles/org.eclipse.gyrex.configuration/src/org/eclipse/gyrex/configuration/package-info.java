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

/**
 * This package defines the API for configuring the CloudFree Platform and 
 * verifying the configuration state.
 * <p>
 * The CloudFree Platform allows for several ways of configuring it. It is
 * expected that some base configuration is necessary before the platform is in
 * a usable and reliable state.
 * </p>
 * <p>
 * The following possibilities exist:
 * <dl>
 * <dt>Debug Configuration</dt>
 * <dd>In this configuration a CloudFree Platform instance maintains its
 * own configuration. The mode is typically used on a development system.
 * Several settings may already set to reasonable defaults for a single
 * instance development system.</dd>
 * <dt>Production Configuration</dt>
 * <dd>In this configuration multiple CloudFree Platform instances share the
 * same configuration. Each instance may be used as a source for
 * obtaining and modifying configuration data. Configuration changes are
 * propagated to all instances. This mode is typically used in a production 
 * cluster backed by a reliable technology such as LDAP supporting <a
 * href="http://en.wikipedia.org/wiki/Multi-master_replication"
 * target="_blank">multi-master replication</a>. It may require more
 * initial configuration.</dd>
 * </dl>
 * </p>
 * <p>
 * The CloudFree Platform configuration API is based on the Eclipse Preferences API.
 * However, it adds a thin layer on top of it to provide a common and simplified way
 * of using it and for better integration with the CloudFree contextual runtime.
 * </p>
 * 
 */
package org.eclipse.cloudfree.configuration;

