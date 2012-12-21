package jdeps;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Factory and implementation of a dependency graph that can topologically sort its vertices.
 */
public class DependencyGraph<TVertex extends IVertex> implements Cloneable, IGraph<TVertex> {
  private Set<TVertex> vertices = new LinkedHashSet<TVertex>(5, 0.8f);
  private Set<IEdge<TVertex>> edges = new LinkedHashSet<IEdge<TVertex>>(8, 0.8f);

  protected DependencyGraph() {
  }

  @Override
  public Set<TVertex> getVertices() {
    return Collections.unmodifiableSet(vertices);
  }

  @Override
  public Set<IEdge<TVertex>> getEdges() {
    return Collections.unmodifiableSet(edges);
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return copyDependencyGraph(this);
  }

  @SuppressWarnings("unchecked")
  protected static <TVertex extends IVertex, TGraph extends DependencyGraph<TVertex>> TGraph copyDependencyGraph(TGraph graph) {
    try {
      final Class g_class = graph.getClass();
      final Constructor<? extends DependencyGraph<TVertex>> construct = g_class.getDeclaredConstructor();
      construct.setAccessible(true);

      final TGraph g = (TGraph)construct.newInstance();

      g.vertices = new LinkedHashSet<TVertex>(graph.vertices);
      g.edges = new LinkedHashSet<IEdge<TVertex>>(graph.edges);
      return g;
    } catch(Throwable t) {
      return null;
    }
  }

  public IGraph<TVertex> copy() {
    return copyDependencyGraph(this);
  }

  public static <TVertex extends IVertex> IGraph<TVertex> build(TVertex...vertices) {
    DependencyGraph<TVertex> g = new DependencyGraph<TVertex>();
    if (vertices != null) {
      for(TVertex d : vertices) {
        g.addVertex(d);
      }
    }
    return g;
  }

  public static <TVertex extends IVertex> IGraph<TVertex> create() {
    return build((TVertex[])null);
  }

  @Override
  public IGraph<TVertex> addVertex(TVertex vertex) {
    if (vertex == null)
      throw new IllegalArgumentException("vertex must not be null");
    vertices.add(vertex);
    return this;
  }

  @Override
  public IGraph<TVertex> removeVertex(TVertex vertex) {
    if (vertex == null)
      throw new IllegalArgumentException("vertex must not be null");
    vertices.remove(vertex);
    return this;
  }

  @Override
  public IGraph<TVertex> addEdge(TVertex from, TVertex to) {
    edges.add(new Edge<TVertex>(from, to));
    return this;
  }

  @Override
  public IGraph<TVertex> removeEdge(TVertex from, TVertex to) {
    edges.remove(new Edge<TVertex>(from, to));
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
  public List<TVertex> sort() throws CyclicGraphException {
    return sort(new SimpleTopologicalSort<TVertex>());
  }

  @Override
  public List<TVertex> sort(ITopologicalSortStrategy<TVertex> strategy) throws CyclicGraphException {
    if (strategy == null)
      throw new IllegalArgumentException("strategy cannot be null");
    if (!validate())
      throw new IllegalStateException("The graph is invalid. Please confirm that all vertices are present for every relationship.");
    return strategy.sort(new AdjacencyList<TVertex>(vertices, edges));
  }

  @Override
  public ITopologicalSortAsyncResult sortAsync(ITopologicalSortCallback<TVertex> callback) {
    return sortAsync(new SimpleTopologicalSort<TVertex>(), callback, null);
  }

  @Override
  public ITopologicalSortAsyncResult sortAsync(ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback) {
    return sortAsync(new SimpleTopologicalSort<TVertex>(), callback, errorCallback);
  }

  @Override
  public ITopologicalSortAsyncResult sortAsync(ITopologicalSortStrategy<TVertex> strategy, ITopologicalSortCallback<TVertex> callback) {
    return sortAsync(strategy, callback, null);
  }

  @Override
  public ITopologicalSortAsyncResult sortAsync(ITopologicalSortStrategy<TVertex> strategy, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback) {
    ExecutorService executor = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() + 1));
    ITopologicalSortAsyncResult result = sortAsync(executor, strategy, callback, errorCallback);

