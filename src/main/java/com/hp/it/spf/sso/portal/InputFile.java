package com.hp.it.spf.sso.portal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator reading through the provided input file and wrapping the file content in {@link AnonymousUserData}
 * object.
 * <p>
 * The input file can be comma/tab/semi-colon separated file with each entry
 * represnted by a single line and having the following columns:
 * <ul>
 * <li>language - 2-letter language code</li>
 * <li>country - 2-letter country code</li>
 * <li>time zone - String representation of the timezone</li>
 * </ul>
 * If the file contains more columns, they will be ignored. Empty lines and lines starting with
 * "#" are also ignored.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
class InputFile implements Iterator<AnonymousUserData> {

	private static final int IDX_LANGUAGE = 0;
	private static final int IDX_COUNTRY = 1;
	private static final int IDX_TIMEZONE = 2;

	/**
	 * Reader representing the input file.
	 */
	private BufferedReader mReader;

	/**
	 * Flag indicating whether the reading process has already started. It's changed only once -
	 * in {@link #hasNext()} method.
	 */
	private boolean mStartedReading = false;

	/**
	 * Next line of input of <tt>null</tt> if the reading has not started yet ({@link #mStartedReading}
	 * is null) or the end of file has been reached.
	 */
	private String mNextLine = null;


	public InputFile(String inputFilePath) throws FileNotFoundException {
		this(new BufferedReader(new FileReader(inputFilePath)));
	}

	InputFile(BufferedReader reader) {
		mReader = reader;
	}

	/**
	 * @return <tt>true</tt> if the input file has more non-empty and non-comment lines. Note
	 * that this does not mean that it has more {@link AnonymousUserData} entries as the file
	 * may contain error lines.
	 *
	 * @throws FileReadException If an error occurred while reading the input file.
	 */
	public boolean hasNext() throws FileReadException {
		if (!mStartedReading) {
			try {
				mNextLine = loadNextNonEmptyOrCommentLine();
			}
			catch (IOException e) {
				throw new FileReadException("Error reading file", e);
			}
			mStartedReading = true;
		}
		return mNextLine != null;
	}

	/**
	 * @return Next successfully {@link AnonymousUserData} object.
	 * @throws NoSuchElementException If there is no more valid data to read.
	 * @throws FileReadException If an error occurs while reading the input file.
	 * @throws IllegalArgumentException If incorrectly formatted line was found in the input file.
	 * Note that a call to this method after an error occurs will read next entry from the input file
	 * and may yield correct results if the data in the file is correct.
	 */
	public AnonymousUserData next() throws NoSuchElementException, FileReadException, IllegalArgumentException {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		AnonymousUserData result = null;
		try {
			result = loadNext();
		}
		catch (IOException e) {
			throw new FileReadException("Error reading file", e);
		}
		if (result == null) {
			try {
				mReader.close();
			} catch (IOException ex) {
				throw new FileReadException("Error closing file.", ex);
			}
		}
		return result;
	}

	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Not supported.");
	}

	/**
	 * @return Next non-empty and non-comment line read from the input file or <tt>null</tt>
	 * if the end of the file has been reached.
	 * @throws IOException If an error occurs when reading data from the file
	 */
	private String loadNextNonEmptyOrCommentLine() throws IOException
	{
		String line = null;
		while ((line = mReader.readLine()) != null) {
			line = line.trim();
			if (!line.startsWith("#") && !line.equals("")) {
				return line;
			}
		}
		return line;
	}

	/**
	 * @return Next valid user based on the data read from the file. This method never returns
	 * <tt>null</tt> - it fails with IllegalArgumentException if the input data is invalid.
	 * @throws IllegalArgumentException If an input data is invalid
	 * @throws IOException If an error occurred while reading input file
	 */
	private AnonymousUserData loadNext() throws IllegalArgumentException, IOException
	{
		try {
			String[] fields = mNextLine.trim().split("\t|;|,");
			if (fields == null || fields.length < 3 ||
					fields[IDX_LANGUAGE] == null || fields[IDX_LANGUAGE].trim().equals("") ||
					fields[IDX_TIMEZONE] == null || fields[IDX_TIMEZONE].trim().equals(""))
			{
				throw new IllegalArgumentException("Line format is incorrect: " + mNextLine);
			} else {
				return new AnonymousUserData(fields[IDX_LANGUAGE], fields[IDX_COUNTRY], fields[IDX_TIMEZONE]);
			}
		}
		finally {
			mNextLine = loadNextNonEmptyOrCommentLine();
		}
	}

}
