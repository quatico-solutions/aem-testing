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

import com.quatico.base.aem.test.TestDriver;
import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.api.setup.AemClient;
import com.quatico.base.aem.test.api.setup.Pages;
import com.quatico.base.aem.test.api.values.ResourceType;
import com.quatico.base.aem.test.model.ResourceProperty;

import org.apache.sling.api.resource.ValueMap;
import org.junit.Before;
import org.junit.Test;

import com.day.cq.wcm.api.Page;


public class PageFromJsonContentTest extends TestDriver {
	
	private Pages pages;
	
	@Override
	protected IAemClient createClient() throws Exception {
		AemClient client = new AemClient(Integration);
		pages = new Pages(client);
		return client;
	}
	
	@Before
	public void loadJson() throws Exception {
		client.loadJson("/sample-content.json", "/content/test/ko");
	}
	
	@Test
	public void getNameWithValidPagePathReturnsLastPathSegment() throws Exception {
		client.setCurrentPage(pages.aPageWithParents("/content/test/ko"));
		
		Page actual = client.getPageManager().getPage("/content/test/ko");
		
		assertEquals("ko", actual.getName());
	}
	
	@Test
	public void getPrimaryTypeWithValidPageReturnsPageContentType() throws Exception {
		client.setCurrentPage(pages.aPageWithParents("/content/test/ko"));
		
		ValueMap actual = client.getRequest().getResource().getValueMap();
		
		assertEquals(ResourceType.PAGE_CONTENT_TYPE, actual.get(ResourceProperty.PRIMARY_TYPE));
	}
	
	@Test
	public void getPageTitleWithValidPageReturnsPageTitleFromJson() throws Exception {
		client.setCurrentPage(pages.aPageWithParents("/content/test/ko"));
		
		Page actual = client.getPageManager().getPage("/content/test/ko");
		
		assertEquals("Handler Sample 2014", actual.getPageTitle());
	}
	
	@Test
	public void getDepthWithValidPageReturnsCorrectDepth() throws Exception {
		client.setCurrentPage(pages.aPageWithParents("/content/test/ko"));
		
		Page actual = client.getPageManager().getPage("/content/test/ko");
		
		assertEquals(3, actual.getDepth());
	}
}
