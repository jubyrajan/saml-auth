package org.jr.samlauth.sp;

/**
 * Configuration
 * @author Juby Rajan
 *
 */
public enum Config {
	INSTANCE;
	
	public String getSamlIdpUrl() {
		return "http://localhost:8080/saml-idp";
	}
	
	public boolean needCertificateVerification() {
		return false;
	}
	
	public String getSamlCertificateFilePath() {
		return "path/to/X.509CertificateFile";
	}
}
