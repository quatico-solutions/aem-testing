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
import static com.quatico.base.aem.test.model.ResourceProperty.NAME;
import static com.quatico.base.aem.test.model.ResourceProperty.TITLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import com.quatico.base.aem.test.TestDriver;
import com.quatico.base.aem.test.api.setup.Resources;
import com.quatico.base.aem.test.model.ResourceProperty;

import java.util.Iterator;

import javax.jcr.Node;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.junit.Before;
import org.junit.Test;


public abstract class ResourceTestDriver extends TestDriver {
	private Resources testObj;
	
	@Before
	public void setUp() throws Exception {
		this.testObj = new Resources(client);
	}
	
	@Test
	public void aResourceWithValidPathReturnsResourceWithPath() throws Exception {
		String expected = "/content/foobar";
		
		Resource actual = testObj.aResource(expected);
		
		assertEquals(expected, actual.getPath());
	}
	
	@Test
	public void aResourceWithNonExistingPathReturnsNull() throws Exception {
		assertNull(testObj.aResource("DOESNOTEXIST"));
	}
	
	@Test
	public void aResourceWithSingleSegmentRelativePathReturnsNull() throws Exception {
		assertNull(testObj.aResource("path"));
	}
	
	@Test
	public void aResourceWithNullPathReturnsRootResource() throws Exception {
		Resource actual = testObj.aResource(null);
		
		assertEquals("/", actual.getPath());
	}
	
	@Test
	public void aResourceWithEmptyPathReturnsRootResource() throws Exception {
		Resource actual = testObj.aResource(StringUtils.EMPTY);
		
		assertEquals("/", actual.getPath());
	}
	
	@Test
	public void aResourceWithValidPathAndNoPropertiesYieldsResourceTypeProperty() throws Exception {
		Resource actual = testObj.aResource("/content/foobar");
		
		assertEquals("nt:unstructured", actual.adaptTo(Node.class).getProperty(ResourceProperty.PRIMARY_TYPE).getString());
	}
	
	@Test
	public void aResourceWithValidPathAndPropertiesYieldsNonEmptyProperties() throws Exception {
		Resource actual = testObj.aResource("/content/foobar", NAME, "foobar");
		
		Node node = actual.adaptTo(Node.class);
		assertEquals("foobar", node.getProperty(NAME).getValue().getString());
	}
	
	@Test
	public void aResourceWithValidPathAndMultiplePropertiesYieldsCorrectProperties() throws Exception {
		Node actual = testObj.aResource("/content/foobar", TITLE, "foobar", ResourceProperty.TEMPLATE, "/libs/foobar/pages/page").adaptTo(Node.class);
		
		assertEquals("/libs/foobar/pages/page", actual.getProperty(ResourceProperty.TEMPLATE).getValue().getString());
		assertEquals("foobar", actual.getProperty(TITLE).getValue().getString());
	}
	
	@Test
	public void getNameWithMultiSegmentPathReturnsLastPathSegment() throws Exception {
		Resource target = testObj.aResource("/a/resource/path");
		
		assertEquals("path", target.getName());
	}
	
	@Test
	public void getNameWithSingleSegmentPathReturnsPathSegment() throws Exception {
		Resource target = testObj.aResource("/path");
		
		assertEquals("path", target.getName());
	}
	
	@Test
	public void getNameWithMultiSegmentRelativePathReturnsLastPathSegment() throws Exception {
		Resource target = testObj.aResource("a/resource/path");
		
		assertEquals("path", target.getName());
	}
	
	@Test
	public void getNameWithNoPathSegmentPathReturnsEmptyString() throws Exception {
		Resource target = testObj.aResource("/");
		
		assertEquals("", target.getName());
	}
	
	@Test
	public void getNameWithNullPathReturnsEmptyString() throws Exception {
		Resource target = testObj.aResource(null);
		
		assertEquals(StringUtils.EMPTY, target.getName());
	}
	
	@Test
	public void getResourceResolverReturnsTheSameInstance() throws Exception {
		Resource target = testObj.aResource(null);
		
		assertSame(client.getResourceResolver(), target.getResourceResolver());
	}
	
	@Test
	public void getParentWithParentReturnsParentResource() throws Exception {
		Resource target = testObj.aResource("/content/ko");
		
		Resource actual = target.getParent();
		
		assertThat(actual, resourceExistsAt("/content"));
	}
	
	@Test
	public void getParentWithNoParentReturnsNull() throws Exception {
		Resource target = testObj.aResource("/");
		
		assertNull(target.getParent());
	}
	
	@Test
	public void getParentWithFirstChildReturnsRootResource() throws Exception {
		Resource target = testObj.aResource("/content");
		
		assertEquals("/", target.getParent().getPath());
	}
	
	@Test
	public void getParentWithParentFindsAllParentResources() throws Exception {
		Resource target = testObj.aResource("/content/ko/home");
		
		assertThat(target.getParent(), resourceExistsAt("/content/ko"));
		assertThat(target.getParent().getParent(), resourceExistsAt("/content"));
		assertEquals("/", target.getParent().getParent().getParent().getPath());
		assertNull(target.getParent().getParent().getParent().getParent());
	}
	
