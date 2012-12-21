package jdeps;

import java.util.List;
import java.util.concurrent.ExecutorService;

public interface ITopologicalSortStrategy<TVertex extends IVertex> {
  List<TVertex> sort(IAdjacencyList<TVertex> adjacencyList) throws CyclicGraphException;
  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, IAdjacencyList<TVertex> adjacencyList, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);
}
