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
package org.eclipse.gyrex.admin.internal.widgets;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.gyrex.admin.internal.ExtensionPointTracker;
import org.eclipse.gyrex.admin.internal.ExtensionPointTracker.Listener;
import org.eclipse.gyrex.toolkit.runtime.IWidgetEnvironment;
import org.eclipse.gyrex.toolkit.runtime.lookup.IWidgetFactory;
import org.eclipse.gyrex.toolkit.runtime.lookup.RegistrationException;
import org.eclipse.gyrex.toolkit.widgets.Widget;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class AdminWidgetServiceRegistryHelper implements Listener {

	public class LazyWidgetFactory implements IWidgetFactory {

		private final IConfigurationElement element;
		private final AtomicReference<IWidgetFactory> wrappedFactory = new AtomicReference<IWidgetFactory>();

		public LazyWidgetFactory(final IConfigurationElement element) {
			this.element = element;
		}

		@Override
		public Widget getWidget(final String id, final IWidgetEnvironment environment) {
			IWidgetFactory factory = wrappedFactory.get();
			if (factory == null) {
				try {
					wrappedFactory.compareAndSet(null, (IWidgetFactory) element.createExecutableExtension(ATTRIBUTE_CLASS));
				} catch (final CoreException e) {
					LOG.warn("Unable to instantiate widget factory {} (contributed by {}): {}", new Object[] { element.getAttribute(ATTRIBUTE_CLASS), element.getContributor(), e.toString() });
					wrappedFactory.compareAndSet(null, NO_OP_FACTORY);
				}
				factory = wrappedFactory.get();
			}
			return factory.getWidget(id, environment);
		}

	}

	static final IWidgetFactory NO_OP_FACTORY = new IWidgetFactory() {
		@Override
		public Widget getWidget(final String id, final IWidgetEnvironment environment) {
			return null;
		}
	};

	private static final Logger LOG = LoggerFactory.getLogger(AdminWidgetServiceRegistryHelper.class);

	private static final String SETUP_WIZARD_EXTENSION_POINT = "org.eclipse.gyrex.admin.widgets";
	private static final String ATTRIBUTE_WIDGET_IDS = "widgetIds";
	private static final String ATTRIBUTE_CLASS = "class";
	private static final String ELEMENT_WIDGET_FACTORY = "widgetFactory";

	private final AdminWidgetServiceImpl service;
	private final ExtensionPointTracker tracker;

	private final ConcurrentMap<IConfigurationElement, IWidgetFactory> factories = new ConcurrentHashMap<IConfigurationElement, IWidgetFactory>();

	/**
	 * Creates a new instance.
	 * 
	 * @param service
	 * @param registryObject
	 */
	public AdminWidgetServiceRegistryHelper(final AdminWidgetServiceImpl service, final Object registryObject) {
		this.service = service;
		tracker = new ExtensionPointTracker((IExtensionRegistry) registryObject, SETUP_WIZARD_EXTENSION_POINT, this);
		tracker.open();
	}

	@Override
	public void added(final IExtension extension) {
		final IConfigurationElement[] elements = extension.getConfigurationElements();
		for (int j = 0; j < elements.length; j++) {
			if (ELEMENT_WIDGET_FACTORY.equalsIgnoreCase(elements[j].getName())) {
				factoryAdded(elements[j]);
			}
		}
	}

	private void factoryAdded(final IConfigurationElement configurationElement) {
		final String[] widgetIds = StringUtils.split(configurationElement.getAttribute(ATTRIBUTE_WIDGET_IDS), ',');
		if ((widgetIds != null) && (widgetIds.length > 0)) {
			final IWidgetFactory factory = new LazyWidgetFactory(configurationElement);
			if (factories.putIfAbsent(configurationElement, factory) == null) {
				try {
					service.registerFactory(factory, widgetIds);
				} catch (final RegistrationException e) {
					LOG.warn("Faild to register widget factory contributed by {}: {}", configurationElement.getContributor(), e.toString());
				}
			}
		}
	}

	private void factoryRemoved(final IConfigurationElement element) {
		final IWidgetFactory factory = factories.remove(element);
		if (factory != null) {
			service.unregisterFactory(factory);
		}
	}

	@Override
	public void removed(final IExtension extension) {
		final IConfigurationElement[] elements = extension.getConfigurationElements();
		for (int j = 0; j < elements.length; j++) {
			factoryRemoved(elements[j]);
		}
	}

	void stop() {
		tracker.close();
	}

}
