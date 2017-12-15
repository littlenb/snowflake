package com.twitter.snowflake.worker;

public class SimpleWorkerIdAssigner implements WorkerIdAssigner{

	@Override
	public long assignWorkerId() {
		return 0;
	}

}
