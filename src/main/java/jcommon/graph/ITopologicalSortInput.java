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

/**
 * Aggregates the output of out neighbor vertices as input to a vertex being processed.
 *
 * @param <TVertex> Type of {@link jcommon.graph.IVertex}.
 */
public interface ITopologicalSortInput<TVertex extends IVertex> {
  /**
   * Determines if the input is empty.
   *
   * @return <code>true</code> if the input is empty; <code>false</code> otherwise.
   */
  boolean isEmpty();

  /**
   * Gets the output of an out neighbor vertex.
   *
   * @param vertex The vertex whose output is desired.
   */
  Object get(TVertex vertex);

  /**
   * The size of the {@link ITopologicalSortInput}. This is the same as the number of in-degree vertices in the {@link IAdjacencyList}.
   *
   * @return An int representing the size of the {@link ITopologicalSortInput}.
   */
  int size();

  /**
   * Returns a boolean indicating if the input contains a key for the provided vertex.
   *
   * @param vertex The vertex for whom membership will be tested.
   * @return <code>true</code> if the provided vertex instance is a member of the input; <code>false</code> otherwise.
   */
  boolean containsVertex(TVertex vertex);

  /**
   * Generates an instance of {@link Iterable} that allows traversing the contents of the input.
   *
   * @return An instance of {@link Iterable} for traversing the contents of the input.
   */
  Iterable<Object> inputs();

  /**
   * Generates an array of the in-degree neighbor outputs as inputs for a vertex being processed.
   *
   * @return An array of the in-degree neighbor outputs as inputs for a vertex being processed.
   */
  Object[] getInputs();
}
