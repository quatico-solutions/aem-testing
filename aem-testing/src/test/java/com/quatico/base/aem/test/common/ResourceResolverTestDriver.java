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


import static com.quatico.base.aem.test.api.AemMatchers.resourceExistsAt;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;
import static com.day.cq.commons.jcr.JcrConstants.JCR_PRIMARYTYPE;

import com.quatico.base.aem.test.TestDriver;
import com.quatico.base.aem.test.api.setup.Pages;
import com.quatico.base.aem.test.api.setup.Resources;
import com.quatico.base.aem.test.model.Properties;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Before;
import org.junit.Test;


public abstract class ResourceResolverTestDriver extends TestDriver {
	
	protected ResourceResolver testObj;
	private   Pages            pages;
	private   Resources        resources;
	
	@Before
	public void setUp() throws Exception {
		this.testObj = client.getResourceResolver();
		this.pages = new Pages(client);
		this.resources = new Resources(client);
	}
	
	@Test
	public void getResourceStringWithNullStringReturnsNull() throws Exception {
		assertNull(this.testObj.getResource(null));
	}
	
	@Test
	public void getResourceStringWithEmptyStringReturnsNull() throws Exception {
		assertNull(this.testObj.getResource(EMPTY));
	}
	
	@Test
	public void getResourceStringWithNonExistingPathReturnsNull() throws Exception {
		assertNull(this.testObj.getResource("DOESNOTEXIST"));
	}
	
	@Test
	public void getResourceStringWithExistingStringReturnsResource() throws Exception {
		resources.aResource("/content/test");
		
		Resource actual = this.testObj.getResource("/content/test");
		
		assertThat(actual, resourceExistsAt("/content/test"));
	}
	
	@Test
	public void getResourceResourceStringWithNullResourceAndStringReturnsNull() throws Exception {
		assertNull(this.testObj.getResource(null, null));
	}
	
	@Test
	public void getResourceResourceStringWithNullResourceAndEmptyStringReturnsNull() throws Exception {
		assertNull(this.testObj.getResource(null, EMPTY));
	}
	
	@Test
	public void getResourceResourceStringWithNullResourceAndNonExistingStringReturnsNull() throws Exception {
		assertNull(this.testObj.getResource(null, "DOESNOTEXIST"));
	}
	
	@Test
	public void getResourceResourceStringWithNullResourceAndExistingStringReturnsResource() throws Exception {
		resources.aResource("/content/test");
		Resource actual = this.testObj.getResource(null, "/content/test");
		
		assertThat(actual, resourceExistsAt("/content/test"));
	}
	
	@Test
	public void getResourceResourceStringWithExistingResourceAndNullStringReturnsNull() throws Exception {
		Resource target = this.testObj.getResource("/content/test");
		
		assertNull(this.testObj.getResource(target, null));
	}
	
	@Test
	public void getResourceResourceStringWithExistingResourceAndEmptyStringReturnsNull() throws Exception {
		Resource target = this.testObj.getResource("/content/test");
		
		assertEquals(target, this.testObj.getResource(target, EMPTY));
	}
	
	@Test
	public void getResourceResourceStringWithExistingResourceAndNonExistingStringReturnsNull() throws Exception {
		Resource target = this.testObj.getResource("/content/test");
		
		assertNull(this.testObj.getResource(target, "DOESNOTEXIST"));
	}
	
	@Test
	public void getResourceResourceStringWithExistingResourceAndRelativeParentPathReturnsDifferentChildOfParent() throws Exception {
		pages.aPageWithParents("/content/test/de");
		Resource target = pages.aPageWithParents("/content/test/ko");
		
		Resource actual = this.testObj.getResource(target, "../de");
		
		assertThat(actual, resourceExistsAt("/content/test/de"));
	}
	
	@Test
	public void getResourceResourceStringWithExistingResourceAndRelativeChildPathReturnsChildResource() throws Exception {
		pages.aPageWithParents("/content/test/ko/home");
		Resource target = pages.aPageWithParents("/content/test/ko");
		
		Resource actual = this.testObj.getResource(target, "./home");
		
		assertThat(actual, resourceExistsAt("/content/test/ko/home"));
	}
	
