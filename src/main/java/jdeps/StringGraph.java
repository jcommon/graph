package jdeps;

/**
 * Factory and implementation of a dependency graph that can topologically sort vertices that are {@link String}s.
 *
 * @see DependencyGraph
 */
public class StringGraph extends DependencyGraph<StringVertex> {
  protected StringGraph() {
    super();
  }

  /**
   * @see DependencyGraph#copy()
   */
  public StringGraph copyAsStringGraph() {
    return copyDependencyGraph(this);
  }

  /**
   * @see DependencyGraph#build(IVertex[])
   */
  public static StringGraph buildFromStrings(String...vertices) {
    final StringGraph g = new StringGraph();
    for(String d : vertices)
      g.addVertex(d);
    return g;
  }

  /**
   * @see DependencyGraph#create()
   */
  public static StringGraph createForStrings() {
    return buildFromStrings();
  }

  /**
   * @see DependencyGraph#addVertex(IVertex)
   */
  public StringGraph addVertex(String vertex) {
    addVertex(StringVertex.from(vertex));
    return this;
  }

  /**
   * @see DependencyGraph#removeVertex(IVertex)
   */
  public StringGraph removeVertex(String vertex) {
    removeVertex(StringVertex.from(vertex));
    return this;
  }

  /**
   * @see DependencyGraph#addEdge(IVertex, IVertex)
   */
  public StringGraph addEdge(String from, String to) {
    addEdge(StringVertex.from(from), StringVertex.from(to));
    return this;
  }

  /**
   * @see DependencyGraph#removeEdge(IVertex, IVertex)
   */
  public StringGraph removeEdge(String from, String to) {
    removeEdge(StringVertex.from(from), StringVertex.from(to));
    return this;
  }
}
