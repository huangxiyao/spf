package com.hp.it.cas.persona.loader;

/**
 * A bulk import facility for Persona user profile information.
 * 
 * @author Quintin May
 */
public interface IUserLoader {

	/**
	 * Imports user profile information from the specified files into Persona. Loading is handled transactionally, i.e.
	 * all user profile information is loaded or none is.
	 * 
	 * @param files
	 *            the files containing user profile information.
	 */
	void load(String... files);

	/**
	 * Validates the input data without loading it into the backing data store.
	 * 
	 * @param files
	 *            the files to validate.
	 */
	void validate(String... files);

	/**
	 * Returns the number of users for whom profile information was loaded.
	 * 
	 * @return the number of users for whom information was loaded. Returns zero if {@link #load(String...)} or
	 *         {@link #validate(String...)} has not yet been called.
	 */
	int getUserLoadCount();

	/**
	 * Returns the total number of user attribute values that were loaded.
	 * 
	 * @return the number of attribute values loaded. Returns zero if {@link #load(String...)} or
	 *         {@link #validate(String...)} has not yet been called.
	 */
	int getAttributeLoadCount();
}
