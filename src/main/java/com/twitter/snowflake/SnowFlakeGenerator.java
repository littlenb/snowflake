package com.twitter.snowflake;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents an implementation of {@link UidGenerator}
 *
 * The unique id has 64bits (long), default allocated as blow:<br>
 * <li>sign: The highest bit is 0
 * <li>delta millisecond: The next 28 bits, represents delta seconds since a
 * customer epoch(2017-01-01 00:00:00.000). Supports about 69 years until to
 * 2086-01-01 00:00:00
 * <li>worker id: The next 10 bits, represents the worker's id which assigns
 * based on database, max id is about 1023
 * <li>sequence: The next 12 bits, represents a sequence within the same
 * millisecond, max for 4095/ms<br>
 * <br>
 *
 * The {@link SnowFlakeGenerator#parseUID(long)} is a tool method to parse the
 * bits
 *
 * <pre>
 * {@code
 * +------+----------------------+----------------+-----------+
 * | sign |     delta seconds    | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          41bits              10bits         12bits
 * }
 * </pre>
 *
 *
 * @author svili
 */
public class SnowFlakeGenerator implements UidGenerator {

	/** Bits allocate */
	private int timeBits = 41;
	private int workerBits = 10;
	private int seqBits = 12;

	/**
	 * Customer epoch, unit as millisecond. For example 2017-01-01
	 * (ms:1483200000000L)
	 */
	private long epochMillis = 1483200000000L;

	/** Stable fields */
	private BitsAllocator bitsAllocator;
	private long workerId;

	/** Volatile fields caused by nextId() */
	private long sequence = 0L;
	private long lastMillis = -1L;

	SnowFlakeGenerator(long workerId) {
		// initialize bits allocator
		bitsAllocator = new BitsAllocator(timeBits, workerBits, seqBits);
		// initialize worker id
		if (workerId > bitsAllocator.getMaxWorkerId()) {
			throw new SnowFlakeException(
					"Worker id " + workerId + " exceeds the max " + bitsAllocator.getMaxWorkerId());
		}
		this.workerId = workerId;
	}

	@Override
	public long getUID() {
		return nextId();
	}

	@Override
	public String parseUID(long uid) {
		long totalBits = BitsAllocator.TOTAL_BITS;
		long signBits = bitsAllocator.getSignBits();
		long timestampBits = bitsAllocator.getTimestampBits();
		long workerIdBits = bitsAllocator.getWorkerIdBits();
		long sequenceBits = bitsAllocator.getSequenceBits();

		// parse UID
		long sequence = (uid << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
		long workerId = (uid << (timestampBits + signBits)) >>> (totalBits - workerIdBits);
		long deltaMillis = uid >>> (workerIdBits + sequenceBits);

		Date thatTime = new Date(epochMillis + deltaMillis);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String thatTimeStr = simpleDateFormat.format(thatTime);

		// format as string
		return String.format("{\"UID\":\"%d\",\"timestamp\":\"%s\",\"workerId\":\"%d\",\"sequence\":\"%d\"}", uid,
				thatTimeStr, workerId, sequence);
	}

	/**
	 * Get UID
	 *
	 * @return UID
	 * @throws UidGenerateException
	 *             in the case: Clock moved backwards; Exceeds the max timestamp
	 */
	private synchronized long nextId() {
		long currentMillis = getCurrentMillis();

		// Clock moved backwards, refuse to generate uid
		if (currentMillis < lastMillis) {
			long refusedSeconds = lastMillis - currentMillis;
			throw new SnowFlakeException("Clock moved backwards. Refusing for %d seconds", refusedSeconds);
		}

		// At the same second, increase sequence
		if (currentMillis == lastMillis) {
			sequence = (sequence + 1) & bitsAllocator.getMaxSequence();
			// Exceed the max sequence, we wait the next second to generate uid
			if (sequence == 0) {
				currentMillis = getNextMillis(lastMillis);
			}

			// At the different second, sequence restart from zero
		} else {
			sequence = 0L;
		}

		lastMillis = currentMillis;

		// Allocate bits for UID
		return bitsAllocator.allocate(currentMillis - epochMillis, workerId, sequence);
	}

	/**
	 * Get next millisecond
	 */
	private long getNextMillis(long lastTimestamp) {
		long timestamp = getCurrentMillis();
		while (timestamp <= lastTimestamp) {
			timestamp = getCurrentMillis();
		}

		return timestamp;
	}

	/**
	 * Get current second
	 */
	private long getCurrentMillis() {
		long currentMilli = System.currentTimeMillis();
		if (currentMilli - epochMillis > bitsAllocator.getMaxDeltaMillis()) {
			throw new SnowFlakeException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentMilli);
		}

		return currentMilli;
	}

}
