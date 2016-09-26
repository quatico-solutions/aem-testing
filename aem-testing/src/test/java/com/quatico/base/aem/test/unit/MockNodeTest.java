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


import static com.quatico.base.aem.test.api.AemMatchers.nodeExistsAt;
import static com.quatico.base.aem.test.api.setup.AemClient.Type.Unit;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.api.setup.AemClient;
import com.quatico.base.aem.test.common.NodeTestDriver;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.Value;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.value.BooleanValue;
import org.apache.jackrabbit.value.DateValue;
import org.apache.jackrabbit.value.DecimalValue;
import org.apache.jackrabbit.value.DoubleValue;
import org.apache.jackrabbit.value.LongValue;
import org.apache.jackrabbit.value.ReferenceValue;
import org.apache.jackrabbit.value.StringValue;
import org.junit.Test;


@SuppressWarnings("deprecation")
public class MockNodeTest extends NodeTestDriver {
	
	@Override
	protected IAemClient createClient() throws Exception {
		return new AemClient(Unit);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getMixinNodeTypesReturnsEmptyArray() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.getMixinNodeTypes();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void isSameThrowsException() throws Exception {
		Node testObj = aNode("/");
		Node other   = aNode("/content");
		
		testObj.isSame(other);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void orderBeforeThrowsException() throws Exception {
		aNode("/content").orderBefore("", "");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void acceptThrowsException() throws Exception {
		Node testObj = aNode("/");
		
		testObj.accept(null);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void saveThrowsException() throws Exception {
		Node testObj = aNode("/");
		
		testObj.save();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void refreshWithFalseThrowsException() throws Exception {
		Node testObj = aNode("/");
		
		testObj.refresh(false);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void refreshWithTrueThrowsException() throws Exception {
		Node testObj = aNode("/");
		
		testObj.refresh(true);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void setPrimaryTypeThrowsException() throws Exception {
		Node testObj = aNode("/content/foobar");
		
		testObj.setPrimaryType("sling:Folder");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void addMixinThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.addMixin(null);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void removeMixinThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.removeMixin(null);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void canAddMixinThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		assertFalse(testObj.canAddMixin(null));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getDefinitionThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.getDefinition();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void checkinThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.checkin();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void checkoutThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.checkout();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void doneMergeThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.doneMerge(null);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void cancelMergeThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.cancelMerge(null);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void updateThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.update(null);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void mergeThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.merge(null, false);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getCorrespondingNodePathThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.getCorrespondingNodePath("ANYTHING");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getSharedSetThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.getSharedSet();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void removeSharedSetThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.removeSharedSet();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void removeShareThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.removeShare();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void isCheckedOutThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.isCheckedOut();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void restoreVersionStringBooleanThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.restore(null, null, false);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void restoreByLabelThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.restoreByLabel(null, false);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getVersionHistoryThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.getVersionHistory();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getBaseVersionThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.getBaseVersion();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void lockThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.lock(false, false);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getLockThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.getLock();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void unlockThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.unlock();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void holdsLockThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.holdsLock();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void isLockedThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.isLocked();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void followLifecycleTransitionThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.followLifecycleTransition(null);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getAllowedLifecycleTransistionsThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.getAllowedLifecycleTransistions();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getIndexThrowsException() throws Exception {
		Node testObj = aNode();
		
		testObj.getIndex();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getReferencesThrowsException() throws Exception {
		Node testObj = aNode();
		
		testObj.getReferences();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getPropertiesStringArrayThrowsException() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.getProperties(new String[0]);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getNodesStringArrayThrowsException() throws Exception {
		aNode("/content/ko/zip");
		aNode("/content/ko/zap");
		aNode("/content/ko/zup");
		Node testObj = aNode("/content/ko");
		
		testObj.getNodes(new String[] { "zip", "zap", "zup" });
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void setPropertyStringStringArrayIntThrowsException() throws Exception {
		Node testObj = aNode();
		
		testObj.setProperty("zip", new String[] { "zap", "zup" }, PropertyType.PATH);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void setPropertyStringValueIntThrowsException() throws Exception {
		Node testObj = aNode();
		Node target  = aNode("/content");
		
		testObj.setProperty("target", new ReferenceValue(target), PropertyType.REFERENCE);
	}
	
	@Test
	public void setPropertyStringValueWithStringValue() throws Exception {
		Node testObj = aNode();
		
		Property actual = testObj.setProperty("zip", new StringValue("zap"));
		
		assertEquals("zap", testObj.getProperty("zip").getString());
		assertEquals(actual, testObj.getProperty("zip"));
	}
	
	@Test(expected = NullPointerException.class)
	public void getParentWithRootThrowsException() throws Exception {
		Node testObj = aNode("/");
		
		testObj.getParent();
	}
	
	@Test
	public void isNodeTypeWithNullReturnsFalse() throws Exception {
		Node testObj = aNode("/content");
		
		assertFalse(testObj.isNodeType(null));
	}
	
	@Test(expected = PathNotFoundException.class)
	public void getNodeWithNullThrowsException() throws Exception {
		Node testObj = aNode();
		
		testObj.getNode(null);
	}
	
	@Test
	public void getAncestorWithThreeReturnsRoot() throws Exception {
		Node testObj = aNode("/content/ko/foobar");
		
		assertEquals("/", testObj.getAncestor(3).getPath());
	}
	
	@Test
	public void setPropertyStringBoolean() throws Exception {
		Node testObj = aNode();
		
		Property expected = testObj.setProperty("zip", true);
		
		assertEquals(new BooleanValue(true), testObj.getProperty("zip").getValue());
		assertEquals(PropertyType.BOOLEAN, testObj.getProperty("zip").getType());
		assertEquals(expected, testObj.getProperty("zip"));
	}
	
	@Test
	public void setPropertyStringDouble() throws Exception {
		Node testObj = aNode();
		
		Property expected = testObj.setProperty("zip", 1.2d);
		
		assertEquals(new DoubleValue(1.2d), testObj.getProperty("zip").getValue());
		assertEquals(PropertyType.DOUBLE, testObj.getProperty("zip").getType());
		assertEquals(expected, testObj.getProperty("zip"));
	}
	
	@Test
	public void getAncestorWithTwoReturnsNodesParent() throws Exception {
		Node testObj = aNode("/content/ko/foobar");
		
		assertEquals("/content", testObj.getAncestor(2).getPath());
	}
	
	@Test
	public void setPropertyStringValueArrayWithStringValues() throws Exception {
		Node testObj = aNode();
		
		Property expected = testObj.setProperty("zip", new Value[] { new StringValue("zap"), new StringValue("zup") });
		
		assertArrayEquals(expected.getValues(), testObj.getProperty("zip").getValues());
		assertEquals(expected, testObj.getProperty("zip"));
	}
	
	@Test
	public void setPropertyStringBigDecimal() throws Exception {
		Node testObj = aNode();
		
		Property expected = testObj.setProperty("zip", new BigDecimal("100.3"));
		
		assertEquals(new DecimalValue(new BigDecimal("100.3")), testObj.getProperty("zip").getValue());
		assertEquals(PropertyType.DECIMAL, testObj.getProperty("zip").getType());
		assertEquals(expected, testObj.getProperty("zip"));
	}
	
	@Test
	public void isNewReturnsTrue() throws Exception {
		Node testObj = aNode("/");
		
		assertTrue(testObj.isNew());
	}
	
	@Test
	public void getAncestorWithZeroReturnsNodeItself() throws Exception {
		Node testObj = aNode("/content/ko/foobar");
		//FIXME(bug) JCR_MOCK runs a faulty implementation of getAncestor(n), report it!
		assertEquals("/content/ko/foobar", testObj.getAncestor(0).getPath());
	}
	
	@Test
	public void getAncestorWithOneReturnsFirstChildOfRoot() throws Exception {
		Node testObj = aNode("/content/ko/foobar");
		
		assertEquals("/content/ko", testObj.getAncestor(1).getPath());
	}
	
	@Test
	public void addNodeStringWithMultiRelativePathSegmentsCreatesChildNode() throws Exception {
		Node testObj = aNode("/content");
		
		testObj.addNode("ko/foobar");
		
		// BUG: DOES NOT EXIST
		assertNull(client.getResourceResolver().getResource("/content/ko"));
		// EXISTS
		assertNotNull(client.getResourceResolver().getResource("/content/ko/foobar"));
	}
	
	@Test(expected = NullPointerException.class)
	public void getNodesStringWithNullThrowsException() throws Exception {
		Node testObj = aNode();
		
		testObj.getNodes((String) null);
	}
	
	@Test
	public void removeWithExistingNodeRemovesNode() throws Exception {
		Node target  = aNode("/content");
		Node testObj = target.addNode("foobar123");
		assertThat(testObj, nodeExistsAt("/content/foobar123"));
		
		testObj.remove();
		
		try {
			testObj.getNode("foobar123");
			fail();
		} catch (PathNotFoundException ex) {
			// passed
		}
	}
	
	@Test
	public void setPropertyStringCalendar() throws Exception {
		Node     testObj = aNode();
		Calendar target  = GregorianCalendar.getInstance();
		
		Property expected = testObj.setProperty("zip", target);
		
		assertEquals(new DateValue(target), testObj.getProperty("zip").getValue());
		assertEquals(PropertyType.DATE, testObj.getProperty("zip").getType());
		assertEquals(expected, testObj.getProperty("zip"));
	}
	
	@Test
	public void setPropertyStringLong() throws Exception {
		Node testObj = aNode();
		
		Property expected = testObj.setProperty("zip", 100L);
		
		assertEquals(new LongValue(100L), testObj.getProperty("zip").getValue());
		assertEquals(PropertyType.LONG, testObj.getProperty("zip").getType());
		assertEquals(expected, testObj.getProperty("zip"));
	}
	
	@Test
	public void setPropertyStringNode() throws Exception {
		Node testObj = aNode();
		Node target  = aNode();
		
		Property expected = testObj.setProperty("zip", target);
		
		assertEquals(new ReferenceValue(target), testObj.getProperty("zip").getValue());
		assertEquals(PropertyType.REFERENCE, testObj.getProperty("zip").getType());
		assertEquals(expected, testObj.getProperty("zip"));
	}
	
	@Test
	public void getNodeWithRootPathReturnsRootNode() throws Exception {
		Node testObj = aNode();
		
		Node actual = testObj.getNode("/");
		
		assertEquals("/", actual.getPath());
	}
	
	@Test
	public void getUuidReturnsNonEmptyString() throws Exception {
		Node testObj = aNode();
		
		assertFalse(StringUtils.isEmpty(testObj.getUUID()));
	}
	
}
