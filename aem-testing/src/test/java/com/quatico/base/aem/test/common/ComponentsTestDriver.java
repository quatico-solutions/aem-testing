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


import static com.quatico.base.aem.test.model.ResourceProperty.RESOURCE_TYPE;
import static junit.framework.TestCase.assertEquals;

import com.quatico.base.aem.test.TestDriver;
import com.quatico.base.aem.test.api.setup.Components;
import com.quatico.base.aem.test.api.setup.Pages;
import com.quatico.base.aem.test.api.setup.Resources;
import com.quatico.base.aem.test.api.values.ComponentType;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Test;


public abstract class ComponentsTestDriver extends TestDriver {
	
	protected Components testObj;
	protected Pages pages;
	
	@Before
	public void setUp() throws Exception {
		this.pages = new Pages(client);
		this.testObj = new Components(new Resources(client), client);
	}
	
	@Test
	public void aParSysWithValidPageParentReturnsValidParsys() throws Exception {
		Resource page   = pages.aPageWithParents("/content/page");
		Resource target = testObj.aParSys(page, "content");
		
		assertEquals(ComponentType.PARSYS.getName(), target.getValueMap().get(RESOURCE_TYPE));
		assertEquals("/content/page/jcr:content/content", target.getPath());
	}
	
	@Test(expected = IllegalStateException.class)
	public void aParSysWithNullParentThrowsException() throws Exception {
		testObj.aParSys(null, "name");
	}
	
	@Test
	public void aFolderWithValidPageParentReturnsValidFolder() throws Exception {
		Resource page   = pages.aPageWithParents("/content/page");
		Resource target = testObj.aFolder(page, "foobar");
		
		assertEquals(ComponentType.FOLDER.getName(), target.getValueMap().get(RESOURCE_TYPE));
		assertEquals("/content/page/jcr:content/foobar", target.getPath());
	}
	
	@Test(expected = IllegalStateException.class)
	public void aFolderWithNullParentThrowsException() throws Exception {
		testObj.aParSys(null, "name");
	}
	
}
