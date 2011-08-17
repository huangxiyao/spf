package com.hp.spp.utility;
/**
 * @author kumarkro
 * @version 1.0
 * @date April 2, 2008
 */

import java.io.File;
import java.util.Date;
import org.apache.log4j.Logger;

public class ZipFileDeleteUtility {

    private static final Logger mLog = Logger.getLogger(ZipFileDeleteUtility.class);

    private static String mDirPath;
	private static long mNumberOfDays;

	public static void main(String[] args){

		if(args.length != 2){
			mLog.info("Insufficient Arguments, Syntax: >java ZipFileDeleteUtility <Path> <Number of Days>");
			return;
		}
		long numberOfDays=0;
		try{
			numberOfDays = Long.parseLong(args[1]);
		}catch(NumberFormatException nfe){
			mLog.error("Incorrect Arguments : Number of Days must be numeric");
		}
		deleteOldZipFiles(args[0], numberOfDays);
	}

	/**
	 * This method always returns a boolean value, whether or not the old zip files are deleted.
	 * The dirpath argument must specify an absolute directory path, where the old zip files are
	 * located.  The numberofdays argument is a specifier that indicates how old zip files needs
	 * to be removed.
	 * <p>
	 *
	 * @param  dirPath  an absolute path to the directory where the utility needs to clean the zip files.
	 * @param  numberOfDays is a specifier that indicates how old log files needs to be archived.
	 * @return a boolean value specifies successful execution.
	 */

	public static boolean deleteOldZipFiles(String dirPath, long numberOfDays){
		mDirPath = dirPath;
		mNumberOfDays = numberOfDays;

        File fileObj = new File(mDirPath);
		File[] files = fileObj.listFiles();
		for(int i=0; i<files.length; i++){
			String fileName = files[i].getName();
			if(fileName.endsWith("_archive.zip")){
				Date lastModifiedDate = new Date(files[i].lastModified());
				Date todayDate = new Date();
				long dateDiff=(todayDate.getTime()-lastModifiedDate.getTime())/(24 * 60 * 60 * 1000);
                if(dateDiff >= mNumberOfDays)
					files[i].delete();
			}
		}
		return true;
	}
}