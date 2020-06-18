package com.littlenb.snowflake.sequence;

import com.littlenb.snowflake.exception.SnowFlakeException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/***
 * The unique id has 64bits (long)
 *
 * <pre>
 * {@code
 * +------+----------------------+----------------+-----------+
 * | sign |  delta millisecond   | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit        timeBits            workerBits      seqBits
 * }
 * </pre>
 *
 * <b>Note that:</b> The total bits must be 64
 *
 * <ul>
 * <li>sign: The highest bit is 0
 * <li>delta millisecond:
 * <li>worker id:
 * <li>sequence:
 *
 * @author sway.li
 *
 */
public class SnowFlakeGenerator implements IdGenerator {

    private TimeUnit timeUnit;

    private long epochTimestamp;
    private long workerId;

    /**
     * Stable fields
     */
    private BitsAllocator bitsAllocator;

    /**
     * Volatile fields caused by nextId()
     */
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowFlakeGenerator(int timeBits, int workerBits, int seqBits, long epochTimestamp, long workerId) {
        // initialize bits allocator
        bitsAllocator = new BitsAllocator(timeBits, workerBits, seqBits);
        // initialize worker id
        if (workerId > bitsAllocator.getMaxWorkerId()) {
            throw new SnowFlakeException(
                    "Worker id " + workerId + " exceeds the max " + bitsAllocator.getMaxWorkerId());
        }
        this.workerId = workerId;

        // initialize epoch
        if (epochTimestamp > bitsAllocator.getMaxDeltaTime()) {
            throw new SnowFlakeException(
                    "epoch timestamp " + epochTimestamp + " exceeds the max " + bitsAllocator.getMaxDeltaTime());
        }
        this.epochTimestamp = epochTimestamp;
    }

    public SnowFlakeGenerator(TimeUnit timeUnit,int timeBits, int workerBits, int seqBits, long epochTimestamp, long workerId) {
        this.timeUnit = timeUnit;

        // initialize bits allocator
        bitsAllocator = new BitsAllocator(timeBits, workerBits, seqBits);
        // initialize worker id
        if (workerId > bitsAllocator.getMaxWorkerId()) {
            throw new SnowFlakeException(
                    "Worker id " + workerId + " exceeds the max " + bitsAllocator.getMaxWorkerId());
        }
        this.workerId = workerId;

//        // initialize epoch
//        if (epochTimestamp > bitsAllocator.getMaxDeltaTime()) {
//            throw new SnowFlakeException(
//                    "epoch timestamp " + epochTimestamp + " exceeds the max " + bitsAllocator.getMaxDeltaTime());
//        }
        this.epochTimestamp = epochTimestamp;
    }

    /**
     * Generate unique id
     *
     * @return id
     * @throws SnowFlakeException in the case: Clock moved backwards; Exceeds the max timestamp
     */
    @Override
    public synchronized long nextId() {
        long currentTime = getTimestamp();

        // Clock moved backwards, refuse to generate id
        if (currentTime < lastTimestamp) {
            long refusedTimeStamp = lastTimestamp - currentTime;
            throw new SnowFlakeException("Clock moved backwards. Refusing for %s timeStamp", refusedTimeStamp);
        }

        // At the same second, increase sequence
        if (currentTime == lastTimestamp) {
            sequence = (sequence + 1) & bitsAllocator.getMaxSequence();
            // Exceed the max sequence, we wait the next second to generate id
            if (sequence == 0) {
                currentTime = getNextTime(lastTimestamp);
            }

            // At the different second, sequence restart from zero
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTime;

        // Allocate bits for ID
        return bitsAllocator.allocate(currentTime - epochTimestamp, workerId, sequence);
    }

    @Override
    public synchronized long[] nextSegment(int size) {
      long[] segment = new long[size];
      for (int i = 0; i < size; i++) {
        segment[i] = nextId();
      }
      return segment;
    }

    @Override
    public String parse(long id) {
        long totalBits = BitsAllocator.TOTAL_BITS;
        long signBits = bitsAllocator.getSignBits();
        long timestampBits = bitsAllocator.getTimestampBits();
        long workerIdBits = bitsAllocator.getWorkerIdBits();
        long sequenceBits = bitsAllocator.getSequenceBits();

        // parse id
        long sequence = (id << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
        long workerId = (id << (timestampBits + signBits)) >>> (totalBits - workerIdBits);
        long deltaMillis = id >>> (workerIdBits + sequenceBits);

        Date thatTime = new Date(timeUnit.toMillis(this.epochTimestamp + deltaMillis));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String thatTimeStr = simpleDateFormat.format(thatTime);

        // format as string
        return String.format("{\"id\":\"%d\",\"timestamp\":\"%s\",\"workerId\":\"%d\",\"sequence\":\"%d\"}", id,
                thatTimeStr, workerId, sequence);
    }

    /**
     * Get next timestamp
     */
    private long getNextTime(long lastTimestamp){
        long timestamp = getTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getTimestamp();
        }

        return timestamp;
    }

    /**
     * Get current timestamp
     */
    private long getTimestamp() {
        long millis = System.currentTimeMillis();
        long timestamp = timeUnit.convert(millis, TimeUnit.MILLISECONDS);
        if (timestamp - epochTimestamp > bitsAllocator.getMaxDeltaTime()) {
            throw new SnowFlakeException("Timestamp bits is exhausted. Refusing UID generate. Now: " + timestamp);
        }

        return timestamp;
    }

}
