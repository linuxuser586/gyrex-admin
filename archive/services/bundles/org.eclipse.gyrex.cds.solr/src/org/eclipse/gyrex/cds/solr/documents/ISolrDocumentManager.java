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
package org.eclipse.gyrex.cds.model.solr;

import org.eclipse.gyrex.cds.documents.IDocumentManager;

import org.eclipse.core.runtime.IAdaptable;

/**
 * A <a href="http://lucene.apache.org/solr/" target="_blank">Apache Solr</a>
 * based listing manager.
 * <p>
 * This is an extension interface provided by the Solr based listing manager via
 * it's {@link IAdaptable#getAdapter(Class) adaptable} capabilities. It allows
 * to interfere directly with the Solr specific behavior in order to optimize
 * clients. Clients using this API <strong>must</strong> understand that they
 * chain themselves to a specific listings manager implementation. Thus, it's
 * strongly advised to only use this interface in very specific scenarios where
 * it's acceptable to tighten a context to a specific implementation.
 * </p>
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ISolrListingManager {

	/**
	 * Commits everything to the underlying Solr repository.
	 * 
	 * @param waitFlush
	 *            <code>true</code> if the method should block till all changes
	 *            have been committed, <code>false</code> otherwise
	 * @param waitSearcher
	 *            <code>true</code> if the method should block till new
	 *            searchers have been opened after committing,
	 *            <code>false</code> otherwise
	 */
	void commit(boolean waitFlush, boolean waitSearcher);

	/**
	 * Optimizes and commits everything to the underlying Solr repository.
	 * 
	 * @param waitFlush
	 *            <code>true</code> if the method should block till all changes
	 *            have been committed, <code>false</code> otherwise
	 * @param waitSearcher
	 *            <code>true</code> if the method should block till new
	 *            searchers have been opened after committing,
	 *            <code>false</code> otherwise
	 */
	void optimize(boolean waitFlush, boolean waitSearcher);

	/**
	 * Allows to temporarily disabled commits from the manager.
	 * <p>
	 * When disabled, the manager will never commit any changes
	 * {@link IDocumentManager#publish(Iterable) submitted} to the underlying
	 * Solr repository. Instead, {@link #commit(boolean, boolean)} must be
	 * called manually in order to apply changes to the Solr repository.
	 * </p>
	 * 
	 * @param enabled
	 *            <code>true</code> if the manager is allowed to commit changes,
	 *            <code>false</code> otherwise
	 * @return <code>true</code> if commit was previously enabled,
	 *         <code>false</code> otherwise
	 */
	boolean setCommitsEnabled(boolean enabled);
}
