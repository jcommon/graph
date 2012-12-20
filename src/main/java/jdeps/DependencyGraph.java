package jdeps;

import java.util.*;

public class DependencyGraph implements Cloneable, IDependencyGraph, IStringDependencyGraph, INumberDependencyGraph {
  private Set<IDependency> dependencies = new LinkedHashSet<IDependency>(5, 0.8f);
  private Set<IDependencyRelationship> edges = new LinkedHashSet<IDependencyRelationship>(8, 0.8f);

  private DependencyGraph() {
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return copyDependencyGraph();
  }

  public DependencyGraph copyDependencyGraph() {
    DependencyGraph g = new DependencyGraph();
    g.dependencies = new LinkedHashSet<IDependency>(dependencies);
    g.edges = new LinkedHashSet<IDependencyRelationship>(edges);
    return g;
  }

  public IDependencyGraph copy() {
    return copyDependencyGraph();
  }

  public IStringDependencyGraph copyAsStringGraph() {
    return copyDependencyGraph();
  }

  public INumberDependencyGraph copyAsNumberGraph() {
    return copyDependencyGraph();
  }

  public static IDependencyGraph build(IDependency...dependencies) {
    DependencyGraph g = new DependencyGraph();
    for(IDependency d : dependencies) {
      g.addDependency(d);
    }
    return g;
  }

  public static IStringDependencyGraph buildFromStrings(String...dependencies) {
    final IStringDependencyGraph g = (IStringDependencyGraph)build();
    for(String d : dependencies)
      g.addDependency(d);
    return g;
  }

  public static INumberDependencyGraph buildFromNumbers(Number...dependencies) {
    final INumberDependencyGraph g = (INumberDependencyGraph)build();
    for(Number d : dependencies)
      g.addDependency(d);
    return g;
  }

  public static IDependencyGraph create() {
    return build();
  }

  public static IStringDependencyGraph createForStrings() {
    return buildFromStrings();
  }

  public static INumberDependencyGraph createForNumbers() {
    return buildFromNumbers();
  }

  @Override
  public IDependencyGraph addDependency(IDependency dependency) {
    if (dependency == null)
      throw new IllegalArgumentException("dependency must not be null");
    dependencies.add(dependency);
    return this;
  }

  @Override
  public IDependencyGraph removeDependency(IDependency dependency) {
    if (dependency == null)
      throw new IllegalArgumentException("dependency must not be null");
    dependencies.remove(dependency);
    return this;
  }

  @Override
  public IDependencyGraph addRelationship(IDependency from, IDependency to) {
    edges.add(new Edge(from, to));
    return this;
  }

  @Override
  public IDependencyGraph removeRelationship(IDependency from, IDependency to) {
    edges.remove(new Edge(from, to));
    return this;
  }

  @Override
  public IStringDependencyGraph addDependency(String dependency) {
    addDependency(StringDependency.from(dependency));
    return this;
  }

  @Override
  public IStringDependencyGraph removeDependency(String dependency) {
    removeDependency(StringDependency.from(dependency));
    return this;
  }

  @Override
  public IStringDependencyGraph addRelationship(String from, String to) {
    addRelationship(StringDependency.from(from), StringDependency.from(to));
    return this;
  }

  @Override
  public IStringDependencyGraph removeRelationship(String from, String to) {
    removeRelationship(StringDependency.from(from), StringDependency.from(to));
    return this;
  }

  @Override
  public INumberDependencyGraph addDependency(Number dependency) {
    addDependency(NumberDependency.from(dependency));
    return this;
  }

  @Override
  public INumberDependencyGraph removeDependency(Number dependency) {
    removeDependency(NumberDependency.from(dependency));
    return this;
  }

  @Override
  public INumberDependencyGraph addRelationship(Number from, Number to) {
    addRelationship(NumberDependency.from(from), NumberDependency.from(to));
    return this;
  }

  @Override
  public INumberDependencyGraph removeRelationship(Number from, Number to) {
    removeRelationship(NumberDependency.from(from), NumberDependency.from(to));
    return this;
  }

  @Override
  public boolean validate() {
    //Ensure that every from/to in a relationship is present in our set of dependencies.
    //If we refer to one that isn't in there, then we've got a problem.
    for(IDependencyRelationship r : edges) {
      if (!dependencies.contains(r.getFrom()) || !dependencies.contains(r.getTo()))
        return false;
    }
    return true;
  }

