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
package com.quatico.base.aem.test.unit;


import static com.quatico.base.aem.test.api.setup.AemClient.Type.Unit;
import static org.junit.Assert.assertNotNull;

import com.quatico.base.aem.test.api.setup.AemClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class AemClientTest {
	private AemClient testObj;
	
	@Before
	public void setUp() throws Exception {
		this.testObj = new AemClient(Unit).startUp();
	}
	
	@After
	public void tearDown() throws Exception {
		testObj.shutDown();
	}
	
	@Test
	public void getPageManagerReturnsNotNull() throws Exception {
		assertNotNull(testObj.getPageManager());
	}
	
	@Test
	public void getContentBuilderReturnsNotNull() throws Exception {
		assertNotNull(testObj.getContentBuilder());
	}
	
	@Test
	public void getResourceResolverReturnsNotNull() throws Exception {
		assertNotNull(testObj.getResourceResolver());
	}
	
	@Test
	public void getBundleContextReturnsNotNull() throws Exception {
		assertNotNull(testObj.bundleContext());
	}
	
	@Test
	public void getRequestReturnsNotNull() throws Exception {
		assertNotNull(testObj.getRequest());
	}
	
	//TODO(clean up tests)
	@Test
	public void getRequest1() throws Exception {
	}
	
	@Test
	public void addBundle() throws Exception {
		
	}
	
	@Test
	public void setUserLocale() throws Exception {
		
	}
	
	@Test
	public void require() throws Exception {
		
	}
	
	@Test
	public void require1() throws Exception {
		
	}
	
	@Test
	public void require2() throws Exception {
		
	}
	
	@Test
	public void service() throws Exception {
		
	}
	
	@Test
	public void service1() throws Exception {
		
	}
	
}