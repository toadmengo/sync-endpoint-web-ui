package org.benetech.util;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.benetech.constants.GeneralConsts;
import org.benetech.security.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.web.client.RestTemplate;

public class OdkClientUtils {

  private static Log logger = LogFactory.getLog(OdkClientUtils.class);

  public static Object getObjFromUserDetail(Authentication authentication, String key) {
    @SuppressWarnings("unchecked")
    Map<String, Object> userDetails = (Map<String, Object>) authentication.getDetails();

    if (userDetails == null || userDetails.getOrDefault(key, null) == null) {
      // TODO: Get reauthenticated. Easy workaround may be to force logout for now.
      SecurityUtils.logout();
      throw new PreAuthenticatedCredentialsNotFoundException("Cannot find.  Please logout and log in again.");

    } else {
      return userDetails.get(key);
    }
  }

  public static RestTemplate getRestTemplate() {
    return (RestTemplate)
            getObjFromUserDetail(SecurityContextHolder.getContext().getAuthentication(), GeneralConsts.ODK_REST_CLIENT);
  }
}