  @Override
  public IDependency[] sort() throws CyclicGraphException {
    return sort(new SimpleTopologicalSort());
  }

  @Override
  public IDependency[] sort(ITopologicalSortStrategy strategy) throws CyclicGraphException {
    if (strategy == null)
      throw new IllegalArgumentException("strategy cannot be null");
    if (!validate())
      throw new IllegalStateException("The graph is invalid. Please confirm that all dependencies are present for every relationship.");
    return strategy.sort(new AdjacencyList(dependencies, edges));
  }

  private static class Edge implements IDependencyRelationship {
    public final IDependency from;
    public final IDependency to;

    public Edge(IDependency from, IDependency to) {
      if (from == null || to == null)
        throw new NullPointerException("from and to both cannot be null");

      this.from = from;
      this.to = to;
    }

    @Override
    public IDependency getFrom() {
      return from;
    }

    @Override
    public IDependency getTo() {
      return to;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Edge))
        return false;
      Edge e = (Edge)obj;
      return from.equals(e.from) && to.equals(e.to);
    }
  }

  private static class AdjacencyListPair extends Pair<IDependency, IDependency[]> implements IAdjacencyListPair {
    public AdjacencyListPair(IDependency value, IDependency[] dependencies) {
      super(value, dependencies);
    }

    @Override
    public IDependency getValue() {
      return getValue1();
    }

    @Override
    public IDependency[] getDependencies() {
      return getValue2();
    }
  }

  private static class AdjacencyList implements IAdjacencyList {
    private final List<IAdjacencyListPair> num_map;
    private final Map<IDependency, Integer> index_map;
    private final Map<IDependency, IDependency[]> dependency_map;

    public AdjacencyList(Set<IDependency> dependencies, Set<IDependencyRelationship> edges) {
      //Create 2 maps.
      //  One maps from a dependency to all of its dependencies.
      //  The other just maps from an integer index to the dependencies at that index.
      //  This means that num_map must preserve insertion order! Easy to do w/ an ArrayList.
      final List<IAdjacencyListPair> num_map = new ArrayList<IAdjacencyListPair>(dependencies.size());
      final Map<IDependency, IDependency[]> dependency_map = new HashMap<IDependency, IDependency[]>(dependencies.size(), 1.0f);
      final Map<IDependency, Integer> index_map = new HashMap<IDependency, Integer>(dependencies.size(), 1.0f);

      for(IDependency d : dependencies) {
        final LinkedList<IDependency> ll_to = new LinkedList<IDependency>();
        for(IDependencyRelationship r : edges) {
          if (d.equals(r.getFrom())) {
            ll_to.add(r.getTo());
          }
        }
        IDependency[] arr_to = !ll_to.isEmpty() ? ll_to.toArray(new IDependency[ll_to.size()]) : EMPTY_DEPENDENCIES;

        dependency_map.put(d, arr_to);
        num_map.add(new AdjacencyListPair(d, arr_to));
        index_map.put(d, num_map.size() - 1);
      }

      //Ensure the maps are read-only at this point.
      this.num_map = Collections.unmodifiableList(num_map);
      this.index_map = Collections.unmodifiableMap(index_map);
      this.dependency_map = Collections.unmodifiableMap(dependency_map);
    }

    /**
     * Calculates an integer array where the value at each index is the number of times that dependency is referenced.
     */
    @Override
    public int[] calculateInDegrees() {
      final int[] in_degrees = new int[size()];
      for(int i = 0; i < size(); ++i) {
        IAdjacencyListPair p = get(i);
        IDependency d = p.getValue();

        for(int j = 0; j < size(); ++j) {
          for(IDependency dep : get(j).getDependencies()) {
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
    public IDependency[] get(IDependency dependency) {
      return dependency_map.get(dependency);
    }

    @Override
    public boolean isEmpty() {
      return dependency_map.isEmpty();
    }

    @Override
    public int size() {
      return dependency_map.size();
    }

    @Override
    public Iterator<IAdjacencyListPair> iterator() {
      return num_map.iterator();
    }

    @Override
    public int indexOf(IDependency dependency) {
      return index_map.get(dependency);
    }
  }
}
