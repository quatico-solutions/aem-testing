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


import static com.quatico.base.aem.test.model.ResourceProperty.PRIMARY_TYPE;
import static com.quatico.base.aem.test.model.ResourceProperty.RESOURCE_TYPE;
import static com.quatico.base.aem.test.model.ResourceProperty.TEMPLATE;

import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.api.IPages;
import com.quatico.base.aem.test.api.builders.PageContextBuilder;
import com.quatico.base.aem.test.api.values.PageType;
import com.quatico.base.aem.test.api.values.ResourceType;
import com.quatico.base.aem.test.model.PathIterator;
import com.quatico.base.aem.test.model.Properties;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.day.cq.wcm.api.Page;


public class Pages implements IPages {
	
	protected IAemClient client;
	
	public Pages(IAemClient client) {
		this.client = client;
	}
	
	@Override
	public Resource aPageWithParents(String path, Object... properties) throws Exception {
		Resource result = null;
		Page     page   = client.getPageManager().getPage(path);
		if (page != null) {
			return page.adaptTo(Resource.class);
		}
		
		PathIterator it = new PathIterator(path);
		it.first(); // root
		while (it.hasNext()) {
			String curPath = it.next();
			if (Arrays.asList("/content", "/libs", "/apps").contains(curPath)) {
				continue;
			}
			result = client.getResourceResolver().getResource(curPath);
			if (result == null) {
				if (it.hasNext()) {
					result = client.getContentBuilder().page(curPath, PageType.DEFAULT_TEMPLATE.getTemplate().getName(), new HashMap<>()).adaptTo(Resource.class);
				} else {
					Properties pairs    = new Properties(properties);
					String     template = toStringValue(pairs.remove(TEMPLATE));
					if (StringUtils.isEmpty(template)) {
						template = PageType.DEFAULT_TEMPLATE.getTemplate().getName();
					}
					if (client.getResourceResolver().getResource(template) == null) {
						client.getContentBuilder().resource(template, Collections.singletonMap(PRIMARY_TYPE, ResourceType.TEMPLATE_TYPE));
					}
					result = client.getContentBuilder().page(curPath, template, pairs.toMap()).adaptTo(Resource.class);
				}
			}
		}
		if (result == null) {
			throw new RuntimeException(MessageFormat.format("Cannot create result at '{0}'", result));
		}
		return result;
	}
	
	@Override
	public Resource aPageWithParents(Resource parent, String relativePath, Object... properties) throws Exception {
		if (relativePath == null) {
			relativePath = StringUtils.EMPTY;
		}
		if (StringUtils.isNotEmpty(relativePath) && !StringUtils.startsWith(relativePath, "/")) {
			relativePath += "/";
		}
		return aPageWithParents(parent.getPath() + relativePath, properties);
	}
	
	@Override
	public PageContextBuilder aPageContext() {
		return new PageContextBuilder(client);
	}
	
	protected Resource createPage(Resource parent, String relativePath, PageType pageType, Object... properties) throws Exception {
		return aPageWithParents(getChildPath(parent, relativePath), new Properties(properties).append(RESOURCE_TYPE, pageType).append(TEMPLATE, pageType.getTemplate()).toArray());
	}
	
	protected Resource createPage(String path, PageType pageType, Object... properties) throws Exception {
		return aPageWithParents(path, new Properties(properties).append(RESOURCE_TYPE, pageType).append(TEMPLATE, pageType.getTemplate()).toArray());
	}
	
	private String getChildPath(Resource pageResource, String relativePath) {
		String result = pageResource.getPath();
		if (!StringUtils.startsWith(relativePath, "/")) {
			result += "/";
		}
		return result + relativePath;
	}
	
	private String toStringValue(Object value) {
		if (value == null) {
			return StringUtils.EMPTY;
		}
		if (value instanceof String) {
			return (String) value;
		}
		return value.toString();
	}
	
}
