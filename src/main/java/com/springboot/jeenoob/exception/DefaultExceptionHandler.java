package com.springboot.jeenoob.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.jeenoob.result.Result;
import com.springboot.jeenoob.result.ResultFactory;

@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public Result handleAuthorizationException(UnauthorizedException e) {
        String message = "权限认证失败";
        return ResultFactory.buildFailResult(message);
    }
}
