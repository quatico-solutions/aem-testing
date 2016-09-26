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
package com.quatico.base.aem.test.api.builders;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public abstract class AbstractBuilder<T> implements ITestBuilder<T> {
	
	private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_WRAPPERS = new HashMap<Class<?>, Class<?>>() {
		{
			put(boolean.class, Boolean.class);
			put(char.class, Character.class);
			put(byte.class, Byte.class);
			put(short.class, Short.class);
			put(int.class, Integer.class);
			put(long.class, Long.class);
			put(float.class, Float.class);
			put(double.class, Double.class);
			put(void.class, Void.class);
		}
	};
	
	protected final Map<String, List<AbstractBuilder<?>>> children;
	private final Class<T>                                type;
	protected     T                                       result;
	
	protected AbstractBuilder(Class<T> type) {
		this(type, new HashMap<>());
	}
	
	protected AbstractBuilder(Class<T> type, Map<String, List<AbstractBuilder<?>>> children) {
		this.type = type;
		this.children = children;
	}
	
	public Class<T> getType() {
		return type;
	}
	
	@Override
	public T build() throws Exception {
		T value = getResult();
		try {
			children.values().forEach(cur -> cur.forEach(builder -> {
				try {
					builder.build();
				} catch (ClassCastException ignored) {
					// continue with remaining
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}));
		} catch (RuntimeException ex) {
			throw (Exception) ex.getCause();
		}
		
		if (value != null) {
			result = value;
		} else {
			result = internalBuild();
		}
		return result;
	}
	
	@Override
	public T getResult() {
		return result;
	}
	
	protected abstract T internalBuild() throws Exception;
	
	protected <R extends AbstractBuilder<T>> R addValue(String property, Object value) {
		try {
			return addValue(property, value, false);
		} catch (IllegalPropertyException ex) {
			// should not happen as property is replaced
			//noinspection unchecked
			return (R) this;
		}
	}
	
	protected <R extends AbstractBuilder<T>> R addValue(String property, Object value, boolean isMultiple) throws IllegalPropertyException {
		return addChild(property, new ValueBuilder<>(value), isMultiple);
	}
	
	protected <R> R getValue(String property) throws IllegalStateException {
		//noinspection unchecked
		return (R) getValue(property, Object.class);
	}
	
	protected <R> R getValue(String property, Class<R> type) throws IllegalStateException {
		try {
			Object result = getChild(property, AbstractBuilder.class).getResult();
			if (result == null) {
				throw new IllegalStateException();
			}
			if (wrapPrimitive(type).isAssignableFrom(result.getClass())) {
				//noinspection unchecked
				return (R) result;
			}
			throw new ClassCastException();
		} catch (ClassCastException | IllegalPropertyException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	protected <R> List<R> getValues(String property, Class<R> type) throws IllegalStateException {
		try {
			List<R> result = new LinkedList<>();
			getChildren(property, AbstractBuilder.class).forEach(cur -> {
				Object curRes = cur.getResult();
				if (curRes == null) {
					throw new IllegalStateException();
				}
				//noinspection unchecked
				result.add((R) curRes);
			});
			return result;
		} catch (NullPointerException | ClassCastException | IllegalPropertyException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	protected <R> List<R> getValues(String property) throws IllegalStateException {
		//noinspection unchecked
		return getValues(property, (Class<R>) Object.class);
	}
	
	protected boolean hasValue(String property) {
		try {
			return getChild(property, AbstractBuilder.class).getResult() != null;
		} catch (IllegalPropertyException ex) {
			return false;
		}
	}
	
	protected <R extends AbstractBuilder<?>> R addChild(String property, AbstractBuilder<?> child) {
		try {
			return addChild(property, child, false);
		} catch (IllegalPropertyException ex) {
			// should not happen as property is replaced
			//noinspection unchecked
			return (R) this;
		}
	}
	
	protected <R extends AbstractBuilder<?>> R addChild(String property, AbstractBuilder<?> child, boolean isMultiple) throws IllegalPropertyException {
		if (StringUtils.isNotBlank(property) && child != null) {
			if (isMultiple && children.containsKey(property)) {
				if (child.getClass() != children.get(property).get(0).getClass()) {
					throw new IllegalPropertyException();
				}
			} else {
				children.put(property, new LinkedList<>());
			}
			children.get(property).add(child);
		}
		//noinspection unchecked
		return (R) this;
	}
	
	protected AbstractBuilder<?> getChild(String property) throws IllegalPropertyException {
		return getChild(property, AbstractBuilder.class);
	}
	
	protected <R extends AbstractBuilder<?>> R getChild(String property, Class<R> type) throws IllegalPropertyException {
		return getChildren(property, type).get(0);
	}
	
	protected <R extends AbstractBuilder<?>> List<R> getChildren(String property, Class<R> type) throws IllegalPropertyException {
		if (StringUtils.isNotBlank(property) && type != null) {
			List<AbstractBuilder<?>> result = children.get(property);
			if (result != null && !result.isEmpty()) {
				if (type.isAssignableFrom(result.get(0).getClass())) {
					//noinspection unchecked
					return (List<R>) result;
				}
			}
		}
		throw new IllegalPropertyException();
	}
	
	protected List<AbstractBuilder> getChildren(String property) throws IllegalPropertyException {
		return getChildren(property, AbstractBuilder.class);
	}
	
	protected boolean hasChildren() {
		return !children.isEmpty();
	}
	
	protected boolean hasChildren(String property) {
		if (StringUtils.isNotBlank(property)) {
			List<AbstractBuilder<?>> result = children.get(property);
			if (result != null && !result.isEmpty()) {
				return true;
			}
		}
		return false;
		
	}
	
	private Class<?> wrapPrimitive(Class<?> type) {
		return type.isPrimitive() ? PRIMITIVE_TYPE_WRAPPERS.get(type) : type;
	}
	
	public static class IllegalPropertyException extends Exception {
		public IllegalPropertyException() {
			super();
		}
		
		public IllegalPropertyException(Exception cause) {
			super(cause);
		}
		
		public IllegalPropertyException(String message) {
			super(message);
		}
	}
}
