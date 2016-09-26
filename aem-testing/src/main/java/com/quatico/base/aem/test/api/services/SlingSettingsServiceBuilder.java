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


import com.quatico.base.aem.test.api.builders.AbstractBuilder;

import java.util.Collections;

import org.apache.sling.settings.SlingSettingsService;
import org.apache.sling.testing.mock.sling.services.MockSlingSettingService;


public class SlingSettingsServiceBuilder extends AbstractBuilder<SlingSettingsService> implements IServiceBuilder<SlingSettingsService> {
	
	public SlingSettingsServiceBuilder() {
		super(SlingSettingsService.class);
		runMode("author");
	}
	
	public String runMode() {
		return getValue("runMode", String.class);
	}
	
	public SlingSettingsServiceBuilder runMode(String value) {
		return addValue("runMode", value);
	}
	
	@Override
	protected SlingSettingsService internalBuild() throws Exception {
		this.result = new MockSlingSettingService(Collections.singleton(runMode()));
		return result;
	}
	
	@Override
	public InjectedService<SlingSettingsService> getService(IServiceInjector<SlingSettingsService> injector) throws Exception {
		return new InjectedService<>(SlingSettingsService.class, injector.create(build()));
	}
}
