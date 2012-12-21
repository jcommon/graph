package jdeps;

/**
 * Factory and implementation of a dependency graph that can topologically sort vertices that are {@link String}.
 */
public class StringGraph extends DependencyGraph<StringVertex> {
  protected StringGraph() {
    super();
  }

  public StringGraph copyAsStringGraph() {
    return copyDependencyGraph(this);
  }

  public static StringGraph buildFromStrings(String...vertices) {
    final StringGraph g = new StringGraph();
    for(String d : vertices)
      g.addVertex(d);
    return g;
  }

  public static StringGraph createForStrings() {
    return buildFromStrings();
  }

  public StringGraph addVertex(String vertex) {
    addVertex(StringVertex.from(vertex));
    return this;
  }

  public StringGraph removeVertex(String vertex) {
    removeVertex(StringVertex.from(vertex));
    return this;
  }

  public StringGraph addEdge(String from, String to) {
    addEdge(StringVertex.from(from), StringVertex.from(to));
    return this;
  }

  public StringGraph removeEdge(String from, String to) {
    removeEdge(StringVertex.from(from), StringVertex.from(to));
    return this;
  }
}
