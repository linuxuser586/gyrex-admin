/**
 * Copyright (c) 2011, 2012 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package org.eclipse.gyrex.admin.ui.jobs.internal;

import java.util.Locale;

import org.eclipse.gyrex.jobs.schedules.ISchedule;
import org.eclipse.gyrex.jobs.schedules.IScheduleEntry;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class JobsLabelProvider extends LabelProvider {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	private ResourceManager manager;

	@Override
	public void dispose() {
		if (null != manager) {
			manager.dispose();
			manager = null;
		}
		super.dispose();
	}

	private ImageDescriptor getElementImage(final ISchedule element) {
		if (element.isEnabled())
			return JobsUiImages.getImageDescriptor(JobsUiImages.IMG_OBJ_SCHEDULE);

		return JobsUiImages.getImageDescriptor(JobsUiImages.IMG_OBJ_SCHEDULE_DISABLED);
	}

	private ImageDescriptor getElementImage(final JobLog element) {
		if (element.isError())
			return JobsUiImages.getImageDescriptor(JobsUiImages.IMG_OBJ_ERROR_RESULT);
		if (element.isWarning())
			return JobsUiImages.getImageDescriptor(JobsUiImages.IMG_OBJ_WARN_RESULT);
		return null;
	}

	private String getElementText(final ISchedule element) {
		if (element.isEnabled())
			return String.format("%s (%s, %d)", element.getId(), element.getTimeZone().getDisplayName(Locale.US), element.getEntries().size());
		else
			return String.format("%s (DISABLED)", element.getId());

	}

	private String getElementText(final IScheduleEntry element) {
		return String.format("%s (%s, %s)", element.getId(), element.getCronExpression(), element.getJobTypeId());
	}

	private String getElementText(final JobLog element) {
		return element.getId();
	}

	@Override
	public Image getImage(final Object element) {
		final ImageDescriptor descriptor = getImageDescriptor(element);
		if (descriptor == null)
			return null;
		return getResourceManager().createImage(descriptor);
	}

	private ImageDescriptor getImageDescriptor(final Object element) {
		if (element instanceof ISchedule)
			return getElementImage((ISchedule) element);
		if (element instanceof JobLog)
			return getElementImage((JobLog) element);
		return null;
	}

	private ResourceManager getResourceManager() {
		if (null == manager) {
			manager = new LocalResourceManager(JFaceResources.getResources());
		}
		return manager;
	}

	@Override
	public String getText(final Object element) {
		if (element instanceof ISchedule)
			return getElementText((ISchedule) element);
		if (element instanceof IScheduleEntry)
			return getElementText((IScheduleEntry) element);
		if (element instanceof JobLog)
			return getElementText((JobLog) element);
		if (element instanceof RunningJob)
			return ((RunningJob) element).getLabel();

		return super.getText(element);
	}
}
