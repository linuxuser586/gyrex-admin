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
package org.eclipse.gyrex.toolkit.widgets;

import org.eclipse.gyrex.toolkit.Toolkit;
import org.eclipse.gyrex.toolkit.content.ContentObject;

/**
 * A dialog field is a UI element for querying user input in a common way.
 * <p>
 * It limits the possibilities of building UIs by combining common set of
 * widgets for the benefit of a common look and an easy to built UI.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * Toolkit implementation.
 * </p>
 * 
 * @param <T>
 *            the content used by the dialog field
 * @see Toolkit#READ_ONLY
 * @see Toolkit#REQUIRED
 */
public abstract class DialogField<T extends ContentObject> extends Widget {

	/** serialVersionUID */
	private static final long serialVersionUID = -6990542404317436623L;

	private final Class<T> contentType;
	private String label, description;
	private DialogFieldRule enablementRule;

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 *            the widget id
	 * @param parent
	 *            the widget parent
	 * @param style
	 *            the widget style
	 * @param contentType
	 *            the content type
	 * @see Toolkit#READ_ONLY
	 * @see Toolkit#REQUIRED
	 */
	public DialogField(final String id, final Container parent, final int style, final Class<T> contentType) {
		super(id, parent, style);
		if (null == contentType) {
			Toolkit.error(Toolkit.ERROR_NULL_ARGUMENT, "content type must not be null");
		}
		this.contentType = contentType;
	}

	/**
	 * Returns the type of this dialog fields content.
	 * 
	 * @return the content type
	 */
	public Class<T> getContentType() {
		return contentType;
	}

	/**
	 * Returns the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the enablement rule.
	 * 
	 * @return the enablement rule
	 * @see #setEnablementRule(DialogFieldRule)
	 */
	public DialogFieldRule getEnablementRule() {
		return enablementRule;
	}

	/**
	 * Returns the label.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	@Override
	protected String getNameText() {
		return getLabel();
	}

	/**
	 * Returns <code>true</code> if if the dialog field is read only,
	 * <code>false</code> otherwise.
	 * <p>
	 * A read only dialog field is not updatable.
	 * </p>
	 * 
	 * @return <code>true</code> if the dialog field is not updatable,
	 *         <code>false</code> otherwise
	 */
	public boolean isReadOnly() {
		return isStyleBitSet(Toolkit.READ_ONLY);
	}

	/**
	 * Returns <code>true</code> if if the dialog field is required,
	 * <code>false</code> otherwise.
	 * <p>
	 * Typically, a required dialog field is decorated in the user interface.
	 * </p>
	 * 
	 * @return <code>true</code> if the dialog field is not updatable,
	 *         <code>false</code> otherwise
	 */
	public boolean isRequired() {
		return isStyleBitSet(Toolkit.REQUIRED);
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Sets an enablement rule for this dialog field.
	 * <p>
	 * If an enablement rule is set it will be used to determine the enablement
	 * state for the dialog field overriding any manually set enablement state.
	 * <p>
	 * <p>
	 * The rule is evaluated at runtime.
	 * </p>
	 * 
	 * @param rule
	 *            the enablement rule to evaluate (use <code>null</code> to
	 *            unset)
	 */
	public void setEnablementRule(final DialogFieldRule rule) {
		enablementRule = rule;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label
	 *            the label to set
	 */
	public void setLabel(final String label) {
		this.label = label;
	}
}
