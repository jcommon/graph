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
 * Factory and implementation of a dependency graph that can topologically sort vertices that are {@link Object}s.
 *
 * @see DirectedAcyclicGraph
 */
public class ObjectGraph<TValue extends Object> extends DirectedAcyclicGraph<IVertex<TValue>, TValue> {
  /**
   * @see DirectedAcyclicGraph#DirectedAcyclicGraph()
   */
  protected ObjectGraph() {
    super();
  }

  /**
   * @see DirectedAcyclicGraph#copy()
   */
  public ObjectGraph<TValue> copyAsObjectGraph() {
    return copyGraph(this);
  }

  /**
   * @see DirectedAcyclicGraph#build(IVertex[])
   */
  public static <TValue extends Object> ObjectGraph<TValue> buildFromObjects(final TValue...vertices) {
    final ObjectGraph<TValue> g = new ObjectGraph<TValue>();
    for(TValue v : vertices)
      g.addVertex(v);
    return g;
  }

  /**
   * @see DirectedAcyclicGraph#create()
   */
  @SuppressWarnings("unchecked")
  public static <TValue extends Object> ObjectGraph<TValue> createForObjects() {
    return buildFromObjects();
  }

  /**
   * @see DirectedAcyclicGraph#addVertex(jcommon.graph.IVertex)
   */
  public ObjectGraph<TValue> addVertex(final TValue vertex) {
    super.addVertex(ObjectVertex.from(vertex));
    return this;
  }

  /**
   * @see DirectedAcyclicGraph#removeVertex(jcommon.graph.IVertex)
   */
  public ObjectGraph<TValue> removeVertex(final TValue vertex) {
    super.removeVertex(ObjectVertex.from(vertex));
    return this;
  }

  /**
   * @see DirectedAcyclicGraph#addEdge(jcommon.graph.IVertex, jcommon.graph.IVertex)
   */
  @SuppressWarnings("unchecked")
  public ObjectGraph<TValue> addEdge(final TValue from, final TValue to) {
    final IVertex<TValue> t = (to instanceof IVertex) ? (IVertex<TValue>)to : ObjectVertex.from(to);
    final IVertex<TValue> f = (from instanceof IVertex) ? (IVertex<TValue>)from : ObjectVertex.from(from);
    super.addEdge(f, t);
    return this;
  }

  /**
   * @see DirectedAcyclicGraph#removeEdge(jcommon.graph.IVertex, jcommon.graph.IVertex)
   */
  @SuppressWarnings("unchecked")
  public ObjectGraph<TValue> removeEdge(final TValue from, final TValue to) {
    final IVertex<TValue> t = (to instanceof IVertex) ? (IVertex<TValue>)to : ObjectVertex.from(to);
    final IVertex<TValue> f = (from instanceof IVertex) ? (IVertex<TValue>)from : ObjectVertex.from(from);
    super.removeEdge(f, t);
    return this;
  }
}
