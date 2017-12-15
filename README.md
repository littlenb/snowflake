# snowflake

[![JDK 1.7](https://img.shields.io/badge/JDK-1.7-green.svg "JDK 1.7")]()

Java code implements Twitter SnowFlake , generate unique ID for Long type(64 bits).

*Twitter SnowFlake算法(Java)，生产有序的数值id。*

| sign | delta millisecond | worker node id  | sequence |
| ---- |:-----------------:|:---------------:| --------:|
| 1bit | 41bits            | 10bits          | 12bits   |


Reference Twitter SnowFlake and Baidu uid-generator.

*参考Twitter SnowFlake 和  百度  uid-generator.*

## Quick Start

### Add maven dependency

```xml
<dependency>
    <groupId>com.github.cnsvili</groupId>
    <artifactId>snowflake</artifactId>
    <version>1.0.0</version>
</dependency>
```

### generate unique Id

There are provider two ways to use IdSequence : use default options or custom options.

*提供2种方式使用IdSequence : 使用默认的配置参数 或者  自定义参数*

But in any case,you must be set worker id.

*但是无论哪种方式，都需要设置worker id*

#### set worker id

There are two ways to set worker id.

```Java
    // create sequence factory
	IdSequenceFactory defaultFactory = new IdSequenceFactory();
	
	// two ways to set worker id

	// 1 for simple , use setter method
	defaultFactory.setWorkerId(1L);

	// 2 for complex , you can implements the Interface of the WorkerIdAssigner.
	
	// use the simple implement in here.
	WorkerIdAssigner workerIdAssigner = new SimpleWorkerIdAssigner(1L);
	
	defaultFactory.setWorkerIdAssigner(workerIdAssigner);
	
```

#### use default options

```Java
    // 1. create sequence factory
	IdSequenceFactory defaultFactory = new IdSequenceFactory();
	
	// 2. set worker id
	defaultFactory.setWorkerId(1L);
	
	// 3. create sequence
	IdSequence sequence = defaultFactory.create();
	
	// 4. generate id
	long uid = sequence.nextId();
	//126364026828492800
```

#### custom options

```Java

	// 1. create sequence factory
	IdSequenceFactory customFactory = new IdSequenceFactory();
	
	// 2. set custom options : TimeBits WorkerBits SeqBits
	// attention : TimeBits + WorkerBits + SeqBits = 64 -1

	// be careful of modify the time length
	// it should be 41 bits at least
	customFactory.setTimeBits(41);
	customFactory.setWorkerBits(5);
	customFactory.setSeqBits(17);

	
	// set epoch time
	customFactory.setEpochMillis(1483200000000L);
	// set worker id
	customFactory.setWorkerId(2L);
	
	// 3. create sequence
	IdSequence sequence = customFactory.create();
	// 4. generate id
	long uid = sequence.nextId();
	//126364026837139460
```