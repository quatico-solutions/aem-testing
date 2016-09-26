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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.quatico.base.aem.test.api.values.MockValue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.jcr.PropertyType;
import javax.jcr.Value;

import org.junit.Test;


public class MockValueTest {
	@Test
	public void getTypeWithDefaultValueYieldsResourceTypeUndefined() throws Exception {
		MockValue testObj = new MockValue();
		
		assertEquals(PropertyType.UNDEFINED, testObj.getType());
	}
	
	@Test
	public void getTypeWithStringValueYieldsResourceTypeString() throws Exception {
		MockValue testObj = new MockValue("foobar");
		
		assertEquals(PropertyType.STRING, testObj.getType());
	}
	
	@Test
	public void getTypeWithBooleanValueYieldsResourceTypeBoolean() throws Exception {
		MockValue testObj = new MockValue(false);
		
		assertEquals(PropertyType.BOOLEAN, testObj.getType());
	}
	
	@Test
	public void getTypeWithBooleanObjectValueYieldsResourceTypeBoolean() throws Exception {
		MockValue testObj = new MockValue(new Boolean("false"));
		
		assertEquals(PropertyType.BOOLEAN, testObj.getType());
	}
	
	@Test
	public void getTypeWithCalendarValueYieldsResourceTypeCalendar() throws Exception {
		MockValue testObj = new MockValue(Calendar.getInstance());
		
		assertEquals(PropertyType.DATE, testObj.getType());
	}
	
	@Test
	public void getTypeWithDoubleValueYieldsResourceTypeDouble() throws Exception {
		MockValue testObj = new MockValue(1.0d);
		
		assertEquals(PropertyType.DOUBLE, testObj.getType());
	}
	
	@Test
	public void getTypeWithDoubleObjectValueYieldsResourceTypeDouble() throws Exception {
		MockValue testObj = new MockValue(new Double("1.0"));
		
		assertEquals(PropertyType.DOUBLE, testObj.getType());
	}
	
	@Test
	public void getTypeWithInputStreamValueYieldsResourceTypeBinary() throws Exception {
		MockValue testObj = new MockValue(anInputStream("foobar"));
		
		assertEquals(PropertyType.BINARY, testObj.getType());
	}
	
	@Test
	public void getTypeWithLongValueYieldsResourceTypeLong() throws Exception {
		MockValue testObj = new MockValue(1L);
		
		assertEquals(PropertyType.LONG, testObj.getType());
	}
	
	@Test
	public void getTypeWithLongObjectValueYieldsResourceTypeLong() throws Exception {
		MockValue testObj = new MockValue(new Long("1"));
		
		assertEquals(PropertyType.LONG, testObj.getType());
	}
	
	@Test
	public void getBooleanWithDefaultValueReturnsFalse() throws Exception {
		MockValue testObj = new MockValue();
		
		assertFalse(testObj.getBoolean());
	}
	
	@Test
	public void getBooleanWithStringValueReturnsFalse() throws Exception {
		MockValue testObj = new MockValue("foobar");
		
		assertFalse(testObj.getBoolean());
	}
	
	@Test
	public void getBooleanWithBooleanObjectValueReturnsFalse() throws Exception {
		//noinspection BooleanConstructorCall
		MockValue testObj = new MockValue(new Boolean("false"));
		
		assertFalse(testObj.getBoolean());
	}
	
	@Test
	public void getBooleanWithCalendarValueReturnsFalse() throws Exception {
		MockValue testObj = new MockValue(Calendar.getInstance());
		
		assertFalse(testObj.getBoolean());
	}
	
	@Test
	public void getBooleanWithDoubleValueReturnsFalse() throws Exception {
		MockValue testObj = new MockValue(1.0d);
		
		assertFalse(testObj.getBoolean());
	}
	
	@Test
	public void getBooleanWithDoubleObjectValueReturnsFalse() throws Exception {
		MockValue testObj = new MockValue(new Double("1.0"));
		
		assertFalse(testObj.getBoolean());
	}
	
	@Test
	public void getBooleanWithInputStreamValueReturnsFalse() throws Exception {
		MockValue testObj = new MockValue(anInputStream("foobar"));
		
		assertFalse(testObj.getBoolean());
	}
	
	@Test
	public void getBooleanWithLongValueReturnsFalse() throws Exception {
		MockValue testObj = new MockValue(1L);
		
		assertFalse(testObj.getBoolean());
	}
	
	@Test
	public void getBooleanWithLongObjectValueReturnsFalse() throws Exception {
		MockValue testObj = new MockValue(new Long("1"));
		
		assertFalse(testObj.getBoolean());
	}
	
	@Test
	public void getBinaryWithStringValueReturnsNull() throws Exception {
		MockValue testObj = new MockValue("foobar");
		
		assertNull(testObj.getBinary());
	}
	
