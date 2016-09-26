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
package com.quatico.base.aem.test.api;


import com.quatico.base.aem.test.api.values.ResourceType;
import com.quatico.base.aem.test.model.ResourceProperty;

import java.text.MessageFormat;
import java.util.UUID;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.day.cq.wcm.api.Page;


public class AemMatchers {
	
	public static Matcher<Node> nodeExistsAt(String path) {
		return new TypeSafeMatcher<Node>() {
			
			@Override
			protected boolean matchesSafely(Node actual) {
				try {
					return actual != null
					       && StringUtils.isNotBlank(actual.getProperty(ResourceProperty.PRIMARY_TYPE).getString())
					       && path.equals(actual.getPath());
				} catch (RepositoryException ex) {
					return false;
				}
			}
			
			@Override
			public void describeTo(Description description) {
				description.appendText(MessageFormat.format("Node with non-null jcr:primaryType at {0} should exist.", path));
			}
		};
	}
	
	public static Matcher<Page> pageExistsAt(String path) {
		return new TypeSafeMatcher<Page>() {
			
			@Override
			protected boolean matchesSafely(Page actual) {
				if (actual == null || actual.getContentResource() == null) {
					return false;
				}
				Resource resource = actual.adaptTo(Resource.class);
				ValueMap content  = actual.getContentResource().adaptTo(ValueMap.class);
				return resource != null
				       && !resource.isResourceType(Resource.RESOURCE_TYPE_NON_EXISTING)
				       && ResourceType.PAGE_CONTENT_TYPE.getName().equals(actual.getProperties().get(ResourceProperty.PRIMARY_TYPE))
				       && StringUtils.isNotBlank(actual.getTemplate().getPath())
				       && path.endsWith(content.get(ResourceProperty.TITLE).toString())
				       && path.equals(resource.getPath());
				
			}
			
			@Override
			public void describeTo(Description description) {
				description.appendText(MessageFormat.format("Page with valid jcr:resourceType at {0} should exist.", path));
			}
		};
	}
	
	public static Matcher<Resource> resourceExistsAt(String path) {
		return new TypeSafeMatcher<Resource>() {
			
			@Override
			protected boolean matchesSafely(Resource actual) {
				if (actual == null) {
					return false;
				}
				return !actual.isResourceType(Resource.RESOURCE_TYPE_NON_EXISTING)
				       && StringUtils.isNotBlank((CharSequence) actual.adaptTo(ValueMap.class).get(ResourceProperty.PRIMARY_TYPE))
				       && path.equals(actual.getPath());
				
			}
			
			@Override
			public void describeTo(Description description) {
				description.appendText(MessageFormat.format("Resource with valid jcr:primaryType at {0} should exist.", path));
			}
		};
	}
	
	public static Matcher<String> pathEndsWithUuid() {
		return new TypeSafeMatcher<String>() {
			@Override
			protected boolean matchesSafely(String value) {
				try {
					UUID.fromString(StringUtils.substringAfterLast(value, "/"));
				} catch (IllegalArgumentException ex) {
					return false;
				}
				return true;
			}
			
			@Override
			public void describeTo(Description description) {
				description.appendText("String should end with a UUID");
			}
		};
	}
	
}
