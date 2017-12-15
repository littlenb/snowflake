package com.twitter.snowflake.algorithm;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.twitter.snowflake.exception.SnowFlakeException;

/***
 * The unique id has 64bits (long)
 * 
 * <pre>
 * {@code
 * +------+----------------------+----------------+-----------+
 * | sign |  delta millisecond   | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit        timeBits            workerBits      seqBits
 * }
 * </pre>
 * 
 * <b>Note that:</b> The total bits must be 64
 * 
 * <ul>
 * <li>sign: The highest bit is 0
 * <li>delta millisecond:
 * <li>worker id:
 * <li>sequence:
 * 
 * @author svili
 *
 */
public class SnowFlakeSequence {

	private long epochMillis;
	private long workerId;

	/** Stable fields */
	private BitsAllocator bitsAllocator;

	/** Volatile fields caused by nextId() */
	private long sequence = 0L;
	private long lastMillis = -1L;

	/*
	 * public SnowFlakeSequence(int timeBits, int workerBits, int seqBits) { //
	 * initialize bits allocator bitsAllocator = new BitsAllocator(timeBits,
	 * workerBits, seqBits); }
	 */

	public SnowFlakeSequence(int timeBits, int workerBits, int seqBits, long epochMillis, long workerId) {
		// initialize bits allocator
		bitsAllocator = new BitsAllocator(timeBits, workerBits, seqBits);
		// initialize worker id
		if (workerId > bitsAllocator.getMaxWorkerId()) {
			throw new SnowFlakeException(
					"Worker id " + workerId + " exceeds the max " + bitsAllocator.getMaxWorkerId());
		}
		this.workerId = workerId;

		// initialize epoch , unit as millisecond.
		if (epochMillis > bitsAllocator.getMaxDeltaMillis()) {
			throw new SnowFlakeException(
					"epoch millisecond " + epochMillis + " exceeds the max " + bitsAllocator.getMaxDeltaMillis());
		}
		this.epochMillis = epochMillis;
	}

	/**
	 * Generate unique id
	 *
	 * @return id
	 * @throws SnowFlakeGenerateException
	 *             in the case: Clock moved backwards; Exceeds the max timestamp
	 */
	public synchronized long nextId() {
		long currentMillis = getCurrentMillis();

		// Clock moved backwards, refuse to generate id
		if (currentMillis < lastMillis) {
			long refusedSeconds = lastMillis - currentMillis;
			throw new SnowFlakeException("Clock moved backwards. Refusing for %d seconds", refusedSeconds);
		}

		// At the same second, increase sequence
		if (currentMillis == lastMillis) {
			sequence = (sequence + 1) & bitsAllocator.getMaxSequence();
			// Exceed the max sequence, we wait the next second to generate id
			if (sequence == 0) {
				currentMillis = getNextMillis(lastMillis);
			}

			// At the different second, sequence restart from zero
		} else {
			sequence = 0L;
		}

		lastMillis = currentMillis;

		// Allocate bits for ID
		return bitsAllocator.allocate(currentMillis - epochMillis, workerId, sequence);
	}

	public String parse(long id) {
		long totalBits = BitsAllocator.TOTAL_BITS;
		long signBits = bitsAllocator.getSignBits();
		long timestampBits = bitsAllocator.getTimestampBits();
		long workerIdBits = bitsAllocator.getWorkerIdBits();
		long sequenceBits = bitsAllocator.getSequenceBits();

		// parse id
		long sequence = (id << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
		long workerId = (id << (timestampBits + signBits)) >>> (totalBits - workerIdBits);
		long deltaMillis = id >>> (workerIdBits + sequenceBits);

		Date thatTime = new Date(epochMillis + deltaMillis);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String thatTimeStr = simpleDateFormat.format(thatTime);

		// format as string
		return String.format("{\"id\":\"%d\",\"timestamp\":\"%s\",\"workerId\":\"%d\",\"sequence\":\"%d\"}", id,
				thatTimeStr, workerId, sequence);
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
