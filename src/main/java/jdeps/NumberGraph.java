package jdeps;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Factory and implementation of a dependency graph that can topologically sort vertices that are {@link Number}s.
 *
 * @see DependencyGraph
 */
public class NumberGraph extends DependencyGraph<NumberVertex> {
  protected NumberGraph() {
    super();
  }

  /**
   * @see DependencyGraph#copy()
   */
  public NumberGraph copyAsNumberGraph() {
    return copyDependencyGraph(this);
  }

  /**
   * @see DependencyGraph#build(IVertex[])
   */
  public static NumberGraph buildFromNumbers(Number...vertices) {
    final NumberGraph g = new NumberGraph();
    for(Number d : vertices)
      g.addVertex(d);
    return g;
  }

  /**
   * @see DependencyGraph#create()
   */
  public static NumberGraph createForNumbers() {
    return buildFromNumbers();
  }

  /**
   * @see DependencyGraph#addVertex(IVertex)
   */
  public NumberGraph addVertex(Number vertex) {
    addVertex(NumberVertex.from(vertex));
    return this;
  }

  /**
   * @see DependencyGraph#removeVertex(IVertex)
   */
  public NumberGraph removeVertex(Number vertex) {
    removeVertex(NumberVertex.from(vertex));
    return this;
  }

  /**
   * @see DependencyGraph#addEdge(IVertex, IVertex)
   */
  public NumberGraph addEdge(Number from, Number to) {
    addEdge(NumberVertex.from(from), NumberVertex.from(to));
    return this;
  }

  /**
   * @see DependencyGraph#removeEdge(IVertex, IVertex)
   */
  public NumberGraph removeEdge(Number from, Number to) {
    removeEdge(NumberVertex.from(from), NumberVertex.from(to));
    return this;
  }
}
