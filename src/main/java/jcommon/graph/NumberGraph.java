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
 * Factory and implementation of a dependency graph that can topologically sort vertices that are {@link Number}s.
 *
 * @see DirectedAcyclicGraph
 */
public class NumberGraph extends DirectedAcyclicGraph<NumberVertex, Number> {
  /**
   * @see DirectedAcyclicGraph#DirectedAcyclicGraph()
   */
  protected NumberGraph() {
    super();
  }

  /**
   * @see DirectedAcyclicGraph#copy()
   */
  public NumberGraph copyAsNumberGraph() {
    return copyGraph(this);
  }

  /**
   * @see DirectedAcyclicGraph#build(IVertex[])
   */
  public static NumberGraph buildFromNumbers(final Number...vertices) {
    final NumberGraph g = new NumberGraph();
    for(Number d : vertices)
      g.addVertex(d);
    return g;
  }

  /**
   * @see DirectedAcyclicGraph#create()
   */
  public static NumberGraph createForNumbers() {
    return buildFromNumbers();
  }

  /**
   * @see DirectedAcyclicGraph#addVertex(IVertex)
   */
  public NumberGraph addVertex(final Number vertex) {
    super.addVertex(NumberVertex.from(vertex));
    return this;
  }

  /**
   * @see DirectedAcyclicGraph#removeVertex(IVertex)
   */
  public NumberGraph removeVertex(final Number vertex) {
    super.removeVertex(NumberVertex.from(vertex));
    return this;
  }

  /**
   * @see DirectedAcyclicGraph#addEdge(IVertex, IVertex)
   */
  public NumberGraph addEdge(final Number from, final Number to) {
    super.addEdge(NumberVertex.from(from), NumberVertex.from(to));
    return this;
  }

  /**
   * @see DirectedAcyclicGraph#removeEdge(IVertex, IVertex)
   */
  public NumberGraph removeEdge(final Number from, final Number to) {
    super.removeEdge(NumberVertex.from(from), NumberVertex.from(to));
    return this;
  }
}
