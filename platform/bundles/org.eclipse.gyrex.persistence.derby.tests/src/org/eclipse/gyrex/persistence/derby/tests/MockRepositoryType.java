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
package org.eclipse.cloudfree.persistence.derby.tests;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.eclipse.cloudfree.persistence.jdbc.internal.JdbcRepositoryImpl;
import org.eclipse.cloudfree.persistence.jdbc.storage.JdbcRepository;
import org.eclipse.cloudfree.persistence.storage.Repository;
import org.eclipse.cloudfree.persistence.storage.provider.RepositoryProvider;
import org.eclipse.cloudfree.persistence.storage.settings.IRepositoryPreferences;

public class MockRepositoryType extends RepositoryProvider {

	public MockRepositoryType() {
		super(MockRepositoryType.class.getName(), JdbcRepository.class);
	}

	private EmbeddedConnectionPoolDataSource createDataSource(final String databaseName) {
		final EmbeddedConnectionPoolDataSource embeddedConnectionPoolDataSource = new EmbeddedConnectionPoolDataSource();
		embeddedConnectionPoolDataSource.setDatabaseName(databaseName);
		embeddedConnectionPoolDataSource.setCreateDatabase("create");
		return embeddedConnectionPoolDataSource;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cloudfree.persistence.storage.provider.RepositoryProvider#newRepositoryInstance(java.lang.String, org.eclipse.cloudfree.persistence.storage.settings.IRepositoryPreferences)
	 */
	@Override
	public Repository createRepositoryInstance(final String repositoryId, final IRepositoryPreferences repositoryPreferences) {
		return new JdbcRepositoryImpl(repositoryId, this, createDataSource(repositoryId), 0);
	}
}
