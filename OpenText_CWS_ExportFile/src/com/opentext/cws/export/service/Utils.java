package com.opentext.cws.export.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

	public static List<Integer> readFolderList(String folderListTxt) {
		List<Integer> folders = new ArrayList<Integer>();

		try {
			System.out.println("starting to read export folder list...");
			FileReader rd = new FileReader(folderListTxt);
			BufferedReader br = new BufferedReader(rd);
			String ln;
			while ((ln = br.readLine()) != null) {
				folders.add(new Integer(ln));
			}
			System.out.println("ending to read export folder list...");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return folders;

	}
	
	public static String escapeStr(String str) {		
		return str.replace("\"", " ").replace("\\", " ").replace("/", " ");
	}
}
