package io.confluent.connect.hdfs;

import java.util.concurrent.atomic.AtomicInteger;

public class TempFileLimiter {

  private int maxOpenTempFiles;
  private final AtomicInteger tempFileCount = new AtomicInteger(0);

  public TempFileLimiter(HdfsSinkConnectorConfig connectorConfig) {
    this.maxOpenTempFiles = connectorConfig.getInt(HdfsSinkConnectorConfig.MAX_TEMP_FILES_CONFIG);
  }

  public void increment() {
    tempFileCount.incrementAndGet();
  }

  public void decrement() {
    tempFileCount.decrementAndGet();
  }


  public int get() {
    return tempFileCount.get();
  }

  public int removeMany(int delta) {
    return tempFileCount.addAndGet(-1 * delta);
  }

  public boolean isBusted() {
    return tempFileCount.get() >= maxOpenTempFiles;
  }

  public int getMaxOpenTempFiles() {
    return maxOpenTempFiles;
  }
}
