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

import java.io.Serializable;
import java.util.Date;


/**
 ***************************************************************************************************
 * Represents the credit card company (Visa, Mastercard, American Express, etc...; should be a java
 * Enumeration, but we want to be able to store/edit the list outside the java code, so we created a
 * simple interface to represent this class
 *
 * @author <a href="mailto:philippe.paravicini@janux.org">philippe.paravicini@janux.org</a>
 * @since 0.4
 ***************************************************************************************************
 */
public interface CreditCardType extends Serializable
{
	/**       
	 * A unique code identifying the credit card company
	 */
	public String getCode();
	public void setCode(String code);

	/**       
	 * Name/Description of the Credit Card Type
	 */
	public String getDescription();
	public void setDescription(String description);

	/**       
	 * An optional sort order for display purposes
	 */
	public int getSortOrder();
	public void setSortOrder(int i);

} // end interface CreditCardType
