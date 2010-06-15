/*******************************************************************************
 * Copyright (c) 2008, 2009 Gunnar Wagenknecht and others.
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
 * The Setup Wizards allows the initial configuration of a new system.
 * <p>
 * The Setup Wizard automatically detects if the platform is a new installation. 
 * If this is the case a wizard is shown in Gyrex Admin and the user is requested 
 * to go through the wizard and perform the initial system configuration.
 * </p>
 * <p>
 * Note, the Setup Wizard purely exists to make the life of developers and 
 * admins easier. It is not intended to be a tool that any client will ever face.
 * Thus, the interface is allowed to be <em>geeky</em>. Its use is also purely 
 * optional but encouraged.
 * </p> 
 */
package org.eclipse.gyrex.admin.configuration.wizard;

