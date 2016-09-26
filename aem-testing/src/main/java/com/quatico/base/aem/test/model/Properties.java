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
package com.quatico.base.aem.test.model;


import com.quatico.base.aem.test.api.values.TypedStringValue;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;


public class Properties extends AbstractList<Object> {
	private final List<Object> data;
	
	public Properties(Object... data) throws IllegalArgumentException {
		if (data.length > 0 && data.length % 2 != 0) {
			throw new IllegalArgumentException("Cannot handle odd number of property entries.");
		}
		this.data = new LinkedList<>(Arrays.asList(data));
	}
	
	public Properties(Map<String, Object> data) {
		this.data = new LinkedList<>();
		for (Entry<String, Object> entry : data.entrySet()) {
			this.data.add(entry.getKey());
			this.data.add(entry.getValue());
		}
	}
	
	public Properties append(String name, Object value) {
		if (StringUtils.isNotEmpty(name) && value != null) {
			this.data.add(name);
			this.data.add(value);
		}
		return this;
	}
	
	public Properties appendIfNotSet(String key, Object value) {
		if (!contains(key)) {
			return append(key, value);
		}
		return this;
	}
	
	public Object remove(String name) {
		if (this.data.contains(name)) {
			int pos = this.data.indexOf(name);
			this.data.remove(pos); // removes property name
			return this.data.remove(pos); // removes property value
		}
		return null;
	}
	
	@Override
	public Object get(int index) {
		return this.data.get(index);
	}
	
	@Override
	public int indexOf(Object element) {
		int result = this.data.indexOf(element);
		if (result == -1 && element instanceof String) {
			result = this.data.indexOf(toQualifiedKey(element));
		}
		return result;
	}
	
	@Override
	public int lastIndexOf(Object element) {
		int result = this.data.lastIndexOf(element);
		if (result == -1 && element instanceof String) {
			result = this.data.lastIndexOf(toQualifiedKey(element));
		}
		return result;
	}
	
	@Override
	public int size() {
		return this.data.size();
	}
	
	@Override
	public boolean contains(Object element) {
		if (this.data.contains(element)) {
			return true;
		}
		if (element instanceof String) {
			if (this.data.contains(toQualifiedKey(element))) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Object[] toArray() {
		Object[] result = new Object[this.data.size()];
		for (int idx = 0; idx < result.length - 1; idx += 2) {
			result[idx] = toQualifiedKey(this.data.get(idx));
			result[idx + 1] = this.data.get(idx + 1);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> toMap() {
		Map<String, Object> result = new LinkedHashMap<>();
		for (int idx = 0; idx < this.data.size() - 1; idx += 2) {
			result.put(toQualifiedKey(toMappedValue(idx)), toMappedValue(idx + 1));
		}
		return result;
	}
	
	public Value[] toValues() {
		List<Value> result = new ArrayList<>(this.data.size() / 2);
		for (int idx = 0; idx < this.data.size() - 1; idx += 2) {
			String key   = (String) this.data.get(idx);
			Object value = this.data.get(idx + 1);
			result.add(new Value(key, value));
		}
		return result.toArray(new Value[result.size()]);
	}
	
	private String toQualifiedKey(Object value) {
		return value != null ? value.toString() : StringUtils.EMPTY;
	}
	
	private Object toMappedValue(int idx) {
		Object value = this.data.get(idx);
		if (value instanceof TypedStringValue) {
			return value.toString();
		} else {
			return value;
		}
	}
	
	public static class Value {
		private final String name;
		private final Object value;
		
		public Value(String name, Object value) {
			this.name = name;
			this.value = value;
		}
		
		public Object getValue() {
			return value;
		}
		
		public String getName() {
			return name;
		}
		
		@Override
		public int hashCode() {
			int result = name != null ? name.hashCode() : 0;
			result = 31 * result + (value != null ? value.hashCode() : 0);
			return result;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) { return true; }
			if (o == null || getClass() != o.getClass()) { return false; }
			
			Value value1 = (Value) o;
			
			if (name != null ? !name.equals(value1.name) : value1.name != null) { return false; }
			return value != null ? value.equals(value1.value) : value1.value == null;
			
		}
	}
}
