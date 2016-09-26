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


import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.quatico.base.aem.test.api.IPages;
import com.quatico.base.aem.test.api.ITags;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.scripting.jsp.jasper.runtime.JspWriterImpl;


public class Tags implements ITags {
	
	protected final IPages pages;
	
	public Tags(IPages pages) {
		this.pages = pages;
	}
	
	@Override
	public String renderTag(TagSupport tag, PageContext pageContext, String body) throws Exception {
		StringWriter        strWriter = new StringWriter();
		HttpServletResponse response  = mock(HttpServletResponse.class);
		when(response.getWriter()).thenReturn(new PrintWriter(strWriter, true));
		
		if (!mockingDetails(pageContext).isSpy()) {
			pageContext = spy(pageContext);
		}
		
		JspWriter jspWriter = new JspWriterImpl(response);
		doReturn(jspWriter).when(pageContext).getOut();
		tag.setPageContext(pageContext);
		
		if (Tag.EVAL_BODY_INCLUDE == tag.doStartTag()) {
			jspWriter.flush();
			strWriter.write(body);
		}
		jspWriter.flush();
		tag.doEndTag();
		jspWriter.flush();
		tag.release();
		return strWriter.toString();
	}
	
	@Override
	public String renderTag(TagSupport tag, PageContext pageContext) throws Exception {
		return renderTag(tag, pageContext, StringUtils.EMPTY);
	}
	
	@Override
	public String renderTag(TagSupport tag, String body) throws Exception {
		PageContext aPageContext = pages.aPageContext().page(pages.aPageWithParents("/content/ko/fakePage")).build();
		
		return renderTag(tag, aPageContext, body);
	}
	
	@Override
	public String renderTag(TagSupport tag) throws Exception {
		return renderTag(tag, StringUtils.EMPTY);
	}
}
