package com.and.utils;

import com.and.response.BaseResponse;
import java.util.Map;

/**
 * 获取一个公共返回的类
 * @author: miaojinyong
 */
public class BaseResponseUtils {
    public static BaseResponse getBaseResponse(String success, String tipMsg, Map<String, Object> data){
    	return new BaseResponse(success, tipMsg, data);
    }
    
}
