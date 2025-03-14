package com.sayantan.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {
    @GetMapping("/get/greetings")
    public String getHello(){
        return "Good Morning....";
    }
    @GetMapping("/get/admin/message")
    public String getAdminMessage(){
        return "Good Morning Admin....";
    }
}
