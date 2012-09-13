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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.management.RuntimeErrorException;
import javax.management.RuntimeOperationsException;

import org.janux.bus.search.SearchCriteria;
import org.jasypt.digest.StringDigester;
import org.jasypt.util.digest.Digester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.janux.commerce.CreditCardMask;

import com.trg.search.Filter;
import com.trg.search.Search;

/**
 * 
 * @author albertobuffagni@gmail.com
 * 
 */
public class CreditCardStorageServiceImpl implements CreditCardStorageService<SearchCriteria> {

	private CreditCardMask creditCardMask;
	private CreditCardDao<CreditCard, Integer, SearchCriteria> creditCardDao;
	private EncryptedStorageService<String, String> encryptedStorageService;
	private BusinessUnitService<SearchCriteria> businessUnitService;
	private StringDigester encryptorOneWay;
	private CreditCardValidator creditCardValidator;
	private AuthorizationHotelService authorizationService;

	public Logger log = LoggerFactory.getLogger(this.getClass());

	public CreditCard decrypt(CreditCard creditCard) {
		log.debug("Attempting to decrypt credit card number for '{}' ...", creditCard.getUuid());

		// the credit card number has to restored.
		String token = creditCard.getToken();
		String sensitiveData = getEncryptedStorageService().decryption(token);
		String number; 
		String track2=null;
			
		if (creditCard.isSwiped() && sensitiveData.startsWith(";"))
		{
			track2 = sensitiveData;
			number = getCreditCardNumber(track2);
		}
		else
			number = sensitiveData; 
		
		creditCard.setTrack2(track2);
		creditCard.setNumber(number);
		creditCard.setNumberMasked(maskNumber(number));

		log.info("Successfully decrypted credit card number for '{}': '{}'", creditCard.getUuid(), creditCard.getNumberMasked());
		return creditCard;
	}

	public boolean deleteOrDisable(String creditCardUuid) {
		log.debug("Attempting to delete credit card with uuid: '{}' ...", creditCardUuid);

		CreditCard creditCard = load(creditCardUuid, false);
		return deleteOrDisable(creditCard);
	}

	public boolean deleteOrDisable(CreditCard creditCard) 
	{
		log.debug("Attempting to delete credit card: {} ...", creditCard);
		if (creditCard != null) {
			String uuid = creditCard.getUuid();
			if (canBeDeleted(uuid)) {
				getCreditCardDao().delete(creditCard);
				log.info("Successfully deleted credit card: {}", creditCard);
				return true;
			} else {
				creditCard.setEnabled(false);
				this.saveOrUpdate(creditCard);
				log.info("Unable to delete credit card, marking it disabled: {}", creditCard);
				return false;
			}
		}
		throw new RuntimeException("EntityNotFound");
	}

	private boolean canBeDeleted(String uuid) 
	{
		log.trace("checking if credit card can be purged - uuid={} ...", uuid);

		if (getAuthorizationService()!=null)
		{
			if (uuid==null || uuid.equalsIgnoreCase(""))
				throw new RuntimeException("credit card with uuid null or empty");
			
			Search searchCriteria = new Search();
			searchCriteria.addFilterEqual("creditCard.uuid", uuid);
			searchCriteria.setMaxResults(1);
			List<AuthorizationHotel> list = getAuthorizationService().findAll(searchCriteria);
			 
			if (!list.isEmpty())
			{
				log.debug("The credit card has authorizations, can not be deleted");
				return false;
			}
		}
		

		log.debug("The credit card with uuid '{}' has no authorizations, and can be deleted", uuid);
		return true;
	}

	public CreditCard encrypt(CreditCard creditCard) {
		return saveOrUpdate(creditCard);
	}

	public List<CreditCard> findByCriteria(SearchCriteria searchCriteria) {
		return findByCriteria(searchCriteria, false);
	}

