/*
 * Copyright 2016 Quatico Solutions Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quatico.base.aem.test.api;


import com.quatico.base.aem.test.api.builders.SlingHttpServletRequestBuilder;
import com.quatico.base.aem.test.api.services.IServiceBuilder;
import com.quatico.base.aem.test.api.services.IServiceBuilder.InjectedService;

import java.util.Locale;
import java.util.MissingResourceException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.osgi.framework.BundleContext;

import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.components.ComponentContext;
import com.google.common.base.Function;

import io.wcm.testing.mock.aem.builder.ContentBuilder;


public interface IAemClient {
	
	/**
	 * Set the locale of the request. This can be useful for testing localization.
	 *
	 * @param userLocale must not be null
	 * @return the client instance
	 */
	IAemClient setUserLocale(Locale userLocale);
	
	/**
	 * Add a resource bundle with using the specified base name and locale,
	 * to the request.
	 *
	 * @param baseName
	 *        the base name of the resource bundle, a fully qualified class name; must not be null
	 * @param locale
	 *        the locale for which a resource bundle is desired; must not be null
	 * @exception NullPointerException
	 *        if <code>baseName</code> or <code>locale</code> is <code>null</code>
	 * @exception MissingResourceException
	 *        if no resource bundle for the specified base name can be found
	 * @return the client instance
	 */
	IAemClient addBundle(String baseName, Locale locale);
	
	/**
	 * Register a mocked instance of the specified service class in the Sling layer.
	 *
	 * @param serviceClass
	 *          class or interface of the service to be mocked and registered; must not be null
	 * @param properties
	 *          service properties to be available at runtime (key-value pairs, must be even numbered)
	 * @param <R>
	 *          the type of the service
	 * @return the mocked service object, never null
	 * @throws Exception
	 *          in case of a problem with registering the service
	 */
	<R> R require(Class<R> serviceClass, Object... properties) throws Exception;
	
	/**
	 * Register an injectable service instance of a service in the Sling layer. The injectable object
	 * can be created using a <code>service()</code> method.
	 *
	 * @param service
	 *          instance of the service to be registered; must not be null
	 * @param properties
	 *          service properties to be available at runtime (key-value pairs, must be even numbered)
	 * @param <R>
	 *          the type of the service
	 * @return the service instance, never null
	 * @throws Exception
	 *          in case of a problem with registering the service
	 */
	<R> R require(InjectedService<R> service, Object... properties) throws Exception;
	
	/**
	 * Register a builder object for a service in the Sling layer. The service builder
	 * can be created as subclass of <code>AbstractBuilder</code>.
	 *
	 * @param builder
	 *          instance of the service builder to be registered; must not be null
	 * @param properties
	 *          service properties to be available at runtime (key-value pairs, must be even numbered)
	 * @param <R>
	 *          the type of the service
	 * @return the service instance, never null
	 * @throws Exception
	 *          in case of a problem with registering the service
	 */
	<R> R require(IServiceBuilder<R> builder, Object... properties) throws Exception;
	
	/**
	 * Creates an injectable service object for a specified service. The service is registered with the specified type
	 * and specified required services.
	 *
	 * @param type
	 *          the type for which the service instance to be registered; must not be null
	 * @param service
	 *          the service object to be used; must not be null
	 * @param requiredServices
	 *          array of services required by the service object; may be omitted
	 * @param <R>
	 *          the type of the service
	 * @param <C>
	 *          the type of the service implementation
	 * @return an injectable service instance; never null
	 * @throws Exception
	 *          in case of a problem with creating the injectable service instance
	 */
	<R, C extends R> InjectedService<R> service(Class<R> type, C service, InjectedService<?>... requiredServices) throws Exception;
	
	/**
	 * Creates an injectable service object for a specified service. The service is registered with the type of it's implementation
	 * and specified required services. Can be used if service type and implementation are the same.
	 *
	 * @param service
	 *          the service object to be used; must not be null
	 * @param requiredServices
	 *          array of services required by the service object; may be omitted
	 * @param <R>
	 *          the type of the service implementation
	 * @return an injectable service instance; never null
	 * @throws Exception
	 *          in case of a problem with creating the injectable service instance
	 */
	<R> InjectedService<R> service(R service, InjectedService<?>... requiredServices) throws Exception;
	
	/**
	 * Creates an injectable service object for a specified service builder. The service is registered with the type of it's implementation
	 * and required services.
	 *
	 * @param builder
	 *          the service builder instance to be used; must not be null
	 * @param requiredServices
	 *          array of services required by the service instance; may be omitted
	 * @param <R>
	 *          the type of the service implementation
	 * @return an injectable service instance; never null
	 * @throws Exception
	 *          in case of a problem with creating the injectable service instance
	 */
	<R> InjectedService<R> service(IServiceBuilder<R> builder, InjectedService<?>... requiredServices) throws Exception;
	
	/**
	 * Register an adapter for the specified adaptable class using the Google Guice framework.
	 *
	 * @param adaptableClass
	 *          the class to be adapted from; must not be null
	 * @param adapterClass
	 *          the class to be adapted to; must not be null
	 * @param adaptHandler
	 *          the handler that implements the adaptation; must not be null
	 */
	<T1, T2> void registerAdapter(Class<T1> adaptableClass, Class<T2> adapterClass, Function<T1, T2> adaptHandler);
	
	/**
	 * Register an adapter for the specified adaptable class using an adapter instance.
	 *
	 * @param adaptableClass
	 *          the class to be adapted from; must not be null
	 * @param adapterClass
	 *          the class to be adapted to; must not be null
	 * @param adapter
	 *          the object that implements the adaptation; must not be null
	 */
	<T1, T2> void registerAdapter(Class<T1> adaptableClass, Class<T2> adapterClass, T2 adapter);
	
	/**
	 * Get the Sling script helper instance used by the SlingMock layer.
	 *
	 * @return script helper instance; never null
	 */
	SlingScriptHelper getSlingScriptHelper();
	
	/**
	 * Get the request instance used by the SlingMock layer. It's an instance of <code>SlingMockHttpServletRequest</code>,
	 * but also a Mockito Spy. You can use Mockito to verify method calls and passed parameter values.
	 *
	 * @return request instance; never null;
	 * @see {@link #aRequest()} for manipulating the request
	 */
	SlingHttpServletRequest getRequest();
	
	/**
	 * Get the response instance used by the SlingMock layer. It's an instance of <code>SlingMockHttpServletResponse</code>,
	 * but also a Mockito Spy. You can use Mockito to verify method calls and passed parameter values.
	 *
	 * @return response instance; never null;
	 */
	SlingHttpServletResponse getResponse();
	
	/**
	 * Get the page manager to create or manipulate pages.
	 *
	 * @return page manager instance; may be null
	 */
	PageManager getPageManager();
	
	/**
	 * Get the content builder to provide test content, e.g. pages or assets for your tests.
	 *
	 * @return content builder instance; never null
	 */
	ContentBuilder getContentBuilder();
	
	/**
	 * Get the administrative resource resolver to resolve paths in your tests.
	 *
	 * @return resolver instance; may be null
	 */
	ResourceResolver getResourceResolver();
	
	/**
	 * Get the resolver factory to create resource resolvers.
	 *
	 * @return resolver instance; may be null
	 */
	ResourceResolverFactory getResourceResolverFactory();
	
	/**
	 * Get the OSGi bundle context.
	 *
	 * @return context instance; may be null
	 */
	BundleContext getBundleContext();
	
	/**
	 * Shut down the AEM client, e.g. to get rid of all in-memory resources, registered services, etc.
	 *
	 * @return the client instance
	 */
	IAemClient shutDown();
	
	/**
	 * Start the client to registered basic services, factories and adapters.
	 *
	 * @return the client instance
	 * @throws Exception
	 *          in case services, factories or adapters cannot be created or registered
	 */
	IAemClient startUp() throws Exception;
	
	/**
	 * Create a request builder object to manipulate a SlingHttpServletRequest. It can be used to manipulate the current request
	 * instance passed into your tests.
	 *
	 * @return a builder instance; never null
	 * @throws Exception in case the builder cannot be created, e.g. due to missing non-optional properties
	 * @see {@link SlingHttpServletRequestBuilder#parent(SlingHttpServletRequest)}
	 */
	SlingHttpServletRequestBuilder aRequest() throws Exception;
	
	/**
	 * Set the current page in the request (via {@link ComponentContext}).
	 * This also sets the current resource to the content resource of the page.
	 * You can set it to a different resources afterwards if required.
	 *
	 * @param pageResource resource of the page; must not be null
	 * @return the client instance
	 * @throws Exception in case the page resource cannot be changed
	 */
	IAemClient setCurrentPage(Resource pageResource) throws Exception;
	
	/**
	 * Load test content from JSON into the AEM test instance.
	 *
	 * @param classpathResource path to a JSON file on the classpath; must be valid
	 * @param destPath          path to which the JSON content is loaded to; must be valid
	 * @return the client instance
	 * @throws Exception in case the content cannot be loaded
	 */
	IAemClient loadJson(String classpathResource, String destPath) throws Exception;
}
