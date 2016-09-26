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
package com.quatico.base.aem.test.api.values;


import static javax.jcr.PropertyType.BINARY;
import static javax.jcr.PropertyType.BOOLEAN;
import static javax.jcr.PropertyType.DATE;
import static javax.jcr.PropertyType.DOUBLE;
import static javax.jcr.PropertyType.LONG;
import static javax.jcr.PropertyType.STRING;
import static javax.jcr.PropertyType.UNDEFINED;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.jcr.RepositoryException;
import javax.jcr.Value;


public class MockValue extends org.apache.sling.commons.testing.jcr.MockValue {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
	private int type;
	
	public MockValue() {
		this.type = UNDEFINED;
	}
	
	public MockValue(String value) {
		super(value);
		this.type = STRING;
	}
	
	public MockValue(boolean value) {
		setValue(value);
		this.type = BOOLEAN;
	}
	
	public MockValue(Calendar value) {
		setValue(value);
		this.type = DATE;
	}
	
	public MockValue(double value) {
		setValue(value);
		this.type = DOUBLE;
	}
	
	public MockValue(InputStream value) {
		setValue(value);
		this.type = BINARY;
	}
	
	public MockValue(long value) {
		setValue(value);
		this.type = LONG;
	}
	
	public static Value[] fromValue(Object... values) throws RepositoryException {
		if (values.length > 0) {
			Value[] result = new Value[values.length];
			for (int idx = 0, valuesLength = values.length; idx < valuesLength; idx++) {
				Object value = values[idx];
				if (value instanceof String) {
					result[idx] = new MockValue((String) value);
				} else if (value instanceof Boolean) {
					result[idx] = new MockValue((boolean) value);
				} else if (value instanceof Calendar) {
					result[idx] = new MockValue((Calendar) value);
				} else if (value instanceof Double) {
					result[idx] = new MockValue((double) value);
				} else if (value instanceof InputStream) {
					result[idx] = new MockValue((InputStream) value);
				} else if (value instanceof Long) {
					result[idx] = new MockValue((long) value);
				}
			}
			return result;
		}
		throw new RepositoryException();
	}
	
	public static Value[] fromString(int typeHint, String... values) throws RepositoryException {
		if (values.length > 0) {
			Value[] result = new Value[values.length];
			for (int idx = 0; idx < values.length; idx++) {
				String value = values[idx];
				switch (typeHint) {
				case STRING:
					result[idx] = new MockValue(value);
					continue;
				case BOOLEAN:
					result[idx] = new MockValue(Boolean.valueOf(value));
					continue;
				case DATE:
					result[idx] = new MockValue(DATE_FORMAT.getCalendar());
					continue;
				case DOUBLE:
					result[idx] = new MockValue(Double.valueOf(value));
					continue;
				case BINARY:
					try {
						result[idx] = new MockValue(new ByteArrayInputStream(value.getBytes("UTF-8")));
						continue;
					} catch (UnsupportedEncodingException ex) {
						throw new RepositoryException(ex);
					}
				case LONG:
					result[idx] = new MockValue(Long.valueOf(value));
				}
			}
			return result;
		}
		throw new RepositoryException();
	}
	
	@Override
	public int getType() {
		return type;
	}
	
	@Override
	public int hashCode() {
		int prime  = 31;
		int result = prime * this.type;
		result = prime * result + this.toString().hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MockValue other = (MockValue) obj;
		if (type != other.getType()) {
			return false;
		}
		return toString().equals(other.toString());
		
	}
	
	@Override
	public String toString() {
		try {
			switch (type) {
			case STRING:
				return getString();
			case BOOLEAN:
				return String.valueOf(getBoolean());
			case DATE:
				return DATE_FORMAT.format(getDate().getTime());
			case DOUBLE:
				return String.valueOf(getDouble());
			case BINARY:
				return "InputStream::Value";
			case LONG:
				return String.valueOf(getLong());
			default:
				return "UNDEFINED";
			}
		} catch (RepositoryException ex) {
			return "UNDEFINED";
		}
	}
}
