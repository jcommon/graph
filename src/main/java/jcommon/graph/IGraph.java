/*
  Copyright (C) 2012-2013 the original author or authors.

  See the LICENSE.txt file distributed with this work for additional
  information regarding copyright ownership.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package jcommon.graph;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * This is a simple graph holding {@link IVertex} vertices and {@link IEdge} edges connecting them.
 * It is intended to be used in a builder-style pattern and then operations such as {@link #sort()} may be called
 * on the graph.
 *
 * Of particular note is the {@link #sortAsync(ITopologicalSortCallback)} method which allows you to asynchronously and
 * in-parallel process the vertices of a graph topologically. Care is taken to prevent the processing of vertices before
 * their parent vertices have completed processing.
 *
 * @param <TVertex> Type of {@link IVertex} vertices that this graph contains.
 */
public interface IGraph<TVertex extends IVertex> {
  /**
   * Reference for an array of empty vertices that can be reused instead
   * of having to allocate a new empty array on the heap every time.
   */
  IVertex[] EMPTY_VERTICES = new IVertex[0];

  /**
   * Retrieves the set of {@link IVertex} vertices of <code>TVertex</code> represented by this {@link IGraph}.
   *
   * @return The set of {@link IVertex} vertices of <code>TVertex</code> represented by this {@link IGraph}.
   */
  Set<TVertex> getVertices();

  /**
   * Retrieves the set of {@link IEdge} edges represented by this {@link IGraph}.
   *
   * @return The set of {@link IEdge} edges represented by this {@link IGraph}.
   */
  Set<IEdge<TVertex>> getEdges();

  /**
   * More type-safe version of {@link Object#clone()}.
   *
   * @return A deep copy of this instance of {@link IGraph}.
   */
  IGraph<TVertex> copy();

  /**
   * Adds a new {@link IVertex} instance of <code>TVertex</code> to this {@link IGraph}.
   *
   * @param vertex The instance of {@link IVertex} of <code>TVertex</code> to add to this {@link IGraph}.
   * @return The current instance of {@link IGraph} for use in a builder-style pattern.
   */
  IGraph<TVertex> addVertex(TVertex vertex);

  /**
   * Removes an {@link IVertex} instance of <code>TVertex</code> from this {@link IGraph}.
   *
   * @param vertex The instance of {@link IVertex} of <code>TVertex</code> to remove from this {@link IGraph}.
   * @return The current instance of {@link IGraph} for use in a builder-style pattern.
   */
  IGraph<TVertex> removeVertex(TVertex vertex);

  /**
   * Adds a new {@link IEdge} instance to this {@link IGraph}.
   *
   * @param from An instance of {@link IVertex} of <code>TVertex</code> that begins the edge.
   * @param to An instance of {@link IVertex} of <code>TVertex</code> that the edge is pointing to.
   * @return The current instance of {@link IGraph} for use in a builder-style pattern.
   */
  IGraph<TVertex> addEdge(TVertex from, TVertex to);

  /**
   * Removes an existing edge from this {@link IGraph}.
   *
   * @param from An instance of {@link IVertex} of <code>TVertex</code> that begins the edge.
   * @param to An instance of {@link IVertex} of <code>TVertex</code> that the edge is pointing to.
   * @return The current instance of {@link IGraph} for use in a builder-style pattern.
   */
  IGraph<TVertex> removeEdge(TVertex from, TVertex to);

  /**
   * Does a simple sanity check on the structure of the graph. Should not be called until the graph has been
   * completely structured. Notably a cycle check is not done at this point. That's only detected upon a topological
   * sort of the graph.
   *
   * @return <code>true</code> if the graph passes a simple sanity check; <code>false</code> otherwise.
   */
  boolean validate();

  /**
   * Provides a topologically sorted list of {@link IVertex} vertices.
   *
   * @return A topologically sorted list of {@link IVertex} vertices in the current graph.
   * @throws CyclicGraphException A {@link CyclicGraphException} is thrown if a cycle is detected during the sort.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Topological_sorting">http://en.wikipedia.org/wiki/Topological_sorting</a>
   */
  List<TVertex> sort() throws CyclicGraphException;

  /**
   * Provides a topologically sorted list of {@link IVertex} vertices.
   *
   * It's strongly recommended that you use {@link #sort()} in most cases unless you require an alternative approach to
   * the topological sorting algorithm.
   *
   * @param strategy An instance of {@link ITopologicalSortStrategy} that will perform the sort.
   * @return A topologically sorted list of {@link IVertex} vertices in the current graph.
   * @throws CyclicGraphException A {@link CyclicGraphException} is thrown if a cycle is detected during the sort.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Topological_sorting">http://en.wikipedia.org/wiki/Topological_sorting</a>
   */
  List<TVertex> sort(ITopologicalSortStrategy<TVertex> strategy) throws CyclicGraphException;

