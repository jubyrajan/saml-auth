# SAML Authentication Sample Implementation

A sample implementation for SAML based SSO (Single Sign On) authentication, with configurable support for X.509 certificate validation. Uses [OpenSAML](http://mvnrepository.com/artifact/org.opensaml) library for parsing SAML assertions.

Contains two projects:
- **saml-sp:** SAML Service Provider (SP) implementation.
- **saml-idp:** SAML Identity Provider (IDP) mock. The saml-sp project can work with any standard SAML IDPs.

Prerequisites:

- Java 5 or above
- Eclipse IDE
- Apache ANT

Note: All dependencies are included with the projects. Library jars files are under WEB-INF/lib folder.

Usage:

1. git clone https://github.com/jubyrajan/saml-auth
2. Import both projects into Eclipse IDE
3. Add a Server Runtime (say Apache Tomcat) to Eclipse
4. Change project configurations if required
5. Deploy the projects into Tomcat
6. Access the saml-sp web app in browser (example: http://localhost:8080/saml-sp)
7. Click on SAML IDP Login
8. Login using credentials user1/pass1


Building WAR files using ant: Using command line, go to the base folder of respective projects and execute **ant war**

##Configuration

- **saml-idp:** In WebContent/saml-sso.jsp change the variable String samlSpUrl = "http://localhost:8080"; appropriately if the SAML SP URL is different. Change the values in the userMap if required.
- **saml-sp:** In _org.jr.samlauth.sp.Config.java_ change the values if required.