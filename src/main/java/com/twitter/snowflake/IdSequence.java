package com.twitter.snowflake;

/**
 * Represents a unique id generator.
 *
 * @author svili
 */
public interface IdSequence {

	/**
	 * Generate a unique id
	 *
	 * @return id
	 */
	long nextId();
	
	/**
	 * Parse the ID into elements which are used to generate the ID. <br>
	 * Such as timestamp & workerId & sequence...
	 *
	 * @param id
	 * @return Parsed info
	 */
	String parse(long id);

}
