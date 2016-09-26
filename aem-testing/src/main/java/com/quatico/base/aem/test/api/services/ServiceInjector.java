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
package com.quatico.base.aem.test.api.services;


import com.quatico.base.aem.test.api.services.IServiceBuilder.InjectedService;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class ServiceInjector<T> implements IServiceInjector<T> {
	private final Map<Class<?>, Object> requiredServicesMap;
	private       T                   service;
	private       State[]             states;
	
	public ServiceInjector(InjectedService<?>... requiredSerivces) {
		this.states = new State[0];
		this.requiredServicesMap = new HashMap<>();
		for (InjectedService<?> required : requiredSerivces) {
			try {
				Object service = getService(required);
				this.requiredServicesMap.put(required.getType(), service);
			} catch (Exception ex) {
				// continue with remaining services
			}
		}
	}
	
	@Override
	public <C extends T> T create(C service) {
		this.service = service;
		for (State state : this.states) {
			injectState(state.getFieldName(), state.getValue());
		}
		initServiceReferenceFields(service.getClass());
		
		return this.service;
	}
	
	@Override
	public ServiceInjector<T> injectState(State... states) {
		this.states = states;
		return this;
	}
	
	private ServiceInjector<T> injectState(String fieldName, Object value) {
		try {
			Field field = findDeclaredField(fieldName, this.service.getClass());
			field.setAccessible(true);
			field.set(this.service, value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this;
	}
	
	private void initServiceReferenceFields(Class<?> serviceClass) {
		for (Field field : serviceClass.getDeclaredFields()) {
			if (this.requiredServicesMap.containsKey(field.getType())) {
				injectState(field.getName(), this.requiredServicesMap.get(field.getType()));
			}
		}
		Class<?> superclass = serviceClass.getSuperclass();
		if (superclass != Object.class) {
			initServiceReferenceFields(superclass);
		}
	}
	
	private Field findDeclaredField(String fieldName, Class<?> container) throws NoSuchFieldException {
		try {
			return container.getDeclaredField(fieldName);
		} catch (NoSuchFieldException ex) {
			Class<?> parent = container.getSuperclass();
			if (parent != null) {
				return findDeclaredField(fieldName, parent);
			}
			throw ex;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <R> R getService(InjectedService<R> service) throws Exception {
		Object result = service.getInstance();
		if (result instanceof IServiceBuilder) {
			result = ((IServiceBuilder) result).getService(new ServiceInjector<>()).getInstance();
		}
		return (R) result;
	}
	
}
