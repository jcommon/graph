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

import jcommon.graph.IEdge;
import jcommon.graph.IVertex;

/**
 * @param <TVertex> Type of {@link IVertex} instances that this edge will connect.
 *
 * @see IEdge
 */
public class Edge<TVertex extends IVertex> implements IEdge<TVertex> {
  /** The object we are connecting from. */
  public final TVertex from;

  /** The object we are connecting to. */
  public final TVertex to;

  /**
   * Create a new directed edge for a graph that defines the relationship between
   * two graph vertices.
   *
   * @param from The object we are connecting from.
   * @param to The object we are connecting to.
   */
  public Edge(final TVertex from, final TVertex to) {
    if (from == null || to == null)
      throw new NullPointerException("from and to both cannot be null");

    this.from = from;
    this.to = to;
  }

  /**
   * Retrieves the {@link Edge#from} instance.
   *
   * @return The provided {@link Edge#from} instance.
   */
  @Override
  public TVertex getFrom() {
    return from;
  }

  /**
   * Retrieves the {@link Edge#to} instance.
   *
   * @return The provided {@link Edge#to} instance.
   */
  @Override
  public TVertex getTo() {
    return to;
  }

  /**
   * @see Object#hashCode()
   */
  @Override
  public int hashCode() {
    int result = from.hashCode();
    result = 31 * result + to.hashCode();
    return result;
  }

  /**
   * @see Object#equals(Object)
   */
  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof Edge))
      return false;
    final Edge e = (Edge)obj;

    return from.equals(e.from) && to.equals(e.to);
  }

  /**
   * @see Object#toString()
   */
  @Override
  public String toString() {
    return ("" + from) + " -> " + ("" + to);
  }
}
