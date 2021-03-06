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

package org.janux.ui.springmvc.web.view.tiles;

import org.springframework.core.NestedRuntimeException;

/**
 *
 * @author <a href="mailto:pavel@jehlanka.cz">Pavel Mueller</a>
 * @author <a href="mailto:kirlen@janux.org">Kevin Irlen</a>
 */
public class TileControllerException extends NestedRuntimeException {

    /**
     * Constructor
     * @param msg message
     */
    public TileControllerException(String msg) {
        super(msg);
    }
    
    /**
     * Constructor
     * @param msg message
     * @param ex exception
     */
    public TileControllerException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
