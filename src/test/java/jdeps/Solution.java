package jdeps;

import java.util.Arrays;

import static org.junit.Assert.*;

public class Solution {
  public static final boolean CYCLE_EXPECTED = true;
  public static final boolean CYCLE_NOT_EXPECTED = false;

  private final IGraph graph;
  private final ValidOrdering[] orderings;
  private final boolean cycle_expected;
  private final String message;

  public Solution(String message, boolean cycle_expected, IGraph graph, ValidOrdering...orderings) {
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

  public IGraph getGraph() {
    return graph;
  }

  public ValidOrdering[] getValidOrderings() {
    return orderings;
  }

  public boolean isValidSolution(IVertex...vertices) {
    for(ValidOrdering o : orderings) {
      if (o.matches(vertices))
        return true;
    }
    return false;
  }

  public boolean anyMatch(IVertex...vertices) {
    return isValidSolution(vertices);
  }

  public String toString(IVertex...vertices) {
    return "" + (vertices == null ? null : Arrays.asList(vertices));
  }

  public void check(IVertex...vertices) {
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

  public static Solution create(IGraph graph, ValidOrdering...orderings) {
    return new Solution("", false, graph, orderings);
  }

  public static Solution create(boolean cycle_expected, IGraph graph, ValidOrdering...orderings) {
    return new Solution("", cycle_expected, graph, orderings);
  }

  public static Solution create(String message, IGraph graph, ValidOrdering...orderings) {
    return new Solution(message, false, graph, orderings);
  }

  public static Solution create(String message, boolean cycle_expected, IGraph graph, ValidOrdering...orderings) {
    return new Solution(message, cycle_expected, graph, orderings);
  }
}