  /**
   * Allows you to asynchronously and in-parallel process the vertices of a graph topologically. Care is taken to
   * prevent the processing of vertices before their parent vertices have completed processing.
   *
   * @param callback An instance of {@link ITopologicalSortCallback} that will be called to process each vertex.
   *                 This may be called concurrently depending on the makeup of the graph.
   * @return         An instance of {@link ITopologicalSortAsyncResult} that allows the caller to coordinate the
   *                 asynchronous processing of the graph.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Topological_sorting">http://en.wikipedia.org/wiki/Topological_sorting</a>
   */
  ITopologicalSortAsyncResult sortAsync(ITopologicalSortCallback<TVertex> callback);

  /**
   * Allows you to asynchronously and in-parallel process the vertices of a graph topologically. Care is taken to
   * prevent the processing of vertices before their parent vertices have completed processing.
   *
   * @param callback      An instance of {@link ITopologicalSortCallback} that will be called to process each vertex.
   *                      This may be called concurrently depending on the makeup of the graph.
   * @param errorCallback An instance of {@link ITopologicalSortErrorCallback} that will be called if an error during
   *                      processing occurs either in the
   *                      {@link ITopologicalSortCallback#handle(IVertex, ITopologicalSortInput, ITopologicalSortCoordinator)} method or in the
   *                      {@link ITopologicalSortStrategy#sortAsync(java.util.concurrent.ExecutorService, IAdjacencyList, ITopologicalSortCallback, ITopologicalSortErrorCallback)}
   *                      method.
   * @return              An instance of {@link ITopologicalSortAsyncResult} that allows the caller to coordinate the
   *                      asynchronous processing of the graph.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Topological_sorting">http://en.wikipedia.org/wiki/Topological_sorting</a>
   */
  ITopologicalSortAsyncResult sortAsync(ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);

  /**
   * Allows you to asynchronously and in-parallel process the vertices of a graph topologically. Care is taken to
   * prevent the processing of vertices before their parent vertices have completed processing.
   *
   * It's strongly recommended that you use {@link #sortAsync(ITopologicalSortCallback)} in most cases unless you
   * require an alternative approach to the topological sorting algorithm.
   *
   * @param strategy An instance of {@link ITopologicalSortStrategy} that will perform the sort.
   * @param callback An instance of {@link ITopologicalSortCallback} that will be called to process each vertex.
   *                 This may be called concurrently depending on the makeup of the graph.
   * @return         An instance of {@link ITopologicalSortAsyncResult} that allows the caller to coordinate the
   *                 asynchronous processing of the graph.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Topological_sorting">http://en.wikipedia.org/wiki/Topological_sorting</a>
   */
  ITopologicalSortAsyncResult sortAsync(ITopologicalSortStrategy<TVertex> strategy, ITopologicalSortCallback<TVertex> callback);

  /**
   * Allows you to asynchronously and in-parallel process the vertices of a graph topologically. Care is taken to
   * prevent the processing of vertices before their parent vertices have completed processing.
   *
   * It's strongly recommended that you use {@link #sortAsync(ITopologicalSortCallback, ITopologicalSortErrorCallback)}
   * in most cases unless you require an alternative approach to the topological sorting algorithm.
   *
   * @param strategy      An instance of {@link ITopologicalSortStrategy} that will perform the sort.
   * @param callback      An instance of {@link ITopologicalSortCallback} that will be called to process each vertex.
   *                      This may be called concurrently depending on the makeup of the graph.
   * @param errorCallback An instance of {@link ITopologicalSortErrorCallback} that will be called if an error during
   *                      processing occurs either in the
   *                      {@link ITopologicalSortCallback#handle(IVertex, ITopologicalSortInput, ITopologicalSortCoordinator)} method or in the
   *                      {@link ITopologicalSortStrategy#sortAsync(java.util.concurrent.ExecutorService, IAdjacencyList, ITopologicalSortCallback, ITopologicalSortErrorCallback)}
   *                      method.
   * @return              An instance of {@link ITopologicalSortAsyncResult} that allows the caller to coordinate the
   *                      asynchronous processing of the graph.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Topological_sorting">http://en.wikipedia.org/wiki/Topological_sorting</a>
   */
  ITopologicalSortAsyncResult sortAsync(ITopologicalSortStrategy<TVertex> strategy, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);

