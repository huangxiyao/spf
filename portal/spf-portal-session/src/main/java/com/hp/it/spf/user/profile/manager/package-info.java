/**
 * Classes defined in this package allow to plug into Shared Portal Framework user profile information
 * providers.
 * <p>
 * User profile information provider should implement {@link com.hp.it.spf.user.profile.manager.IUserProfileRetriever}
 * interface. The name of the implementing class should be defined in <code>SharedPortalSSO.properties</code>
 * as described in {@link com.hp.it.spf.user.profile.manager.UserProfileRetrieverFactory} class
 * documentation.
 * </p>
 * <p>
 * The module provides 3 retriever implementations:
 * <ul>
 * <li>{@link com.hp.it.spf.user.profile.manager.DefaultUserProfileRetriever} - a no-op implementation
 * returing an empty map.</li>
 * <li>{@link com.hp.it.spf.user.profile.manager.CompoundUserProfileRetriever} - a composite implementation
 * allowing to merge user profile maps retrieved by a set of retrievers.</li>
 * <li>{@link com.hp.it.spf.user.profile.manager.PropertyFileUserProfileRetriever} - a test retriever
 * sourcing user profile information from property files. Can be used to test the framework with dummy
 * data before the actual retriever implementation is ready.</li>
 * </ul>
 * </p>
 */
package com.hp.it.spf.user.profile.manager;