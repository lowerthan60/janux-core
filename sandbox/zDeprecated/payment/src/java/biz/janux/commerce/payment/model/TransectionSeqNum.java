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

package biz.janux.commerce.payment.model;

/**
 * 
 * @author Nilesh
 * </br>
 * This Model is mapped to trans_seq_number table
 *
 */
public class TransectionSeqNum {
	
	int merchentId;
	int transID;
	int transnum;
	/**
	 * 
	 * @return int
	 */
	public int getTransID() {
		return transID;
	}
	/**
	 * 
	 * @param transID
	 */
	public void setTransID(int transID) {
		this.transID = transID;
	}
	/**
	 * 
	 * @return int
	 */
	public int getTransnum() {
		return transnum;
	}
	/**
	 * 
	 * @param transnum
	 */
	public void setTransnum(int transnum) {
		this.transnum = transnum;
	}
	/**
	 * 
	 * @return int
	 */
	public int getMerchentId() {
		return merchentId;
	}
	/**
	 * 
	 * @param merchentId
	 */
	public void setMerchentId(int merchentId) {
		this.merchentId = merchentId;
	}
}