	@Test
	public void getResourceResourceStringWithExistingResourceAndChildPathReturnsChildResource() throws Exception {
		pages.aPageWithParents("/content/test/ko/home/something");
		Resource target = this.testObj.getResource("/content/test/ko");
		
		Resource actual = this.testObj.getResource(target, "home");
		
		assertThat(actual, resourceExistsAt("/content/test/ko/home"));
	}
	
	@Test
	public void getResourceResourceStringWithExistingResourceAndRelativePathReturnsChildResource() throws Exception {
		pages.aPageWithParents("/content/test/ko/home/something");
		Resource target = pages.aPageWithParents("/content/test/ko");
		
		Resource actual = this.testObj.getResource(target, "./home/something");
		
		assertThat(actual, resourceExistsAt("/content/test/ko/home/something"));
	}
	
	@Test
	public void getResourceResourceStringWithExistingResourceAndPathReturnsResource() throws Exception {
		pages.aPageWithParents("/content/test/ko/home/something");
		Resource target = this.testObj.getResource("/content/test/ko");
		
		Resource actual = this.testObj.getResource(target, "home/something");
		
		assertThat(actual, resourceExistsAt("/content/test/ko/home/something"));
	}
	
	@Test
	public void getResourceResourceStringWithExistingResourceAndExistingAbsolutePathReturnsResource() throws Exception {
		Resource target = this.testObj.getResource("/content/test");
		
		Resource actual = this.testObj.getResource(target, "/content/test");
		
		assertEquals(target, actual);
	}
	
	@Test
	public void resolveStringWithEmptyStringReturnsRootResource() throws Exception {
		Resource actual = this.testObj.resolve(EMPTY);
		
		assertEquals("/", actual.getPath());
	}
	
	@Test
	public void resolveStringWithNullStringReturnsRootResource() throws Exception {
		//noinspection ConstantConditions
		Resource actual = this.testObj.resolve((String) null);
		
		assertEquals("/", actual.getPath());
	}
	
	@Test
	public void resolveStringWithAbsolutePathToExistingResourceReturnsResource() throws Exception {
		resources.aResource("/content/test");
		
		Resource actual = this.testObj.resolve("/content/test");
		
		assertThat(actual, resourceExistsAt("/content/test"));
	}
	
	@Test
	public void resolveStringWithAbsoluteMultiPathToExistingResourceReturnsResource() throws Exception {
		pages.aPageWithParents("/content/test/ko/home/something");
		
		Resource actual = this.testObj.resolve("/content/test/ko/home/something");
		
		assertThat(actual, resourceExistsAt("/content/test/ko/home/something"));
	}
	
	@Test
	public void resolveStringWithRelativePathToExistingResourceReturnsResource() throws Exception {
		resources.aResource("/content/test");
		
		Resource actual = this.testObj.resolve("content/test");
		
		assertThat(actual, resourceExistsAt("/content/test"));
	}
	
	@Test
	public void resolveStringWithNonExistingResourceReturnsNull() throws Exception {
		assertEquals(Resource.RESOURCE_TYPE_NON_EXISTING, this.testObj.resolve("DOESNOTEXIST").getResourceType());
	}
	
	@Test
	public void resolveRequestResourceWithNullRequestAndEmptyStringReturnsNonExistingResource() throws Exception {
		Resource expected = this.testObj.resolve(EMPTY);
		
		assertEquals(expected.getPath(), this.testObj.resolve(null, EMPTY).getPath());
	}
	
	@Test
	public void resolveRequestResourceWithNullRequestAndNonExistingStringReturnsNonExistingResource() throws Exception {
		assertEquals(Resource.RESOURCE_TYPE_NON_EXISTING, this.testObj.resolve(null, "DOESNOTEXIST").getResourceType());
	}
	
	@Test
	public void resolveRequestResourceWithNullRequestAndExistingStringReturnsResource() throws Exception {
		resources.aResource("/content/test");
		
		Resource actual = this.testObj.resolve(null, "/content/test");
		
		assertThat(actual, resourceExistsAt("/content/test"));
	}
	
