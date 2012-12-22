package jdeps.impl;

import jdeps.IEdge;
import jdeps.IVertex;

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
