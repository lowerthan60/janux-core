/* Copyright 2009 The Revere Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.trg.dao.hibernate.dao;

import java.io.Serializable;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.trg.dao.hibernate.GenericDAOImpl;

/**
 * Extension of GenericDAOImpl that is configured for Autowiring with Spring or J2EE.
 */
@Repository
public class BaseDAOImpl<T, ID extends Serializable> extends GenericDAOImpl<T, ID> {

	@Override
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

}
