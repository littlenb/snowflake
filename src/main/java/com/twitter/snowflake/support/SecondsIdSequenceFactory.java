package com.twitter.snowflake.support;

import java.util.concurrent.TimeUnit;

/**
 * The unique id has 64bits (long), default allocated as blow:<br>
 * <li>sign: The highest bit is 0
 * <li>delta seconds: The next 28 bits, represents delta seconds since a customer epoch(2017-01-01 00:00:00.000).
 *                    Supports about 8.7 years until to 2025-07-04 21:24:15.000
 * <li>worker id: The next 22 bits, represents the worker's id which assigns based on database, max id is about 420W
 * <li>sequence: The next 13 bits, represents a sequence within the same second, max for 8192/s<br><br>
 *
 * <pre>{@code
 * +------+----------------------+----------------+-----------+
 * | sign |     delta seconds    | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          28bits              22bits         13bits
 * }</pre>
 *
 * You can also specified the bits by setter method.
 *
 * @author svili
 **/
public class SecondsIdSequenceFactory extends ElasticIdSequenceFactory{

    public SecondsIdSequenceFactory() {
        super.timeBits = 28;
        super.workerBits = 22;
        super.seqBits = 13;
        // epoch seconds,default: 2017-01-01 (seconds:1483200000L)
        super.epochTimestamp = 1483200000L;
        super.timeUnit = TimeUnit.SECONDS;
    }
}
