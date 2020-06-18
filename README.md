# snowflake

[![JDK 1.7](https://img.shields.io/badge/JDK-1.7-green.svg "JDK 1.7")]()

Twitter SnowFlake算法(Java)，生产有序的长整型数值id(64 bits).

参考Twitter SnowFlake 和 百度 uid-generator.

| sign | delta millisecond | worker node id  | sequence |
| ---- |:-----------------:|:---------------:| --------:|
| 1bit | {}bits            | {}bits          | {}bits   |

## Quick Start

### Add maven dependency

```xml
<dependency>
    <groupId>com.littlenb</groupId>
    <artifactId>snowflake</artifactId>
    <version>1.0.3</version>
</dependency>
```

### step1. 使用ElasticIdSequenceFactory制定算法策略

+ 自定义算法策略

```Java

    ElasticIdSequenceFactory elasticFactory = new ElasticIdSequenceFactory();
    // TimeBits + WorkerBits + SeqBits = 64 -1
    elasticFactory.setTimeBits(41);
    elasticFactory.setWorkerBits(10);
    elasticFactory.setSeqBits(12);
    // 时间单位
    elasticFactory.setTimeUnit(TimeUnit.MILLISECONDS);
    // 时间初始值，用于计算时间偏移量
    elasticFactory.setEpochTimestamp(1483200000000L);
```

+ MillisIdSequenceFactory

| sign | delta millisecond | worker node id  | sequence |
| ---- |:-----------------:|:---------------:| --------:|
| 1bit | 41bits            | 10bits          | 12bits   |

+ SecondsIdSequenceFactory

| sign | delta seconds     | worker node id  | sequence |
| ---- |:-----------------:|:---------------:| --------:|
| 1bit | 28bits            | 22bits          | 13bits   |

### step2. 定制worker id策略,创建IdSequence

+ 直接指定

```Java

    ElasticIdGeneratorFactory elasticFactory = new ElasticIdGeneratorFactory();

    //step1 ...
    
    IdGenerator elasticGenerator = elasticFactory.create(1L);
```

+ 自定义算法(用于分布式系统)

实现WorkerIdAssigner接口即可

```Java

    ElasticIdGeneratorFactory elasticFactory = new ElasticIdGeneratorFactory();

    //step1 ...
    
    // you can implements the WorkerIdAssigner to create worker id.
    // e.g. use the simple implement in here.
    WorkerIdAssigner workerIdAssigner = new SimpleWorkerIdAssigner(1L);
    IdGenerator elasticIdGenerator = elasticFactory.create(workerIdAssigner);
```

## Attention

IdSequence对象的调用应使用单例模式，由客户端自行实现。

## License

```text
    Copyright [2017]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
