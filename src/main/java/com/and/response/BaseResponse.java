package com.and.response;

import java.util.Map;

/**
 * <一句话功能简述> TODO
 * <功能详细描述>
 * @author  
 * @version V0.0.1-SNAPSHOT
 */
public class BaseResponse {
    /**
     * ajax返回结果是否成功
     */
    private String code;
    
    /**
     * ajax返回结果信息
     */
    private String msg;
    
    /**
     * 返回的数据对象（json）
     */
    private Map<String, Object> data;
    
    public BaseResponse(String success, String tipMsg, Map<String, Object> data) {
    	this.code = success;
    	this.msg = tipMsg;
    	this.data = data;
    }
    
    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}



	/**
     * @return the data
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
