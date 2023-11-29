package com.example.tutorialwebflux.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAttributes  extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {

        Map<String, Object> errorAtributes = new HashMap<>();

        Throwable throwable = super.getError(request);

        if (throwable instanceof CustomException ) {
            CustomException customException = (CustomException) throwable;
            errorAtributes.put("status", customException.getStatus());
            errorAtributes.put("message", customException.getMessage());

        }

        return errorAtributes;


    }
}
