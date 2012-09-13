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

package biz.janux.geography;

import java.io.Serializable;

import org.janux.bus.persistence.Persistent;

public interface City extends Serializable, Persistent
{
	String getCode();
	void setCode(String code);

	String getName();
	void setName(String name);

	StateProvince getState();
	void setState(StateProvince o);

	/** alias for getState **/
	StateProvince getProvince();
	void setProvince(StateProvince o);

	/** shortcut for getState().getCountry() */
	Country getCountry();
	void setCountry(Country o);
}
