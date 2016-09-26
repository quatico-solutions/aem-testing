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


public class PageType extends ResourceType {
	
	public static final PageType DEFAULT_TEMPLATE = new PageType("/apps/aem-testing/templates/aemtest-template", new Template("/apps/aem-testing/templates/aemtest-template"));
	
	private final Template template;
	
	protected PageType(String name, Template template) throws IllegalArgumentException {
		super(name, Kind.PAGE);
		this.template = template;
	}
	
	public Template getTemplate() {
		return this.template;
	}
}
