/*
  Copyright (C) 2012-2013 the original author or authors.

  See the LICENSE.txt file distributed with this work for additional
  information regarding copyright ownership.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package jcommon.graph;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Returned by an asynchronous sort call. Used to know or await
 * for notification of asynchronous processing completion.
 */
public interface ITopologicalSortAsyncResult {
  /**
   * Determines if all processing of vertices has completed.
   *
   * @return True if all processing of vertices has completed, otherwise false.
   */
  boolean isDone();

  /**
   * Determines if all vertices have been processed without incident.
   *
   * Should be called only after {@link #waitForCompletion()}, {@link #await()}, or {@link #awaitUninterruptibly()}
   * have returned. Calling {@link #waitForCompletion()} will return this same value after all vertices have been
   * processed. It's suggested you use {@link #waitForCompletion()}.
   *
   * @return True if all vertices were processed without incident, otherwise false.
   */
  boolean isSuccessful();

  /**
   * Determines if {@link #discontinueScheduling()} has been called previously.
   *
   * @return True if {@link #discontinueScheduling()} has been called previously, otherwise false.
   */
  boolean isProcessingDiscontinued();

  /**
   * Provides a reference to the {@link ExecutorService} that asynchronous processing was called with.
   *
   * @return A reference to an {@link ExecutorService} instance.
   */
  ExecutorService getExecutorService();

  /**
   * Discontinues further scheduling of additional vertices for processing.
   *
   * It's highly suggested that you do not shutdown or abort the {@link #getExecutorService()}
   * instance. Instead, call this method and then {@link #waitForCompletion()} which will
   * allow all previously submitted vertices to drain.
   *
   * @return True if the operation was successful, otherwise false.
   */
  boolean discontinueScheduling();

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

  /**
   * Provided for conveniency.
   *
   * Waits indefinitely and uninterruptibly for the asynchronous sort to signal
   * that it's done processing.
   *
   * It's suggested you use {@link #waitForCompletion()}.
   *
   * @return True if processing completed. This is not an indicator of successful completion. Please use
   *         {@link #isSuccessful()} or simply call {@link #waitForCompletion()} instead.
   */
  boolean awaitUninterruptibly();

  /**
   * Provided for conveniency.
   *
   * Waits indefinitely and uninterruptibly for the asynchronous sort to signal
   * that it's done processing.
   *
   * It's suggested you use {@link #waitForCompletion()}.
   *
   * @param timeout Length of time in TimeUnit units to wait for completion.
   * @param unit {@link TimeUnit} unit of time represented by the timeout.
   * @return True if processing completed. This is not an indicator of successful completion. Please use
   *         {@link #isSuccessful()} or simply call {@link #waitForCompletion()} instead.
   */
  boolean awaitUninterruptibly(long timeout, TimeUnit unit);

  /**
   * Provided for conveniency.
   *
   * Waits indefinitely (but can be interrupted) for the asynchronous sort to signal that it's done processing.
   *
   * It's suggested you use {@link #waitForCompletion()}.
   *
   * @return True if processing completed. This is not an indicator of successful completion. Please use
   *         {@link #isSuccessful()} or simply call {@link #waitForCompletion()} instead.
   */
  boolean await() throws InterruptedException;

  /**
   * Provided for conveniency.
   *
   * Waits indefinitely (but can be interrupted) for the asynchronous sort to signal that it's done processing.
   *
   * It's suggested you use {@link #waitForCompletion()}.
   *
   * @param timeout Length of time in TimeUnit units to wait for completion.
   * @param unit {@link TimeUnit} unit of time represented by the timeout.
   * @return True if processing completed. This is not an indicator of successful completion. Please use
   *         {@link #isSuccessful()} or simply call {@link #waitForCompletion()} instead.
   */
  boolean await(long timeout, TimeUnit unit) throws InterruptedException;
}
