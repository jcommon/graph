package jdeps;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Factory and implementation of a dependency graph that can topologically sort vertices that are numbers.
 */
public class NumberGraph extends DependencyGraph<NumberVertex> {
  protected NumberGraph() {
    super();
  }

  public NumberGraph copyAsNumberGraph() {
    return copyDependencyGraph(this);
  }

  public static NumberGraph buildFromNumbers(Number...vertices) {
    final NumberGraph g = new NumberGraph();
    for(Number d : vertices)
      g.addVertex(d);
    return g;
  }

  public static NumberGraph createForNumbers() {
    return buildFromNumbers();
  }

  public NumberGraph addVertex(Number vertex) {
    addVertex(NumberVertex.from(vertex));
    return this;
  }

  public NumberGraph removeVertex(Number vertex) {
    removeVertex(NumberVertex.from(vertex));
    return this;
  }

  public NumberGraph addEdge(Number from, Number to) {
    addEdge(NumberVertex.from(from), NumberVertex.from(to));
    return this;
  }

  public NumberGraph removeEdge(Number from, Number to) {
    removeEdge(NumberVertex.from(from), NumberVertex.from(to));
    return this;
  }
}
