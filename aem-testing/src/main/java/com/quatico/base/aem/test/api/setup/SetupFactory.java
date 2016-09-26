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
package com.quatico.base.aem.test.api.setup;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;


public final class SetupFactory<T> {
	
	final Class<T> interfaze;
	
	private SetupFactory(Class<T> interfaze) {
		this.interfaze = interfaze;
	}
	
	public static <K> SetupFactory<K> create(Class<K> test) {
		return new SetupFactory<>(test);
	}
	
	public T getSetup(final Object... implementors) {
		final Map<Class<?>, Object> interfaces = new HashMap<>();
		for (Object implementor : implementors) {
			List<Class<?>> implementorInterfaces = ClassUtils.getAllInterfaces(implementor.getClass());
			
			assert !implementorInterfaces.isEmpty();
			
			for (Class<?> implementorInterface : implementorInterfaces) {
				interfaces.put(implementorInterface, implementor);
			}
		}
		
		@SuppressWarnings("unchecked")
		T result = (T) Proxy.newProxyInstance(this.interfaze.getClassLoader(), new Class<?>[] { this.interfaze }, new InvocationHandler() {
			private final Object fProxyObject = new Object() {
				@Override
				public String toString() {
					return "Proxy for: " + SetupFactory.this.interfaze.getDeclaringClass().getName(); //$NON-NLS-1$
				}
			};
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if (method.getDeclaringClass().equals(Object.class)) {
					method.invoke(this.fProxyObject, args);
				}
				
				if (interfaces.containsKey(method.getDeclaringClass())) {
					return method.invoke(interfaces.get(method.getDeclaringClass()), args);
				}
				
				throw new UnsupportedOperationException("Created proxy has not received an implementation that supports this method: " + method.getName()); //$NON-NLS-1$
			}
		});
		
		return result;
	}
}
