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


public class ComponentType extends ResourceType {
	
	public static final ComponentType PARSYS = new ComponentType("foundation/components/parsys");
	public static final ComponentType FOLDER = new ComponentType("foundation/components/folder");
	
	private final Class<?> controllerClass;
	
	protected ComponentType(String name, Class<?> controllerClass) throws IllegalArgumentException {
		super(name, Kind.COMPONENT);
		this.controllerClass = controllerClass;
	}
	
	protected ComponentType(String name) throws IllegalArgumentException {
		this(name, null);
	}
	
	public Class<?> getControllerClass() {
		return this.controllerClass;
	}
}
