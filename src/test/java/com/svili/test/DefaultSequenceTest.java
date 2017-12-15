package com.svili.test;

import com.twitter.snowflake.IdSequence;
import com.twitter.snowflake.support.DefaultIdSequence;

public class DefaultSequenceTest {

	public static void main(String[] args) {
		IdSequence sequence = SequenceHolder.SEQUENCE;
		for (int i = 0; i < 10; i++) {
			long uid = sequence.nextId();
			String jsonText = sequence.parse(uid);
			System.out.println(jsonText);
		}

	}

	
	private static class SequenceHolder{
		private static final long WORKER_ID = 1L;
		private static final IdSequence SEQUENCE = new DefaultIdSequence(WORKER_ID);
	}

}
