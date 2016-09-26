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


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;


public class UserManagerBuilder extends AbstractBuilder<UserManager> {
	public UserManagerBuilder() {
		super(UserManager.class);
		authorizable(new AuthorizableBuilder());
	}
	
	public Authorizable authorizable() {
		return getValue("authorizable", Authorizable.class);
	}
	
	public UserManagerBuilder authorizable(AuthorizableBuilder child) {
		return addChild("authorizable", child);
	}
	
	@Override
	protected UserManager internalBuild() throws Exception {
		this.result = mock(UserManager.class);
		when(result.getAuthorizable(anyString())).thenReturn(authorizable());
		when(result.getAuthorizable(any(Principal.class))).thenReturn(authorizable());
		return result;
	}
}
