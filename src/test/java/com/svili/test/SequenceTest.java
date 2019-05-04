package com.svili.test;

import com.twitter.snowflake.sequence.IdGenerator;
import com.twitter.snowflake.support.ElasticIdGeneratorFactory;
import com.twitter.snowflake.support.MillisIdGeneratorFactory;
import com.twitter.snowflake.support.SecondsIdGeneratorFactory;
import com.twitter.snowflake.worker.SimpleWorkerIdAssigner;
import com.twitter.snowflake.worker.WorkerIdAssigner;

import java.util.concurrent.TimeUnit;

public class SequenceTest {

    public static void main(String[] args) {

        final IdGenerator secondsIdGenerator = IdSequenceHolder.secondsIdGenerator;
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long id = secondsIdGenerator.nextId();
                    String jsonText = secondsIdGenerator.parse(id);
                    System.out.println("secondsIdGenerator: " + Thread.currentThread() + ",id: " + id + ",JsonText: " + jsonText);
                }
            });
             thread.start();
        }

        System.out.println("######################################################");

        final IdGenerator millisIdGenerator = IdSequenceHolder.millisIdGenerator;
        for (int i = 0; i < 100; i++) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long id = millisIdGenerator.nextId();
                    String jsonText = millisIdGenerator.parse(id);
                    System.out.println("millisIdGenerator: " + Thread.currentThread() + ",id: " + id + ",JsonText: " + jsonText);
                }
            });
//            thread.start();
        }

    }

    public static class IdSequenceHolder {

        private static IdGenerator secondsIdGenerator;

        private static IdGenerator millisIdGenerator;

        private static IdGenerator elasticIdGenerator;

        static {
            secondsIdGenerator = new SecondsIdGeneratorFactory(1483200000L).create(2L);
            millisIdGenerator = new MillisIdGeneratorFactory(1483200000000L).create(2L);

            ElasticIdGeneratorFactory elasticFactory = new ElasticIdGeneratorFactory();

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
            elasticIdGenerator = elasticFactory.create(workerIdAssigner);
        }

    }
}
