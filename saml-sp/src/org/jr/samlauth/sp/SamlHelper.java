package org.jr.samlauth.sp;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.AssertionImpl;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.X509Data;
import org.opensaml.xml.validation.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Process SAML IDP assertion response and retrieve user name and session index. 
 * @author Juby Rajan
 */
public class SamlHelper {
	public static final String USER_NAME_PARAM = "USER_NAME";
	public static final String SAML_SESSION_INDEX_PARAM = "SAML_SESSION_INDEX";
	private static final String SAML_AUTHENTICATION_FAILED = "SAML Authentication failed";
	private final static Logger LOGGER = Logger.getLogger(SamlHelper.class.getName());
	
	/**
	 * Process SAML IDP assertion response and retrieve user name and session index
	 * @param samlResponse SAML IDP assertion response
	 * @return user name and session index
	 */
	public static SamlResponse processSamlIdpResponse(String samlResponse) {
        LOGGER.info("SAML response: " + samlResponse);
        if(Utils.hasText(samlResponse)) {
            ByteArrayInputStream bis = null;
            try{
                DefaultBootstrap.bootstrap();
                byte[] raw = Base64.decodeBase64(samlResponse.getBytes());
                if (raw != null) {
                    bis = new ByteArrayInputStream(raw);
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    documentBuilderFactory.setNamespaceAware(true);
                    DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document document = docBuilder.parse(bis);
                    Element element = document.getDocumentElement();
                    UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
                    Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
                    Response samlResp = (Response)unmarshaller.unmarshall(element);
                    if(samlResp.getAssertions() == null || samlResp.getAssertions().size() == 0) {
                        throw new RuntimeException("Invalid SAML response. No assertions found.");
                    }
                    AssertionImpl assertion = (AssertionImpl)samlResp.getAssertions().get(0);
                    String statusCode = samlResp.getStatus().getStatusCode().getValue();
                    LOGGER.info("Status code:" + statusCode);
                    if ("urn:oasis:names:tc:SAML:2.0:status:Success".equals(statusCode)){
                    	LOGGER.info("Received success status code");
                        boolean isCertificateValid = true;
                        if(Config.INSTANCE.needCertificateVerification()){
                            isCertificateValid = verifyCertificate(samlResp);
                        }
                        if(isCertificateValid){
                            String userName = assertion.getSubject().getNameID().getValue();
                            String sessionIndex = "";
                            if(assertion.getAuthnStatements() != null && assertion.getAuthnStatements().size() > 0) {
                                sessionIndex = assertion.getAuthnStatements().get(0).getSessionIndex();
                            }
                            return new SamlResponse(userName, sessionIndex);
                        }else {
                            throw new RuntimeException("Certificates are not valid");
                        }
                    }else {
                        throw new RuntimeException("Status is not valid");
                    }
                  }
            }catch(Exception e){
            	Utils.consoleLog(SAML_AUTHENTICATION_FAILED, e);
                throw new RuntimeException(SAML_AUTHENTICATION_FAILED, e);
            }finally {
              try {
                  if (bis != null) {
                      bis.close();
                  }
              }catch (Exception e) {
            	  Utils.consoleLog(e.getMessage(),e);
              }
           }
        }
        return null;
    }

	/**
	 * Verify X.509 certificate in SAML assertion.
	 * @param samlResp SAML response from IDP.
	 * @return true if certifiate is valid and matches the local certificate, false otherwise
	 */
    private static boolean verifyCertificate(Response samlResp) {
        boolean isValid = false;
        ByteArrayInputStream bais = null;
        try {
            AssertionImpl assertion = (AssertionImpl)samlResp.getAssertions().get(0); 
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Signature signature = samlResp.getSignature() != null ? samlResp.getSignature() : assertion.getSignature();
            if(signature == null) {
                throw new RuntimeException("X.509 certificate not found in SAML response or assertion.");
            }
            SAMLSignatureProfileValidator validator = new SAMLSignatureProfileValidator();
            validator.validate(signature);
            String certificate = "-----BEGIN CERTIFICATE-----\n" + ((org.opensaml.xml.signature.X509Certificate)((X509Data)signature.getKeyInfo().getX509Datas().get(0)).getX509Certificates().get(0)).getValue() + "\n-----END CERTIFICATE-----";
            bais = new ByteArrayInputStream(certificate.getBytes());
            java.security.cert.X509Certificate saml2Certificate = (java.security.cert.X509Certificate)cf.generateCertificate(bais);
            saml2Certificate.checkValidity();
            java.security.cert.X509Certificate cert = getLocalCertificate(cf);
            cert.checkValidity();
            if (cert.equals(saml2Certificate)) {
                isValid = true;
            }
        } catch (CertificateException e) {
        	Utils.consoleLog(e.getMessage(), e);
        } catch (ValidationException e) {
        	Utils.consoleLog(e.getMessage(), e);
        } finally {
          if (bais != null) {
            try {
                bais.close();
            } catch (IOException e) {
            	Utils.consoleLog("Error closing stream.", e);
            }
          }
       }
        return isValid;
      }

      /**
       * Load and returns the local X.509 certificate
       * @param cf Java certificate factory
       * @return local X.509 certificate
       */
      private static java.security.cert.X509Certificate getLocalCertificate(CertificateFactory cf){
          FileInputStream in = null;
          java.security.cert.X509Certificate certificate = null;
          try {
              String certPath = Config.INSTANCE.getSamlCertificateFilePath();
              in = new FileInputStream(certPath);
              certificate = (java.security.cert.X509Certificate)cf.generateCertificate(in);
          } catch (Exception e) {
        	  Utils.consoleLog(e.getMessage(),e);
          } finally {
              try {
                if (in != null) {
                  in.close();
                }
              } catch (Exception e) {
            	  Utils.consoleLog(e.getMessage(),e);
              }
          }
          return certificate;
      }
}
