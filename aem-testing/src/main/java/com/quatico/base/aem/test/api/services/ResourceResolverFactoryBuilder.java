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
package com.quatico.base.aem.test.api.services;


import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.api.builders.AbstractBuilder;

import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;


public class ResourceResolverFactoryBuilder extends AbstractBuilder<ResourceResolverFactory> {
	private IAemClient client;
	
	public ResourceResolverFactoryBuilder(IAemClient client) {
		super(ResourceResolverFactory.class);
		this.client = client;
	}
	
	@Override
	protected ResourceResolverFactory internalBuild() throws Exception {
		this.result = new ResourceResolverFactory() {
			@Override
			public ResourceResolver getResourceResolver(Map<String, Object> map) throws LoginException {
				return client.getResourceResolver();
			}
			
			@Override
			public ResourceResolver getAdministrativeResourceResolver(Map<String, Object> map) throws LoginException {
				return client.getResourceResolver();
			}
			
			@Nonnull
			@Override
			public ResourceResolver getServiceResourceResolver(Map<String, Object> authenticationInfo) throws LoginException {
				return client.getResourceResolver();
			}
			
			@Override
			public ResourceResolver getThreadResourceResolver() {
				return client.getResourceResolver();
			}
		};
		
		return result;
	}
}
