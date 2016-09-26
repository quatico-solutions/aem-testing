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


import static com.quatico.base.aem.test.api.AemMatchers.nodeExistsAt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static com.day.cq.commons.jcr.JcrConstants.JCR_PRIMARYTYPE;

import com.quatico.base.aem.test.TestDriver;
import com.quatico.base.aem.test.api.setup.Resources;

import java.util.UUID;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;


public abstract class NodeTestDriver extends TestDriver {
	private Resources resources;
	
	@Before
	public void setUp() throws Exception {
		this.resources = new Resources(client);
	}
	
	@Test
	public void getNameReturnsLastPathSegment() throws Exception {
		Node testObj = aNode("/content/ko");
		
		assertEquals("ko", testObj.getName());
	}
	
	@Test
	public void getPathReturnsResourcePath() throws Exception {
		Node testObj = aNode("/content");
		
		assertEquals("/content", testObj.getPath());
	}
	
	@Test(expected = ItemNotFoundException.class)
	public void getAncestorWithFiveThrowsException() throws Exception {
		Node testObj = aNode("/content/ko/foobar");
		
		testObj.getAncestor(5);
	}
	
	@Test(expected = ItemNotFoundException.class)
	public void getAncestorWithNegativeValueThrowsException() throws Exception {
		Node testObj = aNode("/content/ko/foobar");
		
		testObj.getAncestor(-10);
	}
	
	@Test
	public void getParentWithChildReturnsParent() throws Exception {
		Node testObj = aNode("/content/ko/foobar");
		
		assertEquals("/content/ko", testObj.getParent().getPath());
	}
	
	@Test
	public void getDepthWithRootReturnsZero() throws Exception {
		Node testObj = aNode("/");
		
		assertEquals(0, testObj.getDepth());
	}
	
	@Test
	public void getDepthWithSecondLevelChildReturnsTwo() throws Exception {
		Node testObj = aNode("/content/ko");
		
		assertEquals(2, testObj.getDepth());
	}

	@Test
	public void getSessionReturnsNonNullObject() throws Exception {
		Node testObj = aNode("/");
		
		assertNotNull(testObj.getSession());
	}
	
	@Test
	public void isNodeReturnsTrue() throws Exception {
		Node testObj = aNode("/");
		
		assertTrue(testObj.isNode());
	}
	
	@Test
	public void isModifiedReturnsFalse() throws Exception {
		Node testObj = aNode("/");
		
		assertFalse(testObj.isModified());
	}
	
	@Test
	public void addNodeStringWithSingleRelativePathSegmentCreatesChildNode() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.addNode("test-pages");
		
