package com.and.constant;

/**
 * @Author:         jiangzhihong
 * @CreateDate:     2021/1/22 10:45
 */
public class Constant {
	public interface BaseParam {
		//	private static final String URL = "jdbc:mysql://172.21.3.2:3306/ngyun_3.0?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
		String IP = "ip";
		String DATA_BASE_NAME = "dataBaseName";
		String TABLE_NAME = "tableName";
		String USERNAME = "userName";
		String PASSWORD = "password";
	}

	public interface ResultInfo {
		// 后台查询的数据用户返回给前台放在model中数据的key
		public final static String PAGE = "page";
		// 用于放单个数据的Key
		public final static String DATA = "data";
		// 用于放List数据的Key
		public final static String DATALIST = "dataList";
		// 用于点击按钮时跳转页面的类型
		public final static String TYPE = "type";
		// 数据字典下拉框返回值
		public final static String DICDATA = "dicData";
		// 数据字典下拉框返回值
		public final static String MENU_LIST = "menuList";
		// 返回按钮list
		public final static String BUTTON_LIST = "button";
	}
}
