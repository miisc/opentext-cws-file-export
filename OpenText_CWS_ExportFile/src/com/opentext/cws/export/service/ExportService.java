package com.opentext.cws.export.service;

import java.io.IOException;

import com.opentext.cws.export.entity.AppConstant;
import com.opentext.cws.export.entity.OTNode;
import com.opentext.livelink.service.core.ContentService;
import com.opentext.livelink.service.docman.DocumentManagement;

public class ExportService {

	DocumentManagement docManagement;
	ContentService contentService;
	String path;

	public ExportService(String outputPath) {
		this.path = outputPath;
	}

	/*
	 * check db connect is valid or not
	 */
	public boolean initializeDBConnection() {
		return true;
	}

	public boolean initializeOTService() {
		String token;
		try {
			token = OTService.getAuthenticationToken(AppConstant.OTUsername, AppConstant.OTPassword);
			this.docManagement = OTService.getDMService(token);
			this.contentService = OTService.getCSService(token);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(AppConstant.OTUsername);
			System.out.println(AppConstant.OTPassword);

			e.printStackTrace();
		}
		return false;
	}

	// test file destination
	public boolean checkDestinationFolder() throws IOException {
		FileService.createFolder(this.path);
		return true;
	}

	public boolean initializeApp() throws Exception {
		if (initializeDBConnection() && initializeOTService() && checkDestinationFolder())
			return true;
		else
			return false;
	}

	public boolean exportAllChildNode(OTNode parentNode) {
		for (OTNode childNode : DBService.getChildNode(parentNode)) {
			String prePath = path;
			if (childNode.getNodeType().equals("FOLDER")) {
				path = path + childNode.getName() + "\\";
				childNode.setPath(path);
				FileService.createFolder(path);
			} else if (childNode.getNodeType().equals("FILE") || childNode.getNodeType().equals("EMAIL")) {
				childNode.setPath(path + childNode.getName());
				try {
					if ((FileService.downloadFile(docManagement, contentService, childNode)) == false) {
						System.out.println("failed to export file => " + path + "\\" + childNode.getName());
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			} else {
				System.out.println("this type " + childNode.getNodeType() + " can't be exported. File => " + path + "\\"
						+ childNode.getName());
				break;
			}

			exportAllChildNode(childNode);
			path = prePath;
		}
		return true;
	}
}
