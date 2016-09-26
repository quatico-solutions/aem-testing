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


import static com.quatico.base.aem.test.api.AemMatchers.pageExistsAt;
import static com.quatico.base.aem.test.api.AemMatchers.resourceExistsAt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.quatico.base.aem.test.TestDriver;
import com.quatico.base.aem.test.api.setup.Pages;
import com.quatico.base.aem.test.model.ResourceProperty;

import java.util.Iterator;
import java.util.Locale;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Test;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.Template;


public abstract class PageTestDriver extends TestDriver {
	private Pages testObj;
	
	@Before
	public void setUp() throws Exception {
		this.testObj = new Pages(client);
	}
	
	@Test(expected = RuntimeException.class)
	public void aPageWithContentPathThrowsException() throws Exception {
		aPage("/content");
	}
	
	@Test
	public void adaptToWithResourceReturnsPagesResource() throws Exception {
		Page target = aPage("/content/test");
		
		Resource actual = target.adaptTo(Resource.class);
		
		assertEquals(client.getResourceResolver().getResource("/content/test").getPath(), actual.getPath());
	}
	
	@Test
	public void getDescriptionWithUnsetValueReturnsNull() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertNull(target.getDescription());
	}
	
	@Test
	public void getAbsoluteParentWithZeroReturnsAbsoluteparent() throws Exception {
		Page target = aPage("/content/test/ko/foobar").adaptTo(Page.class);
		
		assertEquals(target.getAbsoluteParent(0), target.getPageManager().getPage("/content"));
	}
	
	@Test
	public void getAbsoluteParentWithOneReturnsFirstChildOfContent() throws Exception {
		Page target = aPage("/content/test/ko/foobar");
		
		Page actual = target.getAbsoluteParent(1);
		
		assertThat(actual, pageExistsAt("/content/test"));
	}
	
	@Test
	public void getAbsoluteParentWithTwoReturns2ndChildOfContent() throws Exception {
		Page target = aPage("/content/test/ko/foobar");
		
		Page actual = target.getAbsoluteParent(2);
		
		assertThat(actual, pageExistsAt("/content/test/ko"));
	}
	
	@Test
	public void getAbsoluteParentWithTwoReturnsNull() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertNull(target.getAbsoluteParent(3));
	}
	
	@Test
	public void getContentResourceWithExistingConentReturnsContentResource() throws Exception {
		Page target = aPage("/content/test/ko");
		
		Resource actual = target.getContentResource();
		
		assertThat(actual, resourceExistsAt("/content/test/ko/jcr:content"));
	}
	
	@Test
	public void getContentResourceStringWithNullReturnsNull() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertNull(target.getContentResource(null));
	}
	
	@Test
	public void getContentResourceStringWithEmptyReturnPageContentResource() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertEquals(target.getContentResource().getPath(), target.getContentResource("").getPath());
	}
	
	@Test
	public void getContentResourceStringWithPathOfNonExistingChildContentPathReturnNull() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertNull(target.getContentResource("ko/entertainment/jcr:content/image"));
	}
	
	@Test
	public void getDepthWithPageReturnsCorrectAmountOfSlashes() throws Exception {
		Page target = aPage("/content/test");
		
		assertEquals(2, target.getDepth());
	}
	
	@Test
	public void getDepthWithChildOn3rdLevelOfContent() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertEquals(3, target.getDepth());
	}
	
	@Test
	public void getLanguageWithNoPropertySetReturnsNull() throws Exception {
		Page target = aPage("/content/foobar");
		
		Locale actual = target.getLanguage(true);
		
		assertEquals(Locale.getDefault(), actual);
	}
	
	@Test
	public void getLastModifiedReturnsNull() throws Exception {
		Page target = aPage("/content/aNewPage");
		
		assertThat(target, pageExistsAt(target.getPath()));
		assertNull(target.getLastModified());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getLockOwner() throws Exception {
		Page target = aPage("/content/test/ko");
		
		target.getLockOwner();
	}
	
	@Test
	public void getNameReturnsLastPathSegment() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertEquals("ko", target.getName());
	}
	
	@Test
	public void getNavigationTitleWithUnsetPropertyReturnsNull() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertNull(target.getNavigationTitle());
	}
	
	@Test
	public void getOffTimeWithUnsetPropertyReturnsNull() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertNull(target.getOffTime());
	}
	
	@Test
	public void getOnTimeWithUnsetPropertyReturnsNull() throws Exception {
		Page target = aPage("/content/test/ko");
		assertNull(target.getOnTime());
	}
	
	@Test
	public void getPageManager() throws Exception {
		Page target = aPage("/content/test/ko");
		
		target.getPageManager();
	}
	
	@Test
	public void getPageTitleWithTitlePropertyReturnsTitle() throws Exception {
		Page target = aPage("/content/test/ko", ResourceProperty.TITLE, "Test Page");
		
		assertEquals("Test Page", target.getTitle());
	}
	
	@Test
	public void getParentWithParentPageReturnsParent() throws Exception {
		Page target = aPage("/content/test/ko/foobar");
		
		Page actual = target.getParent();
		
		assertThat(actual, pageExistsAt("/content/test/ko"));
	}
	
	@Test
	public void getParentWithNoParentPageReturnsNull() throws Exception {
		Page target = aPage("/content/test");
		
		assertNull(target.getParent());
	}
	
	@Test
	public void getParentIntWithZeroReturnsPageItself() throws Exception {
		Page target = aPage("/content/test/ko/foobar");
		
		Page actual = target.getParent(0);
		
		assertThat(actual.adaptTo(Resource.class), resourceExistsAt(target.getPath()));
	}
	
	@Test
	public void getParentIntWithOneReturnsPageParent() throws Exception {
		Page target = aPage("/content/test/ko/foobar");
		
		Page actual = target.getParent(1);
		
		assertThat(actual, pageExistsAt("/content/test/ko"));
	}
	
	@Test
	public void getParentIntWithTwoReturnsPageParentsParent() throws Exception {
		Page target = aPage("/content/test/ko/foobar");
		
		Page actual = target.getParent(2);
		
		assertThat(actual, pageExistsAt("/content/test"));
	}
	
	@Test
	public void getParentIntWithThreeReturnsNull() throws Exception {
		Page target = aPage("/content/test/ko/foobar");
		
		Page actual = target.getParent(3);
		
		assertNull(actual);
	}
	
	@Test
	public void getPathReturnsPageResourcesPath() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertEquals("/content/test/ko", target.getPath());
	}
	
	@Test
	public void getPropertiesReturnNoneEmptyMap() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertFalse(target.getProperties().isEmpty());
	}
	
	@Test
	public void getPropertiesStringWithExistingChildPathReturnsChildsProperties() throws Exception {
		Page parent = aPage("/content/test/ko");
		Page target = aPage("/content/test/ko/foobar");
		
		assertEquals(target.getProperties().size(), parent.getProperties("").size());
	}
	
	@Test
	public void getPropertiesStringWithNonExistingPathReturnsNull() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertNull(target.getProperties("DOESNOTEXIST"));
	}
	
	@Test
	public void getPropertiesWithNullReturnsNull() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertNull(target.getProperties(null));
	}
	
	@Test
	public void getPropertiesStringWithEmptyStringReturnsPageContentProperties() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertEquals(target.getProperties().size(), target.getProperties("").size());
	}
	
	@Test
	public void getTags() throws Exception {
		Page target = aPage("/content/test/ko");
		
		target.getTags();
	}
	
	@Test
	public void getTemplateWithTemplatePropertyReturnsTemplate() throws Exception {
		Page target = aPage("/content/test/ko", ResourceProperty.TEMPLATE, "/libs/foobar/pages/page");
		
		Template actual = target.getTemplate();
		
		assertThat(client.getResourceResolver().getResource(actual.getPath()), resourceExistsAt("/libs/foobar/pages/page"));
	}
	
	@Test
	public void getTitleWithTitlePropertyReturnsTitle() throws Exception {
		Page target = aPage("/content/test/ko", ResourceProperty.TITLE, "Test Page");
		
		assertEquals("Test Page", target.getTitle());
	}
	
	@Test
	public void getVanityUrlWithNoneGivenReturnsNull() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertNull(target.getVanityUrl());
	}
	
	@Test
	public void hasChildWithContentReturnsTrue() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertTrue(target.hasChild(JcrConstants.JCR_CONTENT));
	}
	
	@Test
	public void hasChildWithChildPageReturnsTrue() throws Exception {
		aPage("/content/test/ko/foobar");
		Page target = aPage("/content/test/ko");
		
		assertTrue(target.hasChild("foobar"));
	}
	
	@Test
	public void hasChildWithNullReturnsFalse() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertFalse(target.hasChild(null));
	}
	
	@Test
	public void hasChildWithNonExistingNameReturnsFalse() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertFalse(target.hasChild("DOESNOTEXIST"));
	}
	
	@Test
	public void hasContentWithPageWithContentReturnsTrue() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertTrue(target.hasContent());
	}
	
	@Test
	public void isHideInNavWithUnsetPropertyReturnsFalse() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertFalse(target.isHideInNav());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void isLockedThrowsException() throws Exception {
		Page target = aPage("/content/test/ko");
		
		target.isLocked();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void lockThrowsException() throws Exception {
		Page target = aPage("/content/test/ko");
		
		target.lock();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getLockOwnerThrowsException() throws Exception {
		Page target = aPage("/content/test/ko");
		
		target.getLockOwner();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void canUnlockThrowsException() throws Exception {
		Page target = aPage("/content/test/ko");
		
		target.canUnlock();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void unlockThrowsException() throws Exception {
		Page target = aPage("/content/test/ko");
		
		target.unlock();
	}
	
	@Test
	public void isValidReturnsTrue() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertTrue(target.isValid());
	}
	
	@Test
	public void listChildrenWithSingleChildPageReturnsIteratorWithThatPage() throws Exception {
		aPage("/content/test/ko/foobar");
		Page target = aPage("/content/test/ko");
		
		Iterator<Page> actual = target.listChildren();
		
		assertThat(actual.next(), pageExistsAt("/content/test/ko/foobar"));
		assertFalse(actual.hasNext());
	}
	
	@Test
	public void listChildrenFilterOfPageWithTrueFilterReturnsAllChildren() throws Exception {
		aPage("/content/test/ko/foobar");
		Page target = aPage("/content/test/ko");
		
		Iterator<Page> actual = target.listChildren(element -> true);
		
		assertThat(actual.next(), pageExistsAt("/content/test/ko/foobar"));
		assertFalse(actual.hasNext());
	}
	
	@Test
	public void listChildrenFilterOfPageWithFalseFilterReturnsNoChildren() throws Exception {
		aPage("/content/test/ko/foobar");
		Page target = aPage("/content/test/ko");
		
		Iterator<Page> actual = target.listChildren(element -> false);
		
		assertFalse(actual.hasNext());
	}
	
	@Test
	public void timeUntilValidWithNoOffTimeSetReturnsZero() throws Exception {
		Page target = aPage("/content/test/ko");
		
		assertEquals(0, target.timeUntilValid());
	}
	
	protected Page aPage(String path, Object... properties) throws Exception {
		return testObj.aPageWithParents(path, properties).adaptTo(Page.class);
	}
}