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


import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.api.IAssets;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.day.cq.dam.api.Asset;

import io.wcm.testing.mock.aem.builder.ContentBuilder;


public class AssetBuilder extends AbstractBuilder<Asset> {
	
	private final IAemClient client;
	
	public AssetBuilder(IAemClient client) {
		super(Asset.class);
		this.client = client;
		mimetype(IAssets.DEFAULT_MIMETYPE);
		metadata(new HashMap<>());
		width(10);
		height(10);
	}
	
	public String path() {
		return getValue("path", String.class);
	}
	
	public AssetBuilder path(String value) {
		return addValue("path", value);
	}
	
	public String mimetype() {
		return getValue("mimetype");
	}
	
	public AssetBuilder mimetype(String value) {
		return addValue("mimetype", value);
	}
	
	public Map<String, Object> metadata() {
		return getValue("metadata");
	}
	
	public AssetBuilder metadata(Map<String, Object> value) {
		return addValue("metadata", value);
	}
	
	public Integer width() {
		return getValue("width");
	}
	
	public AssetBuilder width(Integer value) {
		return addValue("width", value);
	}
	
	public Integer height() {
		return getValue("height");
	}
	
	public AssetBuilder height(Integer value) {
		return addValue("height", value);
	}
	
	public InputStream inputStream() {
		return getValue("inputStream");
	}
	
	public AssetBuilder inputStream(InputStream value) {
		return addValue("inputStream", value);
	}
	
	public String classpathResource() {
		return getValue("classpathResource", String.class);
	}
	
	public AssetBuilder classpathResource(String value) {
		return addValue("classpathResource", value);
	}
	
	public List<RenditionBuilder> renditions() throws IllegalPropertyException {
		return getChildren("renditions", RenditionBuilder.class);
	}
	
	public AssetBuilder rendition(RenditionBuilder child) throws IllegalPropertyException {
		return addChild("renditions", child, true);
	}
	
	@Override
	protected Asset internalBuild() throws Exception {
		ContentBuilder content = client.getContentBuilder();
		
		String path = path();
		if (StringUtils.isBlank(path)) {
			throw new RuntimeException("Cannot build asset with null path.");
		}
		
		if (hasValue("classpathResource") && StringUtils.isNotBlank(classpathResource())) {
			this.result = content.asset(path, classpathResource(), mimetype(), metadata());
		} else if (hasValue("inputStream")) {
			this.result = content.asset(path, inputStream(), mimetype(), metadata());
		} else {
			this.result = content.asset(path, width(), height(), mimetype(), metadata());
		}
		
		if (hasChildren("renditions")) {
			for (RenditionBuilder cur : renditions()) {
				cur.build();
				if (cur.hasInputStream()) {
					content.assetRendition(result, cur.name(), cur.inputStream(), cur.mimetype());
				} else if (StringUtils.isNotBlank(cur.classpathResource())) {
					content.assetRendition(result, cur.name(), cur.classpathResource(), cur.mimetype());
				} else {
					content.assetRendition(result, cur.name(), cur.width(), cur.height(), cur.mimetype());
				}
			}
		}
		
		return result;
	}
}
