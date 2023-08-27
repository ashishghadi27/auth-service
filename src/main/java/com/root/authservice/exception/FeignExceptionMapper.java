package com.root.authservice.exception;

import com.root.redis.constants.ExceptionConstants;
import com.root.commondependencies.exception.ValidationException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FeignExceptionMapper implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        String requestUrl = response.request().url();
        Response.Body responseBody = response.body();
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());
        String feignErrorMsg = getErrorMsg(responseBody);
        return new ValidationException.Builder()
                .errorCode(responseStatus.value())
                .errorMessage(ExceptionConstants.INTERNAL_ERROR + "\n" + feignErrorMsg).url(requestUrl).build();
    }


    private String getErrorMsg(Response.Body responseBody){
        try (InputStreamReader reader = new InputStreamReader(responseBody.asInputStream())) {
            // Read the error response as a string
            return new BufferedReader(reader)
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (IOException ioException) {
            // Handle the IOException while reading the response body
            ioException.printStackTrace();
        }
        return null;
    }
}
