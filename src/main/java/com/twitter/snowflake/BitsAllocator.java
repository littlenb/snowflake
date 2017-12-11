package com.twitter.snowflake;

/**
 * Allocate 64 bits for the UID(long)<br>
 * sign (fixed 1bit) -> millisecond -> workerId -> sequence(within the same
 * millisecond)
 * 
 * @author svili
 */
public class BitsAllocator {
	/**
	 * Total 64 bits
	 */
	public static final int TOTAL_BITS = 1 << 6;

	/**
	 * Bits for [sign-> millisecond-> workId-> sequence]
	 */
	private int signBits = 1;
	private final int timestampBits;
	private final int workerIdBits;
	private final int sequenceBits;

	/**
	 * Max value for workId & sequence
	 */
	private final long maxDeltaMillis;
	private final long maxWorkerId;
	private final long maxSequence;

	/**
	 * Shift for timestamp & workerId
	 */
	private final int timestampShift;
	private final int workerIdShift;

	/**
	 * Constructor with timestampBits, workerIdBits, sequenceBits<br>
	 * The highest bit used for sign, so <code>63</code> bits for timestampBits,
	 * workerIdBits, sequenceBits
	 */
	public BitsAllocator(int timestampBits, int workerIdBits, int sequenceBits) {
		// make sure allocated 64 bits
		int allocateTotalBits = signBits + timestampBits + workerIdBits + sequenceBits;
		if (allocateTotalBits != TOTAL_BITS) {
			throw new RuntimeException("allocate not enough 64 bits");
		}

		// initialize bits
		this.timestampBits = timestampBits;
		this.workerIdBits = workerIdBits;
		this.sequenceBits = sequenceBits;

		// initialize max value
		this.maxDeltaMillis = ~(-1L << timestampBits);
		this.maxWorkerId = ~(-1L << workerIdBits);
		this.maxSequence = ~(-1L << sequenceBits);

		// initialize shift
		this.timestampShift = workerIdBits + sequenceBits;
		this.workerIdShift = sequenceBits;
	}

	/**
	 * Allocate bits for UID according to delta millisecond & workerId &
	 * sequence<br>
	 * <b>Note that: </b>The highest bit will always be 0 for sign
	 * 
	 * @param deltaMillis
	 * @param workerId
	 * @param sequence
	 * @return
	 */
	public long allocate(long deltaMillis, long workerId, long sequence) {
		return (deltaMillis << timestampShift) | (workerId << workerIdShift) | sequence;
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

	public long getMaxDeltaMillis() {
		return maxDeltaMillis;
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