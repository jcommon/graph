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

package jcommon.graph.impl;

import jcommon.graph.ITopologicalSortAsyncResult;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @see ITopologicalSortAsyncResult
 */
final class TopologicalSortAsyncResult<TValue extends Object> implements ITopologicalSortAsyncResult<TValue> {
  private final Object lock = new Object();
  private final ExecutorService executor;
  private final CountDownLatch latch;
  private boolean done;
  private boolean successful;
  private boolean discontinue_processing;
  private Map<TValue, TValue> results;

  public TopologicalSortAsyncResult(final ExecutorService executor) {
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

  void asyncComplete(final Map<TValue, TValue> results, final boolean successful) {
    synchronized (lock) {
      this.results = results;
      this.successful = successful;
      this.done = true;
      this.latch.countDown();
    }
  }

  /**
   * @see ITopologicalSortAsyncResult#waitForCompletion()
   */
  @Override
  public boolean waitForCompletion() {
    return awaitUninterruptibly() && isSuccessful();
  }

  /**
   * @see ITopologicalSortAsyncResult#waitForCompletion()
   */
  @Override
  public boolean waitForCompletion(final long timeout, final TimeUnit unit) {
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
  public boolean awaitUninterruptibly(final long timeout, final TimeUnit unit) {
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
  public boolean await(final long timeout, final TimeUnit unit) throws InterruptedException {
    return latch.await(timeout, unit);
  }

  @Override
  public boolean isEmpty() {
    return !isDone() || results.isEmpty();
  }

  @Override
  public TValue get(final TValue value) {
    return resultFor(value);
  }

  @Override
  public TValue first() {
    if (!isDone() || results.isEmpty())
      return null;
    return results.values().iterator().next();
  }

  @Override
  public TValue resultFor(final TValue value) {
    return isDone() ? results.get(value) : null;
  }

  @Override
  public int size() {
    return isDone() ? results.size() : 0;
  }

  @Override
  public boolean contains(final TValue value) {
    return isDone() ? results.containsKey(value) : false;
  }

  @Override
  public Iterable<TValue> results() {
    return isDone() ? results.values() : new ArrayList<TValue>(0);
  }

  @Override
  public Iterable<TValue> endingValues() {
    return isDone() ? results.keySet() : new ArrayList<TValue>(0);
  }
}
