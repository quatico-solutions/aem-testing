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
package com.quatico.base.aem.test.api.builders;


import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.model.Properties;

import java.util.Map;

import org.apache.sling.commons.testing.osgi.MockBundle;
import org.apache.sling.commons.testing.osgi.MockComponentContext;
import org.osgi.service.component.ComponentContext;


public class ComponentContextBuilder extends AbstractBuilder<ComponentContext> {
	
	private final IAemClient client;
	
	public ComponentContextBuilder(IAemClient client) {
		super(ComponentContext.class);
		this.client = client;
		properties(new Object[0]);
	}
	
	public Object[] properties() {
		return getValue("properties", Object[].class);
	}
	
	public ComponentContextBuilder properties(Object... pairs) {
		return addValue("properties", pairs);
	}
	
	@Override
	protected ComponentContext internalBuild() throws Exception {
		MockComponentContext result = new MockComponentContext((MockBundle) client.getBundleContext().getBundle());
		
		Map<String, Object> properties = new Properties(properties()).toMap();
		for (String key : properties.keySet()) {
			result.setProperty(key, properties.get(key));
		}
		
		return result;
	}
}
