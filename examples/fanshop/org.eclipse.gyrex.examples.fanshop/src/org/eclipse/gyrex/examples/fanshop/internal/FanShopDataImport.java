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
package org.eclipse.gyrex.examples.fanshop.internal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.gyrex.cds.documents.IDocument;
import org.eclipse.gyrex.cds.documents.IDocumentAttribute;
import org.eclipse.gyrex.cds.documents.IDocumentManager;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.model.common.ModelUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import org.osgi.framework.Bundle;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 */
public class FanShopDataImport extends Job {

	/** VARIABLE_PRODUCT */
	private static final String VARIABLE_PRODUCT = "variable-product";
	/** VARIATION */
	private static final String VARIATION = "variation";

	private static void appendStringField(final IDocument target, final IDocument source, final String name) {
		if (source.contains(name)) {
			final IDocumentAttribute<String> srcAttr = source.get(name).ofType(String.class);
			final IDocumentAttribute<String> targetAttr = target.getOrCreate(name).ofType(String.class);
			targetAttr.add(srcAttr.getValues());
		}
	}

	private static String toUriPath(String title) {
		title = title.toLowerCase();
		final int length = title.length();
		final StringBuilder uri = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			final char c = title.charAt(i);
			if (((c >= 'a') && (c <= 'z')) || ((c >= '0') && (c <= '9')) || (c == '-') || (c == '_') || (c == '.') || (c == '/')) {
				uri.append(c);
				continue;
			} else if (Character.isWhitespace(c)) {
				if ((uri.length() > 0) && (uri.charAt(uri.length() - 1) != '-')) {
					uri.append('-');
				}
				continue;
			} else {
				// leave out
				continue;
			}
		}
		return uri.toString();
	}

	private final IRuntimeContext context;

	/**
	 * Creates a new instance.
	 * 
	 * @param context
	 * @param name
	 */
	public FanShopDataImport(final IRuntimeContext context) {
		super("fan shop data import");
		this.context = context;
		setPriority(LONG);
	}

	private void fillMissingValuesNonVariations(final IDocument doc, final Map<String, IDocument> docs) {
		// check if we have a variation which inherits missing values
		if (VARIATION.equals(doc.getValue("type"))) {
			return;
		}

		// default name
		if (null == doc.getName()) {
			doc.setName(doc.getId());
		}
		// default title
		if (null == doc.getTitle()) {
			doc.setTitle(doc.getName());
		}
		// default uri path
		if (null == doc.getUriPath()) {
			doc.setUriPath(toUriPath(doc.getTitle()));
		}
	}

	private void fillMissingValuesVariations(final IDocument doc, final Map<String, IDocument> docs) {
		// check if we have a variation which inherits missing values
		if (VARIATION.equals(doc.getValue("type"))) {
			// get parent
			final Object parentId = doc.getValue("parentid");
			if (null == parentId) {
				throw new IllegalArgumentException("variation has no parent id: " + doc);
			}
			final IDocument parent = docs.get(parentId);
			if (null == parent) {
				throw new IllegalArgumentException("variation has no parent: " + doc);
			}
			// copy missing fields
			final Map<String, IDocumentAttribute> attributes = parent.getAttributes();
			for (final IDocumentAttribute attribute : attributes.values()) {
				final String name = attribute.getId();
				if (name.equals(IDocument.ATTRIBUTE_ID) || name.equals(IDocument.ATTRIBUTE_NAME) || name.equals(IDocument.ATTRIBUTE_URI_PATH) || name.equals("variationids")) {
					continue;
				}
				if (!doc.contains(name)) {
					doc.get(name).ofType(Object.class).set(attribute.getValue());
				}
			}
			// TODO: the uri of a variation should default to the parent + variation fields
			if (null == doc.getUriPath()) {
				String suffix = doc.getId();
				if (suffix.startsWith(parent.getId() + "-")) {
					suffix = suffix.substring(parent.getId().length() + 1, suffix.length());
				}
				doc.setUriPath(toUriPath(parent.getTitle() + "/" + suffix));
			}
		}
	}

	private void fillVariableProductValues(final IDocument doc, final Map<String, IDocument> docs) {
		if (VARIABLE_PRODUCT.equals(doc.getValue("type"))) {
			// variable product should have all variable attributes
			// populated from variations for proper facetting
			final String id = doc.getId();
			for (final IDocument variation : docs.values()) {
				if (!variation.contains("parentid")) {
					continue;
				}
				if (id.equals(variation.getValue("parentid"))) {
					// variation attributes are color and size
					appendStringField(doc, variation, "color");
					appendStringField(doc, variation, "size");
					doc.getOrCreate("variationids").ofType(String.class).set(variation.getId());
				}
			}
		}
	}

	/**
	 * Returns the context.
	 * 
	 * @return the context
	 */
	public IRuntimeContext getContext() {
		return context;
	}

	private void parseRow(final String[] header, final String[] row, final IDocument doc) {
		for (int i = 0; i < row.length; i++) {
			final String name = StringUtils.trim(header[i]);
			final String value = StringUtils.trim(row[i]);
			if (StringUtils.isEmpty(value)) {
				continue;
			}
			if ("id".equals(name)) {
				doc.setId(value);
			} else if ("producttype".equals(name)) {
				final IDocumentAttribute<String> type = doc.getOrCreate("type").ofType(String.class);
				if ("product-variation".equals(value)) {
					type.set(VARIATION);
				} else if (VARIABLE_PRODUCT.equals(value)) {
					type.set(VARIABLE_PRODUCT);
				} else {
					type.set("product");
				}
			} else if ("parent".equals(name)) {
				doc.getOrCreate("parentid").ofType(String.class).set(value);
			} else if ("name".equals(name)) {
				doc.setName(value);
			} else if ("uripath".equals(name)) {
				doc.setUriPath(value);
			} else if ("title".equals(name)) {
				doc.setTitle(value);
			} else if ("description".equals(name)) {
				doc.setDescription(value);
			} else if ("price".equals(name)) {
				doc.getOrCreate("price").ofType(Double.class).set(NumberUtils.toDouble(value, 0));
			} else {
				doc.getOrCreate(name).ofType(String.class).set(value);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(final IProgressMonitor monitor) {
		InputStream in = null;
		try {
			final Bundle bundle = FanShopActivator.getInstance().getBundle();
			if (null == bundle) {
				// abort, bundle is inactive
				return Status.CANCEL_STATUS;
			}

			// get manager early
			final IDocumentManager documentManager = ModelUtil.getManager(IDocumentManager.class, getContext());

			final URL sampleDataUrl = bundle.getEntry("/data/sample-products.csv");
			if (null == sampleDataUrl) {
				// abort, no data
				return Status.CANCEL_STATUS;
			}

			in = sampleDataUrl.openStream();
			final CSVParser parser = new CSVParser(new BufferedReader(new InputStreamReader(in, "CP1252")), new CSVStrategy(';', '"', CSVStrategy.COMMENTS_DISABLED, CSVStrategy.ESCAPE_DISABLED, false, false, false, false));
			// first line is header
			final String[] header = parser.getLine();
			if (null == header) {
				// abort, no header
				return Status.CANCEL_STATUS;
			}

			// read all the data
			final Map<String, IDocument> docs = new HashMap<String, IDocument>(30);
			String[] row;
			while (null != (row = parser.getLine())) {
				final IDocument doc = documentManager.createDocument();
				parseRow(header, row, doc);
				final String id = doc.getId();

				// every doc must have an id
				if (StringUtils.isBlank(id)) {
					continue;
				}

				// ignore duplicate ids
				if (docs.containsKey(id)) {
					continue;
				}
				docs.put(doc.getId(), doc);
			}

			// TODO: we need a much better story for variations, bundles & co ....
			// maybe some product type specific publisher story

			// 1nd pass ... fill missing values for non variations
			for (final IDocument doc : docs.values()) {
				fillMissingValuesNonVariations(doc, docs);
			}

			// 2nd pass ... enhance variable products with variation data
			for (final IDocument doc : docs.values()) {
				fillVariableProductValues(doc, docs);
			}

			// 3rd pass ... fill variations
			for (final IDocument doc : docs.values()) {
				fillMissingValuesVariations(doc, docs);
			}

			// publish the docs
			documentManager.publish(docs.values());
		} catch (final IllegalStateException e) {
			// abort, bundle is inactive
			return Status.CANCEL_STATUS;
		} catch (final Exception e) {
			e.printStackTrace();
			return FanShopActivator.getInstance().getStatusUtil().createError(0, e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(in);
		}

		return Status.OK_STATUS;
	}
}
