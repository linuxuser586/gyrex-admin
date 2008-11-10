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
package org.eclipse.cloudfree.toolkit.resources;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * An image resource.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ImageResource extends Resource {

	/** serialVersionUID */
	private static final long serialVersionUID = -3154486982274336338L;

	/**
	 * Creates a new image resource from the specified URL.
	 * <p>
	 * This is a convenience method which will parse the specified string into
	 * an {@link URL} object by invoking <code>new URL(url)</code>. A possibly
	 * malformed URL will be catched and a default missing image resource
	 * returned.
	 * </p>
	 * 
	 * @param url
	 *            the url to the image
	 * @return the image resource (a default missing image resource will be
	 *         returned if the specified url was <code>null</code> or malformed)
	 * @see URL#URL(String)
	 */
	public static ImageResource createFromUrl(final String url) {
		if (url == null) {
			return getMissingImageResource();
		}
		try {
			return new ImageResource(new URL(url));
		} catch (final MalformedURLException e) {
			return getMissingImageResource();
		}
	}

	/**
	 * Creates a new image resource from the specified URL.
	 * 
	 * @param url
	 *            the url to the image
	 * @return the image resource (a default missing image resource will be
	 *         returned if the specified url was <code>null</code>)
	 */
	public static ImageResource createFromUrl(final URL url) {
		if (null == url) {
			return getMissingImageResource();
		}
		return new ImageResource(url);
	}

	/** the image url */
	private final URL url;

	private static ImageResource missingImageResource;

	private static ImageResource getMissingImageResource() {
		if (null == missingImageResource) {
			missingImageResource = new ImageResource(ImageResource.class.getResource("missing.png"));
		}
		return missingImageResource;
	}

	/**
	 * Creates a new image resource using the specified URL.
	 * 
	 * @param url
	 *            the url to the image
	 * @see #getUrl()
	 */
	ImageResource(final URL url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.toolkit.resources.Resource#getUrl()
	 */
	@Override
	public URL getUrl() {
		return url;
	}

}
