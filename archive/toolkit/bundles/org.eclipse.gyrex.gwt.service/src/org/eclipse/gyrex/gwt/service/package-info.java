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
 * CloudFree GWT Service.
 * <p>
 * The {@link GwtService CloudFree GWT Service} is an attempt to simplify working with GWT modules
 * in the OSGi world. It provides API to define GWT modules and their associated 
 * GWT RPC service implementations. The GWT Service in turn uses the OSGi HTTP 
 * Service to register the resources. It aims to reduce complexity.
 * </p>
 */
package org.eclipse.cloudfree.gwt.service;

