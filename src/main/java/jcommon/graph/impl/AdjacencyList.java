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

import jcommon.graph.IAdjacencyList;
import jcommon.graph.IAdjacencyListPair;
import jcommon.graph.IEdge;
import jcommon.graph.IVertex;

import java.util.*;

/**
 * @see IAdjacencyList
 */
public class AdjacencyList<TVertex extends IVertex<TValue>, TValue extends Object> implements IAdjacencyList<TVertex, TValue> {
  private final List<IAdjacencyListPair<TVertex>> num_map;
  private final Map<TVertex, Integer> index_map;
  private final Map<TVertex, List<TVertex>> vertex_map;
  private final Set<TVertex> end_vertices;

  /**
   * @see IAdjacencyList
   */
  public AdjacencyList(final Set<TVertex> vertices, final Set<IEdge<TVertex>> edges) {
    //Create 2 maps.
    //  One maps from a vertex to all of its out-neighbors.
    //  The other just maps from an integer index to the vertices at that index.
    //  This means that num_map must preserve insertion order! Easy to do w/ an ArrayList.
    final List<IAdjacencyListPair<TVertex>> num_map = new ArrayList<IAdjacencyListPair<TVertex>>(vertices.size());
    final Map<TVertex, List<TVertex>> vertex_map = new HashMap<TVertex, List<TVertex>>(vertices.size(), 1.0f);
    final Map<TVertex, Integer> index_map = new HashMap<TVertex, Integer>(vertices.size(), 1.0f);
    final List<TVertex> EMPTY_VERTICES_ARRAYLIST = new ArrayList<TVertex>(0);
    final Set<TVertex> end_vertices = new HashSet<TVertex>(2, 1.0f);

    for(TVertex d : vertices) {
      final List<TVertex> al_to = new ArrayList<TVertex>();
      for(IEdge<TVertex> r : edges) {
        if (d.equals(r.getFrom())) {
          al_to.add(r.getTo());
        }
      }

      final List<TVertex> arr_to = !al_to.isEmpty() ? al_to : EMPTY_VERTICES_ARRAYLIST;

      vertex_map.put(d, arr_to);
      num_map.add(new AdjacencyListPair<TVertex>(d, arr_to));
      index_map.put(d, num_map.size() - 1);

      //Track the vertices that have no edges coming out of them -- those are end points
      //that we will later want to gather in order to return them at the end of async
      //processing.
      if (arr_to.isEmpty()) {
        end_vertices.add(d);
      }
    }

    //Ensure the maps are read-only at this point.
    this.num_map = Collections.unmodifiableList(num_map);
    this.index_map = Collections.unmodifiableMap(index_map);
    this.vertex_map = Collections.unmodifiableMap(vertex_map);
    this.end_vertices = Collections.unmodifiableSet(end_vertices);
  }

  /**
   * Calculates an integer array where the value at each index is the number of times that vertex is
   * referenced elsewhere.
   */
  @Override
  public int[] calculateInDegrees() {
    final int[] in_degrees = new int[size()];
    for(int i = 0; i < size(); ++i) {
      final IAdjacencyListPair<TVertex> p = pairAt(i);
      final TVertex d = p.getVertex();

      for(int j = 0; j < size(); ++j) {
        for(IVertex dep : pairAt(j).getOutNeighbors()) {
          if (d.equals(dep))
            ++in_degrees[i];
        }
      }
    }
    return in_degrees;
  }

  @Override
  public IAdjacencyListPair<TVertex> pairAt(final int index) {
    return num_map.get(index);
  }

  @Override
  public List<TVertex> outNeighborsAt(final int index) {
    final IAdjacencyListPair<TVertex> pair = pairAt(index);
    return pair.getOutNeighbors();
  }

  @Override
  public List<TVertex> outNeighborsFor(final TVertex vertex) {
    return vertex_map.get(vertex);
  }

  @Override
  public boolean isEmpty() {
    return vertex_map.isEmpty();
  }

  @Override
  public int size() {
    return vertex_map.size();
  }

  @Override
  public Set<TVertex> getEndingVertices() {
    return end_vertices;
  }

  @Override
  public Iterator<IAdjacencyListPair<TVertex>> iterator() {
    return num_map.iterator();
  }

  @Override
  public int indexOf(final IVertex vertex) {
    final Integer result = index_map.get(vertex);
    return (result != null) ? result : -1;
  }

  @Override
  public boolean isEndingVertex(final TVertex vertex) {
    return end_vertices.contains(vertex);
  }

  @Override
  public Map<TValue, TValue> createResultMap() {
    return new HashMap<TValue, TValue>(end_vertices.size());
  }
}
