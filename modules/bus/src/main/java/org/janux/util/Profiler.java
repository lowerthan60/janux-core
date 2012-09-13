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

package org.janux.util;

import org.apache.commons.logging.Log;

/**
 ***************************************************************************************************
 * Utility class that provides profiling convenience methods
 * 
 * @author   Philippe Paravicini
 * @version  $Revision: 1.1.1.1 $ - $Date: 2005-10-18 02:24:02 $
 ***************************************************************************************************
 */
public class Profiler
{
	/** 
	 * convenience method to log profile messages at the INFO level 
	 * - wrap this method in a 'isInfoEnabled' if statement to minimize logging overhead
	 */
	public static void recordTime(Log log, String msg, long startTime)
	{
		long end = System.currentTimeMillis();
		log.info(msg + " in " + (end-startTime) + "ms");
	}

} // end class
