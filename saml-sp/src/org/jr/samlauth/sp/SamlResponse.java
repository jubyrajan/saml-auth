package org.jr.samlauth.sp;

/**
 * DTO class to hold relevant SAML response valuess 
 * @author Juby Rajan
 */
public class SamlResponse {
	private String userName;
	private String sessionIndex;
	
	public SamlResponse(String userName, String sessionIndex) {
		super();
		this.userName = userName;
		this.sessionIndex = sessionIndex;
	}

	public String getUserName() {
		return userName;
	}

	public String getSessionIndex() {
		return sessionIndex;
	}
	
}
