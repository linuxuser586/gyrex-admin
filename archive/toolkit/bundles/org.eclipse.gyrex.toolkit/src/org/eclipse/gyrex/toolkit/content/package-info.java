/*******************************************************************************
 * Copyright (c) 2008, 2010 Gunnar Wagenknecht and others.
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
 * Widget content handling API.
 * <p>
 * In order to transfer data to and from widgets API defined in this package is
 * used. Data is represented in a hierarchy of
 * {@link org.eclipse.gyrex.toolkit.content.ContentObject content objects}. Some
 * content objects might be shared between widgets. Custom widgets may provide
 * their own implementation.
 * </p>
 * <p>
 * Note, this is experimental API. It will not be stable until version 1.0 is
 * officially released.
 * </p>
 */
package org.eclipse.gyrex.toolkit.content;

