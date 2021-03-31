package com.and.utils;

/**
 * @Author:         jiangzhihong
 * @CreateDate:     2021/1/22 9:17
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseTransformUtils {
	private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseTransformUtils.class);
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
//	private static final String URL = "jdbc:mysql://172.21.3.2:3306/ngyun_3.0?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
	private static String URL_FIRST_PART = "jdbc:mysql://";
	static String URL_SECOND_PART = "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
//	private static final String USERNAME = "root";
//	private static final String PASSWORD = "nangao2019-";

	private static final String SQL = "SELECT * FROM ";// 数据库操作

	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			LOGGER.error("can not load jdbc driver", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 获取数据库连接
	 *
	 * @return
	 */
	public static Connection getConnection(String ip, String dataBaseName, String userName, String password) {
		Connection conn = null;
		try {
			String URL = URL_FIRST_PART + ip + "/" + dataBaseName + URL_SECOND_PART;
			conn = DriverManager.getConnection(URL, userName, password);
		} catch (SQLException e) {
			LOGGER.error("get connection failure", e);
			throw new RuntimeException(e.getMessage());
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 * @param conn
	 */
	public static void closeConnection(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				LOGGER.error("close connection failure", e);
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	/**
	 * 获取表中所有字段名称
	 * @param tableName 表名
	 * @return
	 */
	public static List<String> getColumnNames(String tableName, String ip, String dataBaseName, String userName, String password) {
		List<String> columnNames = new ArrayList<>();
		List<Integer> columnLengths = new ArrayList<>();
		//与数据库的连接
		Connection conn = getConnection(ip, dataBaseName, userName, password);
		PreparedStatement pStemt = null;
		String tableSql = SQL + tableName;
		try {
			pStemt = conn.prepareStatement(tableSql);
			//结果集元数据
			ResultSetMetaData rsmd = pStemt.getMetaData();
			//表列数
			int size = rsmd.getColumnCount();
			for (int i = 0; i < size; i++) {
				columnNames.add(rsmd.getColumnName(i + 1));
				columnLengths.add(rsmd.getPrecision(i + 1));
			}
		} catch (SQLException e) {
			LOGGER.error("getColumnNames failure", e);
			throw new RuntimeException(e.getMessage());
		} finally {
			if (pStemt != null) {
				try {
					pStemt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					LOGGER.error("getColumnNames close pstem and connection failure", e);
					throw new RuntimeException(e.getMessage());
				}
			}
		}
		return columnNames;
	}

	/**
	 * 获取表中所有字段名称和字段长度
	 * @param tableName 表名
	 * @return
	 */
	public static Map<String, List> getColumnNamesAndLength(String tableName, String ip, String dataBaseName, String userName, String password) {
		List<String> columnNames = new ArrayList<>();
		List<Integer> columnLengths = new ArrayList<>();
		List<String> columnTypes = new ArrayList<>();
		//与数据库的连接
		Connection conn = getConnection(ip, dataBaseName, userName, password);
		PreparedStatement pStemt = null;
		String tableSql = SQL + tableName;
		try {
			pStemt = conn.prepareStatement(tableSql);
			//结果集元数据
			ResultSetMetaData rsmd = pStemt.getMetaData();
			//表列数
			int size = rsmd.getColumnCount();
			for (int i = 0; i < size; i++) {
				columnNames.add(rsmd.getColumnName(i + 1));
				columnLengths.add(rsmd.getColumnDisplaySize(i + 1));
				columnTypes.add(rsmd.getColumnTypeName(i + 1));
			}
		} catch (SQLException e) {
			LOGGER.error("getColumnNames failure", e);
			throw new RuntimeException(e.getMessage());
		} finally {
			if (pStemt != null) {
				try {
					pStemt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					LOGGER.error("getColumnNames close pstem and connection failure", e);
					throw new RuntimeException(e.getMessage());
				}
			}
		}
		Map<String, List> map = new HashMap<>();
		map.put("columnNames", columnNames);
		map.put("columnLengths", columnLengths);
		map.put("columnTypes", columnTypes);
		return map;
	}

	/**
	 * 获取表中字段的所有注释
	 * @param tableName
	 * @return
	 */
	public static List<String> getColumnComments(String tableName, String ip, String dataBaseName, String userName, String password) {
		List<String> columnTypes = new ArrayList<>();
		//与数据库的连接
		Connection conn = getConnection(ip, dataBaseName, userName, password);
		PreparedStatement pStemt = null;
		String tableSql = SQL + tableName;
		List<String> columnComments = new ArrayList<>();//列名注释集合
		ResultSet rs = null;
		try {
			pStemt = conn.prepareStatement(tableSql);
			rs = pStemt.executeQuery("show full columns from " + tableName);
			while (rs.next()) {
				columnComments.add(rs.getString("Comment"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
					closeConnection(conn);
				} catch (SQLException e) {
					LOGGER.error("getColumnComments close ResultSet and connection failure", e);
					throw new RuntimeException(e.getMessage());
				}
			}
		}
		return columnComments;
	}

	/**
	 * 获取信息
	 */
	public static Map<String, String> getInformation(String tableName, String ip, String dataBaseName, String userName, String password) {
		List<String> columnNames = getColumnNames(tableName, ip, dataBaseName, userName, password);
		List<String> columnComments = getColumnComments(tableName, ip, dataBaseName, userName, password);
		String str = "{" + "\n";
		Map<String, String> map = new HashMap<>();
		int size = columnNames.size();
		for (int i = 0; i < size; i++) {
			String columnName = underlineToCamelhump(columnNames.get(i));
			String comment = columnComments.get(i);
			map.put(columnName, comment);
			if (i == size - 1) {
				str += "\t\"" + columnName + "\":\"\"" + " // " + comment + "\n}";
			} else {
				str += "\t\"" + columnName + "\":\"\"," + " // " + comment + "\n";
			}
		}
		System.out.println(str);

		return map;
	}

	/**
	 * 获取信息
	 */
	public static Map<String, String> requireCommentsAndType(String tableName, String ip, String dataBaseName, String userName, String password) {
		Map<String, List> columnNamesAndLength = getColumnNamesAndLength(tableName, ip, dataBaseName, userName, password);
		List<String> columnNames = columnNamesAndLength.get("columnNames");
		List<Integer> columnLengths = columnNamesAndLength.get("columnLengths");
		List<String> columnTypes = columnNamesAndLength.get("columnTypes");
		List<String> columnComments = getColumnComments(tableName, ip, dataBaseName, userName, password);
		String str = "{" + "\n";
		Map<String, String> map = new HashMap<>();
		int size = columnNames.size();
		for (int i = 0; i < size; i++) {
			String columnName = underlineToCamelhump(columnNames.get(i));
			String comment = columnComments.get(i);
			Integer length = columnLengths.get(i);
			String columnType = columnTypes.get(i);
			map.put(columnName, comment + "-" + length + "=" + columnType + "*");
			if (i == size - 1) {
				str += "\t\"" + columnName + "\":\"\"" + " // " + comment + "\n}";
			} else {
				str += "\t\"" + columnName + "\":\"\"," + " // " + comment + "\n";
			}
		}
		System.out.println(str);
		map.put("tableName", LineHumpUtils.underlineToHump(tableName));

		return map;
	}

	/**
	 * 驼峰转换
	 * @param inputString
	 * @return
	 */
	private static String underlineToCamelhump(String inputString) {
		StringBuilder sb = new StringBuilder();
		boolean nextUpperCase = false;
		for (int i = 0; i < inputString.length(); i++) {
			char c = inputString.charAt(i);
			if (c == '_') {
				if (sb.length() > 0) {
					nextUpperCase = true;
				}
			} else {
				if (nextUpperCase) {
					sb.append(Character.toUpperCase(c));
					nextUpperCase = false;
				} else {
					sb.append(Character.toLowerCase(c));
				}
			}
		}
		return sb.toString();
	}
}
