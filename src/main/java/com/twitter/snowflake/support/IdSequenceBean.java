package com.twitter.snowflake.support;

import org.springframework.beans.factory.InitializingBean;

import com.twitter.snowflake.IdSequence;
import com.twitter.snowflake.algorithm.SnowFlakeSequence;
import com.twitter.snowflake.worker.WorkerIdAssigner;

public class IdSequenceBean implements IdSequence, InitializingBean {

	/** Bits allocate */
	private int timeBits = 41;
	private int workerBits = 10;
	private int seqBits = 12;

	/**
	 * epoch millisecond,default: 2017-01-01 (millisecond:1483200000000L)
	 */
	private long epochMillis = 1483200000000L;

	/** Spring property */
	private WorkerIdAssigner workerIdAssigner;

	private SnowFlakeSequence sequence;

	@Override
	public void afterPropertiesSet() throws Exception {
		long workerId = workerIdAssigner.assignWorkerId();

		sequence = new SnowFlakeSequence(timeBits, workerBits, seqBits, epochMillis, workerId);
	}

	@Override
	public long nextId() {
		return sequence.nextId();
	}

	@Override
	public String parse(long id) {
		return sequence.parse(id);
	}

	/**
	 * Setters for spring property
	 */
	public void setTimeBits(int timeBits) {
		this.timeBits = timeBits;
	}

	public void setWorkerBits(int workerBits) {
		this.workerBits = workerBits;
	}

	public void setSeqBits(int seqBits) {
		this.seqBits = seqBits;
	}

	public void setEpochMillis(long epochMillis) {
		this.epochMillis = epochMillis;
	}

	public void setWorkerIdAssigner(WorkerIdAssigner workerIdAssigner) {
		this.workerIdAssigner = workerIdAssigner;
	}

}
