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
import java.util.Set;

/**
 * @see ITopologicalSortInput
 */
public class TopologicalSortInput<TValue extends Object, TProcessedValue extends Object> implements ITopologicalSortInput<TValue, TProcessedValue> {
  private final boolean starting;
  private final Map<TValue, TProcessedValue> inputs;

  /**
   * Instantiates a new instance of {@link TopologicalSortInput}.
   *
   * @param inputs An instance of a {@link Map} that maps between a value and the output from
   *               processing it.
   */
  public TopologicalSortInput(final boolean starting, final Map<TValue, TProcessedValue> inputs) {
    if (inputs == null)
      throw new IllegalArgumentException("inputs cannot be empty");

    this.starting = starting;

    //Make a read-only copy of the map.
    this.inputs = Collections.unmodifiableMap(new HashMap<TValue, TProcessedValue>(inputs));
  }

  /**
   * @see ITopologicalSortInput#isStart()
   */
  @Override
  public boolean isStart() {
    return starting;
  }

  /**
   * @see ITopologicalSortInput#isEmpty()
   */
  @Override
  public boolean isEmpty() {
    return inputs.isEmpty();
  }

  /**
   * @see ITopologicalSortInput#get(Object)
   */
  @Override
  public TProcessedValue get(final TValue value) {
    return inputs.get(value);
  }

  /**
   * @see ITopologicalSortInput#first()
   */
  @Override
  public TProcessedValue first() {
    if (inputs.isEmpty())
      return null;
    return inputs.values().iterator().next();
  }

  /**
   * @see ITopologicalSortInput#size()
   */
  @Override
  public int size() {
    return inputs.size();
  }

  /**
   * @see ITopologicalSortInput#contains(Object)
   */
  @Override
  public boolean contains(final TValue value) {
    return inputs.containsKey(value);
  }

  /**
   * @see ITopologicalSortInput#inputs()
   */
  @Override
  public Iterable<TProcessedValue> inputs() {
    return inputs.values();
  }

  /**
   * @see ITopologicalSortInput#values()
   */
  @Override
  public Set<TValue> values() {
    return inputs.keySet();
  }

  @Override
  public String toString() {
    return inputs().toString();
  }
}
