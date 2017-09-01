package org.benetech.security;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.benetech.client.OdkClient;
import org.benetech.constants.GeneralConsts;
import org.benetech.security.client.digest.DigestRestTemplateFactory;
import org.opendatakit.aggregate.odktables.rest.entity.PrivilegesInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class WebServiceDelegatingAuthenticationProvider implements AuthenticationProvider {

  private static Log logger = LogFactory.getLog(WebServiceDelegatingAuthenticationProvider.class);

  @Autowired
  Properties webServicesProperties;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String odkUrlString = webServicesProperties.getProperty("odk.url");
    String odkRealmName = webServicesProperties.getProperty("odk.realm");
    Map<String, Object> userDetails = new HashMap<>();

    URL odkUrl;
    URI odkUri;
    if (odkUrlString == null) {
      throw new InternalAuthenticationServiceException(
          "Host address is blank.  Did you configure the web service host?");
    }
    try {
      odkUrl = new URL(odkUrlString);
      odkUri = odkUrl.toURI();
    } catch (MalformedURLException | URISyntaxException e) {
      throw new InternalAuthenticationServiceException(
          "Bad host syntax.  Did you configure the web service host?");
    }

    String username = (String) authentication.getPrincipal();
    String password = authentication.getCredentials().toString();

    RestTemplate restTemplate = DigestRestTemplateFactory.getRestTemplate(
            odkUri.getHost(),
            odkUri.getPort(),
            odkUri.getScheme(),
            odkRealmName,
            username,
            password
    );
    String getRolesGrantedUrl = odkUrl.toExternalForm() + OdkClient.TABLE_PRIVILEGES_ENDPOINT;
    ResponseEntity<PrivilegesInfo> getResponse;
    try {
      logger.info("Logging in with " + getRolesGrantedUrl);

      getResponse = restTemplate.exchange(
              getRolesGrantedUrl,
              HttpMethod.GET,
              null,
              PrivilegesInfo.class,
              webServicesProperties.getProperty("odk.app.id")
      );
    } catch (HttpClientErrorException e) {
      logger.info("Received an exception when getting granted roles");
      logger.info("Received " + e.getRawStatusCode());
      logger.info("Received " + e.getResponseBodyAsString());
      if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
        throw new BadCredentialsException("Unable to log in to remote web service.");
      }
      else {
        throw new AuthenticationServiceException(e.getMessage());
      }
    }

    userDetails.put(GeneralConsts.ODK_REST_CLIENT, restTemplate);
    userDetails.put(GeneralConsts.PRIVILEGES_INFO, getResponse.getBody());

    // Cached credentials for file upload form / pre-emptive digest authentication
    UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(username, password);

    userDetails.put(GeneralConsts.PREEMPTIVE_CREDENTIALS, usernamePasswordCredentials);



    if (getResponse.getStatusCode().equals(HttpStatus.OK)) {
      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
              username,
              password,
              getResponse
                      .getBody()
                      .getRoles()
                      .stream()
                      .map(SimpleGrantedAuthority::new)
                      .collect(Collectors.toSet())
      );

      token.setDetails(userDetails);
      return token;
    } else {
      logger.info("Received a non-200 error code when getting granted roles: " + getResponse.getStatusCodeValue());
      logger.info(getResponse.getBody());
      // Add more error cases here, or research how it is handled by default.
      // "Bad Credentials" is only one potential cause.
      if (getResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
        throw new BadCredentialsException("Unable to log in to remote web service.");
      }
      else {
        throw new AuthenticationServiceException(getResponse.getStatusCode().getReasonPhrase());
      }
    }

  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
