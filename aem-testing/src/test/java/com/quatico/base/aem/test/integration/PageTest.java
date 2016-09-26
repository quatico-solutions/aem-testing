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
package com.quatico.base.aem.test.integration;


import static com.quatico.base.aem.test.api.setup.AemClient.Type.Integration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.api.setup.AemClient;
import com.quatico.base.aem.test.api.values.PageType;
import com.quatico.base.aem.test.api.values.ResourceType;
import com.quatico.base.aem.test.common.PageTestDriver;
import com.quatico.base.aem.test.model.ResourceProperty;

import org.apache.sling.api.resource.ValueMap;
import org.junit.Test;

import com.day.cq.wcm.api.Page;


public class PageTest extends PageTestDriver {
	
	@Override
	protected IAemClient createClient() throws Exception {
		return new AemClient(Integration);
	}
	
	@Test
	public void adaptToWithValueMapReturnsPagesProperties() throws Exception {
		Page target = aPage("/content/ko");
		
		ValueMap actual = target.adaptTo(ValueMap.class);
		
		// jcr:created, jcr:createdBy are added
		assertEquals(target.getProperties().size(), actual.size() + 2);
	}
	
	@Test
	public void adaptToWithValueMapReturnsValidDefaultValues() throws Exception {
		ValueMap actual = aPage("/content/test/ko").adaptTo(ValueMap.class);
		
		assertNotNull(actual.get(ResourceProperty.CREATED));
		assertEquals("admin", actual.get(ResourceProperty.CREATED_BY));
		assertEquals(ResourceType.PAGE_TYPE, actual.get(ResourceProperty.PRIMARY_TYPE));
	}
	
	@Test
	public void getPropertiesWithValuePageReturnsValidDefaultProperties() throws Exception {
		Page target = aPage("/content/test/ko");
		
		ValueMap actual = target.getProperties("");
		
		assertNotNull(actual.get(ResourceProperty.CREATED));
		assertEquals("admin", actual.get(ResourceProperty.CREATED_BY));
		assertEquals("ko", actual.get(ResourceProperty.TITLE));
		assertEquals(ResourceType.PAGE_CONTENT_TYPE, actual.get(ResourceProperty.PRIMARY_TYPE));
		assertEquals(PageType.DEFAULT_TEMPLATE.getTemplate().getName(), actual.get(ResourceProperty.TEMPLATE));
	}
	
	@Test
	public void getContentResourceWithValidPageReturnsValidDefaultProperties() throws Exception {
		ValueMap actual = aPage("/content/test/ko").getContentResource().adaptTo(ValueMap.class);
		
		assertNotNull(actual.get(ResourceProperty.CREATED));
		assertEquals("admin", actual.get(ResourceProperty.CREATED_BY));
		assertEquals("ko", actual.get(ResourceProperty.TITLE));
		assertEquals(ResourceType.PAGE_CONTENT_TYPE, actual.get(ResourceProperty.PRIMARY_TYPE));
		assertEquals(PageType.DEFAULT_TEMPLATE.getTemplate().getName(), actual.get(ResourceProperty.TEMPLATE));
	}
}
