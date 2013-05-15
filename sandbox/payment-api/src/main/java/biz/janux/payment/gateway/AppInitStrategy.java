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

package biz.janux.payment.gateway;


/**
 ***************************************************************************************************
 * This interface was created to meet the need to encrypt system-level passwords that exist in
 * configuration files on the filesystem, without also storing on the filesystem the Password
 * Encryption Key (PEK) used to encrypt said passwords; instead, after the payment gateway is
 * started, and before it is operational, the PEK must be provided via a UI form, and is than used
 * to decrypt any user/passwords that may be contained in the configuration files, such as for example
 * the user/password needed to access an encrypted storage back-end; This interface defines a
 * preInit() method for actions to take in order to initialize the application (such as decripting
 * passwords), and an optional postInit() method for any actions that may have to be taken after the
 * application status changes to 'initialized'.
 * 
 * @author  <a href="mailto:philippe.paravicini@janux.org">Philippe Paravicini</a>
 * @since 0.4.0 - 2012-02-28
 ***************************************************************************************************
 */
public interface AppInitStrategy
{
	/** 
	 * The Password Encryption Key that is required to decrypt any encrypted passwords in the
	 * application's configuration 
	 */
  public String getPasswordEncryptionKey();
  public void setPasswordEncryptionKey(String o);

	/** Actions to take in order to initialization the application */
	public void preInit();

	/** Actions to take after the application status has been changed to initialized */
	public void postInit();
}


