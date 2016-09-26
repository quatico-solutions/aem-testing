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


import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.api.IServices;
import com.quatico.base.aem.test.api.services.ResourceResolverFactoryBuilder;
import com.quatico.base.aem.test.api.services.ServiceReferenceBuilder;
import com.quatico.base.aem.test.api.services.SlingSettingsServiceBuilder;


public class Services implements IServices {
	
	protected IAemClient client;
	
	public Services(IAemClient client) {
		this.client = client;
	}
	
	@Override
	public SlingSettingsServiceBuilder aSettingsService() throws Exception {
		return new SlingSettingsServiceBuilder();
	}
	
	@Override
	public ResourceResolverFactoryBuilder aResourceResolverFactory() throws Exception {
		return new ResourceResolverFactoryBuilder(client);
	}
	
	@Override
	public ServiceReferenceBuilder aServiceReference() throws Exception {
		return new ServiceReferenceBuilder(client);
	}
}