  /**
   * Allows you to asynchronously and in-parallel process the vertices of a graph topologically. Care is taken to
   * prevent the processing of vertices before their parent vertices have completed processing.
   *
   * @param executor An instance of {@link ExecutorService} that will be used to submit tasks for processing vertices.
   * @param callback An instance of {@link ITopologicalSortCallback} that will be called to process each vertex.
   *                 This may be called concurrently depending on the makeup of the graph.
   * @return         An instance of {@link ITopologicalSortAsyncResult} that allows the caller to coordinate the
   *                 asynchronous processing of the graph.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Topological_sorting">http://en.wikipedia.org/wiki/Topological_sorting</a>
   */
  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortCallback<TVertex> callback);

  /**
   * Allows you to asynchronously and in-parallel process the vertices of a graph topologically. Care is taken to
   * prevent the processing of vertices before their parent vertices have completed processing.
   *
   * @param executor      An instance of {@link ExecutorService} that will be used to submit tasks for processing
   *                      vertices.
   * @param callback      An instance of {@link ITopologicalSortCallback} that will be called to process each vertex.
   *                      This may be called concurrently depending on the makeup of the graph.
   * @param errorCallback An instance of {@link ITopologicalSortErrorCallback} that will be called if an error during
   *                      processing occurs either in the
   *                      {@link ITopologicalSortCallback#handle(IVertex, ITopologicalSortInput, ITopologicalSortCoordinator)} method or in the
   *                      {@link ITopologicalSortStrategy#sortAsync(java.util.concurrent.ExecutorService, IAdjacencyList, ITopologicalSortCallback, ITopologicalSortErrorCallback)}
   *                      method.
   * @return              An instance of {@link ITopologicalSortAsyncResult} that allows the caller to coordinate the
   *                      asynchronous processing of the graph.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Topological_sorting">http://en.wikipedia.org/wiki/Topological_sorting</a>
   */
  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);

  /**
   * Allows you to asynchronously and in-parallel process the vertices of a graph topologically. Care is taken to
   * prevent the processing of vertices before their parent vertices have completed processing.
   *
   * It's strongly recommended that you use {@link #sortAsync(java.util.concurrent.ExecutorService, ITopologicalSortCallback)}
   * in most cases unless you require an alternative approach to the topological sorting algorithm.
   *
   * @param executor      An instance of {@link ExecutorService} that will be used to submit tasks for processing
   *                      vertices.
   * @param strategy      An instance of {@link ITopologicalSortStrategy} that will perform the sort.
   * @param callback      An instance of {@link ITopologicalSortCallback} that will be called to process each vertex.
   *                      This may be called concurrently depending on the makeup of the graph.
   * @return              An instance of {@link ITopologicalSortAsyncResult} that allows the caller to coordinate the
   *                      asynchronous processing of the graph.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Topological_sorting">http://en.wikipedia.org/wiki/Topological_sorting</a>
   */
  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortStrategy<TVertex> strategy, ITopologicalSortCallback<TVertex> callback);

  /**
   * Allows you to asynchronously and in-parallel process the vertices of a graph topologically. Care is taken to
   * prevent the processing of vertices before their parent vertices have completed processing.
   *
   * It's strongly recommended that you use {@link #sortAsync(java.util.concurrent.ExecutorService, ITopologicalSortCallback, ITopologicalSortErrorCallback)}
   * in most cases unless you require an alternative approach to the topological sorting algorithm.
   *
   * @param executor      An instance of {@link ExecutorService} that will be used to submit tasks for processing
   *                      vertices.
   * @param strategy      An instance of {@link ITopologicalSortStrategy} that will perform the sort.
   * @param callback      An instance of {@link ITopologicalSortCallback} that will be called to process each vertex.
   *                      This may be called concurrently depending on the makeup of the graph.
   * @param errorCallback An instance of {@link ITopologicalSortErrorCallback} that will be called if an error during
   *                      processing occurs either in the
   *                      {@link ITopologicalSortCallback#handle(IVertex, ITopologicalSortInput, ITopologicalSortCoordinator)} method or in the
   *                      {@link ITopologicalSortStrategy#sortAsync(java.util.concurrent.ExecutorService, IAdjacencyList, ITopologicalSortCallback, ITopologicalSortErrorCallback)}
   *                      method.
   * @return              An instance of {@link ITopologicalSortAsyncResult} that allows the caller to coordinate the
   *                      asynchronous processing of the graph.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Topological_sorting">http://en.wikipedia.org/wiki/Topological_sorting</a>
   */
  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, ITopologicalSortStrategy<TVertex> strategy, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);
}
