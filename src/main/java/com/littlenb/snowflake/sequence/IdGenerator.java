package com.littlenb.snowflake.sequence;

/**
 * Represents a unique id generator.
 *
 * @author sway.li
 */
public interface IdGenerator {

  /**
   * Generate a unique id
   *
   * @return id
   */
  long nextId();

  /**
   * Generate unique id
   *
   */
  long[] nextSegment(int size);

  /**
   * Parse the ID into elements which are used to generate the ID. <br>
   * Such as timestamp & workerId & sequence...
   *
   * @return Parsed info
   */
  String parse(long id);

}
