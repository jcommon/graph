package jdeps;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @see ITopologicalSortAsyncResult
 */
final class TopologicalSortAsyncResult implements ITopologicalSortAsyncResult {
  private final Object lock = new Object();
  private final ExecutorService executor;
  private final CountDownLatch latch;
  private boolean done;
  private boolean successful;
  private boolean discontinue_processing;

  public TopologicalSortAsyncResult(ExecutorService executor) {
    this.executor = executor;
    this.latch =  new CountDownLatch(1);
    this.done = false;
    this.successful = false;
    this.discontinue_processing = false;
  }

  @Override
  public boolean isDone() {
    return done;
  }

  @Override
  public boolean isSuccessful() {
    return successful;
  }

  @Override
  public boolean isProcessingDiscontinued() {
    return discontinue_processing;
  }

  @Override
  public ExecutorService getExecutorService() {
    return executor;
  }

  @Override
  public boolean discontinueScheduling() {
    synchronized (lock) {
      discontinue_processing = true;
    }
    return true;
  }

  Object getLock() {
    return lock;
  }

  void asyncComplete(boolean successful) {
    synchronized (lock) {
      this.done = true;
      this.successful = successful;
      this.latch.countDown();
    }
  }

  /**
   * @see jdeps.ITopologicalSortAsyncResult#waitForCompletion()
   */
  @Override
  public boolean waitForCompletion() {
    return awaitUninterruptibly() && isSuccessful();
  }

  /**
   * @see jdeps.ITopologicalSortAsyncResult#waitForCompletion()
   */
  @Override
  public boolean waitForCompletion(long timeout, TimeUnit unit) {
    return awaitUninterruptibly(timeout, unit) && isSuccessful();
  }

  /**
   * Wait for the asynchronous processor to report completion.
   *
   * @return True if completion was signaled otherwise false if interrupted.
   */
  @Override
  public boolean awaitUninterruptibly() {
    try {
      return await();
    } catch(InterruptedException ie) {
      return false;
    }
  }

  @Override
  public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
    try {
      return await(timeout, unit);
    } catch(InterruptedException ie) {
      return false;
    }
  }

  @Override
  public boolean await() throws InterruptedException {
    latch.await();
    return true;
  }

  @Override
  public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
    return latch.await(timeout, unit);
  }
}