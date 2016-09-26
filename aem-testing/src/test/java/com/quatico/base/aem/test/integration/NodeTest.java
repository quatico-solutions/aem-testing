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


import static com.quatico.base.aem.test.api.AemMatchers.pathEndsWithUuid;
import static com.quatico.base.aem.test.api.setup.AemClient.Type.Integration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static junit.framework.TestCase.assertTrue;

import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.api.setup.AemClient;
import com.quatico.base.aem.test.common.NodeTestDriver;

import javax.jcr.Node;
import javax.jcr.UnsupportedRepositoryOperationException;

import org.junit.Test;


public class NodeTest extends NodeTestDriver {
	
	@Override
	protected IAemClient createClient() throws Exception {
		return new AemClient(Integration);
	}
	
	@Test(expected = NullPointerException.class)
	public void isNodeTypeWithNullThrowsException() throws Exception {
		Node target = aNode("/content");
		
		target.isNodeType(null);
	}
	
	@Test
	public void isNodeTypeWithInvalidNodeTypeReturnsFalse() throws Exception {
		Node target = aNode("/content");
		
		assertFalse(target.isNodeType("whatever"));
	}
	
	@Test
	public void isNodeTypeWithValidNodeTypeReturnsTrue() throws Exception {
		Node target = aNode("/content");
		
		assertTrue(target.isNodeType("nt:unstructured"));
	}
	
	@Test
	public void getMixinNodeTypesReturnsEmptyArray() throws Exception {
		Node testObj = aNode("/content");
		
		assertEquals(0, testObj.getMixinNodeTypes().length);
	}
	
	@SuppressWarnings("deprecation")
	@Test(expected = UnsupportedRepositoryOperationException.class)
	public void getUuidReturnsNonEmptyString() throws Exception {
		Node target = aNode();
		
		target.getUUID();
	}
	
	@Test
	public void getNodeWithEmptyPathReturnsRootNode() throws Exception {
		Node target = aNode();
		
		Node actual = target.getNode("");
		
		assertThat(actual.getPath(), pathEndsWithUuid());
	}
	
	@Test
	public void setPropertyWithStringReturnsValidProperty() throws Exception {
		Node target = aNode();
		
		target.setProperty("zip", "zap");
		
		assertEquals("zap", target.getProperty("zip").getValue().getString());
	}
}
