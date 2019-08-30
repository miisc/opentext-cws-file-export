package com.opentext.cws.export.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.xml.ws.soap.SOAPFaultException;

import com.opentext.cws.export.entity.OTNode;
import com.opentext.livelink.service.core.ContentService;
import com.opentext.livelink.service.docman.DocumentManagement;

public class FileService {

	public static String findParentFolder(int folderID) {
		OTNode curNode = DBService.getNodeById(folderID);
		if (curNode == null)
			return "Error";
		String path = "";
		while (curNode.getParentNodeID() > 0) {
			OTNode parentNode = DBService.getNodeById(curNode.getParentNodeID());
			if (parentNode == null) {
				path = "Error";
				break;
			}
			path = parentNode.getName() + "\\" + path;
			curNode = parentNode;
		}
		System.out.println("parent folder =>" + path);
		return path;
	}

	public static boolean createFolder(String folderName) {		
		File folder = new File(folderName);
		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdirs();
			System.out.println("created folder : " + folderName);
			return true;
		} else
			return false;
	}

	public static boolean downloadFile(DocumentManagement dm, ContentService cs, OTNode node)
			throws InterruptedException {
		FileOutputStream fos = null;
		File file = new File(node.getPath());
		try {
			String ctx = dm.getVersionContentsContext(node.getNodeID(), node.getCurrentVersion());
			if (!file.exists()) {
				fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				DataHandler st = cs.downloadContent(ctx);
				st.writeTo(bos);
				bos.flush();
				bos.close();
				System.out.println("created file : " + node.getPath());
			}
		} catch (SOAPFaultException e) {
			System.out.println(node.toString());
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
