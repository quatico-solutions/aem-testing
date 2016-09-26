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
package com.quatico.base.aem.test.common;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mockingDetails;

import com.quatico.base.aem.test.TestDriver;
import com.quatico.base.aem.test.api.services.IServiceBuilder;
import com.quatico.base.aem.test.api.services.IServiceBuilder.InjectedService;
import com.quatico.base.aem.test.api.services.IServiceInjector;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.Test;


public abstract class ServiceTestDriver extends TestDriver {
	
	@Test
	public void requireInjectedServiceWithValidServiceYieldsSameInstanceFromBundleContext() throws Exception {
		SampleServiceImpl expected = new SampleServiceImpl();
		
		client.require(new InjectedService<SampleService>(SampleService.class, expected));
		
		assertSame(expected, client.getBundleContext().getService(client.getBundleContext().getServiceReference(SampleService.class)));
	}
	
	@Test
	public void requireInjectedServiceWithValidServiceYieldsSameInstanceFromSlingScriptHelper() throws Exception {
		SampleServiceImpl expected = new SampleServiceImpl();
		
		client.require(new InjectedService<SampleService>(SampleService.class, expected));
		
		assertSame(expected, client.getSlingScriptHelper().getService(SampleService.class));
	}
	
	@Test
	public void requireClassWithValidClassYieldsMockedServiceInstance() throws Exception {
		client.require(SampleService.class);
		
		SampleService actual = client.getSlingScriptHelper().getService(SampleService.class);
		
		assertTrue(mockingDetails(actual).isMock());
	}
	
	@Test
	public void requireServiceBuilderWithValidBuilderYieldsSameServiceInstance() throws Exception {
		IServiceBuilder<SampleService> target = new IServiceBuilder<SampleService>() {
			
			@Override
			public InjectedService<SampleService> getService(IServiceInjector<SampleService> injector) throws Exception {
				return new InjectedService<SampleService>(SampleService.class, injector.create(getResult()));
			}
			
			@Override
			public SampleService build() throws Exception {
				return getResult();
			}
			
			@Override
			public SampleService getResult() {
				return new SampleServiceImpl();
			}
		};
		
		client.require(target);
		
		assertNotNull(client.getSlingScriptHelper().getService(SampleService.class));
	}
	
	@Test
	public void serviceWithRequiredServiceInjectsInstanceVariable() throws Exception {
		ResourceResolverFactory expected = client.getResourceResolverFactory();
		
		InjectedService<SampleService> actual = client.service(SampleService.class, new SampleServiceImpl(), new InjectedService<ResourceResolverFactory>(ResourceResolverFactory.class, expected));
		
		assertSame(expected, actual.getInstance().getResourceResolverFactory());
	}
	
	private interface SampleService {
		ResourceResolverFactory getResourceResolverFactory();
	}
	
	
	@Component(immediate = true)
	@Service(SampleService.class)
	private static class SampleServiceImpl implements SampleService {
		@Reference
		private ResourceResolverFactory resourceResolverFactory;
		
		public ResourceResolverFactory getResourceResolverFactory() {
			return resourceResolverFactory;
		}
	}
}
