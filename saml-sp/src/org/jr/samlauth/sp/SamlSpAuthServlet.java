package org.jr.samlauth.sp;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;

/**
 * Servlet implementation class SamlSpAuthServlet for SAML service provider (SP).
 * Redirects initial request to SAML identity provider (IDP).
 * Accepts SAML assertion from IDP and creates user session on valid SAML SSO authentication.
 * 
 * @author Juby Rajan
 */
@WebServlet("/saml/*")
public class SamlSpAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UTF_8 = "UTF-8";
	private static final String SAML_AUTHN_REQUEST = "<samlp:AuthnRequest xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\" ID=\"AglSamlRequest\" Version=\"2.0\" AssertionConsumerServiceIndex=\"0\" AttributeConsumingServiceIndex=\"0\"><saml:Issuer>SAML-REQUEST-ISSUER</saml:Issuer><samlp:NameIDPolicy AllowCreate=\"true\" Format=\"urn:oasis:names:tc:SAML:2.0:nameid-format:transient\"/></samlp:AuthnRequest>";
	private final static Logger LOGGER = Logger.getLogger(SamlSpAuthServlet.class.getName());
	
    /**
     * Default constructor. 
     */
    public SamlSpAuthServlet() {
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// to keep things simple handle both GET and POST in doPost
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		if(uri.endsWith("/saml/sso-login")) {
            handleSamlLoginRequest(request, response);
		} else if(uri.endsWith("/saml/sso-auth")) {
			handleSuccessfulSamlAuthentication(request, response);
        } else if(uri.endsWith("/saml/logout")) {
			doLogout(request, response);
        }
	}
	
	private HttpSession getSession(HttpServletRequest request, boolean createNew) {
		return request.getSession(createNew);
	}
	
	/**
	 * Redirects to configured IDP url if user session doesn't exist.
	 * If user session exists, redirects to main page.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws IOException thrown if url redirect fails
	 */
	private void handleSamlLoginRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = getSession(request, false);
        if(session == null || session.getAttribute(SamlHelper.USER_NAME_PARAM) == null) {
            redirectToConfiguredIdpUrl(request, response);
        } else {
        	response.sendRedirect(request.getContextPath() + "/main.jsp");
        }
    }
	
	/**
	 * Handles callback from IDP on successful authentication.
	 * Creates new user session if required.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws IOException thrown if url redirect fails
	 */
	private void handleSuccessfulSamlAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SamlResponse samlResponse = SamlHelper.processSamlIdpResponse(request.getParameter("SAMLResponse"));
		
		String userName = samlResponse.getUserName();
		if (!Utils.hasText(userName)) {
			throw new RuntimeException("Invalid response from SAML IDP");
		}
		LOGGER.info("USER_NAME: " + userName);
		HttpSession session = getSession(request, false);
		if (session == null) {
			session = getSession(request, true);
			setSessionAttributes(session, userName, samlResponse.getSessionIndex());
		}
		response.sendRedirect(request.getContextPath() + "/main.jsp");
	}
	
	/**
	 * Redirects to configured IDP url.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws IOException thrown if url redirect fails
	 */
	private void redirectToConfiguredIdpUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String xmlStr = Base64.encodeBase64String(SAML_AUTHN_REQUEST.getBytes());
        String reqString = Config.INSTANCE.getSamlIdpUrl();
        if(reqString.indexOf('?') == -1) {
            reqString += "?";
        } else {
            reqString += "&";
        }
        reqString += "SAMLRequest=" + URLEncoder.encode(xmlStr, UTF_8);
        response.sendRedirect(reqString);
    }

	/**
	 * Logout by invalidating user session.
	 * 
	 * @param request servlet request
	 * @param response servlet response
	 */
	private void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = getSession(request, false);
		if(session != null) {
			try {
				setSessionAttributes(session, null, null);
				session.invalidate();
			} catch(IllegalStateException e) {
				LOGGER.severe(Utils.throwableTostackTraceString(e));
			}
		}
		response.sendRedirect(request.getContextPath());
	}
	
	/**
	 * Sets USER_NAME & SAML_SESSION_INDEX attribute values to session
	 * @param session The HttpSession object
	 * @param userName Value for USER_NAME attribute
	 * @param samlSessionIndex Value for SAML_SESSION_INDEX attribute
	 */
	private void setSessionAttributes(HttpSession session, String userName, String samlSessionIndex) {
		if(session == null) {
			return;
		}
		session.setAttribute(SamlHelper.USER_NAME_PARAM, userName);
		session.setAttribute(SamlHelper.SAML_SESSION_INDEX_PARAM, samlSessionIndex);
	}
}
