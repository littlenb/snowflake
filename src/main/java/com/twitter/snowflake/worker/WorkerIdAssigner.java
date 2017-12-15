package com.twitter.snowflake.worker;

/**
 * Represents a worker id assigner for {@link com.twitter.snowflake.sequence.IdSequence}
 * 
 * @author svili
 */
public interface WorkerIdAssigner {

    /**
     * Assign worker id for {@link com.twitter.snowflake.sequence.IdSequence}
     * 
     * @return assigned worker id
     */
    long assignWorkerId();

}
