package jdeps;

import java.util.concurrent.ExecutorService;

public interface IGraph {
  public static IVertex[] EMPTY_VERTICES = new IVertex[0];

  IGraph copy();

  IGraph addVertex(IVertex vertex);
  IGraph removeVertex(IVertex vertex);
  IGraph addEdge(IVertex from, IVertex to);
  IGraph removeEdge(IVertex from, IVertex to);

  boolean validate();

  IVertex[] sort() throws CyclicGraphException;
  IVertex[] sort(ITopologicalSortStrategy strategy) throws CyclicGraphException;

  <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ITopologicalSortCallback<T> callback);
  <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ITopologicalSortCallback<T> callback, ITopologicalSortErrorCallback<T> errorCallback);
  <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ITopologicalSortStrategy strategy, ITopologicalSortCallback<T> callback);
  <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ITopologicalSortStrategy strategy, ITopologicalSortCallback<T> callback, ITopologicalSortErrorCallback<T> errorCallback);

  <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortCallback<T> callback);
  <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortCallback<T> callback, ITopologicalSortErrorCallback<T> errorCallback);
  <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortStrategy strategy, ITopologicalSortCallback<T> callback);
  <T extends IVertex> ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortStrategy strategy, ITopologicalSortCallback<T> callback, ITopologicalSortErrorCallback<T> errorCallback);
}
