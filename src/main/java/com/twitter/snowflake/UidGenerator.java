package com.twitter.snowflake;

/**
 * Represents a unique id generator.
 *
 * @author svili
 */
public interface UidGenerator {

	/**
	 * Get a unique ID
	 *
	 * @return UID
	 * @throws SnowFlakeException
	 */
	long getUID();

	/**
	 * Parse the UID into elements which are used to generate the UID. <br>
	 * Such as timestamp & workerId & sequence...
	 *
	 * @param uid
	 * @return Parsed info
	 */
	String parseUID(long uid);

}
