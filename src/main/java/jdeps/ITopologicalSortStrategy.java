package jdeps;

import java.util.concurrent.ExecutorService;

public interface ITopologicalSortStrategy {
  IVertex[] sort(IAdjacencyList adjacencyList) throws CyclicGraphException;
  <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ExecutorService executor, IAdjacencyList adjacencyList, ITopologicalSortCallback<T> callback, ITopologicalSortErrorCallback<T> errorCallback);
}
