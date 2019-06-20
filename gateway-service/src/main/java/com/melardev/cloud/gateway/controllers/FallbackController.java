package com.melardev.cloud.gateway.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> onMicroserviceError() {
        HashMap<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("full_messages", Collections.singletonList("Oops! Something went wrong"));
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }


    @GetMapping("proxy")
    public ResponseEntity<Map<String, Object>> onProxyError() {
        HashMap<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("full_messages", Collections.singletonList("Oops! Proxy may not be working"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
