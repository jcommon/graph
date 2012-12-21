package jdeps;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Factory and implementation of a dependency graph that can topologically sort its vertices.
 */
public class StringGraph implements Cloneable, IGraph, IStringGraph, INumberGraph {
  private Set<IVertex> vertices = new LinkedHashSet<IVertex>(5, 0.8f);
  private Set<IEdge> edges = new LinkedHashSet<IEdge>(8, 0.8f);

  private StringGraph() {
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return copyDependencyGraph();
  }

  public StringGraph copyDependencyGraph() {
    StringGraph g = new StringGraph();
    g.vertices = new LinkedHashSet<IVertex>(vertices);
    g.edges = new LinkedHashSet<IEdge>(edges);
    return g;
  }

  public IGraph copy() {
    return copyDependencyGraph();
  }

  public IStringGraph copyAsStringGraph() {
    return copyDependencyGraph();
  }

  public INumberGraph copyAsNumberGraph() {
    return copyDependencyGraph();
  }

  public static IGraph build(IVertex...vertices) {
    StringGraph g = new StringGraph();
    for(IVertex d : vertices) {
      g.addVertex(d);
    }
    return g;
  }

  public static IStringGraph buildFromStrings(String...vertices) {
    final IStringGraph g = (IStringGraph)build();
    for(String d : vertices)
      g.addVertex(d);
    return g;
  }

  public static INumberGraph buildFromNumbers(Number...vertices) {
    final INumberGraph g = (INumberGraph)build();
    for(Number d : vertices)
      g.addVertex(d);
    return g;
  }

  public static IGraph create() {
    return build();
  }

  public static IStringGraph createForStrings() {
    return buildFromStrings();
  }

  public static INumberGraph createForNumbers() {
    return buildFromNumbers();
  }

  @Override
  public IGraph addVertex(IVertex vertex) {
    if (vertex == null)
      throw new IllegalArgumentException("dependency must not be null");
    vertices.add(vertex);
    return this;
  }

  @Override
  public IGraph removeVertex(IVertex vertex) {
    if (vertex == null)
      throw new IllegalArgumentException("dependency must not be null");
    vertices.remove(vertex);
    return this;
  }

  @Override
  public IGraph addEdge(IVertex from, IVertex to) {
    edges.add(new Edge(from, to));
    return this;
  }

  @Override
  public IGraph removeEdge(IVertex from, IVertex to) {
    edges.remove(new Edge(from, to));
    return this;
  }

  @Override
  public IStringGraph addVertex(String vertex) {
    addVertex(StringVertex.from(vertex));
    return this;
  }

  @Override
  public IStringGraph removeVertex(String vertex) {
    removeVertex(StringVertex.from(vertex));
    return this;
  }

  @Override
  public IStringGraph addEdge(String from, String to) {
    addEdge(StringVertex.from(from), StringVertex.from(to));
    return this;
  }

  @Override
  public IStringGraph removeEdge(String from, String to) {
    removeEdge(StringVertex.from(from), StringVertex.from(to));
    return this;
  }

  @Override
  public INumberGraph addVertex(Number vertex) {
    addVertex(NumberVertex.from(vertex));
    return this;
  }

  @Override
  public INumberGraph removeVertex(Number vertex) {
    removeVertex(NumberVertex.from(vertex));
    return this;
  }

  @Override
  public INumberGraph addEdge(Number from, Number to) {
    addEdge(NumberVertex.from(from), NumberVertex.from(to));
    return this;
  }

  @Override
  public INumberGraph removeEdge(Number from, Number to) {
    removeEdge(NumberVertex.from(from), NumberVertex.from(to));
    return this;
  }

  @Override
  public boolean validate() {
    //Ensure that every from/to in an edge is present in our set of vertices.
    //If we refer to one that isn't in there, then we've got a problem.
    for(IEdge r : edges) {
      if (!vertices.contains(r.getFrom()) || !vertices.contains(r.getTo()))
        return false;
    }
    return true;
  }

  @Override
  public IVertex[] sort() throws CyclicGraphException {
    return sort(new SimpleTopologicalSort());
  }

  @Override
  public IVertex[] sort(ITopologicalSortStrategy strategy) throws CyclicGraphException {
    if (strategy == null)
      throw new IllegalArgumentException("strategy cannot be null");
    if (!validate())
      throw new IllegalStateException("The graph is invalid. Please confirm that all vertices are present for every relationship.");
    return strategy.sort(new AdjacencyList(vertices, edges));
  }

