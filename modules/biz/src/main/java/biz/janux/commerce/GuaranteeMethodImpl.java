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

package biz.janux.commerce;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.janux.bus.persistence.PersistentAbstract;

public class GuaranteeMethodImpl extends PersistentAbstract implements GuaranteeMethod, Serializable, Cloneable
{
	private static final long serialVersionUID = -764599083970448607L;
	private String code;
	private String description;

	
	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**  Two guarantee methods are equal if all the codes are equal */
	public boolean equals(final Object aObject)
	{
		if (this == aObject) 
		{
			return true;
		}
		
		if (!(aObject instanceof GuaranteeMethod)) 
		{
			return false;
		}
		
		final GuaranteeMethod thatMethod = (GuaranteeMethod)aObject; 

		return new EqualsBuilder()
			.append(this.getCode(), thatMethod.getCode())
			.isEquals();
	}

	public int hashCode() 
	{
		return new HashCodeBuilder()
		.append(this.getCode())
		.toHashCode();
	}
	
	public Object clone() 
	{
	    try 
	    {
	        GuaranteeMethodImpl result = (GuaranteeMethodImpl )super.clone();
	        
	        result.setId(-1);
	        
	        return result;
	    } 
	    catch (CloneNotSupportedException e) 
	    {
	        return null;
	    }
	}
	
}
