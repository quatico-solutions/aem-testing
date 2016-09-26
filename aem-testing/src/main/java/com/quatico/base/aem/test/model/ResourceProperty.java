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
package com.quatico.base.aem.test.model;


import static com.day.cq.commons.jcr.JcrConstants.JCR_NAME;
import static com.day.cq.commons.jcr.JcrConstants.JCR_PRIMARYTYPE;
import static com.day.cq.commons.jcr.JcrConstants.JCR_TITLE;

import org.apache.sling.jcr.resource.JcrResourceConstants;

import com.day.cq.commons.jcr.JcrConstants;


public class ResourceProperty {
	public static final String NAME                = JCR_NAME;
	public static final String VALUE               = "value";
	public static final String HIDE_IN_NAV         = "hideInNav";
	public static final String PRIMARY_TYPE        = JCR_PRIMARYTYPE;
	public static final String RESOURCE_TYPE       = JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY;
	public static final String RESOURCE_SUPER_TYPE = JcrResourceConstants.SLING_RESOURCE_SUPER_TYPE_PROPERTY;
	public static final String SEARCH_PATH         = "searchPath";
	public static final String TEMPLATE            = "cq:template";
	public static final String TARGET              = "target";
	public static final String TITLE               = JCR_TITLE;
	public static final String URL                 = "url";
	public static final String DESCRIPTION         = JcrConstants.JCR_DESCRIPTION;
	public static final String CREATED             = JcrConstants.JCR_CREATED;
	public static final String CREATED_BY          = JcrConstants.JCR_CREATED_BY;
	
}
