package com.hp.spp.utility;

import junit.framework.TestCase;

import java.io.File;


public class FileArchivalUtilityTest extends TestCase {
	
	public void testArchiveFileOnWLInstances(){
        String tmpdir = System.getProperty("java.io.tmpdir");
        File ftemp2=new File(tmpdir+"ArchiveDir");
        String destFilePath=ftemp2.toString()+File.separator;
        if(!ftemp2.exists()){
			ftemp2.mkdirs();
		}
        //assertEquals(FileArchival.archiveFiles("C:/DOCUME~1/bondada/LOCALS~1/Temp/ArchiveDir1/",30,destFilePath),true);
        //assertEquals(FileArchival.deleteFiles(destFilePath),true);
        //assertEquals(FileArchivalUtility.archiveFiles("C:/log",2,"c:/log"), false);
		//assertEquals(FileArchivalUtility.archiveFiles("C:/logs",1,"c:/log"), false);
	}
}
