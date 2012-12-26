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

/**
 * Factory and implementation of a dependency graph that can topologically sort vertices that are {@link Number}s.
 *
 * @see DependencyGraph
 */
public class NumberGraph extends DependencyGraph<NumberVertex> {
  protected NumberGraph() {
    super();
  }

  /**
   * @see DependencyGraph#copy()
   */
  public NumberGraph copyAsNumberGraph() {
    return copyDependencyGraph(this);
  }

  /**
   * @see DependencyGraph#build(IVertex[])
   */
  public static NumberGraph buildFromNumbers(Number...vertices) {
    final NumberGraph g = new NumberGraph();
    for(Number d : vertices)
      g.addVertex(d);
    return g;
  }

  /**
   * @see DependencyGraph#create()
   */
  public static NumberGraph createForNumbers() {
    return buildFromNumbers();
  }

  /**
   * @see DependencyGraph#addVertex(IVertex)
   */
  public NumberGraph addVertex(Number vertex) {
    addVertex(NumberVertex.from(vertex));
    return this;
  }

  /**
   * @see DependencyGraph#removeVertex(IVertex)
   */
  public NumberGraph removeVertex(Number vertex) {
    removeVertex(NumberVertex.from(vertex));
    return this;
  }

  /**
   * @see DependencyGraph#addEdge(IVertex, IVertex)
   */
  public NumberGraph addEdge(Number from, Number to) {
    addEdge(NumberVertex.from(from), NumberVertex.from(to));
    return this;
  }

  /**
   * @see DependencyGraph#removeEdge(IVertex, IVertex)
   */
  public NumberGraph removeEdge(Number from, Number to) {
    removeEdge(NumberVertex.from(from), NumberVertex.from(to));
    return this;
  }
}