	public List<CreditCard> findByCriteria(SearchCriteria searchCriteria, boolean decrypt) {

		String propertyName = "enabled";
		boolean containsFilter = containsFilter(searchCriteria, propertyName);
		if (!containsFilter)
			((Search) searchCriteria).addFilterEqual("enabled", true);

		((Search) searchCriteria).setMaxResults(1);

		log.debug("Finding by:{}", searchCriteria);
		List<CreditCard> list = getCreditCardDao().findByCriteria(searchCriteria);

		if (decrypt) {
			for (Iterator<CreditCard> iterator = list.iterator(); iterator.hasNext();) {
				CreditCard creditCard = (CreditCard) iterator.next();
				decrypt(creditCard);
			}
		}

		log.trace("Found {} Credit Card records", list.size());
		return list;

	}

	/**
	 * It is True if the propertyName was included in the searchCriteria
	 * 
	 * @param searchCriteria
	 * @param propertyName
	 * @return
	 */
	private boolean containsFilter(SearchCriteria searchCriteria, String propertyName) {
		Search search = (Search) searchCriteria;
		List<Filter> filters = search.getFilters();
		boolean containsFilter = false;
		for (Iterator<Filter> iterator = filters.iterator(); iterator.hasNext();) {
			Filter filter = iterator.next();
			if (filter.getProperty().equalsIgnoreCase(propertyName)) {
				containsFilter = true;
				break;
			}
		}
		return containsFilter;
	}

	public CreditCard findFirstByCriteria(SearchCriteria searchCriteria) {
		return findFirstByCriteria(searchCriteria, false);
	}

	public CreditCard findFirstByCriteria(SearchCriteria searchCriteria, boolean decrypt) {
		((Search) searchCriteria).setMaxResults(1);

		List<CreditCard> list = findByCriteria(searchCriteria, decrypt);

		if (!list.isEmpty()) {
			CreditCard creditCard = list.get(0);
			return creditCard;
		}

		return null;
	}

	public CreditCard load(String creditCardUuid, boolean decrypt) {

		String action = decrypt ? "decrypt" : "load";
		log.debug("Attempting to {} cc with uuid: '{}'", action, creditCardUuid);

		if (creditCardUuid==null || creditCardUuid.equalsIgnoreCase(""))
			throw new RuntimeException("credit card with uuid null or empty");
		
		Search searchCriteria = new Search(CreditCardImpl.class);
		searchCriteria.addFilter(Filter.equal("uuid", creditCardUuid));
		CreditCard creditCard = findFirstByCriteria(searchCriteria, decrypt);

		if (creditCard != null && decrypt) {
			decrypt(creditCard);
		}

		log.info("Successfully {}ed cc: {}", new Object[] { action, creditCardUuid, creditCard });
		return creditCard;
	}

	public boolean purge(CreditCard creditCard) {
		throw new UnsupportedOperationException();
	}

	public boolean purge(String uuid) {
		throw new UnsupportedOperationException();
	}

	
	public CreditCard saveOrUpdate(CreditCard creditCard) {
		if (((CreditCardImpl)creditCard).getBusinessUnit()!=null)
			return saveOrUpdate(creditCard,((CreditCardImpl)creditCard).getBusinessUnit());
		else
			return saveOrUpdate(creditCard,getBusinessUnitService().BUSINESS_UNIT_CODE_BY_DEFAULT,true,null);
	}
	
	public CreditCard saveOrUpdate(CreditCard creditCard, BusinessUnit businessUnit) {
		return saveOrUpdate(creditCard,businessUnit.getCode(),false,null);
	}


	public CreditCard saveOrUpdate(CreditCard creditCard, BusinessUnit businessUnit, boolean autoCreateBusinessUnit) {
		return saveOrUpdate(creditCard,businessUnit.getCode(),autoCreateBusinessUnit,null);
	}
	
	public CreditCard saveOrUpdate(CreditCard creditCard, String businessUnitCode) {
		return saveOrUpdate(creditCard,businessUnitCode,false,null);
	}
	

