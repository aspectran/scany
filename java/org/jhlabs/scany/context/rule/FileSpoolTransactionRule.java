package org.jhlabs.scany.context.rule;

public class FileSpoolTransactionRule {

	private String host;

	private int timeout;
	
	private String userId;
	
	private String password;
	
	private String passwordEncryption;
	
	private String directory;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordEncryption() {
		return passwordEncryption;
	}

	public void setPasswordEncryption(String passwordEncryption) {
		this.passwordEncryption = passwordEncryption;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
}
