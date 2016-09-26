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
package com.quatico.base.aem.test.api.setup;


import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.api.builders.SlingHttpServletRequestBuilder;
import com.quatico.base.aem.test.api.builders.UserManagerBuilder;
import com.quatico.base.aem.test.api.services.IServiceBuilder;
import com.quatico.base.aem.test.api.services.IServiceBuilder.InjectedService;
import com.quatico.base.aem.test.api.services.ServiceInjector;
import com.quatico.base.aem.test.model.Properties;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.osgi.framework.BundleContext;

import com.day.cq.wcm.api.PageManager;

import io.wcm.testing.mock.aem.builder.ContentBuilder;
import io.wcm.testing.mock.aem.context.AemContextImpl;


public class AemClient extends AemContextImpl implements IAemClient {
	
	public AemClient(Type type) {
		this.resourceResolverType = type.resolverType;
		
		// user default rule that directly executes each test method once
		setResourceResolverType(this.resourceResolverType);
	}
	
	@Override
	public IAemClient setUserLocale(Locale userLocale) {
		when(getRequest().getLocale()).thenReturn(userLocale);
		return this;
	}
	
	public IAemClient addBundle(String baseName, Locale locale) {
		when(getRequest().getResourceBundle(locale)).thenReturn(ResourceBundle.getBundle(baseName, locale));
		return this;
	}
	
	@Override
	public <R> R require(Class<R> serviceClass, Object... properties) throws Exception {
		return require(new InjectedService<R>(serviceClass, mock(serviceClass, RETURNS_DEEP_STUBS)), properties);
	}
	
	@Override
	public <R> R require(InjectedService<R> service, Object... properties) throws Exception {
		registerService(service.getType(), service.getInstance(), new Properties(properties).toMap());
		return service.getInstance();
	}
	
	@Override
	public <R> R require(IServiceBuilder<R> builder, Object... properties) throws Exception {
		return require(builder.getService(new ServiceInjector<>()), properties);
	}
	
	@Override
	public <R, C extends R> InjectedService<R> service(Class<R> type, C service, InjectedService<?>... requiredServices) throws Exception {
		for (InjectedService<?> cur : requiredServices) {
			require(cur);
		}
		
		return new InjectedService<>(type, new ServiceInjector<R>(requiredServices).create(service));
	}
	
	@Override
	public <R> InjectedService<R> service(R service, InjectedService<?>... requiredServices) throws Exception {
		//noinspection unchecked
		return service((Class<R>) service, service, requiredServices);
	}
	
	@Override
	public <R> InjectedService<R> service(IServiceBuilder<R> builder, InjectedService<?>... requiredServices) throws Exception {
		for (InjectedService<?> cur : requiredServices) {
			require(cur);
		}
		
		return builder.getService(new ServiceInjector<>(requiredServices));
	}
	
	@Override
	public SlingScriptHelper getSlingScriptHelper() {
		return slingScriptHelper();
	}
	
	@Override
	public SlingHttpServletRequest getRequest() {
		// TODO(bug?) Sling Request does not support resource / resourcePath / pathInfo properly
		if (request == null) {
			request();
		}
		if (!mockingDetails(request).isSpy()) {
			request = spy(request);
		}
		return request;
	}
	
	@Override
	public SlingHttpServletResponse getResponse() {
		if (response() == null) {
			response();
		}
		if (!mockingDetails(response).isSpy()) {
			response = spy(response);
		}
		return response;
		
	}
	
	@Override
	public PageManager getPageManager() {
		return pageManager();
	}
	
	@Override
	public ContentBuilder getContentBuilder() {
		return create();
	}
	
	@Override
	public ResourceResolver getResourceResolver() {
		return resourceResolver();
	}
	
	public ResourceResolverFactory getResourceResolverFactory() {
		return resourceResolverFactory;
	}
	
	@Override
	public BundleContext getBundleContext() {
		return super.bundleContext();
	}
	
	public AemClient shutDown() {
		super.tearDown();
		return this;
	}
	
	public AemClient startUp() throws Exception {
		registerDefaultServices();
		registerAdapter(ResourceResolver.class, UserManager.class, new UserManagerBuilder().build());
		super.setUp();
		
		return this;
	}
	
	@Override
	public SlingHttpServletRequestBuilder aRequest() throws Exception {
		return new SlingHttpServletRequestBuilder(this);
	}
	
	public IAemClient setCurrentPage(Resource pageResource) {
		super.currentPage(pageResource.getPath());
		return this;
	}
	
	public IAemClient loadJson(String resourcePath, String destPath) {
		super.load().json(resourcePath, destPath);
		return this;
	}
	
	public enum Type {
		Unit(ResourceResolverType.JCR_MOCK), Integration(ResourceResolverType.JCR_OAK);
		
		private ResourceResolverType resolverType;
		
		Type(ResourceResolverType resolverType) {
			this.resolverType = resolverType;
		}
	}
	
}
