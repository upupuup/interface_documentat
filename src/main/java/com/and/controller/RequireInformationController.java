package com.and.controller;

import com.and.constant.Constant;
import com.and.response.BaseResponse;
import com.and.utils.DatabaseTransformUtils;
import com.and.utils.DateUtils;
import com.and.utils.LineHumpUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author:         jiangzhihong
 * @CreateDate:     2021/1/22 10:04
 */
@RequestMapping("/")
@CrossOrigin
@RestController
public class RequireInformationController extends BaseController {
	/**
	 * 返回的key是字段名（驼峰），value是注释
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/requireBaseInfo")
	public BaseResponse requireBaseInfo(@RequestBody Map<String, String> map) throws Exception {
		String ip = MapUtils.getString(map, Constant.BaseParam.IP);
		String dataBaseName = MapUtils.getString(map, Constant.BaseParam.DATA_BASE_NAME);
		String tableName = MapUtils.getString(map, Constant.BaseParam.TABLE_NAME);
		String userName = MapUtils.getString(map, Constant.BaseParam.USERNAME);
		String password = MapUtils.getString(map, Constant.BaseParam.PASSWORD);
		data.put(Constant.ResultInfo.DATA, DatabaseTransformUtils.getInformation(tableName, ip, dataBaseName, userName, password));
		this.msg="";
		return returnBaseResponse();
	}

	/**
	 * 返回的key是字段名（驼峰），value是注释加字段类型
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/requireCommentsAndType")
	public BaseResponse requireCommentsAndType(@RequestBody Map<String, String> map) throws Exception {
		String ip = MapUtils.getString(map, Constant.BaseParam.IP);
		String dataBaseName = MapUtils.getString(map, Constant.BaseParam.DATA_BASE_NAME);
		String tableName = MapUtils.getString(map, Constant.BaseParam.TABLE_NAME);
		String userName = MapUtils.getString(map, Constant.BaseParam.USERNAME);
		String password = MapUtils.getString(map, Constant.BaseParam.PASSWORD);
		data.put(Constant.ResultInfo.DATA, DatabaseTransformUtils.requireCommentsAndType(tableName, ip, dataBaseName, userName, password));
		this.msg="";
		return returnBaseResponse();
	}

	/**
	 * 生成判断语句
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/requireJava")
	public BaseResponse requireJava(@RequestBody Map<String, String> map) throws Exception {
		String resultStr = "";
		String tableName = MapUtils.getString(map,"tableName");
		map.remove("tableName");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			int i = value.indexOf("-");
			// 获取注释
			String comment = value.substring(0, i);
			int j = value.indexOf("=");
			// 获取长度
			String length = value.substring(i + 1, j);
			// 获取类型
			int k = value.indexOf("*");
			String type = value.substring(j + 1, k);

			if ("BIGINT".equals(type)){
				resultStr += "EmptyUtils.isEmpty(" + tableName + ".get" + key.substring(0, 1).toUpperCase() + key.substring(1) + "(), " + comment + ");";
				System.out.println("EmptyUtils.isEmpty(\"" + comment + "\", " + tableName + ".get" + key.substring(0, 1).toUpperCase() + key.substring(1) + "());");
			} else if ("INT".equals(type)) {
				int m = value.indexOf(",");
				Integer lowNum = null;
				Integer highNum = null;
				if (m > 0) {
					if (k + 1 != m) {
						lowNum = Integer.valueOf(value.substring(k + 1, m));
					}
					highNum = Integer.valueOf(value.substring(m + 1));
				} else if (k + 1 != value.length()) {
					lowNum = Integer.valueOf(value.substring(k + 1));
				}
				if (lowNum != null || highNum != null) {
					// 校验范围
					System.out.println("EmptyUtils.checkIntegerRange(\"" + comment + "\", " + tableName + ".get" + key.substring(0, 1).toUpperCase() + key.substring(1) + "(), " +
							lowNum + ", " + highNum + ");");
				} else {
					// 不校验数值位数
					System.out.println("EmptyUtils.isEmpty(\"" + comment + "\", " + tableName + ".get" + key.substring(0, 1).toUpperCase() + key.substring(1) + "());");
				}
			} else if ("VARCHAR".equals(type)) {
				System.out.println("EmptyUtils.isTooLong(\"" + comment + "\", " + tableName + ".get" + key.substring(0, 1).toUpperCase() + key.substring(1) + "(), "
						+ length + ", true);");
			} else if ("DECIMAL".equals(type)) {
				int m = value.indexOf(",");
				Integer lowNum = null;
				Integer highNum = null;
				if (m > 0) {
					if (k + 1 != m) {
						lowNum = Integer.valueOf(value.substring(k + 1, m));
					}
					highNum = Integer.valueOf(value.substring(m + 1));
				} else if (k + 1 != value.length()) {
					lowNum = Integer.valueOf(value.substring(k + 1));
				}
				if (lowNum != null || highNum != null) {
					// 校验范围
					System.out.println("EmptyUtils.checkDoubleLength(\"" + comment + "\", " + tableName + ".get" + key.substring(0, 1).toUpperCase() + key.substring(1) + "(), " +
							lowNum + ", " + highNum + ");");
				} else {
					// 不校验数值位数
					System.out.println("EmptyUtils.isEmpty(\"" + comment + "\", " + tableName + ".get" + key.substring(0, 1).toUpperCase() + key.substring(1) + "());");
				}
			}

		}
		this.msg="";
		return returnBaseResponse();
	}

	/**
	 * 生成SQL queryList的判断语句
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/requireSQLJava")
	public BaseResponse requireSQLJava(@RequestBody Map<String, String> map) throws Exception {
		String resultStr = "";
		String tableName = MapUtils.getString(map,"tableName");
		map.remove("tableName");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			int i = value.indexOf("-");
			// 获取注释
			String comment = value.substring(0, i);
			int j = value.indexOf("=");
			// 获取长度
			String length = value.substring(i + 1, j);
			// 获取类型
			int k = value.indexOf("*");
			String type = value.substring(j + 1, k);

			if (key.contains("Time")) {
				String list = key + "List";
				System.out.println("<if test=\""+ list +" != null and " + list +".size() > 0\">");
				System.out.println("    AND DATE_FORMAT(" + LineHumpUtils.humpToUnderline(key) + ", '%Y-%m-%d') &gt;= #{" + list + "[0]}");
				System.out.println("    AND DATE_FORMAT(" + LineHumpUtils.humpToUnderline(key) + ", '%Y-%m-%d') &lt;= #{" + list + "[1]}");
				System.out.println("</if>");
			} else if (key.contains("Date")) {
				String list = key + "List";
				System.out.println("<if test=\""+ list +" != null and " + list +".size() > 0\">");
				System.out.println("    " + LineHumpUtils.humpToUnderline(key) + " &gt;= #{" + list + "[0]}");
				System.out.println("    " + LineHumpUtils.humpToUnderline(key) + " &lt;= #{" + list + "[1]}");
				System.out.println("</if>");
			} else if ("BIGINT".equals(type)){
				System.out.println("<if test=\""+ key +" != null\">");
				System.out.println("    AND " + LineHumpUtils.humpToUnderline(key) + " = #{" + key + "}");
				System.out.println("</if>");
			} else if ("INT".equals(type)) {
				System.out.println("<if test=\""+ key +" != null\">");
				System.out.println("    AND " + LineHumpUtils.humpToUnderline(key) + " = #{" + key + "}");
				System.out.println("</if>");
			} else if ("VARCHAR".equals(type)) {
				if (key.contains("TypeName")) {
					System.out.println("<if test=\""+ key +" != null and " + key +" != ''\">");
					System.out.println("    AND " + LineHumpUtils.humpToUnderline(key) + " LIKE CONCAT(\"%\", #{" + key + "}, \"%\")");
					System.out.println("</if>");
				} else if (key.contains("Type")) {
					System.out.println("<if test=\""+ key +" != null and " + key +" != ''\">");
					System.out.println("    AND " + LineHumpUtils.humpToUnderline(key) + " = #{" + key + "}");
					System.out.println("</if>");
				} else {
					System.out.println("<if test=\""+ key +" != null and " + key +" != ''\">");
					System.out.println("    AND " + LineHumpUtils.humpToUnderline(key) + " LIKE CONCAT(\"%\", #{" + key + "}, \"%\")");
					System.out.println("</if>");
				}
			} else if ("DECIMAL".equals(type)) {
				System.out.println("<if test=\""+ key +" != null\">");
				System.out.println("    AND " + LineHumpUtils.humpToUnderline(key) + " = #{" + key + "}");
				System.out.println("</if>");
			}

		}
		this.msg="";
		return returnBaseResponse();
	}

	public static void main(String[] args) {
		Set<Integer> set = new HashSet<>();
		List<Integer> list = new ArrayList<>();
		int num = 0;
		while (num++ < 7) {
			genList(set, list, num, 33);
		}

		Collections.sort(list, (p1, p2) -> p1.compareTo(p2));
		set.clear();
		genList(set, list, num, 16);
		list.stream().forEach(ceil -> System.out.print(ceil + "  "));

	}

	private static void genList(Set<Integer> set, List<Integer> list, int num, int randomNum) {
		Double value = Math.random() * randomNum;
		int ceil = value.intValue();
		if (set.contains(ceil) || ceil == 0) {
			genList(set, list, num, randomNum);
			return;
		}
		set.add(ceil);
		list.add(ceil);
	}

	public static void reverseString(char[] s) {
		int n = s.length;
		for (int i = 0; i < n / 2; ++i) {
			int j = n - 1 - i;
			s[i] ^= s[j];
			s[j] ^= s[i];
			s[i] ^= s[j];
		}
	}

	public static int[] twoSum(int[] nums, int target) {
		int[] resultArray = new int[2];
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < nums.length; i++) {
			map.put(nums[i], i);
		}

		for (int i = 0; i < nums.length; i++) {
			int result = target - nums[i];
			if (map.containsKey(result) && map.get(result) != i) {
				resultArray[0] = i;
				resultArray[1] = map.get(result);
				return resultArray;
			}
		}
		return resultArray;
	}

	/**
	 * 导出
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/exportJava")
	public BaseResponse exportJava(@RequestBody Map<String, String> map) throws Exception {
		String resultStr = "";
		String tableName = MapUtils.getString(map,"tableName");
		String className = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
		map.remove("tableName");

		String arr = "";
		String arrName = "";
		String width = "";

		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			int index = key.indexOf("-");
			if (index > 0) {
				key = key.substring(0, index);
			}
			String value = entry.getValue();
			index = value.indexOf("-");
			if (index > 0) {
				value = value.substring(0, index);
			}
			arr += "\"" + key + "\", ";
			arrName += "\"" + value + "\", ";
			width += "5000, ";
		}

		System.out.println("controller开始----------------");
		// controller代码
		// 生成注释
		this.createComment("导出校验", tableName);
		// 校验导出的代码
		System.out.println("//    @RequiresPermissions(\"" + tableName + ":exportExcel\")");
		System.out.println("@PostMapping(value=\"/checkExportExcel\")");
		System.out.println("public BaseResponse exportExcel(@RequestBody " + className + " " + tableName + ") throws Exception  {");
		System.out.println("    clear();");
		System.out.println("    // 查询是否有可以导出的数据");
		System.out.println("    List<" + className + "> list = " + tableName + "Service.checkExportExcel(" + tableName + ");");
		System.out.println("    // 查询出数据，直接返回数据");
		System.out.println("    if (CollectionUtils.isNotEmpty(list)) {");
		System.out.println("        return returnBaseResponse(InterfaceOpEnum.QUERY_OP);");
		System.out.println("    }");
		System.out.println("");
		System.out.println("    // 没有数据，设置错误的编码和提示信息给前端");
		System.out.println("    this.code = \"9999\";");
		System.out.println("    this.msg = \"当前没有数据信息\";");
		System.out.println("    return returnBaseResponse(InterfaceOpEnum.CHECK_EXPORT_OP);");
		System.out.println("    }");
		System.out.println();
		// 生成注释
		this.createComment("导出", tableName);
		// 导出的代码
		System.out.println("//    @RequiresPermissions(\"" + tableName + ":exportExcel\")");
		System.out.println("@PostMapping(value=\"/exportExcel\", consumes=\"application/json\", produces=\"application/json\")");
		System.out.println("public BaseResponse exportExcel(@RequestBody " + className + " " + tableName + ", HttpServletResponse response) throws Exception  {");
		System.out.println("    clear();");
		System.out.println("    " + tableName + "Service.exportExcel(" + tableName + ", response);");
		System.out.println("    return returnBaseResponse(InterfaceOpEnum.OPERATE_OP);");
		System.out.println("}");
		System.out.println();

		System.out.println("service开始----------------");
		// 生成注释
		this.createComment("导出校验", tableName);
		// service代码
		System.out.println("List<" + className + "> checkExportExcel(" + className + " " + tableName + ") throws Exception;");
		System.out.println();
		// 生成注释
		this.createComment("导出", tableName);
		// service代码
		System.out.println("void exportExcel(" + className + " " + tableName + ", HttpServletResponse response) throws Exception;");
		System.out.println();
		System.out.println("serviceImpl开始----------------");

		// 生成注释
		this.createComment("导出校验", tableName);
		// serviceImpl代码
		System.out.println("@Override");
		System.out.println("public List<" + className + "> checkExportExcel(" + className + " " + tableName + ") throws Exception {");
		System.out.println("    return this.dao.queryList(" + tableName + ");");
		System.out.println("}");
		System.out.println();

		// 生成注释
		this.createComment("导出", tableName);
		// serviceImpl代码
		System.out.println("@Override");
		System.out.println("public void exportExcel(" + className + " " + tableName + ", HttpServletResponse response) throws Exception {");
		System.out.println("    List<" + className + "> list = this.dao.queryList(" + tableName + ");");
		arr = "{" + arr.substring(0, arr.length() - 2) + "};";
		System.out.println("    String[] arr = " + arr);
		arrName = "{" + arrName.substring(0, arrName.length() - 2) + "};";
		System.out.println("    String[] arrName = " + arrName);
		width = "{" + width.substring(0, width.length() - 2) + "};";
		System.out.println("    Integer[] width = " + width);
		System.out.println("    SolveExportExcelUtils.solveExportMapToExcel(response, list, arr, arrName, \"\", \"\", width);");
		System.out.println("}");
		this.msg="";
		return returnBaseResponse();
	}

	/**
	 * 导出的注释
	 * @param methodDesc
	 * @param tableName
	 */
	private void createComment(String methodDesc, String tableName) {
		System.out.println("/**");
		System.out.println(" * " + methodDesc);
		System.out.println(" * @param " + tableName);
		if (!"导出校验".equals(methodDesc)) {
			System.out.println(" * @param response");
		}
		System.out.println(" * @Author: jiangzhihong");
		System.out.println(" * @CreateDate: " + DateUtils.getDefaultSysString(DateUtils.YYYY_BIAS_MM_BIAS_DD_HH_MM_SS));
		System.out.println(" */");
	}

	/**
	 * 清除value值
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/clearValue")
	public BaseResponse clearValue(@RequestBody Map<String, String> map) throws Exception {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			map.put(entry.getKey(), "");
		}
		this.data.put(Constant.ResultInfo.DATA, map);
		return returnBaseResponse();
	}
	/**
	 * 清除value值的-后面的数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/clearLineValue")
	public BaseResponse clearLineValue(@RequestBody Map<String, String> map) throws Exception {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String value = entry.getValue();
			int index = value.indexOf("-");
			if (index < 0) {
				continue;
			}
			map.put(entry.getKey(), value.substring(0, index));
		}
		this.data.put(Constant.ResultInfo.DATA, map);
		return returnBaseResponse();
	}
}