	public CreditCard saveOrUpdate(CreditCard creditCardToSave, String businessUnitCode, boolean autoCreateBusinessUnit,IndustryType industryType) 
	{
		log.debug("Attempting to save credit card...: {}", creditCardToSave);
		
		// validate the credit card to be stored
		if (this.getCreditCardValidator() instanceof CreditCardValidator)
		{
			final Map<String, String> errors = this.getCreditCardValidator().validate(creditCardToSave);
			if (errors.size() > 0)
			{
				final CreditCardValidationException ex = new CreditCardValidationException();
				ex.setErrors(errors);
				throw ex;
			}
			
			if ( creditCardToSave.isSwiped() && !this.getCreditCardValidator().isValidTrack2(creditCardToSave.getTrack2(), creditCardToSave.getNumber()))
			{
				creditCardToSave.setTrack2(null);
				creditCardToSave.setSwiped(false);
			}
		}
			
		
		String token = null;
		String creditCardNumber = null;

		BusinessUnit businessUnit = getBusinessUnitService().resolveBusinessUnit(businessUnitCode,autoCreateBusinessUnit,industryType);
		CreditCard creditCardExisting = null;
		CreditCard creditCard = null;
		
		// if the credit card has not been persisted yet
		if (((CreditCardImpl) creditCardToSave).getId() == ((CreditCardImpl) creditCardToSave).UNSAVED_ID) 
		{

			String cardNumberHash = hashNumber(creditCardToSave.getNumber());

			if (businessUnit==null)
				throw new RuntimeException("businessUnit is null");
			
			if (cardNumberHash==null || cardNumberHash.equals(""))
				throw new RuntimeException("numberHash is null or empty");
			
			Search searchCriteria = new Search();
			searchCriteria.addFilterEqual("businessUnit", businessUnit);
			searchCriteria.addFilterEqual("numberHash",   cardNumberHash);
			creditCardExisting = findFirstByCriteria(searchCriteria);
			
			if (creditCardExisting==null)
			{
				if (getEncryptedStorageService() == null) 
					throw new IllegalStateException("EncryptedStorageService has not been set properly");
				
				if (creditCardToSave.isSwiped())
					if (creditCardToSave.getTrack2()==null || !creditCardToSave.getTrack2().startsWith(";"))
						throw new RuntimeException("the cc was swiped but the track2 is invalid");
					else
						token = getEncryptedStorageService().encryption(creditCardToSave.getTrack2());
				else
					token = getEncryptedStorageService().encryption(creditCardToSave.getNumber());
				
				creditCardToSave.setToken(token);
				log.trace("Token created: {}", token);
	
				creditCardToSave.setNumberMasked(maskNumber(creditCardToSave.getNumber()));
				((CreditCardImpl)creditCardToSave).setNumberHash(cardNumberHash);
				creditCardToSave.setNumber(null);
				creditCardToSave.setTrack2(null);
				creditCardToSave.setEnabled(true);
				
				creditCard = creditCardToSave;
				log.trace("CC masked: {}", creditCardToSave.getNumberMasked());
			}
		}
		else
		{
			Search searchCriteria = new Search();
			searchCriteria.addFilterEqual("id", ((CreditCardImpl) creditCardToSave).getId());
			creditCardExisting = findFirstByCriteria(searchCriteria);
		}
		
		
		if (creditCardExisting!=null) 
		{
			log.warn("Attempting to update previously stored Credit Card number");

			creditCardExisting.setExpirationDate(creditCardToSave.getExpirationDate());
			
			if (creditCardToSave.isSwiped() && creditCardToSave.getTrack2()!=null && creditCardToSave.getTrack2().startsWith(";"))
			{
				creditCardExisting.setSwiped(creditCardToSave.isSwiped());
				creditCardExisting.setTrack2(creditCardToSave.getTrack2());
				token = getEncryptedStorageService().encryption(creditCardExisting.getTrack2());
				creditCardExisting.setToken(token);
				log.trace("Token created: {}", token);
			}
			else if (!creditCardToSave.isSwiped() && creditCardToSave.getNumber()!=null && !creditCardToSave.getNumber().isEmpty())
			{
				creditCardExisting.setSwiped(creditCardToSave.isSwiped());
				token = getEncryptedStorageService().encryption(creditCardToSave.getNumber());
				creditCardExisting.setToken(token);
				log.trace("Token created: {}", token);
			}

			creditCardExisting.setNumber(null);
			creditCardExisting.setTrack2(null);
			log.trace("CC masked: {}", creditCardExisting.getNumberMasked());

			creditCardExisting.setBillingAddress(creditCardToSave.getBillingAddress());
			creditCardExisting.setCardholderName(creditCardToSave.getCardholderName());
			creditCardExisting.setSecurityCode(creditCardToSave.getSecurityCode());
			creditCardExisting.setType(creditCardToSave.getType());
			creditCard = creditCardExisting;
		}
		
		((CreditCardImpl)creditCard).setBusinessUnit(businessUnit);
		creditCard = (CreditCard) getCreditCardDao().saveOrUpdate((CreditCardImpl) creditCard);
		log.info("Successfully saved credit card '{}'", creditCard);
		return creditCard;
	}

