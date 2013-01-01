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
 * Factory and implementation of a dependency graph that can topologically sort vertices that are {@link Object}s.
 *
 * @see DirectedAcyclicGraph
 */
public class ObjectGraph extends DirectedAcyclicGraph<ObjectVertex> {
  protected ObjectGraph() {
    super();
  }

  /**
   * @see DirectedAcyclicGraph#copy()
   */
  public ObjectGraph copyAsObjectGraph() {
    return copyGraph(this);
  }

  /**
   * @see DirectedAcyclicGraph#build(jcommon.deps.IVertex[])
   */
  public static ObjectGraph buildFromObjects(Object...vertices) {
    final ObjectGraph g = new ObjectGraph();
    for(Object v : vertices)
      g.addVertex(v);
    return g;
  }

  /**
   * @see DirectedAcyclicGraph#create()
   */
  public static ObjectGraph createForObjects() {
    return buildFromObjects();
  }

  /**
   * @see DirectedAcyclicGraph#addVertex(jcommon.deps.IVertex)
   */
  public ObjectGraph addVertex(Object vertex) {
    addVertex(ObjectVertex.from(vertex));
    return this;
  }

  /**
   * @see DirectedAcyclicGraph#removeVertex(jcommon.deps.IVertex)
   */
  public ObjectGraph removeVertex(Object vertex) {
    removeVertex(ObjectVertex.from(vertex));
    return this;
  }

  /**
   * @see DirectedAcyclicGraph#addEdge(jcommon.deps.IVertex, jcommon.deps.IVertex)
   */
  public ObjectGraph addEdge(ObjectVertex from, ObjectVertex to) {
    addEdge(ObjectVertex.from(from), ObjectVertex.from(to));
    return this;
  }

  /**
   * @see DirectedAcyclicGraph#removeEdge(jcommon.deps.IVertex, jcommon.deps.IVertex)
   */
  public ObjectGraph removeEdge(ObjectVertex from, ObjectVertex to) {
    removeEdge(ObjectVertex.from(from), ObjectVertex.from(to));
    return this;
  }
}
