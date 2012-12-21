package jdeps;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Returned by an asynchronous sort call. Used to know or await
 * for notification of asynchronous processing completion.
 */
public interface ITopologicalSortAsyncResult {
  boolean isDone();
  boolean isSuccessful();
  ExecutorService getExecutorService();

  /**
   * Waits indefinitely and uninterruptibly for the asynchronous sort to signal
   * that it's done processing.
   *
   * @return True if the sorting completed successfully. False if a callback threw
   *         an exception or a cycle was detected.
   */
  boolean waitForCompletion();

  /**
   * Waits indefinitely and uninterruptibly for the asynchronous sort to signal
   * that it's done processing.
   *
   * @param timeout Length of time in TimeUnit units to wait for completion.
   * @param unit {@link TimeUnit} unit of time represented by the timeout.
   * @return True if the sorting completed successfully. False if a callback threw
   *         an exception or a cycle was detected.
   */
  boolean waitForCompletion(long timeout, TimeUnit unit);

  boolean awaitUninterruptibly();
  boolean awaitUninterruptibly(long timeout, TimeUnit unit);

  boolean await() throws InterruptedException;
  boolean await(long timeout, TimeUnit unit) throws InterruptedException;
}