		assertNotNull(client.getResourceResolver().getResource("/content/test-pages"));
	}
	
	@Test
	public void addNodeStringStringWithRelativePathAndPrimaryTypeCreatesNodeAndDoesNOTAssignDifferentType() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.addNode("zipzap", "cq:Page");
		Node actual = client.getResourceResolver().getResource("/content/zipzap").adaptTo(Node.class);
		
		assertTrue(actual.isNodeType("cq:Page"));
	}
	
	@Test
	public void getNodeWithExistingChildReturnsChildNode() throws Exception {
		resources.aResource("/content/ko");
		Node testObj = aNode("/content");
		
		Node actual = testObj.getNode("ko");
		
		assertThat(actual, nodeExistsAt("/content/ko"));
	}
	
	@Test
	public void getNodeWithEmptyStringReturnNodeItself() throws Exception {
		Node testObj = aNode();
		
		Node actual = testObj.getNode("");
		
		assertEquals(testObj.getPath(), actual.getPath());
	}
	
	@Test
	public void getNodesWithExistingChildsReturnsNonEmptyIterator() throws Exception {
		resources.aResource("/content/ko");
		Node testObj = aNode("/content");
		
		NodeIterator actual = testObj.getNodes();
		
		assertTrue(actual.hasNext());
	}
	
	@Test
	public void getNodesWithNoChildrenReturnsEmptyIterator() throws Exception {
		Node testObj = aNode();
		
		NodeIterator actual = testObj.getNodes();
		
		assertFalse(actual.hasNext());
	}
	
	@Test
	public void getNodesStringWithMatchingChildReturnsChildNode() throws Exception {
		resources.aResource("/content/ko");
		Node testObj = aNode("/content");
		
		NodeIterator actual = testObj.getNodes("ko");
		
		assertThat(actual.nextNode(), nodeExistsAt("/content/ko"));
		assertFalse(actual.hasNext());
	}
	
	@Test
	public void getNodesStringWithNoExistentChildrenReturnsEmptyIterator() throws Exception {
		Node testObj = aNode();
		
		NodeIterator actual = testObj.getNodes("\\*");
		
		assertFalse(actual.hasNext());
	}
	
	@Test
	public void getNodesStringWithCombinedStringReturnsAllMatchingDirectChildren() throws Exception {
		aNode("/content/ko/zip");
		aNode("/content/ko/zap");
		aNode("/content/ko/zup");
		Node testObj = aNode("/content/ko");
		
		NodeIterator actual = testObj.getNodes("zip|zap|zup");
		
		assertThat(actual.nextNode(), nodeExistsAt("/content/ko/zip"));
		assertThat(actual.nextNode(), nodeExistsAt("/content/ko/zap"));
		assertThat(actual.nextNode(), nodeExistsAt("/content/ko/zup"));
		assertFalse(actual.hasNext());
	}
	
	@Test
	public void getPropertiesWithDefaultValuesReturnsNonEmptyProperties() throws Exception {
		Node testObj = aNode("/content");
		
		PropertyIterator actual = testObj.getProperties();
		
		assertTrue(actual.hasNext());
	}
	
	@Test(expected = ItemNotFoundException.class)
	public void getPrimaryItemThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.getPrimaryItem();
	}
	
	@Test
	public void hasNodeWithExistingChildReturnsTrue() throws Exception {
		resources.aResource("/content/ko/zip");
		Node testObj = aNode("/content");
		
		assertTrue(testObj.hasNode("ko"));
	}
	
	@Test
	public void hasNodeWithNonExistingChildReturnsFalse() throws Exception {
		Node testObj = aNode("/content");
		
		assertFalse(testObj.hasNode("FOOBAR"));
	}
	
	@Test
	public void hasPropertyWithExistingPropertyReturnsTrue() throws Exception {
		Node testObj = aNode("/content");
		
		assertTrue(testObj.hasProperty(JCR_PRIMARYTYPE));
	}
	
	@Test
	public void hasPropertyWithNonExistingPropertyReturnsFalse() throws Exception {
		Node testObj = aNode("/content");
		
		assertFalse(testObj.hasProperty("FOO_BAR"));
	}
	
	@Test
	public void hasNodesWithNodeWithChildrenReturnsTrue() throws Exception {
		resources.aResource("/content/ko/zip");
		Node testObj = aNode("/content");
		
		assertTrue(testObj.hasNodes());
	}
	
	@Test
	public void hasNodesWithNodeWithNoChildrenReturnsFalse() throws Exception {
		Node testObj = aNode("/content");
		
		assertFalse(testObj.hasNodes());
	}
	
	@Test
	public void hasPropertiesWithPropertiesOnNodeReturnsTrue() throws Exception {
		Node testObj = aNode("/content");
		
		assertTrue(testObj.hasProperties());
	}
	
	@Test
	public void getPrimaryNodeTypeWithDefaultValueReturnsUnstructured() throws Exception {
		Node testObj = aNode("/content");
		
		assertEquals("nt:unstructured", testObj.getPrimaryNodeType().getName());
	}
	
	@Test
	public void isNodeTypeWithUnstructuredReturnsTrue() throws Exception {
		Node testObj = aNode("/content");
		
		assertTrue(testObj.isNodeType("nt:unstructured"));
	}
	
	@Test
	public void isNodeTypeWithWrongTypeReturnsFalse() throws Exception {
		Node testObj = aNode("/content");
		
		assertFalse(testObj.isNodeType("WRONG_TYPE"));
	}
	
	@Test
	public void setPropertyStringStringWithStringValue() throws Exception {
		Node testObj = aNode();
		
		Property expected = testObj.setProperty("zip", "zap");
		
		assertEquals("zap", testObj.getProperty("zip").getValue().getString());
		assertEquals(expected.getPath(), testObj.getProperty("zip").getPath());
	}
	
	@Test
	public void getIdentifierReturnsNonEmptyString() throws Exception {
		Node testObj = aNode();
		
		assertFalse(StringUtils.isEmpty(testObj.getIdentifier()));
	}
	
	protected Node aNode(String path, Object... properties) throws Exception {
		return resources.aResource(path, properties).adaptTo(Node.class);
	}
	
	protected Node aNode() throws Exception {
		return resources.aResource("/" + uniqueName()).adaptTo(Node.class);
	}
	
	private String uniqueName() {
		return UUID.randomUUID().toString();
	}
}
