package com.and.controller;

import com.and.constant.Constant;
import com.and.response.BaseResponse;
import com.and.utils.DatabaseTransformUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author:         jiangzhihong
 * @CreateDate:     2021/1/22 10:04
 */
@RequestMapping("/")
@RestController
public class RequireInformationController extends BaseController {
	@RequestMapping("/requireBaseInfo")
	public BaseResponse requireBaseInfo(@RequestBody Map<String, String> map) {
		String ip = MapUtils.getString(map, Constant.BaseParam.IP);
		String dataBaseName = MapUtils.getString(map, Constant.BaseParam.DATA_BASE_NAME);
		String tableName = MapUtils.getString(map, Constant.BaseParam.TABLE_NAME);
		String userName = MapUtils.getString(map, Constant.BaseParam.USERNAME);
		String password = MapUtils.getString(map, Constant.BaseParam.PASSWORD);

		data.put(Constant.ResultInfo.DATA, DatabaseTransformUtils.getInformation(tableName, ip, dataBaseName, userName, password));
		return returnBaseResponse();
	}

}
