package com.and.exception;

import com.and.response.BaseResponse;
import com.and.utils.BaseResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse unauthorizedException(HttpServletRequest req, Exception exception) {
    	return BaseResponseUtils.getBaseResponse("9999", exception.getMessage(), null);
    }
}
