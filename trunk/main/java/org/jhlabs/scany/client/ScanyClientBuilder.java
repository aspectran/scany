/*******************************************************************************
 * Copyright (c) 2008 Jeong Ju Ho.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Jeong Ju Ho - initial API and implementation
 ******************************************************************************/
package org.jhlabs.scany.client;

import org.jhlabs.scany.config.ScanyConfig;
import org.jhlabs.scany.util.MultipleXReader;

import java.io.InputStream;
import java.util.Map;

/**
 * Scany의 전반적인 설정정보를 담고 있다.
 * 
 * @author gulendol
 *
 */
public class ScanyClientBuilder {

	public static ScanyClient buildScanyClient(InputStream is, String serviceName) {
		try {
			ScanyConfig scanyConfig = new ScanyConfig();
			
			MultipleXReader.Record record = scanyConfig.loadClientConfig(is, serviceName);
			
			String serviceType = record.getAttributeValue("type");
			String scanyHome = record.getColumnValue("ScanyHome");
			String repositoryHome = record.getColumnValue("RopositoryHome");
			String schemasXml = record.getColumnValue("Schemas");
			String characterSet = record.getColumnValue("CharacterSet");

			if(serviceType == null ||
					(!serviceType.equals(ClientConfig.LOCAL_SERVICE_TYPE) &&
							!serviceType.equals(ClientConfig.REMOTE_SERVICE_TYPE)))
				throw new IllegalArgumentException("Scany Client Service Type을 알 수 없습니다.");

			if(serviceType.equals(ClientConfig.REMOTE_SERVICE_TYPE))
				throw new IllegalArgumentException("현재 버전에서는 Scany Client Remote Service를 지원하지 않습니다.");
			
			ScanyClientImpl scanyClient = new ScanyClientImpl();
			scanyClient.setServiceType(serviceType);
			scanyClient.setScanyHome(scanyHome);
			scanyClient.setRepositoryHome(repositoryHome);
			scanyClient.setSchemasXml(schemasXml);
			scanyClient.setCharacterSet(characterSet);
			
			// 스키마 정보 로딩
			Map schemas = scanyConfig.loadSchemas(schemasXml, repositoryHome);
			
			scanyClient.setSchemas(schemas);

			return scanyClient;

		} catch(Exception e) {
			throw new ScanyClientException("ScanyClient 생성에 실패했습니다.", e);
		}
	}
}