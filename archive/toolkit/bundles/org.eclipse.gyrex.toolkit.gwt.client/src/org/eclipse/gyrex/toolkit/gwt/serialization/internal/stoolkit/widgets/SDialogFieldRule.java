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
package org.eclipse.gyrex.toolkit.gwt.serialization.internal.stoolkit.widgets;

import java.util.Arrays;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Serializable DialogFieldRule
 */
public class SDialogFieldRule implements IsSerializable {

	public static final int FST_SPECIFIED_FIELDS = 0;
	public static final int FST_ALL_FIELDS = 1;
	public static final int FST_REQUIRED_FIELDS = 2;
	public static final int FST_NO_FIELDS = 3;

	public static final int FC_IS_VALID = 0;
	public static final int FC_IS_SET = 1;
	public static final int FC_SUBMIT = 2;
	public static final int FC_NEVER = 3;

	private static String fieldCondition(final int fieldCondition) {
		switch (fieldCondition) {
			case FC_IS_VALID:
				return "IS_VALID";

			case FC_IS_SET:
				return "IS_SET";

			case FC_SUBMIT:
				return "SUBMIT";

			case FC_NEVER:
				return "NEVER";

			default:
				return "UNKNOWN";
		}
	}

	private static String fieldSelectionType(final int fieldSelectionType) {
		switch (fieldSelectionType) {
			case FST_ALL_FIELDS:
				return "ALL_FIELDS";

			case FST_REQUIRED_FIELDS:
				return "REQUIRED_FIELDS";

			case FST_SPECIFIED_FIELDS:
				return "SPECIFIED_FIELDS";

			case FST_NO_FIELDS:
				return "NO_FIELDS";

			default:
				return "UNKNOWN";
		}
	}

	public int fieldSelectionType;
	public String[] selectedFieldIds;

	public int fieldCondition;

	public String scopeContainerId;

	@Override
	public String toString() {
		switch (fieldSelectionType) {
			case FST_SPECIFIED_FIELDS:

				return fieldCondition(fieldCondition) + " {" + Arrays.asList(selectedFieldIds) + "}";

			default:
				return fieldCondition(fieldCondition) + " {" + fieldSelectionType(fieldSelectionType) + " in " + scopeContainerId + "}";
		}
	}

}
