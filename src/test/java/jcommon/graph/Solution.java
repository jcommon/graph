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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Solution<TVertex extends IVertex<TValue>, TValue extends Object> {
  public static final boolean CYCLE_EXPECTED = true;
  public static final boolean CYCLE_NOT_EXPECTED = false;

  private final IGraph<TVertex, TValue> graph;
  private final ValidOrdering<TValue>[] orderings;
  private final ValidList<TValue> ending_values_expected;
  private final boolean cycle_expected;
  private final String message;

  public Solution(String message, boolean cycle_expected, IGraph<TVertex, TValue> graph, ValidList<TValue> ending_values_expected, ValidOrdering<TValue>...orderings) {
    this.graph = graph;
    this.message = message == null ? "" : message;
    this.orderings = orderings;
    this.cycle_expected = cycle_expected;
    this.ending_values_expected = ending_values_expected;
  }

  public String getMessage() {
    return message;
  }

  public boolean isCycleExpected() {
    return cycle_expected;
  }

  public IGraph<TVertex, TValue> getGraph() {
    return graph;
  }

  public ValidList<TValue> getExpectedEndingValues() {
    return ending_values_expected;
  }

  public ValidOrdering<TValue>[] getValidOrderings() {
    return orderings;
  }

  public boolean isValidSolution(List<TValue> values) {
    for(ValidOrdering<TValue> o : orderings) {
      if (o.matches(values))
        return true;
    }
    return false;
  }

  public boolean anyMatch(TValue...values) {
    return anyMatch(Arrays.asList(values));
  }

  public boolean anyMatch(List<TValue> values) {
    return isValidSolution(values);
  }

  public String toString(List<TValue> values) {
    return "" + (values == null ? null : values);
  }

  public void check(TValue...values) {
    check(Arrays.asList(values));
  }

  public void check(List<TValue> values) {
    if (!isValidSolution(values)) {
      StringBuilder sb = new StringBuilder(256);
      sb.append("\n");
      sb.append("Found: \n");
      sb.append("  ");
      sb.append(toString(values));
      sb.append("\n");
      sb.append("Expected one of the following: \n");
      for(ValidOrdering o : orderings) {
        sb.append("  ");
        sb.append(o.toString());
        sb.append("\n");
      }
      assertTrue("Unable to find valid solution" + (!"".equals(message) ? ": " + message : "") + sb.toString(), false);
    }
  }

  public void checkSort() {
    try {
      check(graph.sort());
      if (isCycleExpected())
        assertTrue("A cycle was expected, but there was none", false);
    } catch(CyclicGraphException cge) {
      if (!isCycleExpected())
        assertTrue("An unexpected cycle occurred", false);
    }
  }

  public void checkSortAsync(ITopologicalSortCallback<TValue> callback) throws AssertionError {
    //Error callback indicating if a cycle was detected
    //The error callback will be called and we set the boolean value to false and then check it later.
    final boolean[] cycle_detected = new boolean[1];

    ITopologicalSortAsyncResult<TValue> result =  null;
    try {
      final ITopologicalSortErrorCallback<TValue> error_callback = new ITopologicalSortErrorCallback<TValue>() {
        @Override
        public void handleError(Object o, Throwable t, IVertex vertex, ITopologicalSortCoordinator coordinator) {
          if (t instanceof CyclicGraphException)
            cycle_detected[0] = true;
        }
      };

      result = graph.sortAsync(callback, error_callback);
      if (!isCycleExpected()) {
        assertTrue(result.waitForCompletion(10L, TimeUnit.SECONDS));
        assertTrue("The expected value(s) " + ending_values_expected.toString() + " do not match the given result(s): " + result.results(), ending_values_expected.matches(result.results()));
      } else {
        assertFalse(result.waitForCompletion(10L, TimeUnit.SECONDS));
        assertTrue("A cycle was expected, but there was none", cycle_detected[0]);
      }
    } catch(AssertionError ae) {
      throw ae;
    } catch(Throwable t) {
      if (t instanceof CyclicGraphException && !isCycleExpected())
        assertTrue("An unexpected cycle occurred", false);
      throw new AssertionError(t);
    }
  }

  @SuppressWarnings("unchecked")
  public static <TVertex extends IVertex<TValue>, TValue extends Object> Solution<TVertex, TValue> create(IGraph<TVertex, TValue> graph, ValidOrdering<TValue>...orderings) {
    return new Solution<TVertex, TValue>("", false, graph, new ValidList<TValue>(), orderings);
  }

  public static <TVertex extends IVertex<TValue>, TValue extends Object> Solution<TVertex, TValue> create(IGraph<TVertex, TValue> graph, ValidList<TValue> ending_values_expected, ValidOrdering<TValue>...orderings) {
    return new Solution<TVertex, TValue>("", false, graph, ending_values_expected, orderings);
  }

  public static <TVertex extends IVertex<TValue>, TValue extends Object> Solution create(boolean cycle_expected, IGraph<TVertex, TValue> graph, ValidList<TValue> ending_values_expected, ValidOrdering<TValue>...orderings) {
    return new Solution<TVertex, TValue>("", cycle_expected, graph, ending_values_expected, orderings);
  }

  public static <TVertex extends IVertex<TValue>, TValue extends Object> Solution create(String message, IGraph<TVertex, TValue> graph, ValidList<TValue> ending_values_expected, ValidOrdering<TValue>...orderings) {
    return new Solution<TVertex, TValue>(message, false, graph, ending_values_expected, orderings);
  }

  @SuppressWarnings("unchecked")
  public static <TVertex extends IVertex<TValue>, TValue extends Object> Solution create(String message, boolean cycle_expected, IGraph<TVertex, TValue> graph, ValidOrdering<TValue>...orderings) {
    return new Solution<TVertex, TValue>(message, cycle_expected, graph, new ValidList<TValue>(), orderings);
  }

  public static <TVertex extends IVertex<TValue>, TValue extends Object> Solution create(String message, boolean cycle_expected, IGraph<TVertex, TValue> graph, ValidList<TValue> ending_values_expected, ValidOrdering<TValue>...orderings) {
    return new Solution<TVertex, TValue>(message, cycle_expected, graph, ending_values_expected, orderings);
  }
}
