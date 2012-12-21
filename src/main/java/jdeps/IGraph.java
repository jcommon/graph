package jdeps;

import java.util.List;
import java.util.concurrent.ExecutorService;

public interface IGraph<TVertex extends IVertex> {
  public static final IVertex[] EMPTY_VERTICES = new IVertex[0];

  IGraph<TVertex> copy();

  IGraph<TVertex> addVertex(TVertex vertex);
  IGraph<TVertex> removeVertex(TVertex vertex);
  IGraph<TVertex> addEdge(TVertex from, TVertex to);
  IGraph<TVertex> removeEdge(TVertex from, TVertex to);

  boolean validate();

  List<TVertex> sort() throws CyclicGraphException;
  List<TVertex> sort(ITopologicalSortStrategy strategy) throws CyclicGraphException;

  ITopologicalSortAsyncResult sortAsync(ITopologicalSortCallback<TVertex> callback);
  ITopologicalSortAsyncResult sortAsync(ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);
  ITopologicalSortAsyncResult sortAsync(ITopologicalSortStrategy strategy, ITopologicalSortCallback<TVertex> callback);
  ITopologicalSortAsyncResult sortAsync(ITopologicalSortStrategy strategy, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);

  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortCallback<TVertex> callback);
  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);
  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortStrategy strategy, ITopologicalSortCallback<TVertex> callback);
  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortStrategy strategy, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);
}
