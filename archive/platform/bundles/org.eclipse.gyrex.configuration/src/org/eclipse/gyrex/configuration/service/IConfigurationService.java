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
package org.eclipse.gyrex.configuration.service;

import org.eclipse.gyrex.common.context.IContext;
import org.eclipse.gyrex.configuration.PlatformConfiguration;

/**
 * Provides a thin layer on top of the Eclipse Preferences API to streamline
 * access to the Eclipse preferences.
 * <p>
 * The Eclipse Preferences API provide a very rich and flexible interface for
 * storing configuration settings in scoped preferences. The default
 * implementation supports a default, a configuration and an instance scope.
 * Those scopes are related to the OSGi notion of the framework default
 * settings, the settings stored in the configuration area (i.e., apply to all
 * instances started from the same framework base) and those stored in the
 * instance area (i.e., apply to an actual running framework instance).
 * </p>
 * <p>
 * Gyrex adds a new scope to the set of scopes. This scope
 * allows to store preferences in a global repository which can be accessed by
 * framework instances running on different machines. This repository could be a
 * relational database or and LDAP directory.
 * </p>
 * <p>
 * In order to streamline preferences access, this service provides convenience
 * methods for reading and setting preferences. It uses the various scopes as
 * anticipated by Gyrex.
 * </p>
 * <p>
 * The following scopes will be used in Gyrex.
 * <ul>
 * <li><strong>PLATFORM</strong> - This is the primary scope for Gyrex
 * preferences. Whenever a preferences is modified (set or removed) through API
 * provided by this service it will happen in the
 * {@link org.eclipse.gyrex.configuration.preferences.PlatformScope platform
 * scope}.</li>
 * <li><strong>DEFAULT</strong> - This is the default scope which defines
 * default preferences. Default preferences cannot be modified through API
 * defined here. Default preferences are initialized during bundle start (see
 * {@link org.eclipse.gyrex.configuration.preferences.DefaultPreferencesInitializer}
 * ).</li>
 * </ul>
 * All other scopes (i.e., INSTANCE, CONFIGURATION, PROJECT) will
 * <strong>not</strong> be consulted when searching or modifying preferences
 * through this API.
 * </p>
 * <p>
 * When reading preferences the lookup order is: <strong>PLATFORM</strong>,
 * <strong>DEFAULT</strong>. When modifying preferences using the
 * <code>put...</code> and <code>remove</code> methods only nodes from the
 * <strong>PLATFORM</strong> scope will be used.
 * </p>
 * <p>
 * If a preference value is to be encrypted it will be stored encrypted in the
 * PLATFORM scope backing store and not using Equinox
 * <code>ISecurePreferences</code>. The encryption will be done using the
 * Gyrex encryption conventions. Decryption will happen
 * transparently at read time.
 * </p>
 * 
 * @see PlatformConfiguration
 * @see org.eclipse.gyrex.configuration.preferences.PlatformScope
 * @see org.eclipse.gyrex.configuration.preferences.DefaultPreferencesInitializer
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface IConfigurationService {

	/**
	 * Return the value stored in the preference store for the given key. If the
	 * key is not defined then return the specified default value. Use the
	 * canonical Gyrex scope lookup order for finding the preference value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct nodes. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param defaultValue
	 *            the value to use if the preference is not defined
	 * @param context
	 *            optional context object to help scopes determine which nodes
	 *            to search, or <code>null</code>
	 * @return the value of the preference or the given default value
	 */
	boolean getBoolean(String qualifier, String key, boolean defaultValue, IContext context);

	/**
	 * Return the value stored in the preference store for the given key. If the
	 * key is not defined then return the specified default value. Use the
	 * canonical Gyrex scope lookup order for finding the preference value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct nodes. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param defaultValue
	 *            the value to use if the preference is not defined
	 * @param context
	 *            optional context object to help scopes determine which nodes
	 *            to search, or <code>null</code>
	 * @return the value of the preference or the given default value
	 */
	byte[] getByteArray(String qualifier, String key, byte[] defaultValue, IContext context);

	/**
	 * Return the value stored in the preference store for the given key. If the
	 * key is not defined then return the specified default value. Use the
	 * canonical Gyrex scope lookup order for finding the preference value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct nodes. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param defaultValue
	 *            the value to use if the preference is not defined
	 * @param context
	 *            optional context object to help scopes determine which nodes
	 *            to search, or <code>null</code>
	 * @return the value of the preference or the given default value
	 */
	double getDouble(String qualifier, String key, double defaultValue, IContext context);

	/**
	 * Return the value stored in the preference store for the given key. If the
	 * key is not defined then return the specified default value. Use the
	 * canonical Gyrex scope lookup order for finding the preference value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct nodes. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param defaultValue
	 *            the value to use if the preference is not defined
	 * @param context
	 *            optional context object to help scopes determine which nodes
	 *            to search, or <code>null</code>
	 * @return the value of the preference or the given default value
	 */
	float getFloat(String qualifier, String key, float defaultValue, IContext context);

	/**
	 * Return the value stored in the preference store for the given key. If the
	 * key is not defined then return the specified default value. Use the
	 * canonical Gyrex scope lookup order for finding the preference value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct nodes. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param defaultValue
	 *            the value to use if the preference is not defined
	 * @param context
	 *            optional context object to help scopes determine which nodes
	 *            to search, or <code>null</code>
	 * @return the value of the preference or the given default value
	 */
	int getInt(String qualifier, String key, int defaultValue, IContext context);

	/**
	 * Return the value stored in the preference store for the given key. If the
	 * key is not defined then return the specified default value. Use the
	 * canonical Gyrex scope lookup order for finding the preference value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct nodes. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param defaultValue
	 *            the value to use if the preference is not defined
	 * @param context
	 *            optional context object to help scopes determine which nodes
	 *            to search, or <code>null</code>
	 * @return the value of the preference or the given default value
	 */
	long getLong(String qualifier, String key, long defaultValue, IContext context);

	/**
	 * Return the value stored in the preference store for the given key. If the
	 * key is not defined then return the specified default value. Use the
	 * canonical Gyrex scope lookup order for finding the preference value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct nodes. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param defaultValue
	 *            the value to use if the preference is not defined
	 * @param context
	 *            optional context object to help scopes determine which nodes
	 *            to search, or <code>null</code>
	 * @return the value of the preference or the given default value
	 */
	String getString(String qualifier, String key, String defaultValue, IContext context);

	/**
	 * Sets the value stored in the preference store for the given key. If the
	 * key is defined the existing value will be overridden with the specified
	 * value. Use the canonical Gyrex scope lookup order for finding the
	 * preference to set the value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct node. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param value
	 *            the value to set
	 * @param context
	 *            optional context object to help scopes determine at which node
	 *            the preference should be set, or <code>null</code>
	 * @param encrypt
	 *            <code>true</code> if value is to be encrypted,
	 *            <code>false</code> value does not need to be encrypted
	 */
	void putBoolean(String qualifier, String key, boolean value, IContext context, boolean encrypt);

	/**
	 * Sets the value stored in the preference store for the given key. If the
	 * key is defined the existing value will be overridden with the specified
	 * value. Use the canonical Gyrex scope lookup order for finding the
	 * preference to set the value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct node. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param value
	 *            the value to set, or <code>null</code> to
	 *            {@link #remove(String, String, IContext) remove} the
	 *            preference if it is defined
	 * @param context
	 *            optional context object to help scopes determine at which node
	 *            the preference should be set, or <code>null</code>
	 * @param encrypt
	 *            <code>true</code> if value is to be encrypted,
	 *            <code>false</code> value does not need to be encrypted
	 */
	void putByteArray(String qualifier, String key, byte[] value, IContext context, boolean encrypt);

	/**
	 * Sets the value stored in the preference store for the given key. If the
	 * key is defined the existing value will be overridden with the specified
	 * value. Use the canonical Gyrex scope lookup order for finding the
	 * preference to set the value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct node. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param value
	 *            the value to set
	 * @param context
	 *            optional context object to help scopes determine at which node
	 *            the preference should be set, or <code>null</code>
	 * @param encrypt
	 *            <code>true</code> if value is to be encrypted,
	 *            <code>false</code> value does not need to be encrypted
	 */
	void putDouble(String qualifier, String key, double value, IContext context, boolean encrypt);

	/**
	 * Sets the value stored in the preference store for the given key. If the
	 * key is defined the existing value will be overridden with the specified
	 * value. Use the canonical Gyrex scope lookup order for finding the
	 * preference to set the value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct node. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param value
	 *            the value to set
	 * @param context
	 *            optional context object to help scopes determine at which node
	 *            the preference should be set, or <code>null</code>
	 * @param encrypt
	 *            <code>true</code> if value is to be encrypted,
	 *            <code>false</code> value does not need to be encrypted
	 */
	void putFloat(String qualifier, String key, float value, IContext context, boolean encrypt);

	/**
	 * Sets the value stored in the preference store for the given key. If the
	 * key is defined the existing value will be overridden with the specified
	 * value. Use the canonical Gyrex scope lookup order for finding the
	 * preference to set the value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct node. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param value
	 *            the value to set
	 * @param context
	 *            optional context object to help scopes determine at which node
	 *            the preference should be set, or <code>null</code>
	 * @param encrypt
	 *            <code>true</code> if value is to be encrypted,
	 *            <code>false</code> value does not need to be encrypted
	 */
	void putInt(String qualifier, String key, int value, IContext context, boolean encrypt);

	/**
	 * Sets the value stored in the preference store for the given key. If the
	 * key is defined the existing value will be overridden with the specified
	 * value. Use the canonical Gyrex scope lookup order for finding the
	 * preference to set the value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct node. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param value
	 *            the value to set
	 * @param context
	 *            optional context object to help scopes determine at which node
	 *            the preference should be set, or <code>null</code>
	 * @param encrypt
	 *            <code>true</code> if value is to be encrypted,
	 *            <code>false</code> value does not need to be encrypted
	 */
	void putLong(String qualifier, String key, long value, IContext context, boolean encrypt);

	/**
	 * Sets the value stored in the preference store for the given key. If the
	 * key is defined the existing value will be overridden with the specified
	 * value. Use the canonical Gyrex scope lookup order for finding the
	 * preference to set the value.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct node. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param value
	 *            the value to set, or <code>null</code> to
	 *            {@link #remove(String, String, IContext) remove} the
	 *            preference if it is defined
	 * @param context
	 *            optional context object to help scopes determine at which node
	 *            the preference should be set, or <code>null</code>
	 * @param encrypt
	 *            <code>true</code> if value is to be encrypted,
	 *            <code>false</code> value does not need to be encrypted
	 */
	void putString(String qualifier, String key, String value, IContext context, boolean encrypt);

	/**
	 * Removes the value stored in the preference store for the given key. If
	 * the key is not defined then nothing will be removes. Use the canonical
	 * Gyrex scope lookup order for finding the preference to remove.
	 * <p>
	 * The specified key may either refer to a simple key or be the
	 * concatenation of the path of a child node and key. If the key contains a
	 * slash ("/") character, then a double-slash must be used to denote the end
	 * of the child path and the beginning of the key. Otherwise it is assumed
	 * that the key is the last segment of the path. The following are some
	 * examples of keys and their meanings:
	 * <ul>
	 * <li>"a" - look for a value for the property "a"
	 * <li>"//a" - look for a value for the property "a"
	 * <li>"///a" - look for a value for the property "/a"
	 * <li>"//a//b" - look for a value for the property "a//b"
	 * <li>"a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b/c" - look in the child node "a/b" for property "c"
	 * <li>"/a/b//c" - look in the child node "a/b" for the property "c"
	 * <li>"a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c/d" - look in the child node "a/b" for the property "c/d"
	 * <li>"/a/b//c//d" - look in the child node "a/b" for the property "c//d"
	 * </ul>
	 * </p>
	 * <p>
	 * Callers may specify a context object to aid in the determination of the
	 * correct nodes. For each entry in the lookup order, the context is
	 * consulted and if one matching the scope exists, then it is used to
	 * calculate the node. Otherwise a default calculation algorithm is used.
	 * </p>
	 * <p>
	 * An example of a qualifier for a Gyrex preference is the plug-in
	 * identifier (aka. bundle symbolic name). (e.g.
	 * "org.eclipse.core.resources" for "description.autobuild")
	 * </p>
	 * 
	 * @param qualifier
	 *            a namespace qualifier for the preference
	 * @param key
	 *            the name of the preference (optionally including its path)
	 * @param context
	 *            optional context object to help scopes determine which node to
	 *            remove, or <code>null</code>
	 */
	void remove(String qualifier, String key, IContext context);

}