	@Test
	public void resolveRequestResourceWithExistingRequestAndNullStringReturnsRootResource() throws Exception {
		Resource actual = this.testObj.resolve(client.aRequest().resourcePath("/content/test").build(), null);
		
		assertEquals("/", actual.getPath());
	}
	
	@Test
	public void resolveRequestResourceWithExistingRequestAndEmptyStringReturnsRootResource() throws Exception {
		Resource actual = this.testObj.resolve(client.aRequest().resourcePath("/content/test").build(), EMPTY);
		
		assertEquals("/", actual.getPath());
	}
	
	@Test
	public void resolveRequestResourceWithExistingRequestAndNonExistingStringReturnsNonExistingResourceAndSamePath() throws Exception {
		Resource expected = this.testObj.resolve("/content/test");
		
		assertEquals(expected.getPath(), this.testObj.resolve(client.aRequest().resourcePath("/content/test").build(), "content/test").getPath());
	}
	
	@Test
	public void resolveRequestResourceWithExistingResourceAndExistingAbsolutePathReturnsResource() throws Exception {
		resources.aResource("/content/test");
		
		Resource actual = this.testObj.resolve(client.aRequest().resourcePath("/content/test").build(), "/content/test");
		
		assertThat(actual, resourceExistsAt("/content/test"));
	}
	
