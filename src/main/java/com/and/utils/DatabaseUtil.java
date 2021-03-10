package com.and.utils;

/**
 * @Author:         jiangzhihong
 * @CreateDate:     2021/1/22 9:17
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
	private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://172.21.3.2:3306/ngyun_3.0?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "nangao2019-";

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
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
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
	 * 获取数据库下的所有表名
	 */
	public static List<String> getTableNames() {
		List<String> tableNames = new ArrayList<>();
		Connection conn = getConnection();
		ResultSet rs = null;
		try {
			//获取数据库的元数据
			DatabaseMetaData db = conn.getMetaData();
			//从元数据中获取到所有的表名
			rs = db.getTables(null, null, null, new String[] { "TABLE" });
			while(rs.next()) {
				tableNames.add(rs.getString(3));
			}
		} catch (SQLException e) {
			LOGGER.error("getTableNames failure", e);
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				rs.close();
				closeConnection(conn);
			} catch (SQLException e) {
				LOGGER.error("close ResultSet failure", e);
				throw new RuntimeException(e.getMessage());
			}
		}
		return tableNames;
	}

	/**
	 * 获取表中所有字段名称
	 * @param tableName 表名
	 * @return
	 */
	public static List<String> getColumnNames(String tableName) {
		List<String> columnNames = new ArrayList<>();
		//与数据库的连接
		Connection conn = getConnection();
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
	 * 获取表中所有字段类型
	 * @param tableName
	 * @return
	 */
	public static List<String> getColumnTypes(String tableName) {
		List<String> columnTypes = new ArrayList<>();
		//与数据库的连接
		Connection conn = getConnection();
		PreparedStatement pStemt = null;
		String tableSql = SQL + tableName;
		try {
			pStemt = conn.prepareStatement(tableSql);
			//结果集元数据
			ResultSetMetaData rsmd = pStemt.getMetaData();
			//表列数
			int size = rsmd.getColumnCount();
			for (int i = 0; i < size; i++) {
				columnTypes.add(rsmd.getColumnTypeName(i + 1));
			}
		} catch (SQLException e) {
			LOGGER.error("getColumnTypes failure", e);
			throw new RuntimeException(e.getMessage());
		} finally {
			if (pStemt != null) {
				try {
					pStemt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					LOGGER.error("getColumnTypes close pstem and connection failure", e);
					throw new RuntimeException(e.getMessage());
				}
			}
		}
		return columnTypes;
	}

	/**
	 * 获取表中字段的所有注释
	 * @param tableName
	 * @return
	 */
	public static List<String> getColumnComments(String tableName) {
		List<String> columnTypes = new ArrayList<>();
		//与数据库的连接
		Connection conn = getConnection();
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
	public static void main(String[] args) {
//		List<String> tableNames = getTableNames();
//		System.out.println("tableNames:" + tableNames);
//		for (String tableName : tableNames) {
//			System.out.println("ColumnNames:" + getColumnNames(tableName));
//			System.out.println("ColumnTypes:" + getColumnTypes(tableName));
//			System.out.println("ColumnComments:" + getColumnComments(tableName));
//		}
		String tableName = "equipment_knowledge_base";
		List<String> columnNames = getColumnNames(tableName);
		List<String> columnComments = getColumnComments(tableName);
		String str = "{" + "\n";
		int size = columnNames.size();
		for (int i = 0; i < size; i++) {

			if (i == size - 1) {
				str += "\t\"" + underlineToCamelhump(columnNames.get(i)) + "\":\"\"" + " // " + columnComments.get(i) + "\n}";
			} else {
				str += "\t\"" + underlineToCamelhump(columnNames.get(i)) + "\":\"\"," + " // " + columnComments.get(i) + "\n";
			}
		}
		System.out.println(str);
	}

	/**
	 * 获取信息
	 */
	public static void getInfomation(String tableName) {
		List<String> columnNames = getColumnNames(tableName);
		List<String> columnComments = getColumnComments(tableName);
		String str = "{" + "\n";
		int size = columnNames.size();
		for (int i = 0; i < size; i++) {

			if (i == size - 1) {
				str += "\t\"" + underlineToCamelhump(columnNames.get(i)) + "\":\"\"" + " // " + columnComments.get(i) + "\n}";
			} else {
				str += "\t\"" + underlineToCamelhump(columnNames.get(i)) + "\":\"\"," + " // " + columnComments.get(i) + "\n";
			}
		}
		System.out.println(str);
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
