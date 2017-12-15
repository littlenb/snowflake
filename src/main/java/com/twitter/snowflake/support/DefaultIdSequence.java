package com.twitter.snowflake.support;

import com.twitter.snowflake.IdSequence;
import com.twitter.snowflake.algorithm.SnowFlakeSequence;

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
public class DefaultIdSequence implements IdSequence {

	/** Default tits allocate */
	private static final int TIME_BITS = 41;
	private static final int WORKER_BITS = 10;
	private static final int SEQ_BITS = 12;

	private SnowFlakeSequence sequence;

	public DefaultIdSequence(long workerId) {
		/**
		 * default epoch: 2017-01-01 (millisecond:1483200000000L)
		 */
		long epochMillis = 1483200000000L;

		sequence = new SnowFlakeSequence(TIME_BITS, WORKER_BITS, SEQ_BITS, epochMillis, workerId);

	}

	public DefaultIdSequence(long workerId, long epochMillis) {

		sequence = new SnowFlakeSequence(TIME_BITS, WORKER_BITS, SEQ_BITS, epochMillis, workerId);
	}

	@Override
	public long nextId() {
		return sequence.nextId();
	}

	@Override
	public String parse(long id) {
		return sequence.parse(id);
	}

}
