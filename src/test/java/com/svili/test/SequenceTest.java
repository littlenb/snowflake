package com.svili.test;

import com.twitter.snowflake.sequence.IdSequence;
import com.twitter.snowflake.support.IdSequenceFactory;
import com.twitter.snowflake.worker.SimpleWorkerIdAssigner;
import com.twitter.snowflake.worker.WorkerIdAssigner;

public class SequenceTest {

	public static void main(String[] args) {

		IdSequence sequence1 = SequenceHolder.sequence1;
		for (int i = 0; i < 10; i++) {
			long uid = sequence1.nextId();
			String jsonText = sequence1.parse(uid);
			System.out.println(jsonText);
		}

		IdSequence sequence2 = SequenceHolder.sequence2;
		for (int i = 0; i < 10; i++) {
			long uid = sequence2.nextId();
			String jsonText = sequence2.parse(uid);
			System.out.println(jsonText);
		}

	}

	public static class SequenceHolder {

		private static IdSequence sequence1 = null;
		private static IdSequence sequence2 = null;
		static {
			IdSequenceFactory defaultFactory = new IdSequenceFactory();
			// there are two ways to set worker id

			// for simple
			defaultFactory.setWorkerId(1L);

			// for complex , you can implements the WorkerIdAssigner to create
			// worker id.
			// e.g. use the simple implement in here.
			WorkerIdAssigner workerIdAssigner = new SimpleWorkerIdAssigner(1L);
			defaultFactory.setWorkerIdAssigner(workerIdAssigner);
			sequence1 = defaultFactory.create();

			// set custom options : TimeBits WorkerBits SeqBits
			// attention : TimeBits + WorkerBits + SeqBits = 64 -1
			IdSequenceFactory customFactory = new IdSequenceFactory();

			// be careful of modify the time length
			// it should be 41 bits at least
			customFactory.setTimeBits(41);
			customFactory.setWorkerBits(5);
			customFactory.setSeqBits(17);

			// set epoch time
			customFactory.setEpochMillis(1483200000000L);
			// set worker id
			customFactory.setWorkerId(2L);
			sequence2 = customFactory.create();
		}
	}

}
