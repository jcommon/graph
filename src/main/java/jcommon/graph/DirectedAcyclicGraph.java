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

import jcommon.graph.impl.AdjacencyList;
import jcommon.graph.impl.Edge;
import jcommon.graph.impl.SimpleTopologicalSort;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Factory and implementation of a dependency graph that can topologically sort its vertices.
 *
 * @param <TVertex> Type of {@link IVertex} vertices that this graph contains.
 *
 * @see IGraph
 */
public class DirectedAcyclicGraph<TVertex extends IVertex<TValue>, TValue extends Object, TProcessedValue extends Object> implements Cloneable, IGraph<TVertex, TValue, TProcessedValue> {
  private Set<TVertex> vertices = new LinkedHashSet<TVertex>(5, 0.8f);
  private Set<IEdge<TVertex>> edges = new LinkedHashSet<IEdge<TVertex>>(8, 0.8f);

  /**
   * Protected constructor to prevent public instantiation.
   */
  protected DirectedAcyclicGraph() {
  }

  /**
   * @see IGraph#getVertices()
   */
  @Override
  public Set<TVertex> getVertices() {
    return Collections.unmodifiableSet(vertices);
  }

  /**
   * @see IGraph#getEdges()
   */
  @Override
  public Set<IEdge<TVertex>> getEdges() {
    return Collections.unmodifiableSet(edges);
  }

  /**
   * @see IGraph#clone()
   */
  @Override
  protected Object clone() throws CloneNotSupportedException {
    return copyGraph(this);
  }

  /**
   * Copies the {@link DirectedAcyclicGraph} portion of a class that extends {@link DirectedAcyclicGraph}. Anything else that
   * is specific to a superclass should be added to the returned instance.
   *
   * @param graph An instance of {@link DirectedAcyclicGraph} that needs copying.
   * @param <TVertex> Type of {@link IVertex} of the graph vertices.
   * @param <TGraph> Type of {@link DirectedAcyclicGraph} of the graph being copied.
   * @return A new instance of type {@link TGraph} with {@link DirectedAcyclicGraph} specific fields already filled in.
   */
  @SuppressWarnings("unchecked")
  protected static <TGraph extends DirectedAcyclicGraph<TVertex, TValue, TProcessedValue>, TVertex extends IVertex<TValue>, TValue extends Object, TProcessedValue extends Object> TGraph copyGraph(final TGraph graph) {
    try {
      final Class g_class = graph.getClass();
      final Constructor<? extends DirectedAcyclicGraph<TVertex, TValue, TProcessedValue>> construct = g_class.getDeclaredConstructor();
      construct.setAccessible(true);

      final TGraph g = (TGraph)construct.newInstance();

      g.vertices = new LinkedHashSet<TVertex>(graph.vertices);
      g.edges = new LinkedHashSet<IEdge<TVertex>>(graph.edges);
      return g;
    } catch(Throwable t) {
      return null;
    }
  }

  /**
   * @see IGraph#copy()
   */
  public IGraph<TVertex, TValue, TProcessedValue> copy() {
    return copyGraph(this);
  }

  /**
   * Convenience method for easily constructing an instance of {@link IGraph} with the provided vertices.
   *
   * @param vertices A list of {@link IVertex} vertices to be added to a new {@link IGraph}.
   * @param <TVertex> Type of {@link IVertex} of the vertices in the new {@link IGraph}.
   * @return A new instance of {@link IGraph} with the provided vertices already added.
   */
  public static <TVertex extends IVertex<TValue>, TValue extends Object, TProcessedValue extends Object> IGraph<TVertex, TValue, TProcessedValue> build(final TVertex...vertices) {
    final DirectedAcyclicGraph<TVertex, TValue, TProcessedValue> g = new DirectedAcyclicGraph<TVertex, TValue, TProcessedValue>();
    if (vertices != null) {
      for(TVertex d : vertices) {
        g.addVertex(d);
      }
    }
    return g;
  }

  /**
   * Convenience method for easily constructing an instance of {@link IGraph} with an empty set of vertices.
   *
   * @param <TVertex> Type of {@link IVertex} of the vertices in the new {@link IGraph}.
   * @return A new instance of {@link IGraph} with an empty set of vertices.
   */
  public static <TVertex extends IVertex<TValue>, TValue extends Object, TProcessedValue extends Object> IGraph<TVertex, TValue, TProcessedValue> create() {
    return build((TVertex[])null);
  }

  /**
   * @see IGraph#addVertex(IVertex)
   */
  @Override
  public IGraph<TVertex, TValue, TProcessedValue> addVertex(final TVertex vertex) {
    if (vertex == null)
      throw new IllegalArgumentException("vertex must not be null");
    vertices.add(vertex);
    return this;
  }

  /**
   * @see IGraph#removeVertex(IVertex)
   */
  @Override
  public IGraph<TVertex, TValue, TProcessedValue> removeVertex(final TVertex vertex) {
    if (vertex == null)
      throw new IllegalArgumentException("vertex must not be null");
    vertices.remove(vertex);
    return this;
  }

  /**
   * @see IGraph#addEdge(IVertex, IVertex)
   */
  @Override
  public IGraph<TVertex, TValue, TProcessedValue> addEdge(final TVertex from, final TVertex to) {
    edges.add(new Edge<TVertex>(from, to));
    return this;
  }

  /**
   * @see IGraph#removeEdge(IVertex, IVertex)
   */
  @Override
  public IGraph<TVertex, TValue, TProcessedValue> removeEdge(final TVertex from, final TVertex to) {
    edges.remove(new Edge<TVertex>(from, to));
    return this;
  }

