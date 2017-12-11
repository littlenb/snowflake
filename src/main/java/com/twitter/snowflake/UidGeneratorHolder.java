package com.twitter.snowflake;

import java.util.concurrent.ConcurrentHashMap;

public class UidGeneratorHolder {

	private static ConcurrentHashMap<Long, SnowFlakeGenerator> generators = new ConcurrentHashMap<>();

	public static UidGenerator getGenerator(long workerId) {
		if (generators.containsKey(workerId)) {
			return generators.get(workerId);
		}

		SnowFlakeGenerator generator = new SnowFlakeGenerator(workerId);
		generators.put(workerId, generator);
		return generator;
	}

}
