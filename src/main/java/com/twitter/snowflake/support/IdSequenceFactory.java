package com.twitter.snowflake.support;

import com.twitter.snowflake.sequence.IdSequence;
import com.twitter.snowflake.worker.WorkerIdAssigner;

/**
 * @author svili
 */
public interface IdSequenceFactory {

    IdSequence create(WorkerIdAssigner workerIdAssigner);

    IdSequence create(long workerId);

}
