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
package org.eclipse.gyrex.cds.model.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.eclipse.core.runtime.IAdaptable;

/**
 * A <a href="http://lucene.apache.org/solr/" target="_blank">Apache Solr</a>
 * query executor.
 * <p>
 * This is an extension interface provided by the Solr based listing manager via
 * it's {@link IAdaptable#getAdapter(Class) adaptable} capabilities. It allows
 * to interfere directly with the Solr API. Clients using this API
 * <strong>must</strong> understand that they chain themselves to a specific
 * listings manager implementation. Thus, it's strongly advised to only use this
 * interface in very specific scenarios where it's acceptable to tighten a
 * context to a specific implementation.
 * </p>
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ISolrQueryExecutor {

	/**
	 * Executes a SolrJ query.
	 * <p>
	 * Note, this API depends on the SolrJ and Solr API. Thus, it is bound to
	 * the evolution of external API which might not follow the Gyrex <a
	 * href="http://wiki.eclipse.org/Evolving_Java-based_APIs"
	 * target="_blank">API evolution</a> and <a
	 * href="http://wiki.eclipse.org/Version_Numbering"
	 * target="_blank">versioning</a> guidelines.
	 * </p>
	 * 
	 * @param query
	 *            the <code>SolrQuery</code> object
	 * @return the <code>QueryResponse</code> object
	 */
	QueryResponse query(SolrQuery query);

}
