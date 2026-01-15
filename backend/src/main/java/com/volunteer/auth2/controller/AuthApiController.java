package com.volunteer.auth2.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
@Controller
@RequestMapping("${openapi.volunteerInformationSystem.base-path:/api/v1}")
public class AuthApiController{

    private final NativeWebRequest request;

    @Autowired
    public AuthApiController(NativeWebRequest request) {
        this.request = request;
    }

    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }
}
