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
package org.eclipse.cloudfree.toolkit.examples.simple;


import org.eclipse.cloudfree.toolkit.CWT;
import org.eclipse.cloudfree.toolkit.commands.Command;
import org.eclipse.cloudfree.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.cloudfree.toolkit.runtime.lookup.IWidgetFactory;
import org.eclipse.cloudfree.toolkit.widgets.Button;
import org.eclipse.cloudfree.toolkit.widgets.Container;
import org.eclipse.cloudfree.toolkit.widgets.DialogFieldRules;
import org.eclipse.cloudfree.toolkit.widgets.StyledText;
import org.eclipse.cloudfree.toolkit.widgets.TextInput;
import org.eclipse.cloudfree.toolkit.widgets.Widget;

public class Simple1WidgetFactory implements IWidgetFactory {

	private static Widget createSimple1Container(final String id) {
		final Container container = new Container(id, CWT.NONE);

		final StyledText styledText = new StyledText("greeting", container, CWT.NONE);
		styledText.setText("Hello World 1 - Key Generation!", false, false);

		final TextInput name = new TextInput(Simple1Constants.ID_NAME, container, CWT.NONE);
		name.setLabel("Name:");
		name.setToolTipText("Please enter your name.");

		final TextInput emailAddress = new TextInput(Simple1Constants.ID_EMAIL, container, CWT.REQUIRED);
		emailAddress.setLabel("Email Address:");
		emailAddress.setToolTipText("Please enter your email address. This is a required field.");

		final TextInput textInput4 = new TextInput(Simple1Constants.ID_KEY, container, CWT.READ_ONLY);
		textInput4.setLabel("Generated Key:");
		textInput4.setToolTipText("You can't modify this one.");

		final Button findButton = new Button("find", container, CWT.NONE);
		findButton.setLabel("Find");
		findButton.setToolTipText("Click to find a previously generated key.");
		findButton.setCommand(new Command("hello.world.find", DialogFieldRules.fields(name, emailAddress).submit()));
		findButton.setEnablementRule(DialogFieldRules.anyOf(name, emailAddress).isSet());

		final Button generateButton = new Button("generate", container, CWT.NONE);
		generateButton.setLabel("Generate");
		generateButton.setToolTipText("Click to generate a new key.");
		generateButton.setCommand(new Command(Simple1Constants.CMD_GENERATE, DialogFieldRules.fields(name, emailAddress).submit()));
		generateButton.setEnablementRule(DialogFieldRules.allFields().inScope(container).areValid());

		return container;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.runtime.lookup.IWidgetFactory#getWidget(java.lang.String, org.eclipse.cloudfree.toolkit.runtime.lookup.IWidgetEnvironment)
	 */
	public Widget getWidget(final String id, final IWidgetEnvironment environment) {

		// usually, you would perform a lookup based on the id here
		// and return the appropriate widget or null

		// in the future, CloudFree will assist you with the lookup 
		// by integrating with the extension registry and/or OSGi

		// we don't care about the id in this simple example
		return createSimple1Container(id);
	}

}
