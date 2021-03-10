package com.and.utils;

import com.and.response.BaseResponse;
import java.util.Map;

public class BaseResponseUtils {
    public static BaseResponse getBaseResponse(String success, String tipMsg, Map<String, Object> data){
    	return new BaseResponse(success, tipMsg, data);
    }
    
}