	@Test
	public void createWithParentAndNameCreatesResource() throws Exception {
		Resource target = pages.aPageWithParents("/content/test");
		assertNull(this.testObj.getResource("/content/test/foobar"));
		
		Resource actual = this.testObj.create(target, "foobar", new HashMap<>());
		
		assertEquals("foobar", actual.getName());
		assertThat(actual, resourceExistsAt("/content/test/foobar"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createWithnameContainginSlashThrowsIllegalArgumentException() throws Exception {
		Resource target = pages.aPageWithParents("/content/test");
		
		this.testObj.create(target, "/zipzap", new HashMap<>());
	}
	
	@Test
	public void createWithParentAndAbsolutePathStillCreatesChildResource() throws Exception {
		Resource target = pages.aPageWithParents("/content/test");
		assertNull(this.testObj.getResource("/content/test/zipzap"));
		
		Resource actual = this.testObj.create(target, "zipzap", new HashMap<>());
		
		assertEquals("zipzap", actual.getName());
		assertThat(actual, resourceExistsAt("/content/test/zipzap"));
	}
	
	@Test
	public void createWithParentAndNameAndPropertiesCreatesResourceWithProperties() throws Exception {
		Resource target = pages.aPageWithParents("/content/test");
		assertNull(this.testObj.getResource("/content/test/foobar"));
		
		Resource            testObj = this.testObj.create(target, "foobar", new Properties(JCR_PRIMARYTYPE, "sling:Folder", "hidden", "true").toMap());
		Map<String, Object> actual  = testObj.adaptTo(ValueMap.class);
		
		assertThat(testObj, resourceExistsAt("/content/test/foobar"));
		assertEquals("sling:Folder", actual.get(JCR_PRIMARYTYPE));
		assertEquals("true", actual.get("hidden"));
	}
	
	@Test(expected = NullPointerException.class)
	public void createWithParentResourceAndNullPathThrowsException() throws Exception {
		Resource target = this.testObj.getResource("/content/test");
		
		this.testObj.create(target, null, new HashMap<>());
	}
	
	@Test
	public void deleteWithExistingResourceDeletesResource() throws Exception {
		resources.aResource("/content/test");
		assertThat(this.testObj.create(this.testObj.getResource("/content/test"), "woohoo", new HashMap<>()), resourceExistsAt("/content/test/woohoo"));
		
		this.testObj.delete(this.testObj.getResource("/content/test/woohoo"));
		
		assertNull(this.testObj.getResource("/content/test/woohoo"));
	}
	
	@Test
	public void deleteWithNonExistingResourceDoesNothing() throws Exception {
		Resource target = this.testObj.resolve("DOESNOTEXITS");
		assertNotNull(target);
		
		this.testObj.delete(target);
	}
	
	@Test(expected = NullPointerException.class)
	public void deleteWithNullThrowsException() throws Exception {
		this.testObj.delete(null);
	}
	
	@Test
	public void getParentResourceTypeResourceWithNullResourceReturnsNull() throws Exception {
		assertNull(this.testObj.getParentResourceType((Resource) null));
	}
	
	@Test
	public void getParentWithPageResourceTypeSetReturnsSameResourceType() throws Exception {
		pages.aPageWithParents("/content/test/ko");
		Resource target   = this.testObj.getResource("/content/test/ko");
		String   expected = this.testObj.getResource("/content/test").getResourceType();
		
		assertEquals(expected, this.testObj.getParent(target).getResourceType());
	}
	
	@Test
	public void getParentResourceTypeWithPaheResourceTypeReturnsNull() throws Exception {
		pages.aPageWithParents("/content/test/ko");
		Resource target = this.testObj.getResource("/content/test/ko");
		
		assertNull(this.testObj.getParentResourceType(target));
	}
	
	@Test
	public void getParentResourceTypeResourceWithNoResourcePropertySetReturnsParentResourcesType() throws Exception {
		pages.aPageWithParents("/content/test/ko");
		Resource target   = this.testObj.getResource("/content/test/ko");
		String   expected = this.testObj.getResource("/content/test").getResourceType();
		
		assertEquals(expected, this.testObj.getParent(target).getResourceType());
	}
	
	@Test
	public void getParentResourceTypeResourceWithNoParentReturnsNull() throws Exception {
		Resource target = this.testObj.getResource("/");
		
		assertNull(this.testObj.getParentResourceType(target));
	}
	
	@Test
	public void getParentResourceTypeStringWithNullResourceReturnsNull() throws Exception {
		assertNull(this.testObj.getParentResourceType((String) null));
	}
	
	@Test
	public void getResourceTypeOfParentResourceStringWithConvertableResourceTypeToPathReturnsParentResourcesType() throws Exception {
		pages.aPageWithParents("/content/test/ko/home");
		Resource target   = this.testObj.getResource("/content/test/ko");
		String   expected = target.getResourceType();
		
		assertEquals(expected, this.testObj.getParent(target).getResourceType());
	}
	
	@Test
	public void getParentResourceTypeStringWithNoParentReturnsNull() throws Exception {
		assertNull(this.testObj.getParentResourceType("/"));
	}
	
	@Test
	public void getChildrenWithNoChildrenReturnsOneChild() throws Exception {
		pages.aPageWithParents("/content/test");
		Resource target = pages.aPageWithParents("/content/hello");
		
		Iterator<Resource> actual = this.testObj.getChildren(target).iterator();
		
		assertEquals("/content/hello/jcr:content", actual.next().getPath());
		assertFalse(actual.hasNext());
	}
	
	@Test
	public void getChildrenWithChildrenReturnsAllDirectChildren() throws Exception {
		pages.aPageWithParents("/content/test/ko");
		pages.aPageWithParents("/content/test/de");
		
		Resource target = this.testObj.getResource("/content/test");
		
		Iterator<Resource> actual = this.testObj.getChildren(target).iterator();
		
		assertEquals(JCR_CONTENT, actual.next().getName());
		assertEquals("ko", actual.next().getName());
		assertEquals("de", actual.next().getName());
		assertFalse(actual.hasNext());
		
	}
	
	@Test
	public void getChildrenWithNonExistingResourceReturnsNoChildren() throws Exception {
		Resource target = this.testObj.resolve("DOESNOTEXIST");
		
		Iterator<Resource> actual = this.testObj.getChildren(target).iterator();
		
		assertFalse(actual.hasNext());
	}
	
	@Test
	public void listChildrenWithOneChildrenReturnsNoChildren() throws Exception {
		pages.aPageWithParents("/content/test");
		Resource target = pages.aPageWithParents("/content/nothinghere");
		
		Iterator<Resource> actual = this.testObj.listChildren(target);
		
		assertTrue(actual.hasNext());
		actual.next();
		assertFalse(actual.hasNext());
	}
	
	@Test
	public void listChildrenWithNonExistingResourceReturnsNoChildren() throws Exception {
		Resource target = this.testObj.resolve("DOESNOTEXIST");
		
		Iterator<Resource> actual = this.testObj.listChildren(target);
		
		assertFalse(actual.hasNext());
	}
}
