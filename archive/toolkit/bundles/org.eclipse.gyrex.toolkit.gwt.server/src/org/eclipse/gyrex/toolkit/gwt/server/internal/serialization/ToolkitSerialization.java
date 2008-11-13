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
package org.eclipse.cloudfree.toolkit.gwt.server.internal.serialization;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import org.eclipse.cloudfree.toolkit.CWT;
import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedLayout;
import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedLayoutHint;
import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedResource;
import org.eclipse.cloudfree.toolkit.gwt.serialization.ISerializedWidget;
import org.eclipse.cloudfree.toolkit.gwt.serialization.internal.stoolkit.widgets.SContainer;
import org.eclipse.cloudfree.toolkit.layout.Layout;
import org.eclipse.cloudfree.toolkit.layout.LayoutHint;
import org.eclipse.cloudfree.toolkit.resources.Resource;
import org.eclipse.cloudfree.toolkit.widgets.Widget;

/**
 * This is a util for translating CWT elements to the serializable GWT
 * equivalent that gets sent by GWT's RPC implementation across to wire.
 * <p>
 * Right now the translation is static because it's simple and doesn't introduce
 * any dependency to yet another library. This could change in the future if
 * there is a more performant way available or the model becomes too complex.
 * </p>
 */
public class ToolkitSerialization {

	/** the package prefix containing the serializers */
	private static final String SERIALIZERS_PACKAGE_PREFIX = ToolkitSerialization.class.getPackage().getName().concat(".");

	/** the package prefix <code>org.eclipse.cloudfree.toolkit.</code> */
	private static final String CWT_PACKAGE_PREFIX = "org.eclipse.cloudfree.toolkit.";

	/** the class post fix <code>Serializer</code> */
	private static final String SERIALIZER = "Serializer";

	private static final ConcurrentMap<String, WidgetSerializer> cwtSerializersByWidgetClass = new ConcurrentHashMap<String, WidgetSerializer>(20);
	private static final ConcurrentMap<String, LayoutSerializer> cwtSerializersByLayoutClass = new ConcurrentHashMap<String, LayoutSerializer>(5);
	private static final ConcurrentMap<String, LayoutHintSerializer> cwtSerializersByLayoutHintClass = new ConcurrentHashMap<String, LayoutHintSerializer>(5);
	private static final ConcurrentMap<String, ResourceSerializer> cwtSerializersByResourceClass = new ConcurrentHashMap<String, ResourceSerializer>(5);

	private static <T> T getSerializer(final String nameOfClassToSerialize, final String nameOfSerializerClass, final Class<T> serializerType, final ConcurrentMap<String, T> serializerCache) {
		T serializer = serializerCache.get(nameOfSerializerClass);
		if (serializer != null) {

			return serializer;
		}

		// create a new one
		try {
			final Class<?> serializerClass = Class.forName(nameOfSerializerClass);
			serializer = serializerType.cast(serializerClass.newInstance());
		} catch (final ClassNotFoundException e) {
			CWT.error(CWT.ERROR_NOT_IMPLEMENTED, e, MessageFormat.format("unable to serialize layout type {0}, serializer {1} not found", nameOfClassToSerialize, nameOfSerializerClass));
		} catch (final InstantiationException e) {
			CWT.error(CWT.ERROR_NOT_IMPLEMENTED, null, MessageFormat.format("unable to serialize layout type {0}, serializer {1} could not be instantiated", nameOfClassToSerialize, nameOfSerializerClass));
		} catch (final IllegalAccessException e) {
			CWT.error(CWT.ERROR_NOT_IMPLEMENTED, null, MessageFormat.format("unable to serialize layout type {0}, serializer {1} could not be instantiated", nameOfClassToSerialize, nameOfSerializerClass));
		} catch (final ClassCastException e) {
			CWT.error(CWT.ERROR_NOT_IMPLEMENTED, null, MessageFormat.format("unable to serialize layout type {0}, serializer {1} is of wrong type", nameOfClassToSerialize, nameOfSerializerClass));
		}

		if (null == serializer) {
			CWT.error(CWT.ERROR_NOT_IMPLEMENTED, null, MessageFormat.format("unable to serialize layout type {0}, implementation error, serializer {1} is null", nameOfClassToSerialize, nameOfSerializerClass));
		}

		// cache the serializer
		final T existingSerializer = serializerCache.putIfAbsent(nameOfSerializerClass, serializer);
		if (null != existingSerializer) {
			return existingSerializer;
		}

		return serializer;
	}

