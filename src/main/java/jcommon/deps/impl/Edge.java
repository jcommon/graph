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

package jcommon.deps.impl;

import jcommon.deps.IEdge;
import jcommon.deps.IVertex;

/**
 * @see IEdge
 */
public class Edge<TVertex extends IVertex> implements IEdge<TVertex> {
  public final TVertex from;
  public final TVertex to;

  public Edge(TVertex from, TVertex to) {
    if (from == null || to == null)
      throw new NullPointerException("from and to both cannot be null");

    this.from = from;
    this.to = to;
  }

  @Override
  public TVertex getFrom() {
    return from;
  }

  @Override
  public TVertex getTo() {
    return to;
  }

  @Override
  public int hashCode() {
    int result = from.hashCode();
    result = 31 * result + to.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Edge))
      return false;
    Edge e = (Edge)obj;

    return from.equals(e.from) && to.equals(e.to);
  }
}
