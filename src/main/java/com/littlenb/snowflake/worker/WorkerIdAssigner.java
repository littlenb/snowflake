package com.littlenb.snowflake.worker;

import com.littlenb.snowflake.sequence.IdGenerator;

/**
 * Represents a worker id assigner for {@link IdGenerator}
 * 
 * @author sway.li
 */
public interface WorkerIdAssigner {

    /**
     * Assign worker id for {@link IdGenerator}
     * 
     * @return assigned worker id
     */
    long assignWorkerId();

}
