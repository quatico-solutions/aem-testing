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


import com.quatico.base.aem.test.api.builders.ITestBuilder;


public interface IServiceBuilder<T> extends ITestBuilder<T> {
	
	InjectedService<T> getService(IServiceInjector<T> injector) throws Exception;
	
	class InjectedService<T> {
		private final Class<T> type;
		private final Object instance;
		
		public <R extends T> InjectedService(Class<T> type, R instance) {
			this.type = type;
			this.instance = instance;
		}
		
		
		
		public Class<T> getType() {
			return type;
		}
		
		@SuppressWarnings("unchecked")
		public <R extends T> R getInstance() {
			return (R) instance;
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName() + "{type: '" + type.getName() + "'}";
		}
		
	}
}