	private String hashNumber(String string) {
		return getEncryptorOneWay().digest(string).toString();
	}

	public String maskNumber(String num) {
		if (this.getCreditCardMask() == null) {
			throw new IllegalStateException("CreditCardMask has not been instantiated");
		}
		return this.getCreditCardMask().maskNumber(num);
	}

	public CreditCard decrypt(String creditCardUuid) {
		CreditCard creditCard = load(creditCardUuid, true);
		return creditCard;
	}
	
	private String getCreditCardNumber(String track2) {
		StringTokenizer stringTokenizer = new StringTokenizer(track2,";=");
		String number=null;
		if (stringTokenizer.hasMoreTokens())
		{
			number = stringTokenizer.nextToken();
		}
		return number;
	}

	public CreditCardMask getCreditCardMask() {
		return creditCardMask;
	}

	public void setCreditCardMask(CreditCardMask creditCardMask) {
		this.creditCardMask = creditCardMask;
	}

	public void setCreditCardDao(CreditCardDao creditCardDao) {
		this.creditCardDao = creditCardDao;
	}

	public CreditCardDao getCreditCardDao() {
		return creditCardDao;
	}

	public void setEncryptedStorageService(EncryptedStorageService<String, String> encryptedStorageService) 
	{
		log.debug("Setting EncryptedStorageService to: {}", 
				encryptedStorageService == null ? "NULL" : "instance of " + encryptedStorageService.getClass() );
		this.encryptedStorageService = encryptedStorageService;
	}

	public EncryptedStorageService<String, String> getEncryptedStorageService() {
		return encryptedStorageService;
	}

	public void setBusinessUnitService(BusinessUnitService<SearchCriteria> businessUnitService) {
		this.businessUnitService = businessUnitService;
	}

	public BusinessUnitService<SearchCriteria> getBusinessUnitService() {
		return businessUnitService;
	}

	public void setEncryptorOneWay(StringDigester encryptorOneWay) {
		this.encryptorOneWay = encryptorOneWay;
	}

	public StringDigester getEncryptorOneWay() {
		return encryptorOneWay;
	}

	public CreditCardValidator getCreditCardValidator()
	{
		return creditCardValidator;
	}

	public void setCreditCardValidator(CreditCardValidator creditCardValidator)
	{
		this.creditCardValidator = creditCardValidator;
	}

	public boolean isValid(CreditCard aCreditCard)
	{
		return this.getCreditCardValidator().isValid(aCreditCard);
	}

	public Map<String, String> validate(CreditCard aCreditCard)
	{
		return this.getCreditCardValidator().validate(aCreditCard);
	}
	
	public boolean isValidTrack2(String track2, String number) {
		return this.getCreditCardValidator().isValidTrack2(track2, number);
	}
	
	public void setAuthorizationService(AuthorizationHotelService authorizationHotelService) {
		this.authorizationService = authorizationHotelService;
	}

	public AuthorizationHotelService getAuthorizationService() {
		return authorizationService;
	}

}
