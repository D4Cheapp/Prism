package com.messenger.prism.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/prism")
public class IndexController {

  @GetMapping("")
  public String homeGreeting() {
    return "Hello World";
  }

}
