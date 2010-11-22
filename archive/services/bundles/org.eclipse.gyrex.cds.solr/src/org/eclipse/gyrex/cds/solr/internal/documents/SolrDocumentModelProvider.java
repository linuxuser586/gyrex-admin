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
package org.eclipse.gyrex.cds.solr.internal.documents;

import org.eclipse.gyrex.cds.documents.IDocumentManager;
import org.eclipse.gyrex.cds.solr.ISolrCdsConstants;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.model.common.provider.BaseModelManager;
import org.eclipse.gyrex.model.common.provider.ModelProvider;
import org.eclipse.gyrex.persistence.solr.SolrServerRepository;
import org.eclipse.gyrex.persistence.storage.Repository;

/**
 * Solr based CDS model provider.
 */
public class SolrDocumentModelProvider extends ModelProvider {

	/**
	 * Creates a new instance.
	 */
	public SolrDocumentModelProvider() {
		super(ISolrCdsConstants.DOCUMENT_CONTENT_TYPE, IDocumentManager.class);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gyrex.model.common.provider.ModelProvider#createModelManagerInstance(java.lang.Class, org.eclipse.gyrex.persistence.storage.Repository, org.eclipse.gyrex.context.IRuntimeContext)
	 */
	@Override
	public BaseModelManager createModelManagerInstance(final Class modelManagerType, final Repository repository, final IRuntimeContext context) {
		if (IDocumentManager.class.equals(modelManagerType) && (repository instanceof SolrServerRepository)) {
			return new SolrDocumentManager(context, (SolrServerRepository) repository);
		}
		return null;
	}

}