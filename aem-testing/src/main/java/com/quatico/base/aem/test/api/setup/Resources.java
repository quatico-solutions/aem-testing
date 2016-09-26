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

import com.quatico.base.aem.test.api.IAemClient;
import com.quatico.base.aem.test.api.IResources;
import com.quatico.base.aem.test.api.values.ResourceType;
import com.quatico.base.aem.test.model.PathIterator;
import com.quatico.base.aem.test.model.Properties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;


public class Resources implements IResources {
	
	protected final IAemClient client;
	
	public Resources(IAemClient client) {
		this.client = client;
	}
	
	@Override
	public Resource aResource(String path, Object... properties) throws Exception {
		Map<String, Object> pairs = new Properties(properties).toMap();
		pairs.put("path", path);
		
		if (StringUtils.isEmpty(path)) {
			path = "/";
		}
		return getOrCreate(path, pairs);
	}
	
	@Override
	public Resource aResource(Resource parent, String relativePath, Object... properties) throws Exception {
		if (relativePath == null) {
			relativePath = StringUtils.EMPTY;
		}
		if (StringUtils.isNotEmpty(relativePath) && !StringUtils.startsWith(relativePath, "/")) {
			relativePath += "/";
		}
		return aResource(parent.getPath() + relativePath, properties);
	}
	
	@Override
	public Iterator<Resource> aResourceIterator(Iterator<Resource> iter, Resource newChild) {
		List<Resource> result = new LinkedList<>();
		while (iter != null && iter.hasNext()) {
			result.add(iter.next());
		}
		result.add(newChild);
		return result.iterator();
	}
	
	@Override
	public Iterable<Resource> aResourceIterable(Resource... resources) {
		return new ResourceIterable(resources);
	}
	
	protected Resource createResource(String path, ResourceType resourceType, Object... properties) throws Exception {
		return aResource(path, new Properties(properties).append(RESOURCE_TYPE, resourceType).toArray());
	}
	
	protected Resource createResource(Resource parent, String relativePath, ResourceType resourceType, Object... properties) throws Exception {
		return aResource(parent, relativePath, new Properties(properties).append(RESOURCE_TYPE, resourceType).toArray());
	}
	
	private Resource getOrCreate(String path, Map<String, Object> properties) throws PersistenceException, RepositoryException {
		ResourceResolver resolver = client.getResourceResolver();
		Resource         resource = resolver.getResource(path);
		if (properties.containsKey("unit:vanityTarget")) {
			resource = resolver.resolve((String) properties.get("unit:vanityTarget"));
		} else {
			Resource     parent = resolver.getResource("/");
			PathIterator it     = new PathIterator(path);
			it.first();
			while (it.hasNext()) {
				String curPath = it.next();
				String segment = StringUtils.substringAfterLast(curPath, "/");
				resource = resolver.getResource(curPath);
				if (resource == null) {
					if (it.hasNext()) {
						resource = resolver.create(parent, segment, new HashMap<>());
					} else {
						resource = resolver.create(parent, segment, properties);
					}
				}
				parent = resource;
			}
		}
		return resource;
	}
	
	private static class ResourceIterable implements Iterable<Resource> {
		private final Iterator<Resource> iterator;
		
		public ResourceIterable(Resource[] resources) {
			this.iterator = Arrays.asList(resources).iterator();
		}
		
		@Override
		public Iterator<Resource> iterator() {
			return this.iterator;
		}
	}
}
