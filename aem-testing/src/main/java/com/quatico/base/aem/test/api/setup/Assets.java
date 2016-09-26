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


import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.api.IAssets;
import com.quatico.base.aem.test.api.builders.AssetBuilder;
import com.quatico.base.aem.test.api.builders.RenditionBuilder;
import com.quatico.base.aem.test.model.Properties;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.apache.sling.api.resource.Resource;

import com.day.cq.commons.jcr.JcrConstants;


public class Assets implements IAssets {
	
	protected final IAemClient client;
	
	public Assets(IAemClient client) {
		this.client = client;
	}
	
	@Override
	public Resource anAsset(String path, Object... properties) throws Exception {
		Properties          props    = new Properties(properties);
		Map<String, Object> pairs    = new Properties(properties).toMap();
		String              mimeType = (String) pairs.remove(JcrConstants.JCR_MIMETYPE);
		if (mimeType == null) {
			mimeType = DEFAULT_MIMETYPE;
		}
		return client.getContentBuilder().asset(path, new ByteArrayInputStream("".getBytes("UTF-8")), mimeType, props.toMap()).adaptTo(Resource.class);
	}
	
	@Override
	public AssetBuilder anAsset() {
		return new AssetBuilder(client);
	}
	
	@Override
	public RenditionBuilder anAssetRendition() {
		return new RenditionBuilder();
	}
	
}
