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
package org.eclipse.gyrex.toolkit.gwt.server.internal;

import java.io.UnsupportedEncodingException;
import java.net.URL;


import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.eclipse.gyrex.toolkit.CWT;
import org.eclipse.gyrex.toolkit.resources.Resource;

/**
 * A registry to <em>hide</em> resource internals.
 * <p>
 * The Gyrex Toolkit defines resources as directly accessible by
 * {@link Resource#getUrl() an URL}. However, these URLs might be internals and
 * sensitive which should not be exposed. Therefore a resource registry exists
 * to create a mapping between a public exposable value and the actual resource
 * URL.
 * </p>
 * The default implementation provides no sophisticated security, it simply
 * encodes and decodes a resource URL using Base64.
 */
public class ResourceUrlEncoder {

	static String decode(final String reference) throws IllegalArgumentException {
		// parse hex encoded, base 64
		byte[] out = null;
		try {
			out = Base64.decodeBase64(Hex.decodeHex(reference.toCharArray()));
		} catch (final DecoderException e) {
			// fail
			CWT.error(CWT.ERROR_INVALID_ARGUMENT, e.getMessage());
		}

		// convert to String
		try {
			return new String(out, "UTF-8").intern();
		} catch (final UnsupportedEncodingException e) {
			// fallback to platform default
			return new String(out).intern();
		}
	}

	public static URL decodeResourceUrl(final String reference) throws IllegalArgumentException {
		try {
			return new URL(decode(reference));
		} catch (final Exception e) {
			CWT.error(CWT.ERROR_INVALID_ARGUMENT, e.getMessage());
			// should not reach this point
			throw new IllegalStateException();
		}
	}

	static String encode(final String url) {
		// convert to bytes
		byte[] in;
		try {
			in = url.getBytes("UTF-8");
		} catch (final UnsupportedEncodingException e1) {
			// fallback to platform default
			in = url.getBytes();
		}

		// base64, hex encoded
		return new String(Hex.encodeHex(Base64.encodeBase64(in))).intern();
	}

	public static String encodeResourceUrl(final Resource resource) {
		if ((resource == null) || (resource.getUrl() == null)) {
			return null;
		}
		return encode(resource.getUrl().toExternalForm());
	}
}
