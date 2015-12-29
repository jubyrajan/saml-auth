<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	String samlSpUrl = "http://localhost:8080";
	boolean valid = true;
	String errorMessage = "";
	Map<String, String> userMap = new HashMap<String,String>();
	userMap.put("user1", "pass1");
	userMap.put("user2", "pass2");
	userMap.put("user3", "pass3");
	String userName = request.getParameter("username");
	String password = request.getParameter("password");
	if(!userMap.containsKey(userName)) {
		valid = false;
		errorMessage = "Invalid user name";
	} else if(!password.equals(userMap.get(userName))) {
		valid = false;
		errorMessage = "Invalid credentials";
	}
	HttpServletResponse servletResponse = (HttpServletResponse)response;
	if(!valid) {
		servletResponse.getWriter().print(errorMessage);
	} else {
		String xml = "<?xml version=\"1.0\"?><samlp:Response xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ID=\"_162f441d28cff78e3bb1d3c2bf3e48b5ed532605fd\" InResponseTo=\"_ae0216740b5baa4b13c79ffdb2baa82572788fd9a3\" Version=\"2.0\" IssueInstant=\"2008-05-27T07:49:23Z\" Destination=\"https://foodle.feide.no/simplesaml/saml2/sp/AssertionConsumerService.php\"><saml:Issuer xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\">https://openidp.feide.no</saml:Issuer><samlp:Status xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\"><samlp:StatusCode xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" Value=\"urn:oasis:names:tc:SAML:2.0:status:Success\"/></samlp:Status><saml:Assertion Version=\"2.0\" ID=\"pfxb27555d8-8c06-a339-c7ae-f544b2fd1507\" IssueInstant=\"2008-05-27T07:49:23Z\"><saml:Issuer>https://openidp.feide.no</saml:Issuer><ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><ds:SignedInfo><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/><ds:SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/><ds:Reference URI=\"#pfxb27555d8-8c06-a339-c7ae-f544b2fd1507\"><ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/><ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/></ds:Transforms><ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><ds:DigestValue>WUaqPW4nZ8uPyv+sf8qXsaKhHmk=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>CRq1VvptjNHenZ5aWkyD6GqQX+XLgNiqElJnyLbMUgiwrFZ5J8IEGtC8h2YiwID15ScxVt6tjQc8R3gXkP967PIlemmhYQ4US7V3oPczu4MECamj+07wAg7BCp05UVU3RI3pvi/2dQGRRX4tlXgkzUMzx8+cBeyZaI/BXKjhKEY=</ds:SignatureValue><ds:KeyInfo><ds:X509Data><ds:X509Certificate>MIICizCCAfQCCQCY8tKaMc0BMjANBgkqhkiG9w0BAQUFADCBiTELMAkGA1UEBhMCTk8xEjAQBgNVBAgTCVRyb25kaGVpbTEQMA4GA1UEChMHVU5JTkVUVDEOMAwGA1UECxMFRmVpZGUxGTAXBgNVBAMTEG9wZW5pZHAuZmVpZGUubm8xKTAnBgkqhkiG9w0BCQEWGmFuZHJlYXMuc29sYmVyZ0B1bmluZXR0Lm5vMB4XDTA4MDUwODA5MjI0OFoXDTM1MDkyMzA5MjI0OFowgYkxCzAJBgNVBAYTAk5PMRIwEAYDVQQIEwlUcm9uZGhlaW0xEDAOBgNVBAoTB1VOSU5FVFQxDjAMBgNVBAsTBUZlaWRlMRkwFwYDVQQDExBvcGVuaWRwLmZlaWRlLm5vMSkwJwYJKoZIhvcNAQkBFhphbmRyZWFzLnNvbGJlcmdAdW5pbmV0dC5ubzCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAt8jLoqI1VTlxAZ2axiDIThWcAOXdu8KkVUWaN/SooO9O0QQ7KRUjSGKN9JK65AFRDXQkWPAu4HlnO4noYlFSLnYyDxI66LCr71x4lgFJjqLeAvB/GqBqFfIZ3YK/NrhnUqFwZu63nLrZjcUZxNaPjOOSRSDaXpv1kb5k3jOiSGECAwEAATANBgkqhkiG9w0BAQUFAAOBgQBQYj4cAafWaYfjBU2zi1ElwStIaJ5nyp/s/8B8SAPK2T79McMyccP3wSW13LHkmM1jwKe3ACFXBvqGQN0IbcH49hu0FKhYFM/GPDJcIHFBsiyMBXChpye9vBaTNEBCtU3KjjyG0hRT2mAQ9h+bkPmOvlEo/aH0xR68Z9hw4PF13w==</ds:X509Certificate></ds:X509Data></ds:KeyInfo></ds:Signature><saml:Subject><saml:NameID Format=\"urn:oasis:names:tc:SAML:2.0:nameid-format:transient\" SPNameQualifier=\"urn:mace:feide.no:services:no.feide.foodle\">"+userName+"</saml:NameID><saml:SubjectConfirmation Method=\"urn:oasis:names:tc:SAML:2.0:cm:bearer\"><saml:SubjectConfirmationData NotOnOrAfter=\"2008-05-27T07:54:23Z\" InResponseTo=\"_ae0216740b5baa4b13c79ffdb2baa82572788fd9a3\" Recipient=\"https://foodle.feide.no/simplesaml/saml2/sp/AssertionConsumerService.php\"/></saml:SubjectConfirmation></saml:Subject></saml:Assertion></samlp:Response>";
		String xmlStr = org.apache.commons.codec.binary.Base64.encodeBase64String(xml.getBytes());
		servletResponse.sendRedirect(samlSpUrl + "/saml-sp/saml/sso-auth?SAMLResponse=" + java.net.URLEncoder.encode(xmlStr, "ISO-8859-1"));
		//servletResponse.sendRedirect("http://localhost:8889/agweb/pages/saml-sso-response?user=" + userName + "&SAMLResponse=" + java.net.URLEncoder.encode(xmlStr, "ISO-8859-1"));
	}
%>