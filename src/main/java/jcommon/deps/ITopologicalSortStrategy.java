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

package jcommon.deps;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Provides a strategy for implementing a topological sort of an {@link IGraph}.
 *
 * A {@link IGraph} is first transformed into an adjacency list of type {@link IAdjacencyList}. That is then used
 * to topologically sort the graph.
 *
 * Two versions are provided: one will synchronously traverse the graph and provide the ordered list of vertices to
 * visit and the second will asynchronously process the graph executing in parallel as many as possible given the size
 * of a thread pool and the ordering.
 *
 * @param <TVertex> Type of {@link IVertex} that a topological sort will operate on.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Topological_sorting">http://en.wikipedia.org/wiki/Topological_sorting</a>
 */
public interface ITopologicalSortStrategy<TVertex extends IVertex> {
  /**
   * Given an instance of {@link IAdjacencyList}, topologically sorts the graph.
   *
   * @param adjacencyList An instance of {@link IAdjacencyList}.
   * @return The topologically sorted {@link IGraph}.
   * @throws CyclicGraphException A {@link CyclicGraphException} is thrown if a cycle is detected during the sort.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Topological_sorting">http://en.wikipedia.org/wiki/Topological_sorting</a>
   */
  List<TVertex> sort(IAdjacencyList<TVertex> adjacencyList) throws CyclicGraphException;

  /**
   * Allows you to asynchronously and in-parallel process the vertices of a graph topologically. Care is taken to
   * prevent the processing of vertices before their parent vertices have completed processing.
   *
   * @param executor      An instance of {@link ExecutorService} that will be used to submit tasks for processing
   *                      vertices.
   * @param adjacencyList An instance of {@link IAdjacencyList}.
   * @param callback      An instance of {@link ITopologicalSortCallback} that will be called to process each vertex.
   *                      This may be called concurrently depending on the makeup of the graph.
   * @param errorCallback An instance of {@link ITopologicalSortErrorCallback} that will be called if an error during
   *                      processing occurs either in the
   *                      {@link ITopologicalSortCallback#handle(IVertex, ITopologicalSortCoordinator)} method or in the
   *                      {@link ITopologicalSortStrategy#sortAsync(java.util.concurrent.ExecutorService, IAdjacencyList, ITopologicalSortCallback, ITopologicalSortErrorCallback)}
   *                      method.
   * @return              An instance of {@link ITopologicalSortAsyncResult} that allows the caller to coordinate the
   *                      asynchronous processing of the graph.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Topological_sorting">http://en.wikipedia.org/wiki/Topological_sorting</a>
   */
  ITopologicalSortAsyncResult sortAsync(ExecutorService executor, IAdjacencyList<TVertex> adjacencyList, ITopologicalSortCallback<TVertex> callback, ITopologicalSortErrorCallback<TVertex> errorCallback);
}