  @Override
  public <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ITopologicalSortCallback<T> callback) {
    return sortAsync(new SimpleTopologicalSort(), callback, null);
  }

  @Override
  public <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ITopologicalSortCallback<T> callback, ITopologicalSortErrorCallback<T> errorCallback) {
    return sortAsync(new SimpleTopologicalSort(), callback, errorCallback);
  }

  @Override
  public <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ITopologicalSortStrategy strategy, ITopologicalSortCallback<T> callback) {
    return sortAsync(strategy, callback, null);
  }

  @Override
  public <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ITopologicalSortStrategy strategy, ITopologicalSortCallback<T> callback, ITopologicalSortErrorCallback<T> errorCallback) {
    ExecutorService executor = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() + 1));
    return sortAsync(executor, strategy, callback, errorCallback);
  }

  @Override
  public <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortCallback<T> callback) {
    return sortAsync(executor, new SimpleTopologicalSort(), callback, null);
  }

  @Override
  public <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortCallback<T> callback, ITopologicalSortErrorCallback<T> errorCallback) {
    return sortAsync(executor, new SimpleTopologicalSort(), callback, errorCallback);
  }

  @Override
  public <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortStrategy strategy, ITopologicalSortCallback<T> callback) {
    return sortAsync(executor, strategy, callback, null);
  }

  @Override
  public <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortStrategy strategy, ITopologicalSortCallback<T> callback, ITopologicalSortErrorCallback<T> errorCallback) {
    if (strategy == null)
      throw new IllegalArgumentException("strategy cannot be null");
    if (callback == null)
      throw new IllegalArgumentException("callback cannot be null");
    if (!validate())
      throw new IllegalStateException("The graph is invalid. Please confirm that all vertices are present for every relationship.");

    return strategy.sortAsync(executor, new AdjacencyList(vertices, edges), callback, errorCallback);
  }

  private static class Edge implements IEdge {
    public final IVertex from;
    public final IVertex to;

    public Edge(IVertex from, IVertex to) {
      if (from == null || to == null)
        throw new NullPointerException("from and to both cannot be null");

      this.from = from;
      this.to = to;
    }

    @Override
    public IVertex getFrom() {
      return from;
    }

    @Override
    public IVertex getTo() {
      return to;
    }

    @Override
    public int hashCode() {
      int result = from.hashCode();
      result = 31 * result + to.hashCode();
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Edge))
        return false;
      Edge e = (Edge)obj;

      return from.equals(e.from) && to.equals(e.to);
    }
  }

  private static class AdjacencyListPair extends Pair<IVertex, IVertex[]> implements IAdjacencyListPair {
    public AdjacencyListPair(IVertex value, IVertex[] outNeighbors) {
      super(value, outNeighbors);
    }

    @Override
    public IVertex getVertex() {
      return getValue1();
    }

    @Override
    public IVertex[] getOutNeighbors() {
      return getValue2();
    }
  }

  private static class AdjacencyList implements IAdjacencyList {
    private final List<IAdjacencyListPair> num_map;
    private final Map<IVertex, Integer> index_map;
    private final Map<IVertex, IVertex[]> vertex_map;

    public AdjacencyList(Set<IVertex> vertices, Set<IEdge> edges) {
      //Create 2 maps.
      //  One maps from a dependency to all of its vertices.
      //  The other just maps from an integer index to the vertices at that index.
      //  This means that num_map must preserve insertion order! Easy to do w/ an ArrayList.
      final List<IAdjacencyListPair> num_map = new ArrayList<IAdjacencyListPair>(vertices.size());
      final Map<IVertex, IVertex[]> vertex_map = new HashMap<IVertex, IVertex[]>(vertices.size(), 1.0f);
      final Map<IVertex, Integer> index_map = new HashMap<IVertex, Integer>(vertices.size(), 1.0f);

      for(IVertex d : vertices) {
        final LinkedList<IVertex> ll_to = new LinkedList<IVertex>();
        for(IEdge r : edges) {
          if (d.equals(r.getFrom())) {
            ll_to.add(r.getTo());
          }
        }
        IVertex[] arr_to = !ll_to.isEmpty() ? ll_to.toArray(new IVertex[ll_to.size()]) : EMPTY_VERTICES;

        vertex_map.put(d, arr_to);
        num_map.add(new AdjacencyListPair(d, arr_to));
        index_map.put(d, num_map.size() - 1);
      }

      //Ensure the maps are read-only at this point.
      this.num_map = Collections.unmodifiableList(num_map);
      this.index_map = Collections.unmodifiableMap(index_map);
      this.vertex_map = Collections.unmodifiableMap(vertex_map);
    }

    /**
     * Calculates an integer array where the value at each index is the number of times that dependency is referenced.
     */
    @Override
    public int[] calculateInDegrees() {
      final int[] in_degrees = new int[size()];
      for(int i = 0; i < size(); ++i) {
        IAdjacencyListPair p = get(i);
        IVertex d = p.getVertex();

        for(int j = 0; j < size(); ++j) {
          for(IVertex dep : get(j).getOutNeighbors()) {
            if (d.equals(dep))
              ++in_degrees[i];
          }
        }
      }
      return in_degrees;
    }

    @Override
    public IAdjacencyListPair get(int index) {
      return num_map.get(index);
    }

    @Override
    public IVertex[] get(IVertex vertex) {
      return vertex_map.get(vertex);
    }

    @Override
    public boolean isEmpty() {
      return vertex_map.isEmpty();
    }

    @Override
    public int size() {
      return vertex_map.size();
    }

    @Override
    public Iterator<IAdjacencyListPair> iterator() {
      return num_map.iterator();
    }

    @Override
    public int indexOf(IVertex vertex) {
      return index_map.get(vertex);
    }
  }
}
