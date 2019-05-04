package com.twitter.snowflake.sequence;

import com.twitter.snowflake.exception.SnowFlakeException;

/**
 * Allocate 64 bits for the Unique ID(long)<br>
 * sign (fixed 1bit) -> timestamp -> workerId -> sequence(within the same millisecond)
 *
 * @author svili
 */
public class BitsAllocator {

  /**
   * Total 64 bits
   */
  public static final int TOTAL_BITS = 1 << 6;

  /**
   * Bits for [sign-> timestamp-> workId-> sequence]
   */
  private int signBits = 1;
  private final int timestampBits;
  private final int workerIdBits;
  private final int sequenceBits;

  /**
   * Max value for workId & sequence
   */
  private final long maxDeltaTime;
  private final long maxWorkerId;
  private final long maxSequence;

  /**
   * Shift for timestamp & workerId
   */
  private final int timestampShift;
  private final int workerIdShift;

  /**
   * Constructor with timestampBits, workerIdBits, sequenceBits<br>
   * The highest bit used for sign,
   * so <code>63</code> bits for timestampBits, workerIdBits, sequenceBits
   */
  public BitsAllocator(int timestampBits, int workerIdBits, int sequenceBits) {
    // make sure allocated 64 bits
    int allocateTotalBits = signBits + timestampBits + workerIdBits + sequenceBits;
    if (allocateTotalBits != TOTAL_BITS) {
      throw new SnowFlakeException("allocate not enough 64 bits");
    }

    // initialize bits
    this.timestampBits = timestampBits;
    this.workerIdBits = workerIdBits;
    this.sequenceBits = sequenceBits;

    // initialize max value
    this.maxDeltaTime = ~(-1L << timestampBits);
    this.maxWorkerId = ~(-1L << workerIdBits);
    this.maxSequence = ~(-1L << sequenceBits);

    // initialize shift
    this.timestampShift = workerIdBits + sequenceBits;
    this.workerIdShift = sequenceBits;
  }


  /**
   * Allocate bits for ID according to delta timestamp & workerId & sequence</br>
   * <b>Note that:</b> The highest bit will always be 0 for sign
   */
  public long allocate(long deltaTime, long workerId, long sequence) {
    return (deltaTime << timestampShift) | (workerId << workerIdShift) | sequence;
  }

  /**
   * Getters
   */

  public int getSignBits() {
    return signBits;
  }

  public int getTimestampBits() {
    return timestampBits;
  }

  public int getWorkerIdBits() {
    return workerIdBits;
  }

  public int getSequenceBits() {
    return sequenceBits;
  }

  public long getMaxDeltaTime() {
    return maxDeltaTime;
  }

  public long getMaxWorkerId() {
    return maxWorkerId;
  }

  public long getMaxSequence() {
    return maxSequence;
  }

  public int getTimestampShift() {
    return timestampShift;
  }

  public int getWorkerIdShift() {
    return workerIdShift;
  }

}