	@Test
	public void getChildrenWithNoChildrenReturnsNoChildren() throws Exception {
		testObj.aResource("/content/ko");
		Resource target = testObj.aResource("/content/ko/something");
		
		Iterator<Resource> actual = target.getChildren().iterator();
		
		assertFalse(actual.hasNext());
	}
	
	@Test
	public void getChildrenWithChildrenReturnsAllDirectChildren() throws Exception {
		Resource target = testObj.aResource("/content/ko");
		testObj.aResource("/content/ko/one");
		testObj.aResource("/content/ko/two");
		
		Iterator<Resource> actual = target.getChildren().iterator();
		assertEquals("one", actual.next().getName());
		assertEquals("two", actual.next().getName());
		assertFalse(actual.hasNext());
		
	}
	
	@Test
	public void listChildrenWithNoChildrenReturnsNoChildren() throws Exception {
		testObj.aResource("/content/ko");
		Resource target = testObj.aResource("/content/ko/something");
		
		Iterator<Resource> actual = target.listChildren();
		
		assertFalse(actual.hasNext());
	}
	
	@Test
	public void listChildrenWithChildrenReturnsAllDirectChildren() throws Exception {
		Resource target = testObj.aResource("/content/ko");
		testObj.aResource("/content/ko/one");
		testObj.aResource("/content/ko/two");
		
		Iterator<Resource> actual = target.listChildren();
		
		assertEquals("one", actual.next().getName());
		assertEquals("two", actual.next().getName());
		assertFalse(actual.hasNext());
		
	}
	
	@Test
	public void listChildrenWithNonExistingResourceReturnsNoChildren() throws Exception {
		Resource target = client.getResourceResolver().resolve("DOESNOTEXIST");
		
		Iterator<Resource> actual = target.listChildren();
		
		assertFalse(actual.hasNext());
	}
	
	@Test
	public void getChildWithNullStringReturnsNull() throws Exception {
		Resource target = testObj.aResource("/content");
		
		assertNull(target.getChild(null));
	}
	
	@Test
	public void getChildWithEmptyStringReturnsNull() throws Exception {
		Resource target = testObj.aResource("/content");
		
		assertEquals(target.getPath(), target.getChild("").getPath());
	}
	
	@Test
	public void getChildWithNonExistingPathReturnsNull() throws Exception {
		Resource target = testObj.aResource("/content");
		
		assertNull(target.getChild("DOESNOTEXIST"));
	}
	
	@Test
	public void getChildWithSameRelativeParentPathReturnsSameResource() throws Exception {
		Resource target = testObj.aResource("/content/ko");
		testObj.aResource("/content/ko/something");
		
		Resource actual = target.getChild("../ko");
		
		assertEquals(target.getPath(), actual.getPath());
	}
	
	@Test
	public void getChildWithRelativeChildPathReturnsChildResource() throws Exception {
		Resource target = testObj.aResource("/content/ko");
		testObj.aResource("/content/ko/something");
		
		Resource actual = target.getChild("./something");
		
		assertThat(actual, resourceExistsAt("/content/ko/something"));
	}
	
	@Test
	public void getChildWithChildPathReturnsChildResource() throws Exception {
		Resource target = testObj.aResource("/content/ko");
		testObj.aResource("/content/ko/something");
		
		Resource actual = target.getChild("something");
		
		assertThat(actual, resourceExistsAt("/content/ko/something"));
	}
	
	@Test
	public void getChildWithRelativePathReturnsChildResource() throws Exception {
		Resource target = testObj.aResource("/content");
		testObj.aResource("/content/ko");
		testObj.aResource("/content/ko/something");
		
		Resource actual = target.getChild("./ko/something");
		
		assertThat(actual, resourceExistsAt("/content/ko/something"));
	}
	
	@Test
	public void getChildWithPathReturnsResource() throws Exception {
		Resource target = testObj.aResource("/content");
		testObj.aResource("/content/ko");
		testObj.aResource("/content/ko/something");
		
		Resource actual = target.getChild("ko/something");
		
		assertThat(actual, resourceExistsAt("/content/ko/something"));
	}
	
	@Test
	public void getChildWithAbsolutePathReturnsResource() throws Exception {
		Resource target = testObj.aResource("/content");
		
		Resource actual = target.getChild("/content");
		
		assertEquals(target.getPath(), actual.getPath());
	}
	
	@Test
	public void getResourceMetaDataWithExistingResourceContainsResolutionPathOnly() throws Exception {
		Resource target = testObj.aResource("/content");
		
		ResourceMetadata actual = target.getResourceMetadata();
		
		assertEquals("/content", actual.get("sling.resolutionPath"));
	}
	
	@Test
	public void getResourceWithValidPathReturnsValidResourceWithValidProperties() throws Exception {
		Resource target = testObj.aResource("/content/resource");
		
		assertNotNull(target.getValueMap().get(ResourceProperty.PRIMARY_TYPE));
	}
}