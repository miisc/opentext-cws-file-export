package com.opentext.cws.export.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.opentext.cws.export.entity.AppConstant;
import com.opentext.cws.export.entity.OTNode;

public class DBService {

	static Connection conn;
	static Statement stmt;
	static ResultSet rs;

	public static boolean dbInit() {

		try {
			conn = DriverManager.getConnection(AppConstant.dbUrl, AppConstant.dbUserName, AppConstant.dbPassword);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void dbClean() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * return node by node id
	 */
	public static OTNode getNodeById(long nodeId) {
		dbInit();
		OTNode node = new OTNode();
		try {
			String sql = "select dataid, name, parentid, versionnum, subtype from dtree where dataid=" + nodeId;
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				node.setNodeID(rs.getLong("dataid"));
				node.setName(Utils.escapeStr(rs.getString("name")));
				node.setParentNodeID(rs.getLong("parentid"));
				node.setCurrentVersion(rs.getLong("versionnum"));
				switch (rs.getString("subtype")) {
				case "0":
					node.setNodeType("FOLDER");
					break;
				case "298":
					node.setNodeType("COLLECTION");
					break;
				case "749":
					node.setNodeType("EMAIL");
					break;
				case "144":
					node.setNodeType("FILE");
					break;
				default:
					node.setNodeType("OTHER");
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClean();
		}
		return node;
	}

	public static List<OTNode> getChildNode(OTNode parentNode) {
		dbInit();

		List<OTNode> list = new ArrayList<OTNode>();
		try {
			String sql = "select dataid, name, parentid, versionnum, subtype from dtree where parentid="
					+ parentNode.getNodeID();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				OTNode node = new OTNode();
				node.setNodeID(rs.getLong("dataid"));
				node.setName(Utils.escapeStr(rs.getString("name")));
				node.setParentNodeID(rs.getLong("parentid"));
				node.setCurrentVersion(rs.getLong("versionnum"));
				switch (rs.getString("subtype")) {
				case "0":
					node.setNodeType("FOLDER");
					break;
				case "136":
					node.setNodeType("FOLDER");
					break;
				case "298":
					node.setNodeType("COLLECTION");
					break;
				case "749":
					node.setNodeType("EMAIL");
					break;
				case "144":
					node.setNodeType("FILE");
					break;
				default:
					node.setNodeType("OTHER");
					break;
				}
				list.add(node);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClean();
		}

		return list;
	}

	/*
	 * return all nodes under parent node, file or folder, not included attributes
	 */
	public static String[] getNode(long parentNodeId) {
		dbInit();

		String[] list = null;

		try {
			String sql = "select name from dtree where parentid=" + parentNodeId;
			rs = stmt.executeQuery(sql);
			list = new String[getRsSize(rs)];
			int i = 0;
			while (rs.next()) {
				list[i++] = Utils.escapeStr(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClean();
		}
		return list;
	}

	/*
	 * return result set size
	 */
	public static int getRsSize(ResultSet rs) throws SQLException {
		int cnt = 0;
		if (rs.last()) {
			cnt = rs.getRow();
			rs.beforeFirst();
		}
		return cnt;
	}

	public static void log(String log) {
		System.out.println(log);
	}

	public static void log(long log) {
		System.out.println(log);
	}

	public static String getTime() {
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS").format(new Date());
	}

}
