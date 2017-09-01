package org.benetech.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.benetech.client.OdkClient;
import org.benetech.client.OdkClientFactory;
import org.opendatakit.aggregate.odktables.rest.entity.UserInfoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserAdminController {
  private static Log logger = LogFactory.getLog(UserAdminController.class);

  @Autowired
  OdkClientFactory odkClientFactory;

  private void populateDefaultModel(Model model) {
    OdkClient odkClient = odkClientFactory.getOdkClient();
    UserInfoList users = odkClient.getUserAuthorityGrid();
    model.addAttribute("users", users);
  }

  @Secured({"ROLE_SITE_ACCESS_ADMIN"})
  @GetMapping("/admin/users")
  public String userGrid(Model model, Authentication authentication) {
    populateDefaultModel(model);

    return "admin_user_grid";
  }
}
