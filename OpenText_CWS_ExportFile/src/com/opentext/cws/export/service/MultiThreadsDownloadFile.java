package com.opentext.cws.export.service;

import java.util.concurrent.Callable;

import com.opentext.cws.export.entity.OTNode;
import com.opentext.livelink.service.core.ContentService;
import com.opentext.livelink.service.docman.DocumentManagement;

public class MultiThreadsDownloadFile implements Callable<Integer> {
	private String rootFolder;
	private DocumentManagement dm;
	private ContentService cs;
	private OTNode node;

	public MultiThreadsDownloadFile(DocumentManagement dm, ContentService cs, OTNode node, String rootFolder) {
		this.rootFolder = rootFolder;
		this.dm = dm;
		this.cs = cs;
		this.node = node;
	}

	@Override
	public Integer call() throws Exception {
		if (node != null && dm != null && cs != null && rootFolder != null) {			
			FileService.createFolder(node.getPath());
			FileService.downloadFile(dm, cs, node);
			return 1;
		}
		return 0;
	}

}
