package jdeps;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public interface IGraph<TVertex extends IVertex> {
  public static final IVertex[] EMPTY_VERTICES = new IVertex[0];

  Set<TVertex> getVertices();
  Set<IEdge<TVertex>> getEdges();

  IGraph<TVertex> copy();

  IGraph<TVertex> addVertex(TVertex vertex);
  IGraph<TVertex> removeVertex(TVertex vertex);
  IGraph<TVertex> addEdge(TVertex from, TVertex to);
  IGraph<TVertex> removeEdge(TVertex from, TVertex to);

  boolean validate();

  List<TVertex> sort() throws CyclicGraphException;
  List<TVertex> sort(ITopologicalSortStrategy<TVertex> strategy) throws CyclicGraphException;

  ITopologicalSortAsyncResult sortAsync(ITopologicalSortCallback<TVertex> callback);
  ITopologicalSortAsyncResult sortAsync(ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);
  ITopologicalSortAsyncResult sortAsync(ITopologicalSortStrategy<TVertex> strategy, ITopologicalSortCallback<TVertex> callback);
  ITopologicalSortAsyncResult sortAsync(ITopologicalSortStrategy<TVertex> strategy, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);

  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortCallback<TVertex> callback);
  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);
  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortStrategy<TVertex> strategy, ITopologicalSortCallback<TVertex> callback);
  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortStrategy<TVertex> strategy, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);
}
