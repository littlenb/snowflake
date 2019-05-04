package com.twitter.snowflake.support;

import com.twitter.snowflake.sequence.IdGenerator;
import com.twitter.snowflake.worker.WorkerIdAssigner;

/**
 * @author svili
 */
public interface IdGeneratorFactory {

    IdGenerator create(WorkerIdAssigner workerIdAssigner);

    IdGenerator create(long workerId);

}
