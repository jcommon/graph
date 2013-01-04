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

package jcommon.graph.impl;

import jcommon.graph.ITopologicalSortInput;
import jcommon.graph.IVertex;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @see ITopologicalSortInput
 */
public class TopologicalSortInput<TVertex extends IVertex> implements ITopologicalSortInput<TVertex> {
  private final Map<TVertex, Object> inputs;

  /**
   * Instantiates a new instance of {@link TopologicalSortInput}.
   *
   * @param inputs An instance of a {@link Map} that maps between a {@link IVertex} and the output from
   *               processing it.
   */
  public TopologicalSortInput(final Map<TVertex, Object> inputs) {
    if (inputs == null)
      throw new IllegalArgumentException("inputs cannot be empty");

    //Make a read-only copy of the map.
    this.inputs = Collections.unmodifiableMap(new HashMap<TVertex, Object>(inputs));
  }

  /**
   * @see ITopologicalSortInput#isEmpty()
   */
  @Override
  public boolean isEmpty() {
    return inputs.isEmpty();
  }

  /**
   * @see ITopologicalSortInput#get(IVertex)
   */
  @Override
  public Object get(final TVertex vertex) {
    return inputs.get(vertex);
  }

  /**
   * @see ITopologicalSortInput#size()
   */
  @Override
  public int size() {
    return inputs.size();
  }

  /**
   * @see ITopologicalSortInput#containsVertex(IVertex)
   */
  @Override
  public boolean containsVertex(final TVertex vertex) {
    return inputs.containsKey(vertex);
  }

  /**
   * @see ITopologicalSortInput#inputs()
   */
  @Override
  public Iterable<Object> inputs() {
    return inputs.values();
  }

  /**
   * @see ITopologicalSortInput#getInputs()
   */
  @Override
  public Object[] getInputs() {
    return inputs.values().toArray(new Object[inputs.size()]);
  }

  @Override
  public String toString() {
    return inputs().toString();
  }
}
