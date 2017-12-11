# snowflake
Java code implements Twitter SnowFlake，generate unique ID for Long type(64 bits).

| sign | delta millisecond | worker node id  | sequence |
| ---- |:-----------------:|:---------------:| --------:|
| 1bit | 41bits            | 10bits          | 12bits   |


Reference Twitter SnowFlake and Baidu uid-generator

```Java
		long workerId = 1L;
		UidGenerator generator = UidGeneratorHolder.getGenerator(workerId);
		
		long uid = generator.getUID();
```