    //We don't explicity shutdown b/c we may not be done processing
    //at this point. If need to abort prematurely, the caller can
    //access the executor service from the .getExecutorService()
    //method.
    return result;
  }

  @Override
  public ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortCallback<TVertex> callback) {
    return sortAsync(executor, new SimpleTopologicalSort<TVertex>(), callback, null);
  }

  @Override
  public ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback) {
    return sortAsync(executor, new SimpleTopologicalSort<TVertex>(), callback, errorCallback);
  }

  @Override
  public ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortStrategy<TVertex> strategy, ITopologicalSortCallback<TVertex> callback) {
    return sortAsync(executor, strategy, callback, null);
  }

  @Override
  public ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortStrategy<TVertex> strategy, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback) {
    if (strategy == null)
      throw new IllegalArgumentException("strategy cannot be null");
    if (callback == null)
      throw new IllegalArgumentException("callback cannot be null");
    if (!validate())
      throw new IllegalStateException("The graph is invalid. Please confirm that all vertices are present for every relationship.");

    return strategy.sortAsync(executor, new AdjacencyList<TVertex>(vertices, edges), callback, errorCallback);
  }

  private static class Edge<TVertex extends IVertex> implements IEdge<TVertex> {
    public final TVertex from;
    public final TVertex to;

    public Edge(TVertex from, TVertex to) {
      if (from == null || to == null)
        throw new NullPointerException("from and to both cannot be null");

      this.from = from;
      this.to = to;
    }

    @Override
    public TVertex getFrom() {
      return from;
    }

    @Override
    public TVertex getTo() {
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

  private static class AdjacencyListPair<TVertex extends IVertex> extends Pair<TVertex, List<TVertex>> implements IAdjacencyListPair<TVertex> {
    public AdjacencyListPair(TVertex value, List<TVertex> outNeighbors) {
      super(value, outNeighbors);
    }

    @Override
    public TVertex getVertex() {
      return getValue1();
    }

    @Override
    public List<TVertex> getOutNeighbors() {
      return getValue2();
    }
  }

  private static class AdjacencyList<TVertex extends IVertex> implements IAdjacencyList<TVertex> {
    private final List<IAdjacencyListPair<TVertex>> num_map;
    private final Map<TVertex, Integer> index_map;
    private final Map<TVertex, List<TVertex>> vertex_map;

    public AdjacencyList(Set<TVertex> vertices, Set<IEdge<TVertex>> edges) {
      //Create 2 maps.
      //  One maps from a dependency to all of its vertices.
      //  The other just maps from an integer index to the vertices at that index.
      //  This means that num_map must preserve insertion order! Easy to do w/ an ArrayList.
      final List<IAdjacencyListPair<TVertex>> num_map = new ArrayList<IAdjacencyListPair<TVertex>>(vertices.size());
      final Map<TVertex, List<TVertex>> vertex_map = new HashMap<TVertex, List<TVertex>>(vertices.size(), 1.0f);
      final Map<TVertex, Integer> index_map = new HashMap<TVertex, Integer>(vertices.size(), 1.0f);
      final ArrayList<TVertex> EMPTY_VERTICES_ARRAYLIST = new ArrayList<TVertex>(0);

      for(TVertex d : vertices) {
        final ArrayList<TVertex> al_to = new ArrayList<TVertex>();
        for(IEdge<TVertex> r : edges) {
          if (d.equals(r.getFrom())) {
            al_to.add(r.getTo());
          }
        }

        ArrayList<TVertex> arr_to = !al_to.isEmpty() ? al_to : EMPTY_VERTICES_ARRAYLIST;

        vertex_map.put(d, arr_to);
        num_map.add(new AdjacencyListPair<TVertex>(d, arr_to));
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
        IAdjacencyListPair<TVertex> p = pairAt(i);
        TVertex d = p.getVertex();

        for(int j = 0; j < size(); ++j) {
          for(IVertex dep : pairAt(j).getOutNeighbors()) {
            if (d.equals(dep))
              ++in_degrees[i];
          }
        }
      }
      return in_degrees;
    }

    @Override
    public IAdjacencyListPair<TVertex> pairAt(int index) {
      return num_map.get(index);
    }

    @Override
    public List<TVertex> outNeighborsAt(int index) {
      IAdjacencyListPair<TVertex> pair = pairAt(index);
      return pair.getOutNeighbors();
    }

    @Override
    public List<TVertex> outNeighborsFor(TVertex vertex) {
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
    public Iterator<IAdjacencyListPair<TVertex>> iterator() {
      return num_map.iterator();
    }

    @Override
    public int indexOf(IVertex vertex) {
      Integer result = index_map.get(vertex);
      return (result != null) ? result : -1;
    }
  }
}
