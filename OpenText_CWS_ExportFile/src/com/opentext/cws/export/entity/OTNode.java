package com.opentext.cws.export.entity;

import com.opentext.livelink.service.docman.Node;

public class OTNode extends Node {
	long nodeID;
	String name;
	long parentNodeID;
	long currentVersion;
	String nodeType;
	String path;

	public OTNode(long nodeID, String name, long parentNodeID, long currentVersion, String nodeType) {
		super();
		this.nodeID = nodeID;
		this.name = name;
		this.parentNodeID = parentNodeID;
		this.currentVersion = currentVersion;
		this.nodeType = nodeType;
	}

	public OTNode() {
		// TODO Auto-generated constructor stub
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getNodeID() {
		return nodeID;
	}

	public void setNodeID(long nodeID) {
		this.nodeID = nodeID;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "OTNode [id=" + nodeID + ", name=" + name + ", parentNodeID=" + parentNodeID + ", currentVersion="
				+ currentVersion + ", nodeType=" + nodeType + " path=" + path + "]";
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getParentNodeID() {
		return parentNodeID;
	}

	public void setParentNodeID(long parentNodeID) {
		this.parentNodeID = parentNodeID;
	}

	public long getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(long currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

}
