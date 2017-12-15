package com.twitter.snowflake.support;

import com.twitter.snowflake.exception.SnowFlakeException;
import com.twitter.snowflake.sequence.IdSequence;
import com.twitter.snowflake.sequence.SnowFlakeSequence;
import com.twitter.snowflake.worker.WorkerIdAssigner;

/**
 * 
 * The unique id has 64bits (long), default allocated as blow:<br>
 * <li>sign: The highest bit is 0
 * <li>delta millisecond: The next 28 bits, represents delta seconds since a
 * customer epoch(2017-01-01 00:00:00.000). Supports about 69 years until to
 * 2086-01-01 00:00:00
 * <li>worker id: The next 10 bits, represents the worker's id, max id is about
 * 1023
 * <li>sequence: The next 12 bits, represents a sequence within the same
 * millisecond, max for 4095/ms<br>
 * <br>
 * 
 * <pre>
 * {@code
 * +------+----------------------+----------------+-----------+
 * | sign |  delta millisecond   | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          41bits              10bits         12bits
 * }
 * </pre>
 * 
 * @author svili
 *
 */
public class IdSequenceFactory {

	/** Bits allocate */
	private int timeBits = 41;
	private int workerBits = 10;
	private int seqBits = 12;

	/**
	 * epoch millisecond,default: 2017-01-01 (millisecond:1483200000000L)
	 */
	private long epochMillis = 1483200000000L;

	private Long workerId;

	/** worker id assigner */
	private WorkerIdAssigner workerIdAssigner;

	public IdSequenceFactory() {

	}

	public IdSequence create() {
		if (workerIdAssigner != null) {
			workerId = workerIdAssigner.assignWorkerId();
		}
		if (workerId == null) {
			throw new SnowFlakeException("worker id had not set.");
		}

		return new SnowFlakeSequence(timeBits, workerBits, seqBits, epochMillis, workerId);
	}

	/* setter */

	public void setTimeBits(int timeBits) {
		this.timeBits = timeBits;
	}

	public void setWorkerBits(int workerBits) {
		this.workerBits = workerBits;
	}

	public void setSeqBits(int seqBits) {
		this.seqBits = seqBits;
	}

	public void setEpochMillis(long epochMillis) {
		this.epochMillis = epochMillis;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	public void setWorkerIdAssigner(WorkerIdAssigner workerIdAssigner) {
		this.workerIdAssigner = workerIdAssigner;
	}

}