	@Test
	public void getBinaryWithBooleanValueReturnsNull() throws Exception {
		MockValue testObj = new MockValue(false);
		
		assertNull(testObj.getBinary());
	}
	
	@Test
	public void getBinaryWithBooleanObjectValueReturnsNull() throws Exception {
		MockValue testObj = new MockValue(new Boolean("false"));
		
		assertNull(testObj.getBinary());
	}
	
	@Test
	public void getBinaryWithCalendarValueReturnsNull() throws Exception {
		MockValue testObj = new MockValue(Calendar.getInstance());
		
		assertNull(testObj.getBinary());
	}
	
	@Test
	public void getBinaryWithDoubleValueReturnsNull() throws Exception {
		MockValue testObj = new MockValue(1.0d);
		
		assertNull(testObj.getBinary());
	}
	
	@Test
	public void getBinaryWithDoubleObjectValueReturnsNull() throws Exception {
		MockValue testObj = new MockValue(new Double("1.0"));
		
		assertNull(testObj.getBinary());
	}
	
	@Test
	public void getBinaryWithInputStreamValueReturnsNull() throws Exception {
		MockValue testObj = new MockValue(anInputStream("foobar"));
		
		assertNull(testObj.getBinary());
	}
	
	@Test
	public void getBinaryWithLongValueReturnsNull() throws Exception {
		MockValue testObj = new MockValue(1L);
		
		assertNull(testObj.getBinary());
	}
	
	@Test
	public void getBinaryWithLongObjectValueReturnsNull() throws Exception {
		MockValue testObj = new MockValue(new Long("1"));
		
		assertNull(testObj.getBinary());
	}
	
	@Test
	public void getDateWithDefaultValueReturnsNull() throws Exception {
		MockValue testObj = new MockValue();
		
		assertNull(testObj.getDate());
	}
	
	@Test
	public void getDoubleWithDefaultValueReturnsZero() throws Exception {
		MockValue testObj = new MockValue();
		
		assertEquals(0.0d, testObj.getDouble(), 0.1);
	}
	
	@Test
	public void getBinaryWithDefaultValueReturnsNull() throws Exception {
		MockValue testObj = new MockValue();
		
		assertNull(testObj.getBinary());
	}
	
	@Test
	public void getDecimalWithDefaultValueReturnsNull() throws Exception {
		MockValue testObj = new MockValue();
		
		assertNull(testObj.getDecimal());
	}
	
	@Test
	public void getLongWithDefaultValueReturnsZero() throws Exception {
		MockValue testObj = new MockValue();
		
		assertEquals(0, testObj.getLong());
	}
	
	@Test
	public void getStreamWithDefaultValueReturnsNull() throws Exception {
		MockValue testObj = new MockValue();
		
		assertNull(testObj.getStream());
	}
	
	@Test
	public void getValueWithStringValueYieldsResourceTypeString() throws Exception {
		MockValue testObj = new MockValue("foobar");
		
		assertEquals(PropertyType.STRING, testObj.getType());
	}
	
	@Test
	public void getValueWithBooleanValueYieldsResourceTypeBoolean() throws Exception {
		MockValue testObj = new MockValue(false);
		
		assertEquals(PropertyType.BOOLEAN, testObj.getType());
	}
	
	@Test
	public void getValueWithBooleanObjectValueYieldsResourceTypeBoolean() throws Exception {
		MockValue testObj = new MockValue(new Boolean("false"));
		
		assertEquals(PropertyType.BOOLEAN, testObj.getType());
	}
	
	@Test
	public void getValueWithCalendarValueYieldsResourceTypeCalendar() throws Exception {
		MockValue testObj = new MockValue(Calendar.getInstance());
		
		assertEquals(PropertyType.DATE, testObj.getType());
	}
	
	@Test
	public void getValueWithDoubleValueYieldsResourceTypeDouble() throws Exception {
		MockValue testObj = new MockValue(1.0d);
		
		assertEquals(PropertyType.DOUBLE, testObj.getType());
	}
	
	@Test
	public void getValueWithDoubleObjectValueYieldsResourceTypeDouble() throws Exception {
		MockValue testObj = new MockValue(new Double("1.0"));
		
		assertEquals(PropertyType.DOUBLE, testObj.getType());
	}
	
	@Test
	public void getValueWithInputStreamValueYieldsResourceTypeBinary() throws Exception {
		MockValue testObj = new MockValue(anInputStream("foobar"));
		
		assertEquals(PropertyType.BINARY, testObj.getType());
	}
	
	@Test
	public void getValueWithLongValueYieldsResourceTypeLong() throws Exception {
		MockValue testObj = new MockValue(1L);
		
		assertEquals(PropertyType.LONG, testObj.getType());
	}
	
	@Test
	public void getValueWithLongObjectValueYieldsResourceTypeLong() throws Exception {
		MockValue testObj = new MockValue(new Long("1"));
		
		assertEquals(PropertyType.LONG, testObj.getType());
	}
	
