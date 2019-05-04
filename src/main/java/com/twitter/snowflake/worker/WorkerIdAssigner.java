package com.twitter.snowflake.worker;

import com.twitter.snowflake.sequence.IdGenerator;

/**
 * Represents a worker id assigner for {@link IdGenerator}
 * 
 * @author svili
 */
public interface WorkerIdAssigner {

    /**
     * Assign worker id for {@link IdGenerator}
     * 
     * @return assigned worker id
     */
    long assignWorkerId();

}
