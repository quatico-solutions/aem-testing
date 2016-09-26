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


import com.quatico.base.aem.test.api.IAssets;

import java.io.InputStream;


public class RenditionBuilder extends AbstractBuilder<Void> {
	
	public RenditionBuilder() {
		super(Void.class);
		name("original");
		mimetype(IAssets.DEFAULT_MIMETYPE);
		width(10);
		height(10);
	}
	
	public String name() {
		return getValue("name", String.class);
	}
	
	public RenditionBuilder name(String value) {
		return addValue("name", value);
	}
	
	public String mimetype() {
		return getValue("mimetype", String.class);
	}
	
	public RenditionBuilder mimetype(String value) {
		return addValue("mimetype", value);
	}
	
	public Integer width() {
		return getValue("width", Integer.class);
	}
	
	public RenditionBuilder width(Integer value) {
		return addValue("width", value);
	}
	
	public Integer height() {
		return getValue("height", Integer.class);
	}
	
	public RenditionBuilder height(Integer value) {
		return addValue("height", value);
	}
	
	public InputStream inputStream() {
		return getValue("inputStream", InputStream.class);
	}
	
	public RenditionBuilder inputStream(InputStream value) {
		return addValue("inputStream", value);
	}
	
	public boolean hasInputStream() {
		return hasValue("inputStream");
	}
	
	public String classpathResource() {
		return getValue("classpathResource", String.class);
	}
	
	public RenditionBuilder classpathResource(String value) {
		return addValue("classpathResource", value);
	}
	
	@Override
	protected Void internalBuild() throws Exception {
		return null;
	}
	
}
