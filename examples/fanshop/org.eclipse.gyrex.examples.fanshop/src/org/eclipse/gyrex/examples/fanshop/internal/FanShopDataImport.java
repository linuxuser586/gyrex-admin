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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gyrex.cds.model.IListingManager;
import org.eclipse.gyrex.cds.model.documents.Document;
import org.eclipse.gyrex.cds.model.documents.DoubleField;
import org.eclipse.gyrex.cds.model.documents.Field;
import org.eclipse.gyrex.cds.model.documents.StringField;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.model.common.ModelUtil;
import org.osgi.framework.Bundle;

/**
 * 
 */
public class FanShopDataImport extends Job {

	/** VARIABLE_PRODUCT */
	private static final String VARIABLE_PRODUCT = "variable-product";
	/** VARIATION */
	private static final String VARIATION = "variation";

	private static void appendStringField(final Document target, final Document source, final String name) {
		if (source.hasField(name)) {
			final StringField field = source.getField(name, StringField.class);
			if (!target.hasField(name)) {
				target.addField(new StringField(name));
			}
			target.getField(name, StringField.class).addValues(field.getValues());
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

	private void fillMissingValuesNonVariations(final Document doc, final Map<String, Document> docs) {
		// check if we have a variation which inherits missing values
		if (VARIATION.equals(doc.getField("type", StringField.class).getFirstValue())) {
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

	private void fillMissingValuesVariations(final Document doc, final Map<String, Document> docs) {
		// check if we have a variation which inherits missing values
		if (VARIATION.equals(doc.getField("type", StringField.class).getFirstValue())) {
			// get parent
			final StringField parentId = doc.getField("parentid", StringField.class);
			if (null == parentId) {
				throw new IllegalArgumentException("variation has no parent id: " + doc);
			}
			final Document parent = docs.get(parentId.getFirstValue());
			if (null == parent) {
				throw new IllegalArgumentException("variation has no parent: " + doc);
			}
			// copy missing fields
			final Collection<Field<?>> fields = parent.getFields();
			for (final Field<?> field : fields) {
				final String name = field.getName();
				if (name.equals("id") || name.equals("name") || name.equals("uripath") || name.equals("variationids")) {
					continue;
				}
				if (!doc.hasField(name)) {
					doc.addField(field);
				} else if (null == doc.getField(name).getFirstValue()) {
					doc.removeField(name);
					doc.addField(field);
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

	private void fillVariableProductValues(final Document doc, final Map<String, Document> docs) {
		if (VARIABLE_PRODUCT.equals(doc.getField("type", StringField.class).getFirstValue())) {
			// variable product should have all variable attributes 
			// populated from variations for proper facetting
			final String id = doc.getId();
			for (final Document variation : docs.values()) {
				if (!variation.hasField("parentid")) {
					continue;
				}
				if (id.equals(variation.getField("parentid").getFirstValue())) {
					// variation attributes are color and size
					appendStringField(doc, variation, "color");
					appendStringField(doc, variation, "size");
					if (!doc.hasField("variationids")) {
						doc.addField(new StringField("variationids"));
					}
					doc.getField("variationids", StringField.class).addValues(variation.getId());
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

	private void parseRow(final String[] header, final String[] row, final Document doc) {
		for (int i = 0; i < row.length; i++) {
			final String name = StringUtils.trim(header[i]);
			final String value = StringUtils.trim(row[i]);
			if (StringUtils.isEmpty(value)) {
				continue;
			}
			if ("id".equals(name)) {
				doc.setId(value);
			} else if ("producttype".equals(name)) {
				if ("product-variation".equals(value)) {
					doc.addField(new StringField("type").setValue(VARIATION));
				} else if (VARIABLE_PRODUCT.equals(value)) {
					doc.addField(new StringField("type").setValue(VARIABLE_PRODUCT));
				} else {
					doc.addField(new StringField("type").setValue("product"));
				}
			} else if ("parent".equals(name)) {
				doc.addField(new StringField("parentid").setValue(value));
			} else if ("name".equals(name)) {
				doc.setName(value);
			} else if ("uripath".equals(name)) {
				doc.setUriPath(value);
			} else if ("title".equals(name)) {
				doc.setTitle(value);
			} else if ("description".equals(name)) {
				doc.setDescription(value);
			} else if ("price".equals(name)) {
				final double price = NumberUtils.toDouble(value, 0);
				doc.addField(new DoubleField("price").setValue(price));
			} else {
				if (doc.hasField(name)) {
					doc.getField(name, StringField.class).addValues(value);
				} else {
					doc.addField(new StringField(name).setValue(value));
				}
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
			final Map<String, Document> docs = new HashMap<String, Document>(30);
			String[] row;
			while (null != (row = parser.getLine())) {
				final Document doc = new Document();
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
			for (final Document doc : docs.values()) {
				fillMissingValuesNonVariations(doc, docs);
			}

			// 2nd pass ... enhance variable products with variation data
			for (final Document doc : docs.values()) {
				fillVariableProductValues(doc, docs);
			}

			// 3rd pass ... fill variations
			for (final Document doc : docs.values()) {
				fillMissingValuesVariations(doc, docs);
			}

			// publish the docs
			final IListingManager listingManager = ModelUtil.getManager(IListingManager.class, getContext());
			listingManager.publish(docs.values());
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
