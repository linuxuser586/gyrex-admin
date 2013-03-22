/*******************************************************************************
 * Copyright (c) 2013 <enter-company-name-here> and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     <enter-developer-name-here> - initial API and implementation
 *******************************************************************************/
package org.eclipse.gyrex.admin.ui.jobs.internal;

import java.util.Comparator;

import org.eclipse.gyrex.admin.ui.internal.widgets.FilteredItemsSelectionDialog;
import org.eclipse.gyrex.jobs.internal.JobsActivator;
import org.eclipse.gyrex.jobs.internal.registry.JobProviderRegistry;
import org.eclipse.gyrex.jobs.provider.JobProvider;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 *
 */
public class JobTypeSelectionDialog extends FilteredItemsSelectionDialog {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public JobTypeSelectionDialog(final Shell shell) {
		super(shell);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createExtendedContentArea(final Composite parent) {
		return null;
	}

	@Override
	protected ItemsFilter createFilter() {
		return new ItemsFilter() {

			@Override
			public boolean isConsistentItem(final Object item) {
				return true;
			}

			@Override
			public boolean matchItem(final Object item) {
				if (item instanceof JobType) {
					final JobType type = (JobType) item;
					return patternMatcher.matches(type.getName()) || patternMatcher.matches(type.getId());
				}

				return false;
			}
		};
	}

	@Override
	protected void fillContentProvider(final AbstractContentProvider contentProvider, final ItemsFilter itemsFilter, final IProgressMonitor progressMonitor) throws CoreException {
		final JobProviderRegistry registry = JobsActivator.getInstance().getJobProviderRegistry();
		for (final String providerId : registry.getProviders()) {
			final JobProvider provider = registry.getProvider(providerId);
			for (final String typeId : provider.getProvidedTypeIds()) {
				contentProvider.add(new JobType(typeId, provider), itemsFilter);
			}
		}
	}

	@Override
	protected IDialogSettings getDialogSettings() {
		return new DialogSettings(JobTypeSelectionDialog.class.getName());
	}

	@Override
	public String getElementName(final Object item) {
		if (item instanceof JobType)
			return ((JobType) item).getName();

		return null;
	}

	@Override
	protected Comparator<Object> getItemsComparator() {
		return new Comparator<Object>() {

			@Override
			public int compare(final Object o1, final Object o2) {
				String id1 = null;
				String id2 = null;

				if (o1 instanceof JobType) {
					id1 = ((JobType) o1).getName();
				} else
					return 0;

				if (o2 instanceof JobType) {
					id2 = ((JobType) o2).getName();
				} else
					return 0;

				return id1.compareTo(id2);
			}
		};
	}

	@Override
	protected IStatus validateItem(final Object item) {
		return Status.OK_STATUS;
	}

}