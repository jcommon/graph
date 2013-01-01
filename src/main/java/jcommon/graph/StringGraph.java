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
 * Factory and implementation of a dependency graph that can topologically sort vertices that are {@link String}s.
 *
 * @see DirectedAcyclicGraph
 */
public class StringGraph extends DirectedAcyclicGraph<StringVertex> {
  protected StringGraph() {
    super();
  }

  /**
   * @see DirectedAcyclicGraph#copy()
   */
  public StringGraph copyAsStringGraph() {
    return copyGraph(this);
  }

  /**
   * @see DirectedAcyclicGraph#build(IVertex[])
   */
  public static StringGraph buildFromStrings(String...vertices) {
    final StringGraph g = new StringGraph();
    for(String d : vertices)
      g.addVertex(d);
    return g;
  }

  /**
   * @see DirectedAcyclicGraph#create()
   */
  public static StringGraph createForStrings() {
    return buildFromStrings();
  }

  /**
   * @see DirectedAcyclicGraph#addVertex(IVertex)
   */
  public StringGraph addVertex(String vertex) {
    addVertex(StringVertex.from(vertex));
    return this;
  }

  /**
   * @see DirectedAcyclicGraph#removeVertex(IVertex)
   */
  public StringGraph removeVertex(String vertex) {
    removeVertex(StringVertex.from(vertex));
    return this;
  }

  /**
   * @see DirectedAcyclicGraph#addEdge(IVertex, IVertex)
   */
  public StringGraph addEdge(String from, String to) {
    addEdge(StringVertex.from(from), StringVertex.from(to));
    return this;
  }

  /**
   * @see DirectedAcyclicGraph#removeEdge(IVertex, IVertex)
   */
  public StringGraph removeEdge(String from, String to) {
    removeEdge(StringVertex.from(from), StringVertex.from(to));
    return this;
  }
}
