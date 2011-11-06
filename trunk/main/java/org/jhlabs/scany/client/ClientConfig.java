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

import org.jhlabs.scany.engine.entity.Schema;

import java.util.Map;

/**
 * Scany의 전반적인 설정정보를 담고 있다.
 * 
 * @author gulendol
 *
 */
public abstract class ClientConfig {
	
	public static final String LOCAL_SERVICE_TYPE = "local";
	
	public static final String REMOTE_SERVICE_TYPE = "remote";
	
	private String serviceName;
	
	private String serviceType;
	
	private boolean isSearchable;
	
	private boolean isIndexable;
	
	private String host;
	
	private int port;
	
	private int connectionTimeout;
	
	private String connectionTimeoutUnit;
	
	private String scanyCharacterSet;
	
	private String scanyHome;
	
	private String repositoryHome;
	
	private String schemasXml;
	
	private String characterSet;
	
	private Map schemas;

	/**
	 * 스키마 정보를 가져온다.
	 * @param schemaId 스키마 ID
	 * @return
	 */
	public Schema getSchema(String schemaId) {
		if(schemas == null)
			throw new ScanyClientException("등록된 스키마가 없습니다.");
		
		return (Schema)schemas.get(schemaId);
	}
	
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @return the isSearchable
	 */
	public boolean isSearchable() {
		return isSearchable;
	}

	/**
	 * @param isSearchable the isSearchable to set
	 */
	public void setSearchable(boolean isSearchable) {
		this.isSearchable = isSearchable;
	}

	/**
	 * @return the isIndexable
	 */
	public boolean isIndexable() {
		return isIndexable;
	}

	/**
	 * @param isIndexable the isIndexable to set
	 */
	public void setIndexable(boolean isIndexable) {
		this.isIndexable = isIndexable;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the connectionTimeout
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * @param connectionTimeout the connectionTimeout to set
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * @return the connectionTimeoutUnit
	 */
	public String getConnectionTimeoutUnit() {
		return connectionTimeoutUnit;
	}

	/**
	 * @param connectionTimeoutUnit the connectionTimeoutUnit to set
	 */
	public void setConnectionTimeoutUnit(String connectionTimeoutUnit) {
		this.connectionTimeoutUnit = connectionTimeoutUnit;
	}

	/**
	 * @return the scanyCharacterSet
	 */
	public String getScanyCharacterSet() {
		return scanyCharacterSet;
	}

	/**
	 * @param scanyCharacterSet the scanyCharacterSet to set
	 */
	public void setScanyCharacterSet(String scanyCharacterSet) {
		this.scanyCharacterSet = scanyCharacterSet;
	}

	/**
	 * @return the scanyHome
	 */
	public String getScanyHome() {
		return scanyHome;
	}

	/**
	 * @param scanyHome the scanyHome to set
	 */
	public void setScanyHome(String scanyHome) {
		this.scanyHome = scanyHome;
	}

	/**
	 * @return the repositoryHome
	 */
	public String getRepositoryHome() {
		return repositoryHome;
	}

	/**
	 * @param repositoryHome the repositoryHome to set
	 */
	public void setRepositoryHome(String repositoryHome) {
		this.repositoryHome = repositoryHome;
	}

	/**
	 * @return the schemasXml
	 */
	public String getSchemasXml() {
		return schemasXml;
	}

	/**
	 * @param schemasXml the schemasXml to set
	 */
	public void setSchemasXml(String schemasXml) {
		this.schemasXml = schemasXml;
	}

	/**
	 * @return the characterSet
	 */
	public String getCharacterSet() {
		return characterSet;
	}

	/**
	 * @param characterSet the characterSet to set
	 */
	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}

	/**
	 * @return the schemas
	 */
	public Map getSchemas() {
		return schemas;
	}

	/**
	 * @param schemas the schemas to set
	 */
	public void setSchemas(Map schemas) {
		this.schemas = schemas;
	}

	protected boolean isRemoteServiceType() {
		return serviceType != null && serviceType.equals(REMOTE_SERVICE_TYPE);
	}
}
