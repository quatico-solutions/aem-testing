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

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.commons.testing.osgi.MockServiceReference;
import org.osgi.framework.ServiceReference;


public class ServiceReferenceBuilder extends AbstractBuilder<ServiceReference> {
	private IAemClient client;
	
	public ServiceReferenceBuilder(IAemClient client) {
		super(ServiceReference.class);
		this.client = client;
		addValue("properties", new HashMap<String, Object>());
	}
	
	public ServiceReferenceBuilder property(String key, Object value) {
		properties().put(key, value);
		return this;
	}
	
	public Map<String, Object> properties() {
		return getValue("properties");
	}
	
	@Override
	protected ServiceReference internalBuild() throws Exception {
		MockServiceReference result = new MockServiceReference(client.getBundleContext().getBundle());
		properties().forEach(result::setProperty);
		this.result = result;
		return this.result;
	}
}