	private static boolean isCWTClass(final Class clazz) {
		return clazz.getName().startsWith(CWT_PACKAGE_PREFIX);
	}

	/**
	 * Custom serialization hook
	 * 
	 * @param widget
	 * @return
	 */
	private static ISerializedWidget processToolkitSerializer(final Widget widget) {
		// TODO implement custom serialization
		return null;
	}

	private static ISerializedLayout serializeCWTLayout(final Layout layout) {
		final String layoutClassName = layout.getClass().getName();
		final String serializerClassName = SERIALIZERS_PACKAGE_PREFIX.concat(layoutClassName.substring(CWT_PACKAGE_PREFIX.length())).concat(SERIALIZER);

		// get the serializer
		final LayoutSerializer serializer = getSerializer(layoutClassName, serializerClassName, LayoutSerializer.class, cwtSerializersByLayoutClass);

		// serialize widget
		final ISerializedLayout serializedLayout = serializer.serialize(layout);

		if (null == serializedLayout) {
			CWT.error(CWT.ERROR_WIDGET_INITIALIZATION_FAILED, null, MessageFormat.format("unable to serialize layout type {0}, serializer {1} returned null result", layoutClassName, serializerClassName));
		}

		return serializedLayout;
	}

	private static ISerializedLayoutHint serializeCWTLayoutHint(final LayoutHint layoutHint) {
		final String layoutHintClassName = layoutHint.getClass().getName();
		final String serializerClassName = SERIALIZERS_PACKAGE_PREFIX.concat(layoutHintClassName.substring(CWT_PACKAGE_PREFIX.length())).concat(SERIALIZER);

		// get the serializer
		final LayoutHintSerializer serializer = getSerializer(layoutHintClassName, serializerClassName, LayoutHintSerializer.class, cwtSerializersByLayoutHintClass);

		// serialize widget
		final ISerializedLayoutHint serializedLayoutHint = serializer.serialize(layoutHint);

		if (null == serializedLayoutHint) {
			// we allow null for layout hints not applicable to the rendering technology
			// so we don't throw an error here
			//CWT.error(CWT.ERROR_WIDGET_INITIALIZATION_FAILED, null, MessageFormat.format("unable to serialize layout type {0}, serializer {1} returned null result", layoutHintClassName, serializerClassName));
			return null;
		}

		return serializedLayoutHint;
	}

	private static ISerializedResource serializeCWTResource(final Resource resource) {
		final String resourceClassName = resource.getClass().getName();
		final String serializerClassName = SERIALIZERS_PACKAGE_PREFIX.concat(resourceClassName.substring(CWT_PACKAGE_PREFIX.length())).concat(SERIALIZER);

		// get the serializer
		final ResourceSerializer serializer = getSerializer(resourceClassName, serializerClassName, ResourceSerializer.class, cwtSerializersByResourceClass);

		// serialize widget
		final ISerializedResource serializedResource = serializer.serialize(resource);

		if (null == serializedResource) {
			CWT.error(CWT.ERROR_WIDGET_INITIALIZATION_FAILED, null, MessageFormat.format("unable to serialize resource type {0}, serializer {1} returned null result", resourceClassName, serializerClassName));
		}

		return serializedResource;
	}

	private static ISerializedWidget serializeCWTWidget(final Widget widget, final SContainer parent) {
		final String widgetClassName = widget.getClass().getName();
		final String serializerClassName = SERIALIZERS_PACKAGE_PREFIX.concat(widgetClassName.substring(CWT_PACKAGE_PREFIX.length())).concat(SERIALIZER);

		// get the serializer
		final WidgetSerializer serializer = getSerializer(widgetClassName, serializerClassName, WidgetSerializer.class, cwtSerializersByWidgetClass);

		// serialize widget
		final ISerializedWidget serializedWidget = serializer.serialize(widget, parent);

		if (null == serializedWidget) {
			CWT.error(CWT.ERROR_WIDGET_INITIALIZATION_FAILED, null, MessageFormat.format("unable to serialize widget type {0}, serializer {1} returned null result", widgetClassName, serializerClassName));
		}

		return serializedWidget;
	}

