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
import java.util.Map;
import java.util.Set;

/**
 * An adjacency list is essentially an array of size n where A[i] is the list of out-neighbors of node i.
 * Please see the following for a more thorough explanation:
 *   <a href="http://www.cs.cmu.edu/~avrim/451f08/lectures/lect1002.pdf">http://www.cs.cmu.edu/~avrim/451f08/lectures/lect1002.pdf</a>
 *
 * Implementations of this interface must be thread safe.
 *
 * @param <TVertex> Type of {@link IVertex}.
 *
 * @see <a href="http://www.cs.cmu.edu/~avrim/451f08/lectures/lect1002.pdf">http://www.cs.cmu.edu/~avrim/451f08/lectures/lect1002.pdf</a>
 */
public interface IAdjacencyList<TVertex extends IVertex<TValue>, TValue extends Object, TProcessedValue extends Object> extends Iterable<IAdjacencyListPair<TVertex>> {
  /**
   * Determines if the adjacency list is empty.
   *
   * @return <code>true</code> if the adjacency list is empty; <code>false</code> otherwise.
   */
  boolean isEmpty();

  /**
   * Gets the list of out-neighbors for a given vertex of <code>TVertex</code>.
   *
   * @param vertex A vertex of <code>TVertex</code> for whom we wish to retrieve its out-neighbors.
   * @return A list of <code>TVertex</code> instances who are the out-neighbors for the provided vertex argument.
   *
   * @see IAdjacencyListPair#getOutNeighbors()
   */
  List<TVertex> outNeighborsFor(TVertex vertex);

  /**
   * Retrieves an instance of {@link IAdjacencyListPair} at the specified index.
   *
   * @param index The index in the list for which we wish to get the associated {@link IAdjacencyListPair}.
   * @return An instance of {@link IAdjacencyListPair} if found; otherwise <code>null</code>.
   *
   * @see IAdjacencyListPair
   */
  IAdjacencyListPair<TVertex> pairAt(int index);

  /**
   * Gets the list of out-neighbors for a given vertex of <code>TVertex</code> at the provided index argument.
   *
   * @param index The index in the list for which we wish to get the associated out-neighbors.
   * @return A list of <code>TVertex</code> instances who are the out-neighbors for an instance of <code>TVertex</code>
   *         at the specified index.
   *
   * @see IAdjacencyListPair#getOutNeighbors()
   */
  List<TVertex> outNeighborsAt(int index);

  /**
   * Find the index in the {@link IAdjacencyList} at which the provided vertex argument can be found.
   *
   * @param vertex An instance of {@link IVertex} of <code>TVertex</code>.
   * @return The index in the {@link IAdjacencyList} at which the provided vertex argument can be found.
   *         <code>-1</code> if the vertex cannot be found.
   */
  int indexOf(TVertex vertex);

  /**
   * Scans the {@link IAdjacencyList} and for each vertex it counts the number of other vertices referencing it.
   *
   * @return An int array of size {@link IAdjacencyList#size()} representing the in-degrees for each vertex.
   */
  int[] calculateInDegrees();

  /**
   * The size of the {@link IAdjacencyList}. This is the same as the number of vertices in the {@link IGraph}.
   *
   * @return An int representing the size of the {@link IAdjacencyList}.
   */
  int size();

  /**
   * Provides a {@link Set} that contains vertices who have no out neighbors.
   *
   * @return A {@link Set} containing vertices with no out neighbors.
   */
  Set<TVertex> getEndingVertices();

  /**
   * Determines if the provided vertex parameter is in the set of ending vertices.
   *
   * @param vertex The vertex for whom you wish to test for set membership.
   * @return <code>true</code> if the vertex is in the set of ending vertices; <code>false</code> otherwise.
   *
   * @see #getEndingVertices()
   */
  boolean isEndingVertex(TVertex vertex);

  /**
   * Creates an instance of a {@link Map} that's properly sized for holding a
   * mapping of values to their processed result.
   *
   * @return An instance of {@link Map} mapping values to their processed result.
   */
  Map<TValue, TProcessedValue> createResultMap();
}
