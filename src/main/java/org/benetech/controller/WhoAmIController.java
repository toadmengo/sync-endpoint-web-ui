package org.benetech.controller;

import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.benetech.client.OdkClientFactory;
import org.benetech.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WhoAmIController {

  Log logger = LogFactory.getLog(WhoAmIController.class);

  
  @Autowired
  OdkClientFactory odkClientFactory;

  @RequestMapping("/whoami")
  public String whoami(Model model, Authentication authentication, HttpSession session) {

    model.addAttribute("username", authentication.getName());
    model.addAttribute("officeId", UserUtils.getDefaultGroup(authentication));
    model.addAttribute("fullName", UserUtils.getFullName(authentication));

    Map<String, String> roleDescriptions = authentication
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toMap(auth -> auth, auth -> "Description"));
    model.addAttribute("roleDescriptions", roleDescriptions);

    return "whoami";
  }
}
