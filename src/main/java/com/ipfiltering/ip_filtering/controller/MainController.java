package com.ipfiltering.ip_filtering.controller;

import com.ipfiltering.ip_filtering.model.ApiResponseMessage;
import com.ipfiltering.ip_filtering.model.BlackListFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class MainController {
    private final BlackListFilter blackListFilter;

    @Autowired
    public MainController(BlackListFilter blackListFilter) {
        this.blackListFilter = blackListFilter;
    }

    @GetMapping("/ipv4")
    public ApiResponseMessage getRequest(HttpServletRequest req) {
        String ip = getClientIP(req);
        ApiResponseMessage message;
        try {
            message = blackListFilter.filterBlackList(ip);
        } catch (Exception e) {
            message = new ApiResponseMessage("Deny");
        }
        System.out.println(ip);
        return message;
    }

    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (null == ip) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (null == ip) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (null == ip) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (null == ip) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (null == ip) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
