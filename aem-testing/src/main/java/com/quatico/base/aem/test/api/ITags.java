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


import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;


public interface ITags {
	
	String renderTag(TagSupport tag, PageContext pageContext, String body) throws Exception;
	
	String renderTag(TagSupport tag, PageContext pageContext) throws Exception;
	
	String renderTag(TagSupport tag, String body) throws Exception;
	
	String renderTag(TagSupport tag) throws Exception;
	
}
