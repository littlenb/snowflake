package com.twitter.snowflake.worker;

public class SimpleWorkerIdAssigner implements WorkerIdAssigner{
	
	private long workerId;
	
	public SimpleWorkerIdAssigner(long workerId){
		this.workerId = workerId;
	}

	@Override
	public long assignWorkerId() {
		return workerId;
	}

}
