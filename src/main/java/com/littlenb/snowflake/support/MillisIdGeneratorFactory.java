package com.littlenb.snowflake.support;

import java.util.concurrent.TimeUnit;

/**
 * The unique id has 64bits (long), default allocated as blow:
 * <li>sign: The highest bit is 0
 * <li>delta millisecond: The next 28 bits, represents delta seconds since a
 * customer epoch(2017-01-01 00:00:00.000). Supports about 69 years until to
 * 2086-01-01 00:00:00<br>
 * <li>worker id: The next 10 bits, represents the worker's id, max id is about
 * 1023<br>
 * <li>sequence: The next 12 bits, represents a sequence within the same
 * millisecond, max for 4095/ms<br>
 * <br>
 * <pre>{@code
 * +------+----------------------+----------------+-----------+
 * | sign |  delta millisecond   | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          41bits              10bits         12bits
 * }</pre>
 *
 * You can also specified the bits by setter method.
 *
 * @author sway.li
 */
public class MillisIdGeneratorFactory extends ElasticIdGeneratorFactory {

    public MillisIdGeneratorFactory(long epochTimestamp) {
        super.timeBits = 41;
        super.workerBits = 10;
        super.seqBits = 12;
        // epoch millisecond
        super.epochTimestamp = epochTimestamp;
        super.timeUnit = TimeUnit.MILLISECONDS;
    }
}
