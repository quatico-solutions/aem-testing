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


public abstract class TypedStringValue {
	
	protected final String name;
	
	protected TypedStringValue(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj.getClass().isAssignableFrom(getClass())) {
			TypedStringValue other = (TypedStringValue) obj;
			return this.name.equals(other.name);
		}
		if (String.class == obj.getClass()) {
			return obj.equals(this.name);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
