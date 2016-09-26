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


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;

import org.apache.jackrabbit.api.security.user.Authorizable;


public class AuthorizableBuilder extends AbstractBuilder<Authorizable> {
	
	public AuthorizableBuilder() {
		super(Authorizable.class);
		principal(new PrincipalBuilder());
	}
	
	public Principal principal() {
		return getValue("principal", Principal.class);
	}
	
	public AuthorizableBuilder principal(PrincipalBuilder child) {
		return addChild("principal", child);
	}
	
	@Override
	protected Authorizable internalBuild() throws Exception {
		this.result = mock(Authorizable.class);
		when(result.getPrincipal()).thenReturn(principal());
		return result;
	}
}
