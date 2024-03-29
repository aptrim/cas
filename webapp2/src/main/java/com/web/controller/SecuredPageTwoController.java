package com.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/secured/two")
public class SecuredPageTwoController {

    @GetMapping
    public String index(ModelMap modelMap) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if( auth != null && auth.getPrincipal() != null
          && auth.getPrincipal() instanceof UserDetails) {
        modelMap.put("username", ((UserDetails) auth.getPrincipal()).getUsername());
      }
      return "secured/secure2";
    }
}