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

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unchecked")
public class DirectedAcyclicGraphTest {
  @Test
  public void testAllSolutions() throws CyclicGraphException {
    for(Solution solution : Examples.ALL_SOLUTIONS)
      solution.checkSort();
  }

  @Test
  public void testAsync() {
    final ITopologicalSortCallback<Object, Object> CALLBACK_NOOP = new ITopologicalSortCallback<Object, Object>() {
      @Override
      public Object handle(Object o, ITopologicalSortInput<Object, Object> input, IVertex<Object> vertex, ITopologicalSortCoordinator coordinator) throws Throwable {
        return o;
      }
    };

    final ITopologicalSortErrorCallback<Object> ERROR_CALLBACK_NOOP = new ITopologicalSortErrorCallback<Object>() {
      @Override
      public void handleError(Object value, Throwable t, IVertex<Object> vertex, ITopologicalSortCoordinator coordinator) {
      }
    };

    final ITopologicalSortCallback<Object, Object> CALLBACK_DEBUG = new ITopologicalSortCallback<Object, Object>() {
      @Override
      public Object handle(Object o, ITopologicalSortInput<Object, Object> input, IVertex<Object> vertex, ITopologicalSortCoordinator coordinator) throws Throwable {
        System.out.println(Thread.currentThread().getName() + ": " + o.toString() + ", input: " + input.toString());
        Thread.sleep(1000);
        return vertex;
      }
    };

    final ITopologicalSortErrorCallback<Object> ERROR_CALLBACK_DEBUG = new ITopologicalSortErrorCallback<Object>() {
      @Override
      public void handleError(Object value, Throwable t, IVertex<Object> vertex, ITopologicalSortCoordinator coordinator) {
        System.err.println(Thread.currentThread().getName() + ": " + t.getMessage());
      }
    };

    //Test one-by-one execution.

    for(IGraph g : Examples.ALL_VALID_GRAPHS) {
      assertTrue(g.sortAsync(CALLBACK_NOOP, ERROR_CALLBACK_NOOP).waitForCompletion());
    }

    for(IGraph g : Examples.ALL_GRAPHS_WITH_CYCLES) {
      assertFalse(g.sortAsync(CALLBACK_NOOP, ERROR_CALLBACK_NOOP).waitForCompletion());
    }

    //Test multiple in-parallel sharing the same executor service.
    ExecutorService executor = Executors.newFixedThreadPool(6);
    ITopologicalSortAsyncResult[] results = new ITopologicalSortAsyncResult[Examples.ALL_VALID_GRAPHS.length];
    for(int i = 0; i < results.length; ++i)
      results[i] = Examples.ALL_VALID_GRAPHS[i].sortAsync(executor, CALLBACK_NOOP, ERROR_CALLBACK_NOOP);
    assertTrue(allSuccessfullyCompleted(results));
    executor.shutdownNow();

    for(Solution solution : Examples.ALL_SOLUTIONS)
      solution.checkSortAsync(CALLBACK_NOOP);

    //Create a graph like: 1 -> 2 -> 3 -> 4 -> 5
    final NumberGraph<Integer> ng_1 = Examples.VALID_5;

    //Sum up the values as they're processed.
    ITopologicalSortAsyncResult<Integer, Integer> ng_result = ng_1.sortAsync(new ITopologicalSortCallback<Integer, Integer>() {
      @Override
      public Integer handle(Integer number, ITopologicalSortInput<Integer, Integer> input, IVertex<Integer> vertex, ITopologicalSortCoordinator coordinator) throws Throwable {
        int prev_val = (input.isStart() ? 0 : input.first());
        int val = number;
        return prev_val + val;
      }
    });
    assertTrue(ng_result.waitForCompletion(10L, TimeUnit.SECONDS));
    assertEquals(15, (int)ng_result.first());

    //Take the product of the values as they're processed.
    ng_result = ng_1.sortAsync(new ITopologicalSortCallback<Integer, Integer>() {
      @Override
      public Integer handle(Integer number, ITopologicalSortInput<Integer, Integer> input, IVertex<Integer> vertex, ITopologicalSortCoordinator coordinator) throws Throwable {
        int prev_val = (input.isStart() ? 1 : input.first());
        int val = number;
        return prev_val * val;
      }
    });
    assertTrue(ng_result.waitForCompletion(10L, TimeUnit.SECONDS));
    assertEquals(120, (int)ng_result.first());
  }

  private boolean allSuccessfullyCompleted(ITopologicalSortAsyncResult[] results) {
    for(ITopologicalSortAsyncResult result : results) {
      if (!result.waitForCompletion())
        return false;
    }
    return true;
  }
}
