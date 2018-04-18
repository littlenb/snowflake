package com.svili.test;

import com.twitter.snowflake.sequence.IdSequence;
import com.twitter.snowflake.support.ElasticIdSequenceFactory;
import com.twitter.snowflake.support.MillisIdSequenceFactory;
import com.twitter.snowflake.support.SecondsIdSequenceFactory;
import com.twitter.snowflake.worker.SimpleWorkerIdAssigner;
import com.twitter.snowflake.worker.WorkerIdAssigner;

import java.util.concurrent.TimeUnit;

public class SequenceTest {

    public static void main(String[] args) {


        IdSequence secondsIdSequence = IdSequenceHolder.secondsIdSequence;
        for (int i = 0; i < 10; i++) {
            long uid = secondsIdSequence.nextId();
            String jsonText = secondsIdSequence.parse(uid);
            System.out.println(jsonText);
        }
        System.out.println("######################################################");

        IdSequence millisIdSequence = IdSequenceHolder.millisIdSequence;
        for (int i = 0; i < 10; i++) {
            long uid = millisIdSequence.nextId();
            String jsonText = millisIdSequence.parse(uid);
            System.out.println(jsonText);
        }

    }

    public static class IdSequenceHolder {

        private static IdSequence secondsIdSequence;

        private static IdSequence millisIdSequence;

        private static IdSequence elasticIdSequence;

        static {
            secondsIdSequence = new SecondsIdSequenceFactory().create(2L);
            millisIdSequence = new MillisIdSequenceFactory().create(2L);

            ElasticIdSequenceFactory elasticFactory = new ElasticIdSequenceFactory();

            // TimeBits + WorkerBits + SeqBits = 64 -1
            elasticFactory.setTimeBits(41);
            elasticFactory.setWorkerBits(10);
            elasticFactory.setSeqBits(12);
            elasticFactory.setTimeUnit(TimeUnit.MILLISECONDS);
            elasticFactory.setEpochTimestamp(1483200000000L);

            // for complex , you can implements the WorkerIdAssigner to create worker id.
            // e.g. use the simple implement in here.
            WorkerIdAssigner workerIdAssigner = new SimpleWorkerIdAssigner(1L);
            // or set workerId directly
//            elasticSequence = elasticFactory.create(1L);
            elasticIdSequence = elasticFactory.create(workerIdAssigner);
        }

    }
}
