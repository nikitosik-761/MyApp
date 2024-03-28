package com.fullApp.myApp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {

record PingPong(String ping){}

    @GetMapping("/ping")
    public PingPong getPingPong(){
          return new PingPong("Test Ping Pong !!!!");
    }

}
