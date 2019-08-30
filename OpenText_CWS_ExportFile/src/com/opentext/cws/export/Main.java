package com.opentext.cws.export;

import java.util.List;

import com.opentext.cws.export.entity.AppConstant;
import com.opentext.cws.export.entity.OTNode;
import com.opentext.cws.export.service.DBService;
import com.opentext.cws.export.service.ExportService;
import com.opentext.cws.export.service.FileService;
import com.opentext.cws.export.service.Utils;

public class Main {

	public static void main(String[] args) throws Exception {

		System.out.println("starting...");

		List<Integer> folderIDList = Utils.readFolderList(AppConstant.readfolderList);

		for (int folderID : folderIDList) {
			OTNode rootNode = DBService.getNodeById(folderID);
			if (rootNode == null || rootNode.getName() == null)
				continue;
			String parentFolder = FileService.findParentFolder(folderID);
			if (!parentFolder.equals("Error")) {
				System.out.println("starting to export files under \\" + parentFolder + "\\" + rootNode.getName());
				ExportService service = new ExportService(
						AppConstant.outputFolder + parentFolder + "\\" + rootNode.getName() + "\\");
				service.initializeApp();
				service.exportAllChildNode(rootNode);
			}
		}
		System.out.println("ending...");
	}
}
