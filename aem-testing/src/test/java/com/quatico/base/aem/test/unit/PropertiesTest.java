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


import static com.quatico.base.aem.test.model.ResourceProperty.RESOURCE_TYPE;
import static com.quatico.base.aem.test.model.ResourceProperty.TITLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static com.day.cq.commons.jcr.JcrConstants.JCR_TITLE;

import com.quatico.base.aem.test.model.Properties;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;


public class PropertiesTest {
	
	@Test(expected = IllegalArgumentException.class)
	public void ctorWithOddNumberOfValuesThrowsException() throws Exception {
		new Properties("foo");
	}
	
	@Test
	public void toMapQualifiesKeys() throws Exception {
		Map<String, Object> actual = new Properties(TITLE, "foobar").toMap();
		
		assertTrue(actual.containsKey(TITLE));
	}
	
	@Test
	public void toArrayQualifiesKeys() throws Exception {
		Object[] actual = new Properties(TITLE, "foobar").toArray();
		
		assertEquals(TITLE, actual[0]);
	}
	
	@Test
	public void toArrayObjectArrayDoesNotQualifyKeys() throws Exception {
		Object[] actual = new Properties(TITLE, "foobar").toArray(new Object[0]);
		
		assertEquals(TITLE, actual[0]);
	}
	
	@Test
	public void toPairsDoesNotQualifyKeys() throws Exception {
		Properties.Value[] actual = new Properties(TITLE, "foobar").toValues();
		
		assertEquals(TITLE, actual[0].getName());
	}
	
	@Test
	public void containsWithExistingElementReturnsTrueForSimpleAndQualifiedName() throws Exception {
		Properties testObj = new Properties(TITLE, "foobar");
		
		assertTrue(testObj.contains(TITLE));
		assertTrue(testObj.contains(JCR_TITLE));
	}
	
	@Test
	public void containsAllWithExistingElementsReturnsTrueForSimpleAndQualifiedNames() throws Exception {
		Properties testObj = new Properties(TITLE, "foobar", RESOURCE_TYPE, "whatever");
		
		assertTrue(testObj.containsAll(Arrays.asList(TITLE, JCR_TITLE, RESOURCE_TYPE, "sling:resourceType")));
	}
	
	@Test
	public void indexOfWithExistingElementReturnsSameIndexForSimpleAndQualifiedNames() throws Exception {
		Properties testObj = new Properties(TITLE, "foobar", RESOURCE_TYPE, "whatever");
		
		assertEquals(0, testObj.indexOf(TITLE));
		assertEquals(0, testObj.indexOf(JCR_TITLE));
		assertEquals(2, testObj.indexOf(RESOURCE_TYPE));
		assertEquals(2, testObj.indexOf("sling:resourceType"));
	}
	
	@Test
	public void lastIndexOfWithExistingElementReturnsSameIndexForSimpleAndQualifiedNames() throws Exception {
		Properties testObj = new Properties(TITLE, "foobar", RESOURCE_TYPE, "whatever");
		
		assertEquals(0, testObj.lastIndexOf(TITLE));
		assertEquals(0, testObj.lastIndexOf(JCR_TITLE));
		assertEquals(2, testObj.lastIndexOf(RESOURCE_TYPE));
		assertEquals(2, testObj.lastIndexOf("sling:resourceType"));
	}
	
	@Test
	public void appendWithExistingQualifiedNameValueDoesOverrideSimpleNameValue() throws Exception {
		Properties testObj = new Properties(JCR_TITLE, "foobar");
		
		testObj.append(TITLE, "EXPECTED");
		
		assertEquals(JCR_TITLE, testObj.get(0));
		assertEquals("foobar", testObj.get(1));
		assertEquals(TITLE, testObj.get(2));
		assertEquals("EXPECTED", testObj.get(3));
	}
	
	@Test
	public void appendIfNotSetWithExistingQualifiedNameValueDoesNotOverrideSimpleNameValue() throws Exception {
		Properties testObj = new Properties(JCR_TITLE, "EXPECTED");
		
		testObj.appendIfNotSet(TITLE, "foobar");
		
		assertEquals(JCR_TITLE, testObj.get(0));
		assertEquals("EXPECTED", testObj.get(1));
	}
	
	@Test
	public void appendIfNotSetWithExistingSimpleNameValueDoesNotOverrideQualifiedNameValue() throws Exception {
		Properties testObj = new Properties(TITLE, "EXPECTED");
		
		testObj.appendIfNotSet(JCR_TITLE, "foobar");
		
		assertEquals(TITLE, testObj.get(0));
		assertEquals("EXPECTED", testObj.get(1));
	}
}