	/**
	 * Translates a widget by delegating to the corresponding translation
	 * method.
	 * <p>
	 * Throws and exception if translation of the passed in widget class is not
	 * supported.
	 * </p>
	 * 
	 * @param layout
	 *            the layout to serialize
	 * @return the translated layout
	 */
	public static ISerializedLayout serializeLayout(final Layout layout) {
		final Class cwtClass = layout.getClass();

		if (isCWTClass(cwtClass)) {
			return serializeCWTLayout(layout);
		}

		// fallback to custom serialization
		// TODO: implement custom serialization
		final ISerializedLayout serializedLayout = null;

		if (null == serializedLayout) {
			CWT.error(CWT.ERROR_UNSPECIFIED, null, MessageFormat.format("unable to serialize layout type {0}", cwtClass.getName()));
		}

		return serializedLayout;
	}

	/**
	 * Translates a widget by delegating to the corresponding translation
	 * method.
	 * <p>
	 * Throws and exception if translation of the passed in widget class is not
	 * supported.
	 * </p>
	 * 
	 * @param layoutHint
	 *            the layout hint to serialize
	 * @return the serialized layout hint (maybe <code>null</code> if layout
	 *         hint is not applicable to the used rendering technology)
	 */
	public static ISerializedLayoutHint serializeLayoutHint(final LayoutHint layoutHint) {
		if (null == layoutHint) {
			return null;
		}

		final Class cwtClass = layoutHint.getClass();

		if (isCWTClass(cwtClass)) {
			return serializeCWTLayoutHint(layoutHint);
		}

		// TODO: implement custom serialization

		// note, we return null for layout hints not applicable to the rendering technology
		return null;
	}

	/**
	 * Translates a resource by delegating to the corresponding translation
	 * method.
	 * <p>
	 * Throws and exception if translation of the passed in resource class is
	 * not supported.
	 * </p>
	 * 
	 * @param resource
	 *            the resource to serialize
	 * @return the translated resource
	 */
	public static ISerializedResource serializeResource(final Resource resource) {
		final Class cwtClass = resource.getClass();

		if (isCWTClass(cwtClass)) {
			return serializeCWTResource(resource);
		}

		// fallback to custom serialization
		// TODO: implement custom serialization
		final ISerializedResource serializedResource = null;

		if (null == serializedResource) {
			CWT.error(CWT.ERROR_UNSPECIFIED, null, MessageFormat.format("unable to serialize resource type {0}", cwtClass.getName()));
		}

		return serializedResource;
	}

	/**
	 * Translates a widget by delegating to the corresponding translation
	 * method.
	 * <p>
	 * Throws and exception if translation of the passed in widget class is not
	 * supported.
	 * </p>
	 * 
	 * @param widget
	 * @return the translated widget
	 */
	public static ISerializedWidget serializeWidget(final Widget widget) {
		return serializeWidget(widget, null);
	}

	/**
	 * Translates a widget by delegating to the corresponding translation
	 * method.
	 * <p>
	 * Throws and exception if translation of the passed in widget class is not
	 * supported.
	 * </p>
	 * 
	 * @param widget
	 * @param container
	 *            the parent container
	 * @return the translated widget
	 */
	public static ISerializedWidget serializeWidget(final Widget widget, final SContainer parent) {
		final Class cwtClass = widget.getClass();

		if (isCWTClass(cwtClass)) {
			return serializeCWTWidget(widget, parent);
		}

		// fallback to custom serialization
		final ISerializedWidget serializedWidget = processToolkitSerializer(widget);

		if (null == serializedWidget) {
			CWT.error(CWT.ERROR_UNSPECIFIED, null, MessageFormat.format("unable to serialize widget type {0}", cwtClass.getName()));
		}

		return serializedWidget;
	}
}
