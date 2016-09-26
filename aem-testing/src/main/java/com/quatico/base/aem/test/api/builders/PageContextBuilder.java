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


import static org.apache.sling.scripting.jsp.taglib.DefineObjectsTag.DEFAULT_REQUEST_NAME;
import static org.apache.sling.scripting.jsp.taglib.DefineObjectsTag.DEFAULT_RESOURCE_NAME;
import static org.apache.sling.scripting.jsp.taglib.DefineObjectsTag.DEFAULT_RESPONSE_NAME;
import static org.apache.sling.scripting.jsp.taglib.DefineObjectsTag.DEFAULT_SLING_NAME;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;
import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_CURRENT_DESIGN_NAME;
import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_CURRENT_PAGE_NAME;
import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_CURRENT_STYLE_NAME;
import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_PAGE_PROPERTIES_NAME;
import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_PROPERTIES_NAME;

import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.model.Properties;

import java.util.Map;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMMode;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Style;


public class PageContextBuilder extends AbstractBuilder<PageContext> {
	
	private final IAemClient client;
	
	public PageContextBuilder(IAemClient client) {
		super(PageContext.class);
		this.client = client;
		properties(new Object[0]);
		response(client.getResponse());
		request(client.getRequest());
		currentDesign(new DesignBuilder());
		currentStyle(new StyleBuilder());
	}
	
	public Resource page() {
		return getValue("page", Resource.class);
	}
	
	public PageContextBuilder page(Resource value) {
		return addValue("page", value);
	}
	
	public String pathInfo() {
		return getValue("pathInfo", String.class);
	}
	
	public PageContextBuilder pathInfo(String value) {
		return addValue("pathInfo", value);
	}
	
	public Resource component() {
		return getValue("component", Resource.class);
	}
	
	public PageContextBuilder component(Resource value) {
		return addValue("component", value);
	}
	
	public Design currentDesign() {
		return getValue("currentDesign", Design.class);
	}
	
	public PageContextBuilder currentDesign(DesignBuilder child) {
		return addChild("currentDesign", child);
	}
	
	public Style currentStyle() {
		return getValue("currentStyle", Style.class);
	}
	
	public PageContextBuilder currentStyle(StyleBuilder child) {
		return addChild("currentStyle", child);
	}
	
	public SlingHttpServletRequest request() {
		return getValue("request", SlingHttpServletRequest.class);
	}
	
	public PageContextBuilder request(SlingHttpServletRequest value) {
		return addValue("request", value);
	}
	
	public SlingHttpServletResponse response() {
		return getValue("response", SlingHttpServletResponse.class);
	}
	
	public PageContextBuilder response(SlingHttpServletResponse value) {
		return addValue("response", value);
	}
	
	public Object[] properties() {
		return getValue("properties");
	}
	
	public PageContextBuilder properties(Object... properties) {
		return addValue("properties", properties);
	}
	
	@Override
	protected PageContext internalBuild() throws Exception {
		this.result = mock(PageContext.class);
		
		if (!hasValue("component")) {
			component(page());
		}
		
		request(client.aRequest()
				        .parent(request())
				        .pageContext(result)
				        .resourcePath(hasValue("pathInfo") ? pathInfo() : component().getPath()).build()).build();
		
		Map<String, Object> props = new Properties(properties())
				.appendIfNotSet(DEFAULT_CURRENT_PAGE_NAME, page().adaptTo(Page.class))
				.appendIfNotSet(DEFAULT_CURRENT_DESIGN_NAME, currentDesign())
				.appendIfNotSet(DEFAULT_CURRENT_STYLE_NAME, currentStyle())
				.appendIfNotSet(DEFAULT_PROPERTIES_NAME, component().adaptTo(ValueMap.class))
				.appendIfNotSet(DEFAULT_RESOURCE_NAME, component())
				.appendIfNotSet(DEFAULT_SLING_NAME, client.getSlingScriptHelper())
				.appendIfNotSet(DEFAULT_REQUEST_NAME, request())
				.appendIfNotSet(DEFAULT_RESPONSE_NAME, response()).toMap();
		
		//noinspection SuspiciousMethodCalls
		when(result.getAttribute(anyString())).thenAnswer(invocationOnMock -> props.get(invocationOnMock.getArguments()[0]));
		//noinspection SuspiciousMethodCalls
		when(result.getAttribute(anyString(), anyInt())).thenAnswer(invocationOnMock -> props.get(invocationOnMock.getArguments()[0]));
		
		
		if (props.containsKey("isEditMode") && Boolean.parseBoolean((String) props.get("isEditMode"))) {
			WCMMode.EDIT.toRequest(request());
		}
		
		doAnswer(invocation -> {
			String key   = (String) invocation.getArguments()[0];
			Object value = invocation.getArguments()[1];
			when(((PageContext) invocation.getMock()).getAttribute(key)).thenReturn(value);
			return null;
		}).when(result).setAttribute(anyString(), anyObject());
		
		Resource contentResource = page().getChild(JCR_CONTENT);
		if (contentResource != null) {
			ValueMap map = contentResource.adaptTo(ValueMap.class);
			when(result.getAttribute(DEFAULT_PAGE_PROPERTIES_NAME)).thenReturn(map);
		}
		
		when(result.getResponse()).thenReturn(response());
		when(result.getRequest()).thenReturn(request());
		
		JspWriter writer = mock(JspWriter.class);
		when(result.getOut()).thenReturn(writer);
		
		return result;
	}
}
