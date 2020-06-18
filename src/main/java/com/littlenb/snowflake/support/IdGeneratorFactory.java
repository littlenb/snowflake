package com.littlenb.snowflake.support;

import com.littlenb.snowflake.worker.WorkerIdAssigner;
import com.littlenb.snowflake.sequence.IdGenerator;

/**
 * @author sway.li
 */
public interface IdGeneratorFactory {

    IdGenerator create(WorkerIdAssigner workerIdAssigner);

    IdGenerator create(long workerId);

}