	@Test
	public void toStringWithLongValueReturnsValidString() throws Exception {
		MockValue testObj = new MockValue(1L);
		
		assertEquals("1", testObj.toString());
	}
	
	@Test
	public void toStringWithBoolenaValueReturnsValidString() throws Exception {
		MockValue testObj = new MockValue(true);
		
		assertEquals("true", testObj.toString());
	}
	
	@Test
	public void toStringWithDoubleValueReturnsValidString() throws Exception {
		MockValue testObj = new MockValue(1.0d);
		
		assertEquals("1.0", testObj.toString());
	}
	
	@Test
	public void toStringWithStringValueReturnsValidString() throws Exception {
		MockValue testObj = new MockValue("foobar");
		
		assertEquals("foobar", testObj.toString());
	}
	
	@Test
	public void toStringWithNoValueReturnsValidString() throws Exception {
		MockValue testObj = new MockValue();
		
		assertEquals("UNDEFINED", testObj.toString());
	}
	
	@Test
	public void toStringWithDateValueReturnsValidString() throws Exception {
		Calendar  target  = aCalendar();
		MockValue testObj = new MockValue(target);
		
		assertEquals("2014-12-15T04:46:00+0100", testObj.toString());
	}
	
	@Test
	public void toStringWithInputStreamValueReturnsValueString() throws Exception {
		MockValue testObj = new MockValue(anInputStream("foobar"));
		
		assertEquals("InputStream::Value", testObj.toString());
	}
	
	@Test
	public void createObjectWithStringReturnsStringValue() throws Exception {
		Value actual = MockValue.fromValue("foobar")[0];
		
		assertEquals(new MockValue("foobar"), actual);
	}
	
	@Test
	public void createObjectWithBooleanReturnsBooleanValue() throws Exception {
		Value actual = MockValue.fromValue(true)[0];
		
		assertEquals(new MockValue(true), actual);
	}
	
	@Test
	public void createObjectWithCalendarReturnsCalendarValue() throws Exception {
		Calendar target = aCalendar();
		
		Value actual = MockValue.fromValue(target)[0];
		
		assertEquals(new MockValue(target), actual);
	}
	
	@Test
	public void createObjectWithDoubleReturnsDoubleValue() throws Exception {
		Value actual = MockValue.fromValue(1.0d)[0];
		
		assertEquals(new MockValue(1.0d), actual);
	}
	
	@Test
	public void createObjectWithInputStreamReturnsInputStreamValue() throws Exception {
		InputStream target = anInputStream("foobar");
		
		Value actual = MockValue.fromValue(target)[0];
		
		assertEquals(new MockValue(target), actual);
	}
	
	@Test
	public void createObjectWithLongReturnsLongValue() throws Exception {
		Value actual = MockValue.fromValue(1L)[0];
		
		assertEquals(new MockValue(1L), actual);
	}
	
	@Test
	public void createStringWithStringAndValidTypeReturnsStringValue() throws Exception {
		Value actual = MockValue.fromString(PropertyType.STRING, "foobar")[0];
		
		assertEquals(new MockValue("foobar"), actual);
	}
	
	@Test
	public void createStringWithBooleanAndValidTypeReturnsBooleanValue() throws Exception {
		Value actual = MockValue.fromString(PropertyType.BOOLEAN, "true")[0];
		
		assertEquals(new MockValue(true), actual);
	}
	
	@Test
	public void createStringWithCalendarAndValidTypeReturnsCalendarValue() throws Exception {
		Calendar target = aCalendar();
		
		Value actual = MockValue.fromString(PropertyType.DATE, MockValue.DATE_FORMAT.format(target.getTime()))[0];
		
		assertEquals(new MockValue(target), actual);
	}
	
	@Test
	public void createStringWithDoubleAndValidTypeReturnsDoubleValue() throws Exception {
		Value actual = MockValue.fromString(PropertyType.DOUBLE, "1.0")[0];
		
		assertEquals(new MockValue(1.0d), actual);
	}
	
	@Test
	public void createStringWithInputAndValidTypeStreamReturnsInputStreamValue() throws Exception {
		InputStream target = anInputStream("foobar");
		
		Value actual = MockValue.fromString(PropertyType.BINARY, "foobar")[0];
		
		assertEquals(new MockValue(target), actual);
	}
	
	@Test
	public void createStringWithLongAndValidTypeReturnsLongValue() throws Exception {
		Value actual = MockValue.fromString(PropertyType.LONG, "1")[0];
		
		assertEquals(new MockValue(1L), actual);
	}
	
	private Calendar aCalendar() {
		Calendar target = GregorianCalendar.getInstance();
		target.set(2014, 11, 15, 4, 46, 0);
		return target;
	}
	
	private InputStream anInputStream(String value) throws UnsupportedEncodingException {
		return new ByteArrayInputStream(value.getBytes("UTF-8"));
	}
}