package jdeps.impl;

import jdeps.IAdjacencyListPair;
import jdeps.IVertex;
import jdeps.Pair;

import java.util.List;

/**
 * @see IAdjacencyListPair
 */
public class AdjacencyListPair<TVertex extends IVertex> extends Pair<TVertex, List<TVertex>> implements IAdjacencyListPair<TVertex> {
  public AdjacencyListPair(TVertex value, List<TVertex> outNeighbors) {
    super(value, outNeighbors);
  }

  @Override
  public TVertex getVertex() {
    return getValue1();
  }

  @Override
  public List<TVertex> getOutNeighbors() {
    return getValue2();
  }
}