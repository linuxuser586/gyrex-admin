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
 * Gyrex Widget Toolkit (Toolkit).
 * <p>
 * The Gyrex Widget Toolkit (Toolkit) is an attempt to define a
 * cross-technology toolkit. Its base concept is a widget model for defining
 * the user interface. This model is rendered by technology specific renders.
 * For example, one could generate an Eclipse Forms UI based user
 * interface out of a Toolkit model whereas another would generate HTML and
 * JavaScript.
 * </p>
 * <p>
 * The Toolkit is designed with a distributed runtime model in mind. Therefore,
 * certain parts are not strictly coupled through object references but by
 * an identifier. For example, once a widget was created and rendered in the
 * UI the rendering layer generally uses the widget identifier when calling
 * back into client code. Factories and registries are used to lookup the actual
 * objects for an identifier.
 * </p>
 * <p>
 * Due to limitations on various technologies only a very common subset of widgets
 * are supported. Toolkit is a higher level toolkit originally designed to allow the
 * single definition of UIs that can be used in different worlds (for example,
 * Eclipse RCP and web based front-ends). The idea is similar to
 * <a href="http://en.wikipedia.org/wiki/XUL">XUL</a>. However, Toolkit follows a
 * different approach in defining the UI in pure Java and goes beyond XUL by also
 * providing a cross-technology concept for data transport.
 * </p>
 */
package org.eclipse.gyrex.toolkit;

