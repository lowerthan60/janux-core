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


import java.util.ArrayList;
import java.util.List;

import org.janux.bus.search.SearchCriteria;

import biz.janux.payment.CreditCardType;
import biz.janux.payment.CreditCardTypeEnum;
import biz.janux.payment.CreditCardTypeImpl;
import biz.janux.payment.CreditCardTypeService;

public class CreditCardTypeServiceMockImpl  
implements CreditCardTypeService<SearchCriteria>  {

	public CreditCardType findFirstByCriteria(SearchCriteria arg0) {
		CreditCardType creditCardType;
		creditCardType = new CreditCardTypeImpl();
		creditCardType.setCode(CreditCardTypeEnum.VISA.getCode());
		creditCardType.setDescription(CreditCardTypeEnum.VISA.name());
		return creditCardType;
	}

	public List<CreditCardType> findAll() {
		CreditCardType creditCardType;
		ArrayList<CreditCardType> list = new ArrayList<CreditCardType>();
		
		creditCardType = new CreditCardTypeImpl();
		creditCardType.setCode(CreditCardTypeEnum.VISA.getCode());
		creditCardType.setDescription(CreditCardTypeEnum.VISA.name());
		list.add(creditCardType);
		
		creditCardType = new CreditCardTypeImpl();
		creditCardType.setCode(CreditCardTypeEnum.AMERICAN_EXPRESS.getCode());
		creditCardType.setDescription(CreditCardTypeEnum.AMERICAN_EXPRESS.name());
		list.add(creditCardType);
		
		creditCardType = new CreditCardTypeImpl();
		creditCardType.setCode(CreditCardTypeEnum.DISCOVER.getCode());
		creditCardType.setDescription(CreditCardTypeEnum.DISCOVER.name());
		list.add(creditCardType);
		
		creditCardType = new CreditCardTypeImpl();
		creditCardType.setCode(CreditCardTypeEnum.MASTERCARD.getCode());
		creditCardType.setDescription(CreditCardTypeEnum.MASTERCARD.name());
		list.add(creditCardType);
		
		return list;
	}

	public CreditCardType findByCode(String code) {
		CreditCardType creditCardType;
		creditCardType = new CreditCardTypeImpl();
		
		if (CreditCardTypeEnum.VISA.code.equalsIgnoreCase(code))
		{	
			creditCardType.setCode(CreditCardTypeEnum.VISA.getCode());
			creditCardType.setDescription(CreditCardTypeEnum.VISA.name());
		}
		
		if (CreditCardTypeEnum.AMERICAN_EXPRESS.code.equalsIgnoreCase(code))
		{
			creditCardType.setCode(CreditCardTypeEnum.AMERICAN_EXPRESS.getCode());
			creditCardType.setDescription(CreditCardTypeEnum.AMERICAN_EXPRESS.name());
		}
				
		if (CreditCardTypeEnum.DISCOVER.code.equalsIgnoreCase(code))
		{
			creditCardType.setCode(CreditCardTypeEnum.DISCOVER.getCode());
			creditCardType.setDescription(CreditCardTypeEnum.DISCOVER.name());
		}
		
		if (CreditCardTypeEnum.MASTERCARD.code.equalsIgnoreCase(code))
		{
			creditCardType.setCode(CreditCardTypeEnum.MASTERCARD.getCode());
			creditCardType.setDescription(CreditCardTypeEnum.MASTERCARD.name());
		}
		
		return creditCardType;
	}

	

}
