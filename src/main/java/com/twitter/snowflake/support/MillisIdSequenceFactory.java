package com.twitter.snowflake.support;

import java.util.concurrent.TimeUnit;

/**
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
 * <pre>{@code
 * +------+----------------------+----------------+-----------+
 * | sign |  delta millisecond   | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          41bits              10bits         12bits
 * }</pre>
 *
 * You can also specified the bits by setter method.
 *
 * @author svili
 */
public class MillisIdSequenceFactory extends ElasticIdSequenceFactory {

    public MillisIdSequenceFactory() {
        super.timeBits = 41;
        super.workerBits = 10;
        super.seqBits = 12;
        // epoch millisecond,default: 2017-01-01 (millisecond:1483200000000L)
        super.epochTimestamp = 1483200000000L;
        super.timeUnit = TimeUnit.MILLISECONDS;
    }
}
