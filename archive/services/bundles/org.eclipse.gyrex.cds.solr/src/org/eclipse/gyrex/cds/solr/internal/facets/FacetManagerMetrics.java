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
package org.eclipse.gyrex.cds.solr.internal.facets;

import org.eclipse.gyrex.monitoring.metrics.MetricSet;
import org.eclipse.gyrex.monitoring.metrics.ThroughputMetric;

/**
 * {@link MetricSet} for {@link FacetManager}
 */
public class FacetManagerMetrics extends MetricSet {

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 * @param metrics
	 */
	FacetManagerMetrics(final String id) {
		super(id, new ThroughputMetric(id + ".facets.write"), new ThroughputMetric(id + ".facets.read"));
	}

}
