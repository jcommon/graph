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

package jcommon.deps;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Solution<TVertex extends IVertex> {
  public static final boolean CYCLE_EXPECTED = true;
  public static final boolean CYCLE_NOT_EXPECTED = false;

  private final IGraph<TVertex> graph;
  private final ValidOrdering<TVertex>[] orderings;
  private final boolean cycle_expected;
  private final String message;

  public Solution(String message, boolean cycle_expected, IGraph<TVertex> graph, ValidOrdering<TVertex>...orderings) {
    this.graph = graph;
    this.message = message == null ? "" : message;
    this.orderings = orderings;
    this.cycle_expected = cycle_expected;
  }

  public String getMessage() {
    return message;
  }

  public boolean isCycleExpected() {
    return cycle_expected;
  }

  public IGraph<TVertex> getGraph() {
    return graph;
  }

  public ValidOrdering<TVertex>[] getValidOrderings() {
    return orderings;
  }

  public boolean isValidSolution(List<TVertex> vertices) {
    for(ValidOrdering<TVertex> o : orderings) {
      if (o.matches(vertices))
        return true;
    }
    return false;
  }

  public boolean anyMatch(TVertex...vertices) {
    return anyMatch(Arrays.asList(vertices));
  }

  public boolean anyMatch(List<TVertex> vertices) {
    return isValidSolution(vertices);
  }

  public String toString(List<TVertex> vertices) {
    return "" + (vertices == null ? null : vertices);
  }

  public void check(TVertex...vertices) {
    check(Arrays.asList(vertices));
  }

  public void check(List<TVertex> vertices) {
    if (!isValidSolution(vertices)) {
      StringBuilder sb = new StringBuilder(256);
      sb.append("\n");
      sb.append("Found: \n");
      sb.append("  ");
      sb.append(toString(vertices));
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

  public static <TVertex extends IVertex> Solution<TVertex> create(IGraph<TVertex> graph, ValidOrdering<TVertex>...orderings) {
    return new Solution<TVertex>("", false, graph, orderings);
  }

  public static <TVertex extends IVertex> Solution create(boolean cycle_expected, IGraph<TVertex> graph, ValidOrdering<TVertex>...orderings) {
    return new Solution<TVertex>("", cycle_expected, graph, orderings);
  }

  public static <TVertex extends IVertex> Solution create(String message, IGraph<TVertex> graph, ValidOrdering<TVertex>...orderings) {
    return new Solution<TVertex>(message, false, graph, orderings);
  }

  public static <TVertex extends IVertex> Solution create(String message, boolean cycle_expected, IGraph<TVertex> graph, ValidOrdering<TVertex>...orderings) {
    return new Solution<TVertex>(message, cycle_expected, graph, orderings);
  }
}