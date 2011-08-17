package com.hp.spp.utility;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;



/**
 * @author kumarkro
 * @version 1.0
 * @date April 2, 2008
 */
public class FileArchival {

    private static final Logger mLog = Logger.getLogger(FileArchivalUtility.class);

    private static String mDirPath;
	private static long mNumberOfDays;
    private static String mOutputPath;
    private static ZipOutputStream mOut;
    private static boolean mMinOneEntry;
    private static final String COMPAR_DATE = "yyyy_MM_dd_hh_mm_ss";
    private static final String COMPAR_OUT = "[A-z]*\\.out[0-9]*";
    private static final String COMPAR_LOG = "[A-z]*\\.log[0-9]*";
    private static final String COMPAR_TEXT = "[0-9]*_[0-9]*\\.txt";


    public static void main(String[] args){

		if(args.length != 3){
			mLog.info("Insufficient Arguments, Syntax: >java FileArchivalUtility <Path> <Number of Days> <zipfile output folder path>");
			return;
		}

		try{
			long numberOfDays  = Long.parseLong(args[1]);
			archiveFiles(args[0], numberOfDays, args[2]);

		}catch(NumberFormatException nfe){
			mLog.error("Incorrect Arguments : Number of Days must be numeric type");
		}
	}

	/**
	 * This method always returns a boolean value, whether or not the old log files are there to archive.
	 * The dirpath argument must specify an absolute directory path, Utility will recursively run
	 * thru all the sub-directories and clean the logs by archiving them. The numberOfDays argument
	 * is a specifier that indicates how old log files needs to be archived. And the outputpath
	 * argument indicates where the output zip file needs to be stored.
	 * <p>
	 *
	 * @param  dirPath  an absolute path to the directory where the utility needs to clean the logs recursively.
	 * @param  numberOfDays is a specifier that indicates how old log files needs to be archived.
	 * @param  outPutPath where to store the archived zip file.
	 * @return a boolean value specifies successful execution.
	 */

	public static boolean archiveFiles(String dirPath, long numberOfDays, String outPutPath){
		//convert directory path to File Object
		SimpleDateFormat simpleDataFormat = new SimpleDateFormat(COMPAR_DATE);
		String outputFileName = simpleDataFormat.format(new Date())+"_archive";
        //String outputFileName = new Date()+"_archive";
        mDirPath = dirPath;
		mOutputPath = outPutPath;
        mNumberOfDays= numberOfDays;
        try{
			mOut = new ZipOutputStream(new FileOutputStream(mOutputPath + File.separator+ outputFileName + ".zip"));
			File fileObj = new File(mDirPath);
		    //check fileObj is null or not a directory
		    if(!fileObj.isDirectory()){
		          mLog.info("Please Enter a valid directory path");
		          return false;
		    }
		    else{
		    	recurseDirectories(fileObj);
		    }
		    //if there are no entries in zip file, it will throw exception
		    if(mMinOneEntry){
		    	mOut.close();
            }
            else{
		    	mOut = null;
		    	mLog.info("No old log files to Zip");
		    	return false;
		    }

		}catch(Exception ioe){
			mLog.error(ioe.getMessage(),ioe);
			return false;
		}
	    return true;
	}

	private static void recurseDirectories(File fileObj) throws IOException{
		File[] fileObjs = fileObj.listFiles();

		for(int i = 0; i < fileObjs.length; i++){
			if(fileObjs[i].isDirectory()){
				recurseDirectories(fileObjs[i]);
			}
            else{
				if(fileObjs[i].isFile()){
					processFile(fileObjs[i]);
                }
            }
		}
	}

    private static void processFile(File file) throws IOException{
        String fileName = file.getName();
        if (Pattern.compile(COMPAR_LOG).matcher(fileName).find() || Pattern.compile(COMPAR_TEXT).matcher(fileName).find()||Pattern.compile(COMPAR_OUT).matcher(fileName).find()) {
            processAutoCleanup(file);
        }
    }


	private static void processAutoCleanup(File file) throws IOException{
		long lastModified = file.lastModified();
		Date lastModifiedDate = new Date(lastModified);
		Date todayDate = new Date();
        long dateDiff=(todayDate.getTime()-lastModifiedDate.getTime())/(24 * 60 * 60 * 1000);
        if(dateDiff >= mNumberOfDays){
			createZip(file,lastModified);
		}
	}

    private static void createZip(File file,long lastModified) throws IOException
    {
        try {
           // Add ZIP entry to output stream.
           mOut.putNextEntry(new ZipEntry(file.getAbsolutePath()));
           mMinOneEntry = true;
           // Transfer bytes from the file to the ZIP file
           FileInputStream in = new FileInputStream(file.getAbsolutePath());
           byte[] bytes = new byte[4096];
		   int bytesRead = 0;
		   while ((bytesRead = in.read(bytes)) != -1) {
			mOut.write(bytes, 0, bytesRead);
		   }
           // Complete the entry
           mOut.closeEntry();
           in.close();
            file.setLastModified(lastModified);

        } catch (IOException e) {
            mLog.error(e);
        }
    }
    public static boolean deleteFiles(String desPath){
        String path=desPath;
        File destPath=new File(path);
        String[] files=destPath.list();

        for(int i=0;i<files.length;i++){
            String destName=destPath+File.separator+files[i];
            File deletFile=new File(destName);
            deletFile.delete();
        }
        destPath.delete();
        return true;
    }
}
