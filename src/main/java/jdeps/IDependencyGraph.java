package jdeps;

public interface IDependencyGraph {
  public static IDependency[] EMPTY_DEPENDENCIES = new IDependency[0];

  IDependencyGraph copy();

  IDependencyGraph addDependency(IDependency dependency);
  IDependencyGraph removeDependency(IDependency dependency);
  IDependencyGraph addRelationship(IDependency from, IDependency to);
  IDependencyGraph removeRelationship(IDependency from, IDependency to);

  boolean validate();

  IDependency[] sort() throws CyclicGraphException;
  IDependency[] sort(ITopologicalSortStrategy strategy) throws CyclicGraphException;
}
