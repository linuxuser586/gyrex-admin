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
package org.eclipse.cloudfree.toolkit.widgets;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.cloudfree.toolkit.CWT;
import org.eclipse.cloudfree.toolkit.CWTException;
import org.eclipse.cloudfree.toolkit.layout.LayoutHint;

/**
 * A widget is the base class for all UI elements.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * CWT implementation.
 * </p>
 */
public abstract class Widget implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -8014326890115740560L;

	/** constant for no layout hints */
	private static final LayoutHint[] NO_HINTS = new LayoutHint[0];

	/* package name */
	private static final Set<String> ALLOWED_PACKAGE_PREFIXS;
	static {
		final Set<String> allowedPackagePrefixes = new HashSet<String>(2);
		allowedPackagePrefixes.add("org.eclipse.cloudfree.toolkit.widgets."); //$NON-NLS-1$
		allowedPackagePrefixes.add("org.eclipse.cloudfree.toolkit.wizard."); //$NON-NLS-1$
		ALLOWED_PACKAGE_PREFIXS = Collections.unmodifiableSet(allowedPackagePrefixes);
	}

	/**
	 * Returns a style with exactly one style bit set out of the specified set
	 * of exclusive style bits. All other possible bits are cleared when the
	 * first matching bit is found. Bits that are not part of the possible set
	 * are untouched.
	 * 
	 * @param style
	 *            the original style bits
	 * @param int0
	 *            the 0th possible style bit
	 * @param int1
	 *            the 1st possible style bit
	 * @param int2
	 *            the 2nd possible style bit
	 * @param int3
	 *            the 3rd possible style bit
	 * @param int4
	 *            the 4th possible style bit
	 * @param int5
	 *            the 5th possible style bit
	 * @return the new style bits
	 */
	static int checkBits(int style, final int int0, final int int1, final int int2, final int int3, final int int4, final int int5) {
		final int mask = int0 | int1 | int2 | int3 | int4 | int5;
		if ((style & mask) == 0) {
			style |= int0;
		}
		if ((style & int0) != 0) {
			style = (style & ~mask) | int0;
		}
		if ((style & int1) != 0) {
			style = (style & ~mask) | int1;
		}
		if ((style & int2) != 0) {
			style = (style & ~mask) | int2;
		}
		if ((style & int3) != 0) {
			style = (style & ~mask) | int3;
		}
		if ((style & int4) != 0) {
			style = (style & ~mask) | int4;
		}
		if ((style & int5) != 0) {
			style = (style & ~mask) | int5;
		}
		return style;
	}

	/**
	 * Returns <code>true</code> when subclassing is allowed and
	 * <code>false</code> otherwise
	 * 
	 * @return <code>true</code> when subclassing is allowed and
	 *         <code>false</code> otherwise
	 */
	static boolean isValidSubclass(final Class clazz) {
		final String name = clazz.getName();
		final int index = name.lastIndexOf('.');
		final String clazzPackagePrefix = name.substring(0, index + 1);
		return ALLOWED_PACKAGE_PREFIXS.contains(clazzPackagePrefix);
	}

	private final String id;
	private final Container parent;
	private String toolTipText;
	private LayoutHint[] layoutHints = NO_HINTS;
	private DialogFieldRule visibilityRule;
	private final int style;

	//	private boolean enabled;
	// TODO: private DialogFieldRule enablementRule;

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 *            the widget id
	 * @param parent
	 *            the widget parent
	 * @param style
	 *            the widget style
	 */
	public Widget(final String id, final Container parent, final int style) {
		checkSubclass();
		checkParent(parent);

		this.id = id;
		this.style = style;
		this.parent = parent;
		addWidgetToParent();
	}

	/**
	 * Adds a layout hint.
	 * 
	 * @param layoutHint
	 *            the layout hint to add
	 */
	public void addLayoutHint(final LayoutHint layoutHint) {
		if (null == layoutHint) {
			CWT.error(CWT.ERROR_INVALID_ARGUMENT, "layout hint must not be null");
		}

		final LayoutHint[] oldHints = layoutHints;
		final LayoutHint[] newHints = new LayoutHint[oldHints.length + 1];
		System.arraycopy(oldHints, 0, newHints, 0, oldHints.length);
		newHints[newHints.length - 1] = layoutHint;
		layoutHints = newHints;
	}

	/**
	 * Adds the widget to its parent.
	 */
	void addWidgetToParent() {
		checkParent(parent);
		parent.addWidget(this);
	}

	/**
	 * Throws an exception if the specified widget can not be used as a parent
	 * for the receiver.
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 */
	protected void checkParent(final Container parent) {
		if (parent == null) {
			CWT.error(CWT.ERROR_NULL_ARGUMENT, "parent");
		}
	}

	/**
	 * Checks that this class can be subclassed.
	 * <p>
	 * The REP Toolkit Widget class library is intended to be subclassed only at
	 * specific, controlled points (most notably, <code>Composite</code> when
	 * implementing new widgets). This method enforces this rule unless it is
	 * overridden.
	 * </p>
	 * <p>
	 * <em>IMPORTANT:</em> By providing an implementation of this method that
	 * allows a subclass of a class which does not normally allow subclassing to
	 * be created, the implementer agrees to be fully responsible for the fact
	 * that any such subclass will likely fail between CWT releases and will be
	 * strongly platform specific. No support is provided for user-written
	 * classes which are implemented in this fashion.
	 * </p>
	 * <p>
	 * The ability to subclass outside of the allowed CWT classes is intended
	 * purely to enable those not on the CWT development team to implement
	 * patches in order to get around specific limitations in advance of when
	 * those limitations can be addressed by the team. Subclassing should not be
	 * attempted without an intimate and detailed understanding of the
	 * hierarchy.
	 * </p>
	 * 
	 * @exception CWTException
	 *                <ul>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 */
	protected void checkSubclass() {
		if (!isValidSubclass(getClass())) {
			CWT.error(CWT.ERROR_INVALID_SUBCLASS, getClass().getName());
		}
	}

	/**
	 * Returns the id of this widget.
	 * 
	 * @return the widget id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the layout hints.
	 * 
	 * @return the layout hints
	 */
	public LayoutHint[] getLayoutHints() {
		return layoutHints;
	}

	/**
	 * Returns the name of the widget. This is the name of the class without the
	 * package name.
	 * 
	 * @return the name of the widget
	 */
	private String getName() {
		String string = getClass().getName();
		final int index = string.lastIndexOf('.');
		if (index != -1) {
			string = string.substring(index + 1, string.length());
		}
		return string;
	}

	/**
	 * Returns a short printable representation for the contents of a widget.
	 * For example, a button may answer the label text. This is used by
	 * <code>toString</code> to provide a more meaningful description of the
	 * widget.
	 * 
	 * @return the contents string for the widget
	 * @see #toString
	 */
	protected String getNameText() {
		return "";
	}

	/**
	 * Returns the parent container.
	 * 
	 * @return the parent container.
	 * @see org.eclipse.cloudfree.toolkit.widgets.Container#getWidgets
	 */
	public Container getParent() {
		return parent;
	}

	/**
	 * Returns the receiver's style information.
	 * 
	 * @return the style bits
	 */
	public int getStyle() {
		return style;
	}

	/**
	 * Returns the tool tip text.
	 * 
	 * @return the tool tip text
	 */
	public String getToolTipText() {
		return toolTipText;
	}

	//	/**
	//	 * Returns <code>true</code> if the receiver is enabled and all ancestors
	//	 * are enabled. Otherwise, <code>false</code> is returned.
	//	 * <p>
	//	 * A disabled widget is typically not selectable from the user interface and
	//	 * draws with an inactive or "grayed" look.
	//	 * </p>
	//	 * 
	//	 * @return the receiver's enabled state
	//	 */
	//	public boolean isEnabled() {
	//		final Container parent = this.parent;
	//		if (null != parent) {
	//			return enabled && parent.isEnabled();
	//		} else {
	//			return enabled;
	//		}
	//	}

	/**
	 * Returns the visibility rule.
	 * 
	 * @return the visibility rule (maybe <code>null</code>)
	 * @see #setVisibilityRule(DialogFieldRule)
	 */
	public DialogFieldRule getVisibilityRule() {
		return visibilityRule;
	}

	//	/**
	//	 * Enables the receiver if the argument is <code>true</code>, and disables
	//	 * it otherwise.
	//	 * <p>
	//	 * A disabled widget is typically not selectable from the user interface and
	//	 * draws with an inactive or "grayed" look.
	//	 * </p>
	//	 * 
	//	 * @param enabled
	//	 *            the new enabled state
	//	 */
	//	public void setEnabled(final boolean enabled) {
	//		this.enabled = enabled;
	//	}

	/**
	 * Indicates if a specific bit mask is set in the reciever's style bits.
	 * 
	 * @param mask
	 *            the bit mask to check
	 * @return <code>true</code> if the style bits contain the specified bit
	 *         mask
	 */
	public boolean isStyleBitSet(final int mask) {
		return ((getStyle() & mask) != 0);
	}

	/**
	 * Sets the layout hints overwriting all existing layout hints.
	 * 
	 * @param layoutHints
	 *            the layout hints to set (maybe <code>null</code> to unset)
	 */
	public void setLayoutHints(LayoutHint[] layoutHints) {
		if (null == layoutHints) {
			layoutHints = NO_HINTS;
		}

		this.layoutHints = layoutHints;
	}

	/**
	 * Sets the tool tip text.
	 * 
	 * @param toolTipText
	 *            the tool tip text (maybe <code>null</code> to unset)
	 */
	public void setToolTipText(final String toolTipText) {
		this.toolTipText = toolTipText;
	}

	/**
	 * Sets a visibility rule for this widget.
	 * <p>
	 * If a visibility rule is set it will be used to determine the visibility
	 * for the widget overriding any manually set visibility.
	 * <p>
	 * <p>
	 * The rule is evaluated at runtime.
	 * </p>
	 * 
	 * @param rule
	 *            the visibility rule to evaluate (use <code>null</code> to
	 *            unset)
	 */
	public void setVisibilityRule(final DialogFieldRule rule) {
		visibilityRule = rule;
	}

	/**
	 * Returns a string containing a concise, human-readable description of the
	 * widget.
	 * 
	 * @return a string representation of the widget
	 */
	@Override
	public String toString() {
		final StringBuilder toString = new StringBuilder();
		toString.append(getName());
		toString.append(" {");
		toString.append(getId());
		final String nameText = getNameText();
		if ((null != nameText) && (nameText.trim().length() > 0)) {
			toString.append(", ").append(nameText);
		}
		toString.append("}");
		return toString.toString();
	}
}
