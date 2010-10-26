/*******************************************************************************
 * Copyright (c) 2010 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.cds.solr;

import org.eclipse.gyrex.cds.documents.IDocumentManager;
import org.eclipse.gyrex.cds.facets.IFacetManager;
import org.eclipse.gyrex.persistence.context.preferences.ContextPreferencesRepository;
import org.eclipse.gyrex.persistence.solr.SolrServerRepository;
import org.eclipse.gyrex.persistence.storage.content.RepositoryContentType;

/**
 * Interface with shared constants of the Solr based CDS implementation.
 */
public interface ISolrCdsConstants {

	/**
	 * The {@link RepositoryContentType content type} required for
	 * {@link IDocumentManager document model implementation}.
	 */
	RepositoryContentType DOCUMENT_CONTENT_TYPE = new RepositoryContentType("application", "x-gyrex-cds-solr-documents", SolrServerRepository.TYPE_NAME, "1.0");

	/**
	 * The {@link RepositoryContentType content type} required for
	 * {@link IFacetManager facet model implementation}.
	 */
	RepositoryContentType FACET_CONTENT_TYPE = new RepositoryContentType("application", "x-gyrex-cds-solr-facets", ContextPreferencesRepository.TYPE_NAME, "1.0");
}
