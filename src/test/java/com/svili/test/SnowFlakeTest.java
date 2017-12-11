package com.svili.test;

import com.twitter.snowflake.UidGenerator;
import com.twitter.snowflake.UidGeneratorHolder;

public class SnowFlakeTest {

	public static void main(String[] args) {
		UidGenerator generator = UidGeneratorHolder.getGenerator(1L);
		for (int i = 0; i < 10; i++) {
			long uid = generator.getUID();
			System.out.println(uid);
		}

	}

}
