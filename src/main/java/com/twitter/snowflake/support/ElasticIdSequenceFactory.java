package com.twitter.snowflake.support;

import com.twitter.snowflake.sequence.IdSequence;
import com.twitter.snowflake.sequence.SnowFlakeSequence;
import com.twitter.snowflake.worker.WorkerIdAssigner;

import java.util.concurrent.TimeUnit;

/**
 * <pre>{@code
 * +------+----------------------+----------------+-----------+
 * | sign |  delta millisecond   | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit       timeBits             workerBits      seqBits
 * }</pre>
 *
 * <b>Note that:</b> The total bits must be 64 -1
 *
 * @author svili
 **/
public class ElasticIdSequenceFactory implements IdSequenceFactory {

    /**
     * Bits allocate
     */
    protected int timeBits;
    protected int workerBits;
    protected int seqBits;

    /**
     * epoch timestamp
     */
    protected long epochTimestamp;

    protected TimeUnit timeUnit;

    public ElasticIdSequenceFactory() {
    }

    @Override
    public IdSequence create(WorkerIdAssigner workerIdAssigner) {
        return create(workerIdAssigner.assignWorkerId());
    }

    @Override
    public IdSequence create(long workerId) {
        return new SnowFlakeSequence(timeUnit, timeBits, workerBits, seqBits, epochTimestamp, workerId);
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

    public void setEpochTimestamp(long epochTimestamp) {
        this.epochTimestamp = epochTimestamp;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

}
