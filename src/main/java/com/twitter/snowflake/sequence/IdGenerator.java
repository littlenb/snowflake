package com.twitter.snowflake.sequence;

/**
 * Represents a unique id generator.
 *
 * @author svili
 */
public interface IdGenerator {

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
   * @return Parsed info
   */
  String parse(long id);

}
