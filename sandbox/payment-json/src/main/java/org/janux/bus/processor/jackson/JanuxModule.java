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

package org.janux.bus.processor.jackson;

import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.module.SimpleModule;

/**
 ***************************************************************************************************
 * This is a convenience class that extends org.codehaus.jackson.map.module.SimpleModule, with
 * a constructor that accepts a list of JanuxJsonSerializers, and a list of JanuxJsonDeserializers,
 * that can be used to configure the underlying SimpleModule.
 * 
 * @author  <a href="mailto:alberto.buffagni@janux.org">Alberto Buffagni</a>
 * @since 0.4.0 - 2011-11-17
 ***************************************************************************************************
 */
public class JanuxModule extends SimpleModule
{
	public JanuxModule(
			String name, Version version,
			List<JanuxJsonSerializer>   serializers,
			List<JanuxJsonDeserializer> deserializers) 
	{
		super(name, version);
		
		for (Iterator<JanuxJsonDeserializer> iterator = deserializers.iterator(); iterator.hasNext();) {
			JanuxJsonDeserializer jsonDeserializer = iterator.next();
			this.addDeserializer(jsonDeserializer.handledType(), jsonDeserializer);
		}
		
		for (Iterator<JanuxJsonSerializer> iterator = serializers.iterator(); iterator.hasNext();) {
			JanuxJsonSerializer jsonSerializer = iterator.next();
			this.addSerializer(jsonSerializer);
		}
	}
	
	public void addAbstractTypeMapping(List<List<Class>> mappings)
	{
		for (Iterator iterator = mappings.iterator(); iterator.hasNext();) {
			List<Class> list = (List<Class>) iterator.next();
			addAbstractTypeMapping(list.get(0), list.get(1));
		}
	}
	
}
