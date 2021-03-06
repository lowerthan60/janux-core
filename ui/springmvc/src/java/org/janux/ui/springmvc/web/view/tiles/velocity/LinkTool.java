/* Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Copyright 2005-2012 janux.org */

package org.janux.ui.springmvc.web.view.tiles.velocity;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:pavel@jehlanka.cz">Pavel Mueller</a>
 */
public class LinkTool extends org.apache.velocity.tools.view.tools.LinkTool 
{
	/**
	 * 
	 */
	public LinkTool(HttpServletRequest request, HttpServletResponse response, ServletContext application) 
	{
		this.request     = request;
		this.response    = response;
		this.application = application;
	}
}
