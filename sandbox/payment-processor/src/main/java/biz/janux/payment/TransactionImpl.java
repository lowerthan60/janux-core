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

package biz.janux.payment;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.janux.bus.persistence.PersistentVersionableIdentifiableAbstract;
import org.janux.util.JanuxToStringStyle;

public abstract class TransactionImpl<T extends TransactionResponse>  extends PersistentVersionableIdentifiableAbstract  implements Transaction<T> {

	private BusinessUnit businessUnit;
	private boolean enabled;
	private T transactionResponse;
	private Date date;

	/**
	 * used by hibernate
	 */
	public TransactionImpl() {
		super();
	}
	
	public TransactionImpl(BusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}

	public BusinessUnit getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(BusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public T getTransactionResponse() {
		return transactionResponse;
	}

	public void setTransactionResponse(T transactionResponse) {
		this.transactionResponse = transactionResponse;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String toString() 
	{
		ToStringBuilder sb = new ToStringBuilder(this, JanuxToStringStyle.COMPACT);

		if (getId() != null)             sb.append("id",   getId());
		if (getUuid() != null)           sb.append("uuid", getUuid());
		if (getDate() != null)           sb.append("date", getDate());
		if (getBusinessUnit() != null)   sb.append("businessUnit",  getBusinessUnit());
		if (getTransactionResponse() != null) sb.append("transactionResponse", getTransactionResponse());
		if (getDateCreated() != null)    sb.append("created", getDateCreated());
		if (getDateUpdated() != null)    sb.append("updated", getDateUpdated());
		sb.append("enabled", isEnabled());

		return sb.toString();
	}


	public boolean equals(Object other)
	{
		if ( (this == other) ) return true;
		if ( !(other instanceof TransactionImpl) ) return false;

		TransactionImpl castOther = (TransactionImpl)other; 

		return new org.apache.commons.lang.builder.EqualsBuilder()
			.append(this.getUuid(), castOther.getUuid())
			.isEquals();
	}

	public int hashCode() 
	{
		return new org.apache.commons.lang.builder.HashCodeBuilder()
			.append(this.getUuid())
			.toHashCode();
	}

}