  /**
   * @see IGraph#validate()
   */
  @Override
  public boolean validate() {
    //Ensure that every from/to in an edge is present in our set of vertices.
    //If we refer to one that isn't in there, then we've got a problem.
    for(IEdge r : edges) {
      if (!vertices.contains(r.getFrom()) || !vertices.contains(r.getTo()))
        return false;
    }
    return true;
  }

  /**
   * @see IGraph#sort()
   */
  @Override
  public List<TValue> sort() throws CyclicGraphException {
    return sort(new SimpleTopologicalSort<TVertex, TValue, TProcessedValue>());
  }

  /**
   * @see IGraph#sort(ITopologicalSortStrategy)
   */
  @Override
  public List<TValue> sort(final ITopologicalSortStrategy<TVertex, TValue, TProcessedValue> strategy) throws CyclicGraphException {
    if (strategy == null)
      throw new IllegalArgumentException("strategy cannot be null");
    if (!validate())
      throw new IllegalStateException("The graph is invalid. Please confirm that all vertices are present for every relationship.");
    return strategy.sort(new AdjacencyList<TVertex, TValue, TProcessedValue>(vertices, edges));
  }

  /**
   * @see IGraph#sortAsync(ITopologicalSortCallback)
   */
  @Override
  public ITopologicalSortAsyncResult<TValue, TProcessedValue> sortAsync(final ITopologicalSortCallback<TValue, TProcessedValue> callback) {
    return sortAsync(new SimpleTopologicalSort<TVertex, TValue, TProcessedValue>(), callback, null);
  }

  /**
   * @see IGraph#sortAsync(ITopologicalSortCallback, ITopologicalSortErrorCallback)
   */
  @Override
  public ITopologicalSortAsyncResult<TValue, TProcessedValue> sortAsync(final ITopologicalSortCallback<TValue, TProcessedValue> callback, final ITopologicalSortErrorCallback<TValue> errorCallback) {
    return sortAsync(new SimpleTopologicalSort<TVertex, TValue, TProcessedValue>(), callback, errorCallback);
  }

  /**
   * @see IGraph#sortAsync(ITopologicalSortStrategy, ITopologicalSortCallback)
   */
  @Override
  public ITopologicalSortAsyncResult<TValue, TProcessedValue> sortAsync(final ITopologicalSortStrategy<TVertex, TValue, TProcessedValue> strategy, final ITopologicalSortCallback<TValue, TProcessedValue> callback) {
    return sortAsync(strategy, callback, null);
  }

  /**
   * @see IGraph#sortAsync(ITopologicalSortStrategy, ITopologicalSortCallback, ITopologicalSortErrorCallback)
   */
  @Override
  public ITopologicalSortAsyncResult<TValue, TProcessedValue> sortAsync(final ITopologicalSortStrategy<TVertex, TValue, TProcessedValue> strategy, final ITopologicalSortCallback<TValue, TProcessedValue> callback, final ITopologicalSortErrorCallback<TValue> errorCallback) {
    final ExecutorService executor = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() + 1));
    final ITopologicalSortAsyncResult<TValue, TProcessedValue> result = sortAsync(executor, strategy, callback, errorCallback);

    //We don't explicity shutdown b/c we may not be done processing
    //at this point. If need to abort prematurely, the caller can
    //access the executor service from the .getExecutorService()
    //method.
    return result;
  }

  /**
   * @see IGraph#sortAsync(java.util.concurrent.ExecutorService, ITopologicalSortCallback)
   */
  @Override
  public ITopologicalSortAsyncResult<TValue, TProcessedValue> sortAsync(final ExecutorService executor, final ITopologicalSortCallback<TValue, TProcessedValue> callback) {
    return sortAsync(executor, new SimpleTopologicalSort<TVertex, TValue, TProcessedValue>(), callback, null);
  }

  /**
   * @see IGraph#sortAsync(java.util.concurrent.ExecutorService, ITopologicalSortCallback, ITopologicalSortErrorCallback)
   */
  @Override
  public ITopologicalSortAsyncResult<TValue, TProcessedValue> sortAsync(final ExecutorService executor, final ITopologicalSortCallback<TValue, TProcessedValue> callback, final ITopologicalSortErrorCallback<TValue> errorCallback) {
    return sortAsync(executor, new SimpleTopologicalSort<TVertex, TValue, TProcessedValue>(), callback, errorCallback);
  }

  /**
   * @see IGraph#sortAsync(java.util.concurrent.ExecutorService, ITopologicalSortStrategy, ITopologicalSortCallback)
   */
  @Override
  public ITopologicalSortAsyncResult<TValue, TProcessedValue> sortAsync(final ExecutorService executor, final ITopologicalSortStrategy<TVertex, TValue, TProcessedValue> strategy, final ITopologicalSortCallback<TValue, TProcessedValue> callback) {
    return sortAsync(executor, strategy, callback, null);
  }

  /**
   * @see IGraph#sortAsync(java.util.concurrent.ExecutorService, ITopologicalSortStrategy, ITopologicalSortCallback, ITopologicalSortErrorCallback)
   */
  @Override
  public ITopologicalSortAsyncResult<TValue, TProcessedValue> sortAsync(final ExecutorService executor, final ITopologicalSortStrategy<TVertex, TValue, TProcessedValue> strategy, final ITopologicalSortCallback<TValue, TProcessedValue> callback, final ITopologicalSortErrorCallback<TValue> errorCallback) {
    if (strategy == null)
      throw new IllegalArgumentException("strategy cannot be null");
    if (callback == null)
      throw new IllegalArgumentException("callback cannot be null");
    if (!validate())
      throw new IllegalStateException("The graph is invalid. Please confirm that all vertices are present for every relationship.");

    return strategy.sortAsync(executor, new AdjacencyList<TVertex, TValue, TProcessedValue>(vertices, edges), callback, errorCallback);
  }
}
