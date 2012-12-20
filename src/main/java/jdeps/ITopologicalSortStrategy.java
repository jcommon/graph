package jdeps;

public interface ITopologicalSortStrategy {
  IDependency[] sort(IAdjacencyList adjacencyList) throws CyclicGraphException;
}
