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


import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.NonExistingResource;
import org.apache.sling.api.resource.Resource;


public class ResourceType extends TypedStringValue {
	
	public static final ResourceType NON_EXISTING      = new ResourceType(Resource.RESOURCE_TYPE_NON_EXISTING);
	public static final ResourceType NODE_TYPE         = new ResourceType("cq:Node", Kind.NODE);
	public static final ResourceType FOLDER_TYPE       = new ResourceType("cq:Folder", Kind.NODE);
	public static final ResourceType PAGE_TYPE         = new ResourceType("cq:Page", Kind.NODE);
	public static final ResourceType PAGE_CONTENT_TYPE = new ResourceType("cq:PageContent", Kind.NODE);
	public static final ResourceType TEMPLATE_TYPE     = new ResourceType("cq:Template", Kind.NODE);
	
	private final Kind kind;
	
	public ResourceType(String name) throws IllegalArgumentException {
		this(name, null);
	}
	
	public ResourceType(String name, Kind kind) throws IllegalArgumentException {
		super(name);
		this.kind = kind;
	}
	
	public Kind getKind() {
		return this.kind;
	}
	
	/*
	 * Returns true if the given resource has this {@link ResourceType}.
	 *
	 * @param resource {@link Resource}
	 * @return {@link Boolean}
	 */
	public boolean appliesTo(Resource resource) {
		if (resource == null || resource instanceof NonExistingResource) {
			return this == NON_EXISTING;
		}
		
		String resourceType = resource.getResourceType();
		if (isTypeEqualTo(resourceType)) {
			return true;
		}
		Resource contentResource = resource.getChild(JCR_CONTENT);
		return contentResource != null && isTypeEqualTo(contentResource.getResourceType());
	}
	
	private boolean isTypeEqualTo(String resourceType) {
		String pageName = StringUtils.substringAfterLast(getName(), "/");
		return StringUtils.isNotEmpty(pageName) && resourceType != null ? resourceType.endsWith(pageName) : this.equals(resourceType);
	}
	
	public enum Kind {
		COMPONENT, PAGE, NODE
	}
}
