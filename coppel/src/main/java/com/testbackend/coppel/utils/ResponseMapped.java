package com.testbackend.coppel.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseMapped {

    public static ResponseEntity<Object> setResponse(HttpStatusCode statusName, Object responseData,
            HttpStatus statusCode) {
        Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("Status", statusName);

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("Meta", meta);
        res.put("Data", responseData);

        return new ResponseEntity<Object>(res, statusCode);
    }

}
