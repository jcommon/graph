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

import jcommon.graph.IAdjacencyListPair;
import jcommon.graph.IVertex;
import jcommon.graph.Pair;

import java.util.List;

/**
 * @see IAdjacencyListPair
 */
public class AdjacencyListPair<TVertex extends IVertex> extends Pair<TVertex, List<TVertex>> implements IAdjacencyListPair<TVertex> {
  /**
   * @see IAdjacencyListPair
   */
  public AdjacencyListPair(final TVertex value, final List<TVertex> outNeighbors) {
    super(value, outNeighbors);
  }

  /**
   * @see IAdjacencyListPair#getVertex()
   */
  @Override
  public TVertex getVertex() {
    return getValue1();
  }

  /**
   * @see IAdjacencyListPair#getOutNeighbors()
   */
  @Override
  public List<TVertex> getOutNeighbors() {
    return getValue2();
  }
}
