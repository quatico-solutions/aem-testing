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


import static com.quatico.base.aem.test.model.ResourceProperty.RESOURCE_TYPE;
import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;

import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.api.IComponents;
import com.quatico.base.aem.test.api.IResources;
import com.quatico.base.aem.test.api.builders.ComponentContextBuilder;
import com.quatico.base.aem.test.api.values.ComponentType;
import com.quatico.base.aem.test.model.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;


public class Components implements IComponents {
	protected final IResources resources;
	protected final IAemClient client;
	
	public Components(IResources resources, IAemClient client) {
		this.resources = resources;
		this.client = client;
	}
	
	@Override
	public Resource aParSys(Resource parent, String nodeName, Object... properties) throws Exception {
		return createComponent(parent, nodeName, ComponentType.PARSYS, properties);
	}
	
	@Override
	public Resource aFolder(Resource parent, String nodeName, Object... properties) throws Exception {
		return createComponent(parent, nodeName, ComponentType.FOLDER, properties);
	}
	
	@Override
	public Resource aFolderWithParents(String path, Object... properties) throws Exception {
		return createComponent(path, ComponentType.FOLDER, properties);
	}
	
	@Override
	public ComponentContextBuilder aComponentContext() {
		return new ComponentContextBuilder(client);
	}
	
	protected Resource createComponent(Resource parent, String relativePath, ComponentType type, Object... properties) throws Exception {
		if (parent == null) {
			throw new IllegalStateException();
		}
		return createComponent(getComponentPath(parent, relativePath), type, properties);
	}
	
	protected Resource createComponent(String path, ComponentType type, Object... properties) throws Exception {
		if (StringUtils.isBlank(path)) {
			path = StringUtils.EMPTY;
		}
		return resources.aResource(path, new Properties(properties).append(RESOURCE_TYPE, type).toArray());
	}
	
	protected String getComponentPath(Resource pageResource, String relativePath) {
		String   result  = pageResource.getPath();
		Resource content = pageResource.getChild(JCR_CONTENT);
		if (content != null) {
			result = result + "/" + JCR_CONTENT;
		}
		if (!StringUtils.startsWith(relativePath, "/")) {
			result += "/";
		}
		return result + relativePath;
	}
}
