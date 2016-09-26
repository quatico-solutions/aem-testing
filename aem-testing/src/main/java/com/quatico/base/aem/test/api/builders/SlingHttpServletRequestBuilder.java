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


import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.spy;

import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.model.Properties;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.scripting.jsp.util.TagUtil;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;

import com.day.cq.commons.PathInfo;


public class SlingHttpServletRequestBuilder extends AbstractBuilder<SlingHttpServletRequest> {
	
	private final IAemClient client;
	
	public SlingHttpServletRequestBuilder(IAemClient client) {
		super(SlingHttpServletRequest.class);
		this.client = client;
		params(new Object[0]);
	}
	
	public Object[] params() {
		return getValue("params", Object[].class);
	}
	
	public SlingHttpServletRequestBuilder params(Object... value) {
		return addValue("params", value);
	}
	
	public SlingHttpServletRequest parent() {
		return getValue("parent", SlingHttpServletRequest.class);
	}
	
	public SlingHttpServletRequestBuilder parent(SlingHttpServletRequest value) {
		return addValue("parent", value);
	}
	
	public String resourcePath() {
		return getValue("resourcePath", String.class);
	}
	
	public SlingHttpServletRequestBuilder resourcePath(String value) {
		return addValue("resourcePath", value);
	}
	
	public Resource resource() {
		return getValue("resource", Resource.class);
	}
	
	public SlingHttpServletRequestBuilder resource(Resource value) {
		return addValue("resource", value);
	}
	
	public PageContext pageContext() {
		return getValue("pageContext", PageContext.class);
	}
	
	public SlingHttpServletRequestBuilder pageContext(PageContext value) {
		return addValue("pageContext", value);
	}
	
	@Override
	protected SlingHttpServletRequest internalBuild() throws Exception {
		if (hasValue("pageContext")) {
			try {
				this.result = TagUtil.getRequest(pageContext());
			} catch (IllegalStateException ignored) {
				// falls through
			}
		} else if (hasValue("parent")) {
			this.result = parent();
		}
		
		if (result == null) {
			this.result = client.getRequest();
		}
		
		Properties requestParams = new Properties(params());
		
		String resourcePath = hasValue("resource") ? resource().getPath() : resourcePath();
		if (result instanceof MockSlingHttpServletRequest) {
			MockSlingHttpServletRequest request = (MockSlingHttpServletRequest) result;
			if (!requestParams.isEmpty()) {
				request.setParameterMap(requestParams.toMap());
			}
			if (StringUtils.isNotBlank(resourcePath)) {
				request.setResource(client.getResourceResolver().getResource(resourcePath));
			}
		}
		
		if (StringUtils.isNotBlank(resourcePath)) {
			if (!mockingDetails(result).isSpy()) {
				this.result = spy(result);
			}
			doReturn(resourcePath).when(result).getPathInfo();
			doReturn(resourcePath).when(result).getRequestURI();
			doReturn(new PathInfo(resourcePath)).when(result).getRequestPathInfo();
		}
		
		return result;
	}
}
